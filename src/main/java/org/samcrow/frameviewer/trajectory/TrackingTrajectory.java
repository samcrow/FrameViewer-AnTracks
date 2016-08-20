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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.samcrow.frameviewer.track.Tracker;

/**
 * A trajectory that consists of some user-defined points and interpolated
 * points
 * between all user-defined points calculated by tracking ants.
 *
 * When a point is added to this trajectory, points with a source of
 * {@link org.samcrow.frameviewer.trajectory.Point.Source#Tracking} are added
 * so that this trajectory contains a point for each frame between its first
 * frame and its last frame.
 *
 * @author Sam Crow
 * @param <P> The point type to use
 */
public class TrackingTrajectory<P extends Point> extends BasicTrajectory<P> {

    /**
     * An interface for something that can create a point
     *
     * @param <T> the type of point
     */
    public interface PointCreator<T> {

	/**
	 * Creates a point with the specified X and Y coordinates and source.
	 *
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @param source The source
	 * @param previous The user-defined point before the point to be created
	 * @param next The user-defined point after the point to be created.
	 * This may be null.
	 * @return A point of type T with the specified coordinates,
	 */
	public T newPoint(int x, int y, Point.Source source, T previous, T next);
    }

    /**
     * The tracker
     */
    private Tracker tracker;

    /**
     * Stores points after the last frame
     */
    private Trajectory<P> pastEnd;

    /**
     * Used to create points
     */
    private final PointCreator<P> pointCreator;

    public TrackingTrajectory(Tracker tracker, PointCreator<P> pointCreator) {
	super();
	if (pointCreator == null) {
	    throw new NullPointerException("pointCreator must not be null");
	}

	this.tracker = tracker;
	this.pointCreator = pointCreator;
	this.pastEnd = null;
    }

    @Override
    public void putAll(
	    Trajectory<? extends P> other) {
	// Ensure that this is implemented using put
	for (Entry<? extends P> entry : other) {
	    put(entry.frame, entry.point);
	}
    }

    @Override
    public P remove(int frame) {
	P removed = super.remove(frame); //To change body of generated methods, choose Tools | Templates.

	if (removed != null) {
	    // Find the previous and next user-defined frames
	    Entry<P> next = findNextUserDefined(frame);
	    Entry<P> previous = findPreviousUserDefined(frame);

	    if (next != null && previous != null) {
		// Removed point was not at an end
		// Recalculate
		updateTracking(previous.frame, previous.point, next.frame,
			next.point);
	    } else {
		// Removed point was at an end
		// Remove any non-user-defined points
		if (next == null) {
		    // Remove preceding tracked points
		    Entry<P> entry = findPrevious(frame);
		    while (entry != null) {
			if (entry.point.getSource() != Point.Source.Tracking) {
			    break;
			}
			super.remove(entry.frame);
			entry = findPrevious(entry.frame);
		    }
		    // Clear the past-end trajectory
		    pastEnd = null;
		}
		if (previous == null) {
		    // Remove following tracked points
		    Entry<P> entry = findNext(frame);
		    while (entry != null) {
			if (entry.point.getSource() != Point.Source.Tracking) {
			    break;
			}
			super.remove(entry.frame);
			entry = findNext(entry.frame);
		    }
		}
	    }
	}

	return removed;
    }

    @Override
    public P put(int frame, P point) {
	P result = super.put(frame, point); //To change body of generated methods, choose Tools | Templates.

	Entry<P> previous = findPreviousUserDefined(frame);
	Entry<P> next = findNextUserDefined(frame);
	if (previous != null) {
	    updateTracking(previous.frame, previous.point, frame, point);
	}
	if (next != null) {
	    updateTracking(frame, point, next.frame, next.point);
	} else {
	    // No next user-defined point
	    // Clear past-end trajectory
	    pastEnd = null;
	}

	return result;
    }

