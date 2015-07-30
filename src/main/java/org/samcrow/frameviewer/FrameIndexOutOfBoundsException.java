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

/**
 *
 * @author Sam Crow
 */
public class FrameIndexOutOfBoundsException extends IndexOutOfBoundsException {

    /**
     * The lowest-numbered frame that can be accessed
     */
    private final int firstFrame;

    /**
     * The frame that was requested and that led to this exception being thrown
     */
    private final int requestedFrame;

    /**
     * The highest-numbered frame that can be accessed
     */
    private final int lastFrame;

    /**
     * Constructor
     * @param firstFrame The minimum valid frame index
     * @param requestedFrame The frame index that was requested
     * @param lastFrame The maximum valid frame index
     */
    public FrameIndexOutOfBoundsException(int firstFrame, int requestedFrame, int lastFrame) {
        this.requestedFrame = requestedFrame;
        this.firstFrame = firstFrame;
        this.lastFrame = lastFrame;
    }

    public int getFirstFrame() {
        return firstFrame;
    }

    public int getLastFrame() {
        return lastFrame;
    }

    public int getRequestedFrame() {
        return requestedFrame;
    }

    @Override
    public String getMessage() {
        return "Frame index out of bounds: Lowest allowed index "+firstFrame+", highest allowed index "+lastFrame+". Frame "+requestedFrame+" was requested.";
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }
    
    
}
