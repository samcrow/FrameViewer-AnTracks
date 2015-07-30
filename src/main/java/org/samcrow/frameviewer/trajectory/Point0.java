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
