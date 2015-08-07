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
package org.samcrow.frameviewer.track;

import javafx.scene.image.Image;
import org.samcrow.frameviewer.FrameFinder;
import org.samcrow.frameviewer.FrameIndexOutOfBoundsException;
import org.samcrow.frameviewer.trajectory.BasicTrajectory;
import org.samcrow.frameviewer.trajectory.Point;
import org.samcrow.frameviewer.trajectory.Point.Source;
import org.samcrow.frameviewer.trajectory.Trajectory;
import org.samcrow.tracking.TemplateTracker;
import org.samcrow.tracking.TemplateTracker.Config;

/**
 * Tracks ants between frames
 *
 * @author Sam Crow
 */
public class Tracker {

    /**
     * The tracker implementation
     */
    private final TemplateTracker tracker;

    /**
     * The source of frames
     */
    private final FrameFinder frameSource;

    public Tracker(FrameFinder frameSource, Config config) {
	this.frameSource = frameSource;
	this.tracker = new TemplateTracker(config);
    }

    /**
     * Tracks an ant forward from the provided point
     *
     * @param currentPosition The current position of the ant
     * @param currentFrameNumber The frame number where the ant is located at
     * currentPosition
     * @param nextFrameNumber The frame to find the next position of the ant in
     * @return A Point in the next frame indicating the most likely position
     * of the ant in the next frame, or null if no frame is available
     */
    public Point trackOne(Point currentPosition, int currentFrameNumber,
	    int nextFrameNumber) {
	try {
	    Image currentFrame = frameSource
		    .getImage(currentFrameNumber);
	    Image nextFrame = frameSource.getImage(nextFrameNumber);
	    java.awt.Point nextPosition = tracker.track(new java.awt.Point(
		    currentPosition.getX(), currentPosition.getY()),
		    currentFrame,
		    nextFrame);

	    // Give the new point the same settings but a different position
	    // and frame number
	    Point nextPoint = new Point(nextPosition.x, nextPosition.y, Source.Tracking);

	    return nextPoint;
	} catch (FrameIndexOutOfBoundsException e) {
	    return null;
	}
    }

    /**
     * Tracks an ant from a position in one frame through a sequence of frames
     *
     * @param startPos The position of the ant in the start frame
     * @param start the frame at which to start tracking
     * @param end the frame at which to end tracking
     * @return A trajectory with start as its start frame and end as its end
     * frame, with a position of the ant included for the start and end frames
     * and all frames in between
     */
    public Trajectory<Point> track(Point startPos, int start, int end) {
	
	Trajectory<Point> trajectory;
	if(start <= end) {
	    trajectory = trackForwards(startPos, start, end);
	}
	else {
	    trajectory = trackBackwards(startPos, start, end);
	}

	assert trajectory.size() == Math.abs(end - start) + 1;
	return trajectory;
    }
    
    private Trajectory<Point> trackForwards(Point startPos, int start, int end) {
	Trajectory<Point> trajectory = new BasicTrajectory<>();
	trajectory.put(start, startPos);
	Point lastPosition = startPos;
	// Track from start + 1 to end (inclusive)
	for (int frame = start + 1; frame <= end; frame++) {
	    final Point position = trackOne(lastPosition, frame - 1, frame);
	    trajectory.put(frame, position);

	    lastPosition = position;
	}
	assert trajectory.getFirstFrame() == start;
	assert trajectory.getLastFrame() == end;
	return trajectory;
    }
    
    private Trajectory<Point> trackBackwards(Point startPos, int start, int end) {
	Trajectory<Point> trajectory = new BasicTrajectory<>();
	trajectory.put(start, startPos);
	Point lastPosition = startPos;
	// Track from start - 1 to end (inclusive)
	for (int frame = start - 1; frame >= end; frame--) {
	    final Point position = trackOne(lastPosition, frame + 1, frame);
	    trajectory.put(frame, position);

	    lastPosition = position;
	}
	assert trajectory.getFirstFrame() == end;
	assert trajectory.getLastFrame() == start;
	return trajectory;
    }

    /**
     * Tracks an ant from a position in one frame to a position in another frame
     *
     * @param startPos the position of the ant in the start frame
     * @param start the start frame number
     * @param endPos the position of the ant in the end frame
     * @param end the end frame number
     * @param forwards An optional trajectory containing the results of forward
     * tracking from startFrame to endFrame. May be null. If not null, must
     * have a first frame &lt;= start and a last frame &gt;= end.
     * @return A trajectory with start as its start frame and end as its end
     * frame, with a position of the ant included for all frames in between,
     * starting at startPos and ending at endPos
     */
    public Trajectory<Point> trackBidirectional(Point startPos,
	    int start, Point endPos, int end, Trajectory<Point> forwards) {

	// Track forwards and backwards
	if(forwards == null) {
	    forwards = track(startPos, start, end);
	}
	assert forwards.getFirstFrame() <= start;
	assert forwards.getLastFrame() >= end;
	Trajectory<? extends Point> backwards = track(endPos, end, start);

	// Find the frame with the minimum squared distance between tracked points
	int minDistanceSquared = Integer.MAX_VALUE;
	int bestSplitFrame = -1;
	for (int frame = start; frame <= end; frame++) {
	    Point forwardsPoint = forwards.get(frame);
	    Point backwardsPoint = backwards.get(frame);
	    int dx = forwardsPoint.getX() - backwardsPoint.getX();
	    int dy = forwardsPoint.getY() - backwardsPoint.getY();
	    int distanceSquared = dx * dx + dy * dy;

	    if (distanceSquared < minDistanceSquared) {
		minDistanceSquared = distanceSquared;
		bestSplitFrame = frame;
	    }
	}
	assert bestSplitFrame != -1;

	// Create a merged trajectory
	Trajectory<Point> merged = new BasicTrajectory<>();
	for (int frame = start; frame <= end; frame++) {
	    if (frame < bestSplitFrame) {
		merged.put(frame, forwards.get(frame));
	    } else if (frame > bestSplitFrame) {
		merged.put(frame, backwards.get(frame));
	    } else {
		// frame == bestSplitFrame
		// Insert the midpoint between the forwards and backwards point
		Point forwardsPoint = forwards.get(frame);
		Point backwardsPoint = backwards.get(frame);

		Point midpoint = new Point((forwardsPoint.getX()
			+ backwardsPoint.getX()) / 2, (forwardsPoint.getY()
			+ backwardsPoint.getY()) / 2, Source.Tracking);
		merged.put(frame, midpoint);
	    }
	}

	assert merged.getFirstFrame() == start;
	assert merged.getLastFrame() == end;
	assert merged.size() == end - start + 1;
	return merged;
    }
}
