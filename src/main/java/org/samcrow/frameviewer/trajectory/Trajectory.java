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

import org.samcrow.frameviewer.MultiFrameObject;

/**
 * A sequence of frames and corresponding points, representing a path over time.
 *
 * A Trajectory's iterator must provide a point for each frame in the trajectory,
 * ordered by increasing frame number. The iterator must provide exactly
 * this.size() elements. For each entry e returned by the iterator,
 * this.get(e.frame) must compare equal to e.point.
 * 
 * A trajectory cannot contain any null points. A null value returned from
 * {@link #get(int)} indicates that the trajectory does not have a point for
 * the specified frame number
 *
 * @author Sam Crow
 * @param <P> The type of point that this trajectory stores
 */
public interface Trajectory<P> extends MultiFrameObject, Iterable<Trajectory.Entry<P>> {
    
    /**
     * Stores a frame number and an associated point
     * @param <T> The point type
     */
    public class Entry<T> {
	protected Entry(int frame, T point) {
	    this.frame = frame;
	    this.point = point;
	}
	/**
	 * The frame number
	 */
	public int frame;
	/**
	 * The point
	 */
	public T point;
    }

    /**
     * Sets the point associated with a frame in this trajectory.
     * 
     * Depending on the implementation, calling this method may cause other
     * frames in this trajectory to change.
     *
     * @param frame the frame to assign
     * @param point the point to assign
     * @return the point that was previously assigned to this frame, or null
     * if no point was associated with this frame
     *
     * @throws IndexOutOfBoundsException if frame is negative
     * @throws NullPointerException if point is null
     */
    public P put(int frame, P point);
    
    /**
     * Inserts all points from another trajectory into this trajectory.
     * 
     * If both this and the other trajectory have a point associated with any
     * frame number, the point from the other trajectory replaces the point
     * in this trajectory.
     * 
     * @param other the trajectory to copy points from
     */
    public void putAll(Trajectory<? extends P> other);

    /**
     * Returns the point associated with the requested frame
     *
     * @param frame the frame to get a point for
     * @return The point associated with the requested frame, or null if
     * no point is associated with this frame
     *
     * @throws IndexOutOfBoundsException if frame is negative
     */
    public P get(int frame);
    
    /**
     * Removes a point from this trajectory
     * @param frame the frame to remove a point from
     * @return the point that was removed, or null if no point was removed
     * 
     * @throws IndexOutOfBoundsException if frame is negative
     */
    public P remove(int frame);

    /**
     * Returns the frame number of the first frame in this trajectory
     *
     * @return the lowest frame number i for which this.get(i) would return a
     * non-null value and not throw any exceptions. If this trajectory is
     * empty, -1 is returned.
     */
    @Override
    public int getFirstFrame();

    /**
     * Returns the frame number of the last frame in this trajectory
     *
     * @return the highest frame number i for which this.get(i) would return a
     * non-null value and not throw any exceptions. If this trajectory is empty,
     * -1 is returned.
     */
    @Override
    public int getLastFrame();

    /**
     * Returns the number of points in this trajectory. This must be the same
     * as the number of distinct frame numbers for which this.get() returns a
     * non-null value.
     *
     * @return the number of points in this trajectory
     */
    public int size();

    /**
     * Determines if this trajectory contains no points
     *
     * @return true if this.size() == 0, otherwise false
     */
    public boolean isEmpty();

    /**
     * Finds and returns an entry containing the next point with a higher
     * frame number than the specified frame number.
     * @param frame the frame to find the next point after
     * @return An entry, or null if the end of the trajectory was reached
     */
    public Entry<P> findNext(int frame);
    
    /**
     * Finds and returns an entry containing the next point with a lower
     * frame number than the specified frame number.
     * @param frame the frame to find the nearest point before
     * @return An entry, or null if the beginning of the trajectory was reached
     */
    public Entry<P> findPrevious(int frame);
    
}
