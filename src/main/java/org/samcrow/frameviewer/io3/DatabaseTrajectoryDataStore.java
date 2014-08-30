package org.samcrow.frameviewer.io3;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.samcrow.frameviewer.MultiFrameDataStore;
import org.samcrow.frameviewer.trajectory.InteractionPoint;
import org.samcrow.frameviewer.trajectory.InteractionType;
import org.samcrow.frameviewer.trajectory.Point;
import org.samcrow.frameviewer.trajectory.Trajectory;

/**
 *
 * @author samcrow
 */
public class DatabaseTrajectoryDataStore extends MultiFrameDataStore<Trajectory> implements Closeable, AutoCloseable {

    private final Connection connection;
    private final String pointsTableName;
    private final String trajectoriesTableName;

    public DatabaseTrajectoryDataStore(Connection connection, String pointsTableName, String trajectoriesTableName) throws SQLException {
        this.connection = connection;
        this.pointsTableName = pointsTableName;
        this.trajectoriesTableName = trajectoriesTableName;
        checkSchema();

        try (ResultSet trajectories = selectTrajectories()) {
            while (trajectories.next()) {

                final Trajectory trajectory = createTrajectoryAndPoints(trajectories);
                if (trajectory != null) {
                    // Add this trajectory to the instance's list
                    data.add(trajectory);
                }
            }
        }
        // Now that the trajectories are known, hook up the relations among the InteractionPoints
        connectInteractionPoints();
    }

    /**
     * Refreshes the data from the database
     * <p>
     * @throws java.io.IOException
     */
    public void refresh() throws IOException {
        try (ResultSet trajectories = selectTrajectories()) {

            final List<Trajectory> updated = new ArrayList<>();

            while (trajectories.next()) {
                final Trajectory existingTrajectory = findTrajectoryById(trajectories.getInt("trajectory_id"));

                if (existingTrajectory != null) {
                    // Propagate properties from the database trajectory to the existing one

                    existingTrajectory.setFromAction(Trajectory.FromAction.safeValueOf(trajectories.getString("from_action")));
                    existingTrajectory.setToAction(Trajectory.ToAction.safeValueOf(trajectories.getString("to_action")));

                    // Points
                    try (ResultSet points = selectPointsInTrajectory(existingTrajectory.getId())) {
                        while (points.next()) {
                            final int frame = points.getInt("frame_number");
                            final Point existingPoint = existingTrajectory.get(frame);
                            if (existingPoint != null) {
                                // Update this point
                                existingPoint.setActivity(Point.Activity.valueOf(points.getString("activity")));
                                existingPoint.setX(points.getInt("frame_x"));
                                existingPoint.setY(points.getInt("frame_y"));

                                if (existingPoint instanceof InteractionPoint) {
                                    final InteractionPoint iPoint = (InteractionPoint) existingPoint;

                                    // Check if the point should be preserved as an interaction point
                                    if (points.getBoolean("is_interaction")) {

                                        iPoint.setType(InteractionType.valueOf(points.getString("interaction_type")));
                                        iPoint.setMetAntActivity(Point.Activity.valueOf(points.getString("interaction_met_ant_activity")));
                                        iPoint.setMetAntId(points.getInt("interaction_met_trajectory_id"));
                                    }
                                    else {
                                        demoteFromInteraction(iPoint, frame);
                                    }
                                }
                                else {
                                    // Not an interaction point
                                    // Check if it should be promoted
                                    if (points.getBoolean("is_interaction")) {
                                        // Promote
                                        final InteractionPoint iPoint = new InteractionPoint(existingPoint);
                                        iPoint.setType(InteractionType.valueOf(points.getString("interaction_type")));
                                        iPoint.setMetAntActivity(Point.Activity.valueOf(points.getString("interaction_met_ant_activity")));
                                        iPoint.setMetAntId(points.getInt("interaction_met_trajectory_id"));
                                        // Put the promoted point into the trajectory
                                        existingTrajectory.set(frame, iPoint);
                                    }
                                }

                            }
                            else {
                                // Add a point
                                existingTrajectory.set(frame, pointFromResultSet(points));
                            }
                        }
                    }

                    // Mark the trajectory updated
                    updated.add(existingTrajectory);
                }
                else {
                    // Create a new trajectory
                    final Trajectory trajectory = createTrajectoryAndPoints(trajectories);
                    if (trajectory != null) {
                        updated.add(trajectory);
                    }
                }
            }

            // Clear the existing data and put the updated trajectories in
            data.clear();
            data.addAll(updated);

            // Indicate that this object has changed
            fireValueChangedEvent();
        }
        catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    private void checkSchema() throws SQLException {
        DatabaseMetaData dbData = connection.getMetaData();
        ResultSet tableResults = dbData.getTables(connection.getCatalog(), null, null, null);

        boolean hasTrajectories = false;
        boolean hasPoints = false;

        while (tableResults.next()) {
            final String tableName = tableResults.getString("TABLE_NAME");
            if (tableName.equals(trajectoriesTableName)) {
                hasTrajectories = true;
            }
            if (tableName.equals(pointsTableName)) {
                hasPoints = true;
            }
        }

        if (!hasPoints && !hasTrajectories) {
            setUpSchema();
        }
    }

    private Trajectory createTrajectoryAndPoints(ResultSet trajectories) throws SQLException {
        final int trajectoryId = trajectories.getInt("trajectory_id");
        final Trajectory.FromAction startAction = Trajectory.FromAction.safeValueOf(trajectories.getString("from_action"));
        final Trajectory.ToAction endAction = Trajectory.ToAction.safeValueOf(trajectories.getString("to_action"));

        try (ResultSet points = selectPointsInTrajectory(trajectoryId)) {

            Trajectory trajectory;
            // Get the first point. Because the query involved an ORDER BY frame_number request,
            // this point must have the lowest frame number.
            if (points.next()) {
                final Point firstPoint = pointFromResultSet(points);
                trajectory = new Trajectory(firstPoint.getFrame(), firstPoint.getFrame() + 1, trajectoryId);
                trajectory.setFromAction(startAction);
                trajectory.setToAction(endAction);
                trajectory.setDataStore(this);

                trajectory.set(firstPoint.getFrame(), firstPoint);
            }
            else {
                System.err.println("Got a trajectory with ID " + trajectoryId + " that does not have any points. This trajectory will be deleted.");
                deleteTrajectoryFromDatabaseOnly(trajectoryId);
                return null;
            }

            while (points.next()) {
                final Point point = pointFromResultSet(points);
                trajectory.set(point.getFrame(), point);
            }
            return trajectory;
        }
    }

    private ResultSet selectTrajectories() throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM `" + trajectoriesTableName + "` ORDER BY `trajectory_id`");
    }

