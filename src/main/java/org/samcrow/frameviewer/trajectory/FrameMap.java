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
// </editor-fold>package org.samcrow.frameviewer.trajectory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.samcrow.frameviewer.FrameObject;

/**
 * A data structure that associates data with frame numbers.
 * 
 * A FrameMap has a first frame number, which is provided when it is constructed.
 * It can store data corresponding to the first frame and frames with greater
 * numbers.
 * 
 * Null values may not be stored.
 * 
 * @param <T> The type of data to store
 * @author Sam Crow
 */
public class FrameMap<T extends FrameObject> implements Iterable<T> {

    
    // Resizable array implementation
    
    /**
     * The first frame (lowest frame number) for which this object can contain
     * data.
     */
    private final int firstFrame;

    /**
     * The list of data.
     * 
     * For any frame number i, the associated data is in this list at index
     * i - firstFrame.
     */
    private final List<T> list;
    
    /**
     * Creates a new FrameMap with the specified first frame
     * @param firstFrame The first frame to store data for
     */
    public FrameMap(int firstFrame) {
	this.firstFrame = firstFrame;
	list = new ArrayList<>();
    }
    
    /**
     * Returns the item corresponding to the specified frame number, or null
     * if no item exists
     * @param frame the frame number to find an item for
     * @return The item corresponding to the requested frame, or null if no item
     * exists
     * @throws IndexOutOfBoundsException if frame is less than the first frame
     * of this object
     */
    public T get(int frame) {
	if(frame < firstFrame) {
	    throw new IndexOutOfBoundsException("Requested a frame before the first frame");
	}
	int index = frameToIndex(frame);
	if(index >= list.size()) {
	    return null;
	}
	return list.get(index);
    }
    
    /**
     * Associates the specified value with the specified frame
     * @param frame The frame to assign the value to
     * @param value The value to assign. The frame associated with this object
     * will be set to frame.
     * @throws IndexOutOfBoundsException if frame is less than the first frame
     * of this object
     * @throws NullPointerException if value is null
     */
    public void put(int frame, T value) {
	if(value == null) {
	    throw new NullPointerException("value must not be null");
	}
	if(frame < firstFrame) {
	    throw new IndexOutOfBoundsException("Requested a frame before the first frame");
	}
	int index = frameToIndex(frame);
	if(index >= list.size()) {
	    // Add null values to the list up to and including the index for this frame
	    for(int i = list.size(); i <= index; i++) {
		list.add(null);
	    }
	}
	value.setFrame(frame);
	list.set(index, value);
    }
    
    /**
     * Determines whether this frame map contains data for the specified frame.
     * 
     * @param frame the frame to check
     * @return true if this object has data for the specified frame number,
     * otherwise false
     */
    public boolean containsFrame(int frame) {
	if(frame < firstFrame) {
	    return false;
	}
	int index = frameToIndex(frame);
	if(index >= list.size()) {
	    return false;
	}
	return list.get(index) != null;
    }
    
    /**
     * Converts a frame to a list index
     * @param frame a frame number
     * @return the corresponding index in the list
     */
    private int frameToIndex(int frame) {
	return frame - firstFrame;
    }
    
    /**
     * Returns the frame number of the first valid frame
     * @return the first frame number
     */
    public int getFirstFrame() {
	return firstFrame;
    }

    @Override
    public Iterator<T> iterator() {
	return list.iterator();
    }
    
    
    @Override
    public int hashCode() {
	int hash = 7;
	hash = 79 * hash + this.firstFrame;
	hash = 79 * hash + Objects.hashCode(this.list);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final FrameMap<?> other = (FrameMap<?>) obj;
	if (this.firstFrame != other.firstFrame) {
	    return false;
	}
	if (!Objects.equals(this.list, other.list)) {
	    return false;
	}
	return true;
    }
}
