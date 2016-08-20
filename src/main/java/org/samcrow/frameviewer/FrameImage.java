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

import java.util.Objects;
import javafx.scene.image.Image;

/**
 * An image with a preferred display aspect ratio
 * @author samcrow
 */
public class FrameImage {
    /**
     * The inner image
     */
    private final Image image;
    /**
     * The display aspect ratio
     */
    private final double aspectRatio;

    public FrameImage(Image image, double aspectRatio) {
	Objects.requireNonNull(image);
	this.image = image;
	this.aspectRatio = aspectRatio;
    }
    
    public double getDisplayHeight() {
	return image.getHeight();
    }
    
    public double getDisplayWidth() {
	return image.getHeight() * aspectRatio;
    }

    public Image getImage() {
	return image;
    }
}
