package org.samcrow.frameviewer;

/**
 * An interface for an object that is present on multiple frames
 * @author samcrow
 */
public interface MultiFrameObject {
    /**
     * @return The first frame on which this object should be displayed
     */
    public int getFirstFrame();
    
    /**
     * 
     * @return The last frame on which this object should be displayed
     */
    public int getLastFrame();
}