    private ResultSet selectOneTrajectory(int trajectoryId) throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM `" + trajectoriesTableName + "` WHERE `trajectory_id`=" + trajectoryId + " LIMIT 1");
    }

    private ResultSet selectPointsInTrajectory(int trajectoryId) throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM `" + pointsTableName + "` WHERE `trajectory_id`=" + trajectoryId + " ORDER BY `frame_number`");
    }

    /**
     * Creates or updates records for the provided trajectory and all its points
     * <p>
     * @param trajectory
     * @throws java.sql.SQLException
     */
    public void persistTrajectory(Trajectory trajectory) throws SQLException {
        try (ResultSet existing = selectOneTrajectory(trajectory.getId())) {
            if (existing.next()) {
                // A trajectory already exists
                updateTrajectory(trajectory);
            }
            else {
                // No trajectory already exists
                insertTrajectory(trajectory);
            }
        }
    }

    /**
     * Updates the provided trajectory and all its points
     * <p>
     * @param trajectory
     * @throws java.sql.SQLException
     */
    private void updateTrajectory(Trajectory trajectory) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("UPDATE `" + trajectoriesTableName + "` SET "
                    + "`from_action` = '" + trajectory.getFromAction().name() + "',"
                    + " `to_action` = '" + trajectory.getToAction().name() + '\''
                    + " WHERE `trajectory_id` = '" + trajectory.getId() + '\'');
        }

        // Update all points
        for (Point point : trajectory) {
            persistPoint(point, trajectory.getId());
        }
    }

    private void insertTrajectory(Trajectory trajectory) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO `" + trajectoriesTableName + "` ("
                    + "`trajectory_id`, "
                    + "`from_action`, "
                    + "`to_action`"
                    + " ) VALUES ( "
                    + trajectory.getId() + ","
                    + '\'' + trajectory.getFromAction().name() + "', "
                    + '\'' + trajectory.getToAction().name() + "'"
                    + ")");

            // Points: It is safe to assume that no points with this trajectory exist
            for (Point point : trajectory) {
                insertPoint(point, trajectory.getId());
            }
        }
    }

    public void deleteTrajectory(Trajectory trajectory) throws SQLException {
        final int trajectoryId = trajectory.getId();
        deleteTrajectoryFromDatabaseOnly(trajectoryId);
        // Remove the trajectory from the local data
        data.remove(trajectory);
    }

    private void deleteTrajectoryFromDatabaseOnly(int trajectoryId) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM `" + pointsTableName + "` WHERE `trajectory_id`=" + trajectoryId);
            // Then delete the trajectory
            statement.executeUpdate("DELETE FROM `" + trajectoriesTableName + "` WHERE `trajectory_id`=" + trajectoryId);
        }
    }

    public void deletePoint(int trajectoryId, int frame) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM `" + pointsTableName + "` WHERE `trajectory_id` = " + trajectoryId + " AND `frame_number` = " + frame);
        }
    }

    public void persistPoint(Point point, int trajectoryId) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet existingPoint
                    = statement.executeQuery(
                            "SELECT * FROM `" + pointsTableName + "` WHERE `trajectory_id` = "
                            + trajectoryId + " AND `frame_number` = " + point.getFrame())) {
                        if (existingPoint.next()) {
                            updatePoint(point, trajectoryId);
                        }
                        else {
                            insertPoint(point, trajectoryId);
                        }
                    }
        }
    }

    private void updatePoint(Point point, int trajectoryId) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            if (point instanceof InteractionPoint) {
                InteractionPoint iPoint = (InteractionPoint) point;
                statement.executeUpdate("UPDATE `" + pointsTableName + "` SET "
                        + "`frame_x` = " + point.getX() + ','
                        + "`frame_y` = " + point.getY() + ','
                        + "`activity` = '" + point.getActivity().name() + "',"
                        + "`is_interaction` = 1,"
                        + "`interaction_met_trajectory_id` = " + iPoint.getMetAntId() + ','
                        + "`interaction_type` = '" + iPoint.getType().name() + "',"
                        + "`interaction_met_ant_activity` = '" + iPoint.getMetAntActivity().name() + '\''
                        + " WHERE  `trajectory_id` = " + trajectoryId + " AND `frame_number` = " + point.getFrame());
            }
            else {
                statement.executeUpdate("UPDATE `" + pointsTableName + "` SET "
                        + "`frame_x` = " + point.getX() + ','
                        + "`frame_y` = " + point.getY() + ','
                        + "`activity` = '" + point.getActivity().name() + "',"
                        + "`is_interaction` = 0"
                        + " WHERE  `trajectory_id` = " + trajectoryId + " AND `frame_number` = " + point.getFrame());
            }
        }
    }

    /**
     * Demotes a point's entry in the database so that it is no longer an
     * interaction
     * <p>
     * @param point
     * @param trajectoryId
     * @throws java.sql.SQLException
     */
    private void demoteFromInteraction(InteractionPoint point, int trajectoryId) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("UPDATE `" + pointsTableName + "` SET"
                    + "`is_interaction` = 0"
                    + " WHERE  `trajectory_id` = " + trajectoryId + " AND `frame_number` = " + point.getFrame());
        }
    }

    private void insertPoint(Point point, int trajectoryId) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            if (point instanceof InteractionPoint) {
                InteractionPoint iPoint = (InteractionPoint) point;

                statement.executeUpdate("INSERT INTO `" + pointsTableName + "` ("
                        + "`trajectory_id`,"
                        + "`frame_number`,"
                        + "`frame_x`,"
                        + "`frame_y`,"
                        + "`activity`,"
                        + "`is_interaction`,"
                        + "`interaction_met_trajectory_id`,"
                        + "`interaction_type`,"
                        + "`interaction_met_ant_activity`"
                        + ") VALUES ("
                        + trajectoryId + ','
                        + point.getFrame() + ','
                        + point.getX() + ','
                        + point.getY() + ','
                        + '\'' + point.getActivity().name() + "',"
                        + "1," // is interaction
                        + iPoint.getMetAntId() + ','
                        + '\'' + iPoint.getType().name() + "',"
                        + '\'' + iPoint.getMetAntActivity().name() + '\''
                        + ")");

            }
            else {
                final String query = "INSERT INTO `" + pointsTableName + "` ("
                        + "`trajectory_id`,"
                        + "`frame_number`,"
                        + "`frame_x`,"
                        + "`frame_y`,"
                        + "`activity`"
                        + ") VALUES ("
                        + trajectoryId + ','
                        + point.getFrame() + ','
                        + point.getX() + ','
                        + point.getY() + ','
                        + '\'' + point.getActivity().name() + '\''
                        + ")";

                statement.executeUpdate(query);
            }
        }
    }

    private void setUpSchema() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS `" + trajectoriesTableName + "`");
            statement.executeUpdate("CREATE TABLE `" + trajectoriesTableName + "` ("
                    + "`trajectory_id` INTEGER PRIMARY KEY,"
                    + "`from_action` varchar(255) NOT NULL DEFAULT 'Unknown',"
                    + "`to_action` varchar(255) NOT NULL DEFAULT 'Unknown'"
                    + ")");

            // Set up Points table
            statement.executeUpdate("DROP TABLE IF EXISTS `" + pointsTableName + "`");
            statement.executeUpdate("CREATE TABLE `" + pointsTableName + "` ("
                    + "`point_id` INTEGER PRIMARY KEY AUTO_INCREMENT,"
                    + "`trajectory_id` INTEGER NOT NULL,"
                    + "`frame_number` INTEGER NOT NULL,"
                    + "`frame_x` INTEGER NOT NULL,"
                    + "`frame_y` INTEGER NOT NULL,"
                    + "`activity` varchar(255) NOT NULL DEFAULT 'NotCarrying',"
                    + "`is_interaction` SMALLINT NOT NULL DEFAULT 0,"
                    + "`interaction_met_trajectory_id` int,"
                    + "`interaction_type` varchar(255),"
                    + "`interaction_met_ant_activity` varchar(255)"
                    + ")");
        }
    }

    private static Point pointFromResultSet(ResultSet result) throws SQLException {
        Point point;
        if (result.getBoolean("is_interaction")) {
            point = new InteractionPoint(result.getInt("frame_x"), result.getInt("frame_y"));
            // Set interaction-specific properties
            ((InteractionPoint) point).setType(InteractionType.safeValueOf(result.getString("interaction_type")));
            ((InteractionPoint) point).setMetAntId(result.getInt("interaction_met_trajectory_id"));
            ((InteractionPoint) point).setMetAntActivity(Point.Activity.safeValueOf(result.getString("interaction_met_ant_activity")));
        }
        else {
            point = new Point(result.getInt("frame_x"), result.getInt("frame_y"));
        }

        // Set common attributes
        point.setFrame(result.getInt("frame_number"));
        point.setActivity(Point.Activity.safeValueOf(result.getString("activity")));

        return point;
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        }
        catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Finds a trajectory with the requested ID in this instance's list
     * and returns it, or null if none exists
     * <p>
     * @param trajectoryId
     * @return
     */
    private Trajectory findTrajectoryById(int trajectoryId) {
        // Because the results are ordered by trajectory ID,
        // binary search can be used
        final int foundIndex = Collections.binarySearch(data,
                new Trajectory(1, 2, trajectoryId),
                new Comparator<Trajectory>() {

                    @Override
                    public int compare(Trajectory o1, Trajectory o2) {
                        if (o1.getId() < o2.getId()) {
                            return -1;
                        }
                        else if (o1.getId() > o2.getId()) {
                            return 1;
                        }
                        else {
                            return 0;
                        }
                    }

                });

        if (foundIndex >= 0) {
            return data.get(foundIndex);
        }
        else {
            return null;
        }
    }

    private void connectInteractionPoints() throws SQLException {
        for (Trajectory trajectory : data) {
            for (Point point : trajectory) {
                if (point instanceof InteractionPoint) {
                    InteractionPoint iPoint = (InteractionPoint) point;

                    if (iPoint.getOtherPoint() == null) {
                        // Find and assign the other point
                        final int targetTrajectoryId = iPoint.getMetAntId();
                        final int targetFrame = iPoint.getFrame();

                        final Trajectory foundTrajectory = findTrajectoryById(targetTrajectoryId);

                        if (foundTrajectory != null) {
                            // Found trajectory
                            // Look for the frame
                            try {
                                final Point matchingPoint = foundTrajectory.get(targetFrame);

                                if (matchingPoint != null) {

                                    if (matchingPoint instanceof InteractionPoint) {

                                        ((InteractionPoint) matchingPoint).setOtherPoint(iPoint);
                                        iPoint.setOtherPoint((InteractionPoint) matchingPoint);

                                    }
                                    else {
                                        // Point not an InteractionPoint
                                        // Do nothing
                                    }
                                }
                                else {
                                    // No point
                                    // Do nothing
                                }
                            }
                            catch (IndexOutOfBoundsException ex) {
                                // No point
                                // Do nothing
                            }
                        }
                        else {
                            // No trajectory
                            // Do nothing
                        }
                    }
                }
            }
        }
    }

}
