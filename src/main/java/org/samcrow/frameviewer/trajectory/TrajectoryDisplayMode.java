package org.samcrow.frameviewer.trajectory;

/**
 * Modes for the display of trajectories
 * @author Sam Crow
 */
public enum TrajectoryDisplayMode {
    
    /**
     * Trajectories not displayed
     */
    Hidden,
    
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
    
}
