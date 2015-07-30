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

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * A simple trajectory that contains only the points that have been inserted.
 *
 * @author Sam Crow
 * @param <P> The point type
 */
public class BasicTrajectory<P> implements Trajectory<P> {

    /**
     * A mapping from frame numbers to points, ordered by frame number
     */
    private final NavigableMap<Integer, P> map;

    public BasicTrajectory() {
	map = new TreeMap<>();
    }

    @Override
    public P put(int frame, P point) {
	if (frame < 0) {
	    throw new IndexOutOfBoundsException(
		    "Frame number must not be negative");
	}
	if (point == null) {
	    throw new NullPointerException("Point must not be null");
	}
	return map.put(frame, point);
    }

    @Override
    public P get(int frame) {
	if (frame < 0) {
	    throw new IndexOutOfBoundsException(
		    "Frame number must not be negative");
	}
	return map.get(frame);
    }

    @Override
    public int getFirstFrame() {
	if (!isEmpty()) {
	    return map.firstKey();
	} else {
	    return -1;
	}
    }

    @Override
    public int getLastFrame() {
	if (!isEmpty()) {
	    return map.lastKey();
	} else {
	    return -1;
	}
    }

    @Override
    public int size() {
	return map.size();
    }

    @Override
    public boolean isEmpty() {
	return map.isEmpty();
    }

    @Override
    public Iterator<Entry<P>> iterator() {
	return new Iterator<Entry<P>>() {

	    private final Iterator<Map.Entry<Integer, P>> underlying = map
		    .entrySet().iterator();

	    @Override
	    public boolean hasNext() {
		return underlying.hasNext();
	    }

	    @Override
	    public Entry<P> next() {
		Map.Entry<Integer, P> entry = underlying.next();
		return new Entry<>(entry.getKey(), entry.getValue());
	    }
	};
    }

    @Override
    public P remove(int frame) {
	return map.remove(frame);
    }

    @Override
    public void putAll(Trajectory<? extends P> other) {
	for(Entry<? extends P> entry : other) {
	    put(entry.frame, entry.point);
	}
    }

    @Override
    public Entry<P> findNext(int frame) {
	Map.Entry<Integer, P> entry = map.ceilingEntry(frame + 1);
	if(entry != null) {
	    return new Entry(entry.getKey(), entry.getValue());
	}
	else {
	    return null;
	}
    }

    @Override
    public Entry<P> findPrevious(int frame) {
	Map.Entry<Integer, P> entry = map.floorEntry(frame - 1);
	if(entry != null) {
	    return new Entry(entry.getKey(), entry.getValue());
	}
	else {
	    return null;
	}
    }
    
}
