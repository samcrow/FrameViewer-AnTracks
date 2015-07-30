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
package org.samcrow.frameviewer;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;

/**
 * A canvas that provides a mechanism for requesting repaints
 * @author Sam Crow
 */
public abstract class PaintableCanvas extends Canvas {
    
    /**
     * This method is called to paint the contents of a canvas.
     * This is only called from the JavaFX application thread.
     */
    protected abstract void paint();
    
    /**
     * Causes this canvas to be painted. This method can safely be
     * called from any thread.
     */
    public void repaint() {
        if(!Platform.isFxApplicationThread()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    paint();
                }
            });
        }
        else {
            paint();
        }
    }
}
