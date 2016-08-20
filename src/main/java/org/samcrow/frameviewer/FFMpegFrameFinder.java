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

import java.awt.image.BufferedImage;
import java.io.File;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

/**
 * A frame source that uses JavaCV/FFMpeg to read frames directly from a video
 * file
 *
 * @author samcrow
 */
public class FFMpegFrameFinder implements FrameSource {

    /**
     * The frame source
     */
    private final FFmpegFrameGrabber frameGrabber;

    /**
     * The Frame->BufferedImage converter
     */
    private final Java2DFrameConverter converter;

    /**
     * The last frame that was returned
     */
    private int lastFrame = 0;

    public FFMpegFrameFinder(File file) throws FrameGrabber.Exception {
	frameGrabber = new FFmpegFrameGrabber(file);
	converter = new Java2DFrameConverter();
	frameGrabber.start();
    }

    @Override
    public int getFirstFrame() {
	return 1;
    }

    @Override
    public FrameImage getImage(int frameNumber) throws Exception {
	if (frameNumber < 1) {
	    throw new FrameIndexOutOfBoundsException(1, frameNumber,
		    getMaximumFrame());
	}

	if (frameNumber <= lastFrame) {
	    // Reopen file to go back to the beginning
	    frameGrabber.restart();
	    lastFrame = 0;
	}
	// Seek forward zero or more times to get to the next frame
	final int difference = (frameNumber - lastFrame) - 1;
	for (int i = 0; i < difference; i++) {
	    frameGrabber.grabFrame(false, true, false, false);
	}
	// Frame reader is now at the right position
	final Frame frame = frameGrabber.grabFrame(false, true, true, false);
	final BufferedImage bi = converter.convert(frame);

	
	final WritableImage image = SwingFXUtils.toFXImage(bi, null);
	
	
	lastFrame = frameNumber;
	// Change the width of the image to account for non-square pixels
	final double aspectRatio = 1920.0 / 1080.0;
	return new FrameImage(image, aspectRatio);
    }

    @Override
    public int getMaximumFrame() {
	return frameGrabber.getLengthInFrames();
    }

}
