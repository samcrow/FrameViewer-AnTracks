package org.samcrow.frameviewer;

/**
 * An object that can be associated with a frame of video
 * @author samcrow
 */
public class FrameObject {

    public FrameObject() {}
    
    public FrameObject(FrameObject other) {
        this.frame = other.frame;
    }
    
    /**
     * The frame number at which this object exists
     */
    protected int frame;

    
    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }
    
}
