// </editor-fold>
package org.samcrow.frameviewer;

import javafx.scene.image.Image;

/**
 * An interface for things that can provide frames
 * @author samcrow
 */
public interface FrameSource {

    /**
     * @return The lowest frame number that is available
     */
    int getFirstFrame();

    /**
     * Reads the requested frame from the cache or from the file system
     * and returns it
     * @param frameNumber The 1-based frame index to get
     * @return An image for the frame
     * @throws java.lang.Exception if an error occurs
     * @throws FrameIndexOutOfBoundsException if the frame number is out of range
     */
    FrameImage getImage(int frameNumber) throws Exception;

    /**
     * @return The highest frame number that is available
     */
    int getMaximumFrame();
    
}
