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

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * A map with a configurable maximum capacity that removes old items when this
 * capacity is exceeded. The item that was accessed the least recently is the
 * first item to be removed.
 * @param <K> The key type
 * @param <V> The value type
 * @author Sam Crow
 */
public class CachingMap <K, V> extends LinkedHashMap <K, V> {
    
    /**
     * The maximum capacity of this map
     */
    private final int capacity;
    
    /**
     * The default maximum capacity
     */
    private static final int DEFAULT_CAPACITY = 100;
    
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    private static final int DEFAULT_INITIAL_CAPACITY = 100;

    /**
     * Constructor
     * @param initialCapacity The initial capacity of this map. The map can expand
     * beyond this capacity.
     * @param capacity The capacity of this map. If the map exceeds this capacity,
     * old items will be removed.
     */
    public CachingMap(int initialCapacity, int capacity) {
        super(initialCapacity, 0.75f, true);
        this.capacity = capacity;
    }
    
    /**
     * Constructor
     * @param capacity The capacity of this map. If the map exceeds this capacity,
     * old items will be removed.
     */
    public CachingMap(int capacity) {
        super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, true);
        this.capacity = capacity;
    }
    
    /**
     * Constructor. The capacity will be initialized to the default value of 100.
     */
    public CachingMap() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Returns true to clear the oldest entry when the current size exceeds
     * the capacity
     * @param eldest
     * @return 
     */
    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        boolean remove = size() > capacity;
        return remove;
    }
}
