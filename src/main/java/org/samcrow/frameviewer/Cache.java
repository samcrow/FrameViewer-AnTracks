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

import java.lang.ref.SoftReference;
import java.util.Map;

/**
 * Caches several values that can be referred to by their indexes
 * @param <K> The type of key to use to access elements
 * @param <V> The type of value to store
 * @author Sam Crow
 */
public class Cache <K, V> {
    
    private final CacheSource<? extends V> source;
    
    private final Map<Integer, SoftReference<V>> map;
    
    
    /**
     * The maximum number of objects that this cache should maintain
     */
    private static final int MAX_CACHE_COUNT = 128;
    
    public Cache(int initialCapacity, CacheSource<? extends V> cache) {
        map = new CachingMap<>(initialCapacity, MAX_CACHE_COUNT);
        
        this.source = cache;
    }
    
    public Cache(CacheSource<? extends V> cache) {
        this.source = cache;
        map = new CachingMap<>(MAX_CACHE_COUNT);
    }
    
    /**
     * Returns a value
     * @param index The index to get a value for
     * @return
     */
    public synchronized V get(int index) throws Exception {
        load(index);
        SoftReference<V> ref = map.get(index);
        if(ref == null) {
            return null;
        }
        else {
            return ref.get();
        }
    }
    
    /**
     * Determines if this cache has a cached value for the given index
     * @param index
     * @return 
     */
    public synchronized boolean contains(int index) {
        SoftReference<V> ref = map.get(index);
        if(ref == null) {
            return false;
        }
        if(ref.get() == null) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Clears the value at a given index
     * @param index The index to clear
     */
    public synchronized void clearAt(int index) {
        
        SoftReference<V> ref = map.get(index);
        
        if(ref != null) {
            ref.clear();
            ref.enqueue();
            //Remove this reference from the list
            map.remove(index);
        }
    }
    
    /**
     * Ensures that this cache has an entry for the given index
     * @param index The index to load
     */
    public synchronized void load(int index) throws Exception {
	SoftReference<V> ref = map.get(index);
	if(ref == null) {
	    ref = new SoftReference<>(source.load(index));
	    map.put(index, ref);
	}
	if(ref.get() == null) {
	    ref = new SoftReference<>(source.load(index));
	    map.put(index, ref);
	}
    }
    
    /**
     * Sets the cached object for a given index
     * @param index The index to set the value for
     * @param value The value to set
     */
    public synchronized void set(int index, V value) {
        SoftReference<V> ref = new SoftReference<>(value);
        map.put(index, ref);
    }

    
    /**
     * An interface for something that can provide an image to add to the cache
     * @param <T2> The type of image
     */
    public interface CacheSource<T2> {
        
        /**
         * Loads and returns an object identified by the given index
         * @param index the 0-based index to return
         * @return the object
         * @throws Exception  
         */
        public T2 load(int index) throws Exception;
        
    }
}
