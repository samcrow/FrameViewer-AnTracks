package org.samcrow.frameviewer.trajectory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.samcrow.frameviewer.MultiFrameObject;

/**
 * Stores a trajectory from AnTracks
 * <p>
 * @author Sam Crow
 */
public class Trajectory implements MultiFrameObject, Iterable<Point> {

    public static enum MoveType {
        Unknown,
        Ascending,
        Descending,
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
     * Returns a point for the given frame
     * <p>
     * @param frame
     * @return
     */
    public Point get(int frame) {
        validateFrame(frame);
        return points.get(frameNumberToIndex(frame));
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

        points.set(index, newPoint);
        // Set the correct frame for the point
        if(newPoint != null) {
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
     * Creates and returns a copy of the Point that was most recently added.
     * @throws IllegalStateException if this trajectory does not contain any points
     * @return 
     */
    public Point copyLastPoint() {
        // Find the last point
        // Create an iterator pointing past the last element
        ListIterator<Point> iter = points.listIterator(points.size());
        while(iter.hasPrevious()) {
            Point point = iter.previous();
            if(point != null) {
                return new Point(point);
            }
        }
        
        throw new IllegalStateException("A copy of the last point cannot be returned when no points are in the trajectory");
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

    private void validateFrame(int frame) {
        if (frame < firstFrame) {
            throw new IndexOutOfBoundsException("Requested a trajectory point for frame " + frame + ", but this trajectory does not begin until frame " + firstFrame);
        }
        if (frame > lastFrame) {
            throw new IndexOutOfBoundsException("Requested a trajectory point for frame " + frame + ", but this trajectory ends at frame " + lastFrame);
        }
    }

    public void paint(GraphicsContext gc, double nativeImageWidth, double nativeImageHeight, double actualImageWidth, double actualImageHeight,
            double imageTopLeftX, double imageTopLeftY, int currentFrame) {

        Point2D lastLocation = null;
        for(Point point : this) {
            final double xRatio = point.getX() / nativeImageWidth;
            final double yRatio = point.getY() / nativeImageHeight;
            final double canvasX = imageTopLeftX + xRatio * actualImageWidth;
            final double canvasY = imageTopLeftY + yRatio * actualImageHeight;
            
            // Draw a line from the last point to this one
            if(lastLocation != null) {
                gc.setStroke(Color.LIGHTGREEN);
                gc.strokeLine(lastLocation.getX(), lastLocation.getY(), canvasX, canvasY);
            }
            
            final boolean hilighted = currentFrame == point.getFrame();
            // Draw the marker
            point.paint(gc, canvasX, canvasY, hilighted);
            
            lastLocation = new Point2D(canvasX, canvasY);
        }
        
    }

}
