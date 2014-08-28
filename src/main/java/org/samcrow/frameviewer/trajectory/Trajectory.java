package org.samcrow.frameviewer.trajectory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.samcrow.frameviewer.MultiFrameObject;
import org.samcrow.frameviewer.io3.DatabaseTrajectoryDataStore;

/**
 * Stores a trajectory from AnTracks
 * <p>
 * @author Sam Crow
 */
public class Trajectory implements MultiFrameObject, Iterable<Point> {

    public static enum MoveType {

        Unknown,
        Ascending,
        Descending,;

        /**
         * Returns the type corresponding to a name, but never
         * throws an exception. Returns Unknown if a valid value
         * could not be found.
         * <p>
         * @param name
         * @return
         */
        public static MoveType safeValueOf(String name) {
            try {
                return valueOf(name);
            }
            catch (Exception ex) {
                return Unknown;
            }
        }

    }

    /**
     * The first frame for which this trajectory has a position
     */
    private int firstFrame;

    /**
     * The last frame for which this trajectory has a position
     */
    private int lastFrame;

    private int id;

    private MoveType moveType;

    /**
     * The points in this trajectory.
     * <p>
     * The indexes in this array are set up so that index 0 corresponds
     * to firstFrame.
     * Generally, index n corresponds to frame firstFrame + n
     * <p>
     * The last valid index is firstFrame + (lastFrame - firstFrame)
     * = lastFrame
     */
    private final List<Point> points = new ArrayList<>();

    public Trajectory(int firstFrame, int lastFrame) {
        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;

        ensureCapacityForFrame(lastFrame);
    }

    public Trajectory(int firstFrame, int lastFrame, int id) {
        this(firstFrame, lastFrame);
        this.id = id;
    }

    public void setId(int newId) {
        id = newId;
    }

    public int getId() {
        return id;
    }

