package org.samcrow.frameviewer.trajectory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A point in a trajectory
 * <p>
 * @author Sam Crow
 */
public class Point0 extends Point {
    
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

    protected Activity activity;

    public Point0(int x, int y, Source source) {
        super(x, y, source);
    }
    
    public void setX(int newX) {
	x = newX;
    }
    
    public void setY(int newY) {
	y = newY;
    }

    /**
     * Constructs a copy of another point
     * <p>
     * @param other
     */
    public Point0(Point0 other) {
        super(other);
        this.activity = other.activity;
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
}