    /**
     * Updates this trajectory with points based on tracking between two points
     *
     * @param startFrame The frame with a user-defined point to start tracking
     * at
     * @param startPos The position at frame startFrame
     * @param endFrame The frame with a user-defined point to stop tracking at
     * @param endPos The position at frame endFrame
     */
    private void updateTracking(int startFrame, P startPos, int endFrame,
	    P endPos) {
	if (tracker != null) {
	    try {
		// See if the past-end trajectory contains points from startFrame
		// to endFrame
		// If so, it can be used instead of doing the forward tracking again
		Trajectory<Point> pastEndCopy = null;
		if(pastEnd != null) {
		    if(pastEnd.getFirstFrame() <= (startFrame + 1) && pastEnd.getLastFrame() >= endFrame) {
			pastEndCopy = new BasicTrajectory<>(pastEnd);
			// Insert the last user-defined point
			final int lastUserFrame = this.getLastFrame();
			pastEndCopy.put(lastUserFrame, this.get(lastUserFrame));
		    }
		}
		
		Trajectory<Point> track = tracker.trackBidirectional(startPos,
			startFrame, endPos, endFrame, pastEndCopy);
		for (Entry<Point> entry : track) {
		    if (entry.frame > startFrame && entry.frame < endFrame) {
			super.put(entry.frame, pointCreator.newPoint(entry.point
				.getX(),
				entry.point.getY(), Point.Source.Tracking,
				startPos, endPos));
		    }
		}
	    } catch (Exception ex) {
		Logger.getLogger(TrackingTrajectory.class.getName())
			.log(Level.SEVERE, "Failed to get frames for tracking", ex);
	    }
	}
    }

    private Entry<P> findNextUserDefined(int frame) {
	int currentFrame = frame;
	while (true) {
	    Entry<P> next = findNext(currentFrame);
	    if (next == null) {
		return null;
	    }
	    if (next.point.getSource() == Point.Source.User) {
		return next;
	    }
	    currentFrame = next.frame;
	}
    }

    private Entry<P> findPreviousUserDefined(int frame) {
	int currentFrame = frame;
	while (true) {
	    Entry<P> prev = findPrevious(currentFrame);
	    if (prev == null) {
		return null;
	    }
	    if (prev.point.getSource() == Point.Source.User) {
		return prev;
	    }
	    currentFrame = prev.frame;
	}
    }

    /**
     * Returns the location of the ant in the requested frame.
     *
     * This method works for frame numbers greater than the last frame of this
     * trajectory. For frames beyond the end of the trajectory, the position
     * of the ant will be calculated by analyzing frames.
     *
     * For frame numbers less than or equal to the last frame of this
     * trajectory,
     * this method functions like {@link #get(int)}.
     *
     * @param frame the frame number to get
     * @return The position of the ant in the requested frame, or null if the
     * requested frame number is greater than the highest frame number available
     * from the frame provider
     *
     * @throws IndexOutOfBoundsException if frame is negative
     */
    public P getWithTracking(int frame) {
	if (frame < 0) {
	    throw new IndexOutOfBoundsException(
		    "Frame numbers cannot be negative");
	}
	if (frame <= getLastFrame()) {
	    return get(frame);
	}
	updatePastEnd(frame);
	return pastEnd.get(frame);
    }

    /**
     * Ensures that the past-end trajectory is not null and has points for
     * frames up to and including the requested frame
     *
     * @param frameTo the frame to ensure that a point is available for
     */
    private void updatePastEnd(int frameTo) {
	if (tracker != null) {
	    try {
		if (pastEnd == null) {
		    pastEnd = new BasicTrajectory<>();
		    pastEnd.put(getLastFrame(), get(getLastFrame()));
		}
		// Add lastFrame + 1 to the past end trajectory
		final int lastFrame = pastEnd.getLastFrame();
		final P lastPoint = pastEnd.get(lastFrame);
		if(lastFrame >= frameTo) {
		    return;
		}
		
		P onePastEnd = newPoint(tracker.trackOne(lastPoint, lastFrame,
			lastFrame + 1), lastPoint, null);
		pastEnd.put(lastFrame + 1, onePastEnd);
		
		P previousPoint = onePastEnd;
		for (int frame = lastFrame + 2; frame <= frameTo; frame++) {
		    P currentPoint = newPoint(tracker.trackOne(previousPoint,
			    frame - 1, frame), lastPoint, null);
		    pastEnd.put(frame, currentPoint);
		    
		    previousPoint = currentPoint;
		}
	    } catch (Exception ex) {
		Logger.getLogger(TrackingTrajectory.class.getName())
			.log(Level.SEVERE, null, ex);
	    }
	}
    }

    public Tracker getTracker() {
	return tracker;
    }

    public void setTracker(Tracker tracker) {
	this.tracker = tracker;
    }

    private P newPoint(Point other, P previous, P next) {
	return pointCreator.newPoint(other.x, other.y, Point.Source.Tracking,
		previous, next);
    }
}
