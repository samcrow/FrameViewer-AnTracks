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

/**
 * A caching adapter for a FrameSource
 * @author samcrow
 */
public class FrameCache implements FrameSource {

    /**
     * The frame source being wrapped
     */
    private final FrameSource inner;
    
    /**
     * The frame cache
     */
    private final Cache<Integer, FrameImage> cache;
    
    public FrameCache(FrameSource inner) {
	this.inner = inner;
	this.cache = new Cache<>((int index) -> {
	    return inner.getImage(index);
	});
    }
    
    
    @Override
    public int getFirstFrame() {
	return inner.getFirstFrame();
    }

    @Override
    public FrameImage getImage(int frameNumber) throws Exception {
	return cache.get(frameNumber);
    }

    @Override
    public int getMaximumFrame() {
	return inner.getMaximumFrame();
    }
    
}
