package org.samcrow.frameviewer.trajectory;

import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.samcrow.frameviewer.FrameObject;

/**
 * A point in a trajectory
 * <p>
 * @author Sam Crow
 */
public class Point extends FrameObject {

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }
    
    // added by Jacob Davidson
    public void setTrajectoryID(int trajectoryid) {
        this.trajectoryid = trajectoryid;
    }

    /**
     * @return the activity
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(Activity activity) {
        if (activity != null) {
            this.activity = activity;
        }
        else {
            // Ensure not null
            this.activity = Activity.NotCarrying;
        }
    }

    public static enum Activity {

        NotCarrying,
        CarryingFood,
        CarryingSomethingElse,;

        /**
         * Returns the type corresponding to a name, but never
         * throws an exception. Returns NotCarrying if a valid value
         * could not be found.
         * <p>
         * @param name
         * @return
         */
        public static Activity safeValueOf(String name) {
            try {
                return valueOf(name);
            }
            catch (Exception ex) {
                return NotCarrying;
            }
        }

    }

    

    private int x;

    private int y;

    protected Activity activity;
    
    // added by Jacob Davidson
    private int trajectoryid;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a copy of another point
     * <p>
     * @param other
     */
    public Point(Point other) {
        super(other);
        this.x = other.x;
        this.y = other.y;
        this.activity = other.activity;
        this.frame = other.frame;
        this.trajectoryid = other.trajectoryid;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    // added by Jacob Davidson
    public int getTrajectoryID() {
        return trajectoryid;
    }

    public void paint(GraphicsContext gc, double canvasX, double canvasY, boolean hilighted) {
        final int RADIUS = 3;
        if (hilighted) {
            gc.setStroke(Color.RED);
        }
        else {
            gc.setStroke(Color.LIGHTGREEN);
        }
        gc.setLineWidth(2);
        gc.strokeOval(canvasX - RADIUS, canvasY - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }
    
    public void paintID(GraphicsContext gc, double canvasX, double canvasY, boolean hilighted) {
        if (hilighted) {
            gc.setStroke(Color.RED);
        }
        else {
            gc.setStroke(Color.LIGHTGREEN);
        }
        gc.setLineWidth(1);
        gc.strokeText(Integer.toString(trajectoryid), canvasX - 10, canvasY + 15);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.x;
        hash = 13 * hash + this.y;
        hash = 13 * hash + Objects.hashCode(this.activity);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.trajectoryid != other.trajectoryid) {
            return false;
        }
        return this.activity == other.activity;
    }
    
    
    

}
