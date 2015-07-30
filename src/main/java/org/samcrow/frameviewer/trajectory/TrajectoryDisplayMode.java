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

/**
 * Modes for the display of trajectories
 * @author Sam Crow
 */
public enum TrajectoryDisplayMode {
    
    /**
     * Every point of every trajectory is displayed
     */
    Full,
    
    /**
     * Only 2-3 points of a trajectory that are near the current frame are displayed
     */
    NearbyPoints,
    
    /**
     * Instead of displaying lines between points, a point showing the
     * linearly interpolated position of each ant at the current frame
     * is displayed
     */
    Interpolated,
    
    //added by Jacob
    
    InterpolatedwithIDs,
    
    /**
     * Trajectories not displayed
     */
    Hidden,
    
}