    /**
     * Returns a point for the given frame.
     * <p>
     * This method never throws an IndexOutOfBoundsException. If no point
     * exists for this frame, null is returned.
     * <p>
     * @param frame
     * @return
     */
    public Point get(int frame) {
        try {
            return points.get(frameNumberToIndex(frame));
        }
        catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Sets the point for the given frame
     * <p>
     * @param frame
     * @param newPoint
     */
    public void set(int frame, Point newPoint) {
        // Ensure that capacity is available for this point
        int index = frameNumberToIndex(frame);
        ensureCapacityForFrame(frame);
        updateLastFrame(frame);

        points.set(index, newPoint);
        // Set the correct frame for the point
        if (newPoint != null) {
            newPoint.setFrame(frame);
        }
    }

    @Override
    public int getFirstFrame() {
        return firstFrame;
    }

    @Override
    public int getLastFrame() {
        return lastFrame;
    }

    /**
     * Appends each point from the provided trajectory to this trajectory.
     * <p>
     * The start and end times of this trajectory are updated.
     * The provided trajectory is not modified.
     * <p>
     * If the first frame of the provided directory is before the last frame
     * of this trajectory, points will be removed from this trajectory
     * and replaced by points from the provided trajectory.
     * <p>
     * @param other The trajectory to append
     */
    public void append(Trajectory other) {

        // Replace each frame that overlaps
        if (other.firstFrame <= this.lastFrame) {
            // Iterate over interval [this.firstFrame, other.lastFrame]
            for (int frame = other.firstFrame; frame <= this.lastFrame; frame++) {
                this.set(frame, other.get(frame));
            }
        }
        // Copy points from other to this
        ensureCapacityForFrame(other.lastFrame);
        for (int frame = this.lastFrame + 1; frame <= other.lastFrame; frame++) {
            this.set(frame, other.get(frame));
        }
        // Update end frame
        this.lastFrame = other.lastFrame;
    }

    /**
     * Return an iterator that does not allow the list to be modified
     * and only iterates over the non-null points
     */
    @Override
    public Iterator<Point> iterator() {
        final Iterator<Point> underlying = points.iterator();

        return new NotNullIterator<>(underlying);
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    /**
     * Creates and returns a copy of the Point corresponding to the highest
     * numbered frame.
     * <p>
     * @throws IllegalStateException if this trajectory does not contain any
     * points
     * @return
     */
    public Point copyLastPoint() {
        return new Point(getLastPoint());
    }

    /**
     * Returns a reference to the Point corresponding to the highest numbered
     * frame.
     * <p>
     * @return
     */
    public Point getLastPoint() {
        // Find the last point
        // Create an iterator pointing past the last element
        ListIterator<Point> iter = points.listIterator(points.size());
        while (iter.hasPrevious()) {
            Point point = iter.previous();
            if (point != null) {
                return point;
            }
        }

        throw new IllegalStateException("The last point cannot be returned when no points are in the trajectory");
    }

    /**
     * Verifies that a frame number is within the expected range,
     * and converts it into a list index
     * <p>
     * @param frame
     * @return
     */
    private int frameNumberToIndex(int frame) {
        return frame - firstFrame;
    }

    private void ensureCapacityForFrame(int frame) {
        final int index = frameNumberToIndex(frame);

        while (points.size() <= index) {
            points.add(null);
        }
    }

    private void updateLastFrame(int frame) {
        // Correct the last frame
        if (lastFrame < frame) {
            lastFrame = frame;
        }
    }

    public void paint(GraphicsContext gc, double nativeImageWidth, double nativeImageHeight, double actualImageWidth, double actualImageHeight, double imageTopLeftX, double imageTopLeftY, int currentFrame, TrajectoryDisplayMode mode) {
        switch (mode) {
            case Full:
                paintFull(gc, nativeImageWidth, nativeImageHeight, actualImageWidth, actualImageHeight, imageTopLeftX, imageTopLeftY, currentFrame);
                break;
            case Hidden:
                // Do nothing
                break;
            case Interpolated:
                paintInterpolated(gc, nativeImageWidth, nativeImageHeight, actualImageWidth, actualImageHeight, imageTopLeftX, imageTopLeftY, currentFrame);
                break;
            case NearbyPoints:
                // TODO
                break;
        }
    }

    private void paintFull(GraphicsContext gc, double nativeImageWidth, double nativeImageHeight, double actualImageWidth, double actualImageHeight, double imageTopLeftX, double imageTopLeftY, int currentFrame) {
        Point2D lastLocation = null;
        for (Point point : this) {
            final double xRatio = point.getX() / nativeImageWidth;
            final double yRatio = point.getY() / nativeImageHeight;
            final double canvasX = imageTopLeftX + xRatio * actualImageWidth;
            final double canvasY = imageTopLeftY + yRatio * actualImageHeight;

            // Draw a line from the last point to this one
            if (lastLocation != null) {
                gc.setStroke(Color.LIGHTGREEN);
                gc.strokeLine(lastLocation.getX(), lastLocation.getY(), canvasX, canvasY);
            }

            final boolean hilighted = currentFrame == point.getFrame();
            // Draw the marker
            point.paint(gc, canvasX, canvasY, hilighted);

            lastLocation = new Point2D(canvasX, canvasY);
        }

    }

    private void paintInterpolated(GraphicsContext gc, double nativeImageWidth, double nativeImageHeight, double actualImageWidth, double actualImageHeight, double imageTopLeftX, double imageTopLeftY, int currentFrame) {

        // Do nothing if this frame is not within the range of this trajectory
        if (currentFrame < firstFrame || currentFrame > lastFrame) {
            return;
        }

        // Part 1: See if a point corresponds directly to this frame
        final Point point = get(currentFrame);
        if (point != null) {
            // Just draw this point, hilighted
            final Point2D pos = imageToCanvasPosition(new Point2D(point.getX(), point.getY()), nativeImageWidth, nativeImageHeight, actualImageWidth, actualImageHeight, imageTopLeftX, imageTopLeftY);
            point.paint(gc, pos.getX(), pos.getY(), true);
        }
        else {
            // Draw a non-hilighted point linearly interpolated between the before and after frames
            Point previousPoint = null;
            Point nextPoint = null;
            // Find the previous non-null point
            for (int pf = currentFrame - 1; pf >= firstFrame; pf--) {
                final Point attempt = get(pf);
                if (attempt != null) {
                    previousPoint = attempt;
                    break;
                }
            }
            // Find the next non-null point
            for (int nf = currentFrame + 1; nf <= lastFrame; nf++) {
                final Point attempt = get(nf);
                if (attempt != null) {
                    nextPoint = attempt;
                    break;
                }
            }
            assert previousPoint != null;
            assert nextPoint != null;

            final double ratio = (currentFrame - previousPoint.getFrame()) / (double) (nextPoint.getFrame() - previousPoint.getFrame());
            final double x = nextPoint.getX() * ratio + previousPoint.getX() * (1 - ratio);
            final double y = nextPoint.getY() * ratio + previousPoint.getY() * (1 - ratio);

            final Point2D canvasPos = imageToCanvasPosition(new Point2D(x, y), nativeImageWidth, nativeImageHeight, actualImageWidth, actualImageHeight, imageTopLeftX, imageTopLeftY);

            new Point(0, 0).paint(gc, canvasPos.getX(), canvasPos.getY(), false);

        }
    }

    private Point2D imageToCanvasPosition(Point2D imagePosition, double nativeImageWidth, double nativeImageHeight, double actualImageWidth, double actualImageHeight, double imageTopLeftX, double imageTopLeftY) {
        final double xRatio = imagePosition.getX() / nativeImageWidth;
        final double yRatio = imagePosition.getY() / nativeImageHeight;
        final double canvasX = imageTopLeftX + xRatio * actualImageWidth;
        final double canvasY = imageTopLeftY + yRatio * actualImageHeight;

        return new Point2D(canvasX, canvasY);
    }

    // Persistence section
    private DatabaseTrajectoryDataStore dataStore;

    public DatabaseTrajectoryDataStore getDataStore() {
        return dataStore;
    }

    public void setDataStore(DatabaseTrajectoryDataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Saves this trajectory and all its points
     * <p>
     * @throws java.io.IOException
     */
    public void save() throws IOException {
        if (dataStore != null) {
            try {
                dataStore.persistTrajectory(this);
            }
            catch (SQLException ex) {
                throw new IOException(ex);
            }
        }
        else {
            throw new IllegalStateException("Can't call save() on a Trajectory with no datastore defined");
        }
    }

}
