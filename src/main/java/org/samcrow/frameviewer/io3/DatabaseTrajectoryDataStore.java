// <editor-fold defaultstate="collapsed" desc="License">
/* 
 * Copyright 2012-2015 Sam Crow
 * 
 * This file is part of Trajectory.
 * 
 * Trajectory is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Trajectory is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Trajectory.  If not, see <http://www.gnu.org/licenses/>.
 */
// </editor-fold>
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
import java.util.List;
import org.samcrow.frameviewer.MultiFrameDataStore;
import org.samcrow.frameviewer.trajectory.InteractionPoint;
import org.samcrow.frameviewer.trajectory.InteractionType;
import org.samcrow.frameviewer.trajectory.Point;
import org.samcrow.frameviewer.trajectory.Point.Source;
import org.samcrow.frameviewer.trajectory.Point0;
import org.samcrow.frameviewer.trajectory.Trajectory.Entry;
import org.samcrow.frameviewer.trajectory.Trajectory0;

/**
 *
 * @author samcrow
 */
public class DatabaseTrajectoryDataStore extends MultiFrameDataStore<Trajectory0> implements
	Closeable, AutoCloseable {

    private final Connection connection;
    private final String pointsTableName;
    private final String trajectoriesTableName;

    public DatabaseTrajectoryDataStore(Connection connection,
	    String pointsTableName, String trajectoriesTableName) throws SQLException {
	this.connection = connection;
	this.pointsTableName = pointsTableName;
	this.trajectoriesTableName = trajectoriesTableName;
	checkSchema();

	try (ResultSet trajectories = selectTrajectories()) {
	    while (trajectories.next()) {

		final Trajectory0 trajectory = createTrajectoryAndPoints(
			trajectories);
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

	    final List<Trajectory0> updated = new ArrayList<>();

	    while (trajectories.next()) {
		final Trajectory0 existingTrajectory = findTrajectoryById(
			trajectories.getInt("trajectory_id"));

		if (existingTrajectory != null) {
		    // Propagate properties from the database trajectory to the existing one

		    existingTrajectory.setFromAction(Trajectory0.FromAction
			    .safeValueOf(trajectories.getString("from_action")));
		    existingTrajectory.setToAction(Trajectory0.ToAction
			    .safeValueOf(trajectories.getString("to_action")));

		    // Points
		    try (ResultSet points = selectPointsInTrajectory(
			    existingTrajectory.getId())) {
			while (points.next()) {
			    final int frame = points.getInt("frame_number");
			    final Point0 existingPoint = existingTrajectory.get(
				    frame);
			    if (existingPoint != null) {
				// Update this point
				existingPoint.setActivity(Point0.Activity
					.valueOf(points.getString("activity")));
				existingPoint.setX(points.getInt("frame_x"));
				existingPoint.setY(points.getInt("frame_y"));

				if (existingPoint instanceof InteractionPoint) {
				    final InteractionPoint iPoint = (InteractionPoint) existingPoint;

				    // Check if the point should be preserved as an interaction point
				    if (points.getBoolean("is_interaction")) {

					iPoint.setType(InteractionType.valueOf(
						points.getString(
							"interaction_type")));
					iPoint.setMetAntActivity(Point0.Activity
						.valueOf(points.getString(
								"interaction_met_ant_activity")));
					iPoint.setMetAntId(points.getInt(
						"interaction_met_trajectory_id"));
				    } else {
					demoteFromInteraction(iPoint, frame, existingTrajectory.getId());
				    }
				} else {
				    // Not an interaction point
				    // Check if it should be promoted
				    if (points.getBoolean("is_interaction")) {
					// Promote
					final InteractionPoint iPoint = new InteractionPoint(
						existingPoint);
					iPoint.setType(InteractionType.valueOf(
						points.getString(
							"interaction_type")));
					iPoint.setMetAntActivity(Point0.Activity
						.valueOf(points.getString(
								"interaction_met_ant_activity")));
					iPoint.setMetAntId(points.getInt(
						"interaction_met_trajectory_id"));
					// Put the promoted point into the trajectory
					existingTrajectory.put(frame, iPoint);
				    }
				}

			    } else {
				// Add a point
				existingTrajectory.put(frame,
					pointFromResultSet(points));
			    }
			}
		    }

		    // Mark the trajectory updated
		    updated.add(existingTrajectory);
		} else {
		    // Create a new trajectory
		    final Trajectory0 trajectory = createTrajectoryAndPoints(
			    trajectories);
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
	} catch (SQLException ex) {
	    throw new IOException(ex);
	}
    }

    private void checkSchema() throws SQLException {
	DatabaseMetaData dbData = connection.getMetaData();
	ResultSet tableResults = dbData.getTables(connection.getCatalog(), null,
		null, null);

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

    private Trajectory0 createTrajectoryAndPoints(ResultSet trajectories) throws SQLException {
	final int trajectoryId = trajectories.getInt("trajectory_id");
	final Trajectory0.FromAction startAction = Trajectory0.FromAction
		.safeValueOf(trajectories.getString("from_action"));
	final Trajectory0.ToAction endAction = Trajectory0.ToAction.safeValueOf(
		trajectories.getString("to_action"));

	try (ResultSet points = selectPointsInTrajectory(trajectoryId)) {

	    Trajectory0 trajectory;
	    // Get the first point. Because the query involved an ORDER BY frame_number request,
	    // this point must have the lowest frame number.
	    if (points.next()) {
		final Point0 firstPoint = pointFromResultSet(points);
		final int firstPointFrame = points.getInt("frame_number");
		trajectory = new Trajectory0(null);
		trajectory.setFromAction(startAction);
		trajectory.setToAction(endAction);
		trajectory.setDataStore(this);
		trajectory.setId(trajectoryId);

		trajectory.put(firstPointFrame, firstPoint);
	    } else {
		System.err.println("Got a trajectory with ID " + trajectoryId
			+ " that does not have any points. This trajectory will be deleted.");
		deleteTrajectoryFromDatabaseOnly(trajectoryId);
		return null;
	    }

	    while (points.next()) {
		final Point0 point = pointFromResultSet(points);
		final int frame = points.getInt("frame_number");
		trajectory.put(frame, point);
	    }
	    return trajectory;
	}
    }

    private ResultSet selectTrajectories() throws SQLException {
	return connection.createStatement().executeQuery("SELECT * FROM `"
		+ trajectoriesTableName + "` ORDER BY `trajectory_id`");
    }

    private ResultSet selectOneTrajectory(int trajectoryId) throws SQLException {
	return connection.createStatement().executeQuery("SELECT * FROM `"
		+ trajectoriesTableName + "` WHERE `trajectory_id`="
		+ trajectoryId + " LIMIT 1");
    }

    private ResultSet selectPointsInTrajectory(int trajectoryId) throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM `" + pointsTableName + "` WHERE `trajectory_id`=" + trajectoryId + " ORDER BY `frame_number`,`is_interaction`");
    }

    /**
     * Creates or updates records for the provided trajectory and all its points
     * <p>
     * @param trajectory
     * @throws java.sql.SQLException
     */
    public void persistTrajectory(Trajectory0 trajectory) throws SQLException {
	try (ResultSet existing = selectOneTrajectory(trajectory.getId())) {
	    if (existing.next()) {
		// A trajectory already exists
		updateTrajectory(trajectory);
	    } else {
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
    private void updateTrajectory(Trajectory0 trajectory) throws SQLException {
	try (Statement statement = connection.createStatement()) {
	    statement.executeUpdate("UPDATE `" + trajectoriesTableName
		    + "` SET "
		    + "`from_action` = '" + trajectory.getFromAction().name()
		    + "',"
		    + " `to_action` = '" + trajectory.getToAction().name()
		    + '\''
		    + " WHERE `trajectory_id` = '" + trajectory.getId() + '\'');
	}

	// Update all points
	for (Entry<Point0> entry : trajectory) {
	    persistPoint(entry.point, entry.frame, trajectory.getId());
	}
    }

    private void insertTrajectory(Trajectory0 trajectory) throws SQLException {
	try (Statement statement = connection.createStatement()) {
	    statement.executeUpdate("INSERT INTO `" + trajectoriesTableName
		    + "` ("
		    + "`trajectory_id`, "
		    + "`from_action`, "
		    + "`to_action`"
		    + " ) VALUES ( "
		    + trajectory.getId() + ","
		    + '\'' + trajectory.getFromAction().name() + "', "
		    + '\'' + trajectory.getToAction().name() + "'"
		    + ")");

	    // Points: It is safe to assume that no points with this trajectory exist
	    for (Entry<Point0> entry : trajectory) {
		insertPoint(entry.point, entry.frame, trajectory.getId());
	    }
	}
    }

    public void deleteTrajectory(Trajectory0 trajectory) throws SQLException {
	final int trajectoryId = trajectory.getId();
	deleteTrajectoryFromDatabaseOnly(trajectoryId);
	// Remove the trajectory from the local data
	data.remove(trajectory);
    }

    private void deleteTrajectoryFromDatabaseOnly(int trajectoryId) throws SQLException {
	try (Statement statement = connection.createStatement()) {
	    statement.executeUpdate("DELETE FROM `" + pointsTableName
		    + "` WHERE `trajectory_id`=" + trajectoryId);
	    // Then delete the trajectory
	    statement.executeUpdate("DELETE FROM `" + trajectoriesTableName
		    + "` WHERE `trajectory_id`=" + trajectoryId);
	}
    }

    public void deletePoint(int trajectoryId, int frame) throws SQLException {
	try (Statement statement = connection.createStatement()) {
	    statement.executeUpdate("DELETE FROM `" + pointsTableName
		    + "` WHERE `trajectory_id` = " + trajectoryId
		    + " AND `frame_number` = " + frame);
	}
    }

    public void persistPoint(Point0 point, int frame, int trajectoryId) throws SQLException {
	try (Statement statement = connection.createStatement()) {
	    try (ResultSet existingPoint
		    = statement.executeQuery(
			    "SELECT * FROM `" + pointsTableName
			    + "` WHERE `trajectory_id` = "
			    + trajectoryId + " AND `frame_number` = " + frame)) {
			if (existingPoint.next()) {
			    updatePoint(point, frame, trajectoryId);
			} else {
			    insertPoint(point, frame, trajectoryId);
			}
		    }
	}
    }

    private void updatePoint(Point0 point, int frame, int trajectoryId) throws SQLException {
	try (Statement statement = connection.createStatement()) {
	    if (point instanceof InteractionPoint) {
		InteractionPoint iPoint = (InteractionPoint) point;
		statement.executeUpdate("UPDATE `" + pointsTableName + "` SET "
			+ "`frame_x` = " + point.getX() + ','
			+ "`frame_y` = " + point.getY() + ','
			+ "`activity` = '" + point.getActivity().name() + "',"
			+ "`source` = '" + point.getSource().name() + "',"
			+ "`is_interaction` = 1,"
			+ "`interaction_met_trajectory_id` = " + iPoint
			.getMetAntId() + ','
			+ "`interaction_type` = '" + iPoint.getType().name()
			+ "',"
			+ "`interaction_met_ant_activity` = '" + iPoint
			.getMetAntActivity().name() + '\''
			+ " WHERE  `trajectory_id` = " + trajectoryId
			+ " AND `frame_number` = " + frame);
	    } else {
		statement.executeUpdate("UPDATE `" + pointsTableName + "` SET "
			+ "`frame_x` = " + point.getX() + ','
			+ "`frame_y` = " + point.getY() + ','
			+ "`activity` = '" + point.getActivity().name() + "',"
			+ "`source` = '" + point.getSource().name() + "',"
			+ "`is_interaction` = 0"
			+ " WHERE  `trajectory_id` = " + trajectoryId
			+ " AND `frame_number` = " + frame);
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
    private void demoteFromInteraction(InteractionPoint point, int frame,
	    int trajectoryId) throws SQLException {
	try (Statement statement = connection.createStatement()) {
	    statement.executeUpdate("UPDATE `" + pointsTableName + "` SET"
		    + "`is_interaction` = 0"
		    + " WHERE  `trajectory_id` = " + trajectoryId
		    + " AND `frame_number` = " + frame);
	}
    }

    private void insertPoint(Point0 point, int frame, int trajectoryId) throws SQLException {
	try (Statement statement = connection.createStatement()) {
	    if (point instanceof InteractionPoint) {
		InteractionPoint iPoint = (InteractionPoint) point;

		statement.executeUpdate("INSERT INTO `" + pointsTableName
			+ "` ("
			+ "`trajectory_id`,"
			+ "`frame_number`,"
			+ "`frame_x`,"
			+ "`frame_y`,"
			+ "`activity`,"
			+ "`source`,"
			+ "`is_interaction`,"
			+ "`interaction_met_trajectory_id`,"
			+ "`interaction_type`,"
			+ "`interaction_met_ant_activity`"
			+ ") VALUES ("
			+ trajectoryId + ','
			+ frame + ','
			+ point.getX() + ','
			+ point.getY() + ','
			+ '\'' + point.getActivity().name() + "',"
			+ '\'' + point.getSource().name() + "',"
			+ "1," // is interaction
			+ iPoint.getMetAntId() + ','
			+ '\'' + iPoint.getType().name() + "',"
			+ '\'' + iPoint.getMetAntActivity().name() + '\''
			+ ")");

	    } else {
		final String query = "INSERT INTO `" + pointsTableName + "` ("
			+ "`trajectory_id`,"
			+ "`frame_number`,"
			+ "`frame_x`,"
			+ "`frame_y`,"
			+ "`activity`,"
			+ "`source`"
			+ ") VALUES ("
			+ trajectoryId + ','
			+ frame + ','
			+ point.getX() + ','
			+ point.getY() + ','
			+ '\'' + point.getActivity().name() + "',"
			+ '\'' + point.getSource().name() + '\''
			+ ")";

		statement.executeUpdate(query);
	    }
	}
    }

    private void setUpSchema() throws SQLException {
	try (Statement statement = connection.createStatement()) {
	    statement.executeUpdate("DROP TABLE IF EXISTS `"
		    + trajectoriesTableName + "`");
	    statement.executeUpdate("CREATE TABLE `" + trajectoriesTableName
		    + "` ("
		    + "`trajectory_id` INTEGER PRIMARY KEY,"
		    + "`from_action` varchar(255) NOT NULL DEFAULT 'Unknown',"
		    + "`to_action` varchar(255) NOT NULL DEFAULT 'Unknown'"
		    + ")");

	    // Set up Points table
	    statement.executeUpdate("DROP TABLE IF EXISTS `" + pointsTableName
		    + "`");
	    statement.executeUpdate("CREATE TABLE `" + pointsTableName + "` ("
		    + "`point_id` INTEGER PRIMARY KEY AUTO_INCREMENT,"
		    + "`trajectory_id` INTEGER NOT NULL,"
		    + "`frame_number` INTEGER NOT NULL,"
		    + "`frame_x` INTEGER NOT NULL,"
		    + "`frame_y` INTEGER NOT NULL,"
		    + "`activity` varchar(255) NOT NULL DEFAULT 'NotCarrying',"
		    + "`source` varchar(255) NOT NULL DEFAULT 'User',"
		    + "`is_interaction` SMALLINT NOT NULL DEFAULT 0,"
		    + "`interaction_met_trajectory_id` int,"
		    + "`interaction_type` varchar(255),"
		    + "`interaction_met_ant_activity` varchar(255)"
		    + ")");
	}
    }

    private static Point0 pointFromResultSet(ResultSet result) throws SQLException {
	Point0 point;
	if (result.getBoolean("is_interaction")) {
	    point = new InteractionPoint(result.getInt("frame_x"), result
		    .getInt("frame_y"));
	    // Set interaction-specific properties
	    ((InteractionPoint) point).setType(InteractionType.safeValueOf(
		    result.getString("interaction_type")));
	    ((InteractionPoint) point).setMetAntId(result.getInt(
		    "interaction_met_trajectory_id"));
	    ((InteractionPoint) point).setMetAntActivity(Point0.Activity
		    .safeValueOf(result
			    .getString("interaction_met_ant_activity")));
	} else {
	    point = new Point0(result.getInt("frame_x"), result
		    .getInt("frame_y"), Source.User); // TODO: Get source
	}

	// Set common attributes
	point.setActivity(Point0.Activity.safeValueOf(result.getString(
		"activity")));
	point.setSource(Point.Source.safeValueOf(result.getString("source")));

	return point;
    }

    @Override
    public void close() throws IOException {
	try {
	    connection.close();
	} catch (SQLException ex) {
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
    private Trajectory0 findTrajectoryById(int trajectoryId) {
	// Because the results are ordered by trajectory ID,
	// binary search can be used
	Trajectory0 key = new Trajectory0(null);
	key.setId(trajectoryId);
	final int foundIndex = Collections.binarySearch(data,
		key, (Trajectory0 o1, Trajectory0 o2) -> {
		    if (o1.getId() < o2.getId()) {
			return -1;
		    } else if (o1.getId() > o2.getId()) {
			return 1;
		    } else {
			return 0;
		    }
		});

	if (foundIndex >= 0) {
	    return data.get(foundIndex);
	} else {
	    return null;
	}
    }

    private void connectInteractionPoints() throws SQLException {
	for (Trajectory0 trajectory : data) {
	    for (Entry<Point0> entry : trajectory) {
		Point0 point = entry.point;
		if (point instanceof InteractionPoint) {
		    InteractionPoint iPoint = (InteractionPoint) point;

		    if (iPoint.getOtherPoint() == null) {
			// Find and assign the other point
			final int targetTrajectoryId = iPoint.getMetAntId();
			final int targetFrame = entry.frame;

			final Trajectory0 foundTrajectory = findTrajectoryById(
				targetTrajectoryId);

			if (foundTrajectory != null) {
			    // Found trajectory
			    // Look for the frame
			    try {
				final Point0 matchingPoint = foundTrajectory
					.get(targetFrame);

				if (matchingPoint != null) {

				    if (matchingPoint instanceof InteractionPoint) {

					((InteractionPoint) matchingPoint)
						.setOtherPoint(iPoint);
					iPoint.setOtherPoint(
						(InteractionPoint) matchingPoint);

				    } else {
					// Point not an InteractionPoint
					// Do nothing
				    }
				} else {
				    // No point
				    // Do nothing
				}
			    } catch (IndexOutOfBoundsException ex) {
				// No point
				// Do nothing
			    }
			} else {
			    // No trajectory
			    // Do nothing
			}
		    }
		}
	    }
	}
    }

}
