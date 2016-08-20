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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

/**
 * A frame source that uses JavaCV/FFMpeg to read frames directly from a video file
 * @author samcrow
 */
public class FFMpegFrameFinder implements FrameSource {

    private final FrameGrabber frameGrabber;
    
    private final Java2DFrameConverter converter;
    
    public FFMpegFrameFinder(File file) {
	frameGrabber = new FFmpegFrameGrabber(file);
	converter = new Java2DFrameConverter();
    }
    
    @Override
    public int getFirstFrame() {
	return 1;
    }

    @Override
    public Image getImage(int frameNumber) {
	try {
	    frameGrabber.setFrameNumber(frameNumber);
	    final Frame frame = frameGrabber.grab();
	    final BufferedImage bi = converter.convert(frame);
	    return SwingFXUtils.toFXImage(bi, null);
	} catch (FrameGrabber.Exception ex) {
	    Logger.getLogger(FFMpegFrameFinder.class.getName())
		    .log(Level.SEVERE, "Failed to set frame number", ex);
	    return null;
	}
    }

    @Override
    public int getMaximumFrame() {
	return frameGrabber.getLengthInFrames();
    }
    
}
