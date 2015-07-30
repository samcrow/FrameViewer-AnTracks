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
package org.samcrow.tracking;



import java.awt.Point;
import java.awt.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

/**
 * Tracks objects in an image based on initial positions
 *
 * @author Sam Crow
 */
public class TemplateTracker {

    /**
     * Stores configuration for the tracker. Immutable.
     */
    public static class Config {

	public Config(int regionSize, int maxMovement) {
	    this.regionSize = regionSize;
	    this.maxMovement = maxMovement;
	}

	/**
	 * The width and height, in pixels, of the rectangular region used to
	 * track an object. This region should be slightly larger than the
	 * object being tracked.
	 */
	public final int regionSize;
	/**
	 * The maximum distance, in pixels, that the object is expected to move
	 * between frames. If the object moves horizontally or vertically by
	 * more than this distance in between frames, it will be lost.
	 */
	public final int maxMovement;
    }

    private Config config;

    /**
     * Creates a tracker using the specified configuration
     *
     * @param config The configuration to use
     * @throws NullPointerException if config is null
     */
    public TemplateTracker(Config config) {
	if (config == null) {
	    throw new NullPointerException("config must not be null");
	}
	this.config = config;
    }

    /**
     * Tracks an object based on a known position in one frame and returns its
     * most likely position in the next frame
     *
     * @param position The position of the center of the object in the initial
     * frame, with the origin in the upper left corner of the image
     * @param initialFrame The frame where the object was centered on
     * initialPosition
     * @param nextFrame The next frame to find the object's position in
     * @return The most likely center position of the object in nextFrame
     *
     * @throws IllegalArgumentException if either of the coordinates in
     * initialPosition is negative, if the X coordinate of initialPosition is
     * greater than or equal to the width of initialFrame, if the Y coordinate
     * of initialPosition is greater than or equal to the height of
     * initialFrame, or if initialFrame and nextFrame do not have the same width
     * and height
     * @throws NullPointerException if any argument is null
     */
    public Point track(Point position, Image initialFrame,
	    Image nextFrame) {
	if (position == null || initialFrame == null || nextFrame == null) {
	    throw new NullPointerException("No argument may be null");
	}
	// Check frame dimensions
	if (initialFrame.getWidth() != nextFrame.getWidth()
		|| initialFrame.getHeight() != nextFrame.getHeight()) {
	    throw new IllegalArgumentException(
		    "Frames must have the same dimensions");
	}
	if (position.getX() < 0 || position.getX() >= initialFrame.getWidth()) {
	    throw new IllegalArgumentException(
		    "Initial X coordinate out of range");
	}
	if (position.getY() < 0 || position.getY() >= initialFrame.getHeight()) {
	    throw new IllegalArgumentException(
		    "Initial Y coordinate out of range");
	}

	final Rectangle initialRegion = new Rectangle(position.x
		- config.regionSize / 2,
		position.y - config.regionSize / 2, config.regionSize,
		config.regionSize);

	final long initialSum = brightnessSum(initialFrame, initialRegion);

	// Consider center points up to config.maxMovement pixels from
	// the initial position.
	// Define a search region. All integer points in or on the edge of this
	// rectangle are checked
	final Rectangle searchRegion = new Rectangle(position.x
		- config.maxMovement / 2,
		position.y - config.maxMovement / 2, config.maxMovement,
		config.maxMovement);

	// The smallest absolute value difference in brightness sum
	long minDeviation = Long.MAX_VALUE;
	// Most likely location
	Point bestPoint = null;

	for (int x = searchRegion.x; x <= searchRegion.x + searchRegion.width; x++) {
	    for (int y = searchRegion.y; y <= searchRegion.y
		    + searchRegion.height; y++) {
		final Rectangle nextFrameRegion = new Rectangle(x
			- config.maxMovement / 2, y - config.maxMovement / 2,
			config.maxMovement, config.maxMovement);

		final long sum = brightnessSum(nextFrame, nextFrameRegion);
		final long deviation = Math.abs(sum - initialSum);

		if (deviation < minDeviation) {
		    minDeviation = deviation;
		    bestPoint = new Point(x, y);
		}
	    }
	}

	assert bestPoint != null;

	return bestPoint;
    }

    /**
     * Returns the sum of the red, green, and blue components of all the pixels
     * of an image within a region.
     *
     * If the region extends outside the bounds of the image, each pixel
     * outside the bounds of the image is considered to have a value of zero.
     *
     * @param image the image to sum. This must not be null.
     * @param region The region of the image to sum pixels from. This must not
     * be null.
     * @return The sum of components
     */
    private static long brightnessSum(Image image, Rectangle region) {
	final PixelReader reader = image.getPixelReader();
	long sum = 0;
	for (int y = region.y; y < region.y + region.height + 1; y++) {
	    for (int x = region.x; x < region.x + region.width + 1; x++) {
		if (x >= 0 && x < image.getWidth() && y >= 0 && y < image
			.getHeight()) {
		    int argb = reader.getArgb(x, y);
		    // Sum the red, green, and blue components to get a value
		    // between 0 and 765
		    long brightness = ((argb >> 16) & 0xFF) + ((argb >> 8) & 0xFF) + (argb & 0xFF);
		    sum += brightness;
		}
	    }
	}
	return sum;
    }

    /**
     * Returns the configuration used for tracking
     *
     * @return the configuration
     */
    public Config getConfig() {
	return config;
    }

    /**
     * Sets the configuration to use for tracking
     *
     * @param newConfig the configuration to use
     * @throws NullPointerException if newConfig is null
     */
    public void setConfig(Config newConfig) {
	if (config == null) {
	    throw new NullPointerException("newConfig must not be null");
	}
	config = newConfig;
    }
}
