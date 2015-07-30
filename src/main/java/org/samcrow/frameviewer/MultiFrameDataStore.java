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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValueBase;

/**
 * Stores data associated with frames. Each frame can have any number
 * of data objects associated with it.
 * <p/>
 * If this class is provided with a value that extends {@link FrameObject},
 * the value's frame will be set to match the frame at which it was added.
 * <p/>
 * This class is observable, so other classes can be notified when it changes.
 * This can be used to keep track of data save status.
 * <p/>
 * @param <T> The type of data to store.
 * @author Sam Crow
 */
public class MultiFrameDataStore<T extends MultiFrameObject> extends ObservableValueBase<MultiFrameDataStore<T>> implements Iterable<T> {

    protected final List<T> data = createList();

    /**
     * The current frame for which data is returned
     */
    private final IntegerProperty currentFrame = new SimpleIntegerProperty();
    
    public MultiFrameDataStore() {
        
    }
    
    /**
     * 
     * @return All the objects that should be displayed for this frame
     */
    public List<T> getObjectsForCurrentFrame() {
        return getObjectsNearCurrentFrame(0);
    }
    
    /**
     * 
     * @param range
     * @return A list of object that should be displayed for the current frame, or 
     * for a frame within range of the current frame
     */
    public List<T> getObjectsNearCurrentFrame(int range) {
        List<T> list = new LinkedList<>();
        
        for(T object : this) {
            final int firstFrameDifference = object.getFirstFrame() - getCurrentFrame();
            final int lastFrameDifference = getCurrentFrame() - object.getLastFrame();
            
            if(firstFrameDifference < range && lastFrameDifference < range) {
                list.add(object);
            }
        }
        
        return list;
    }

    /**
     * Fills the list to ensure that the list has a value for the given index.
     * Null values will be inserted as necessary.
     * <p/>
     * @param lastIndex The index to ensure a value for
     */
    private void fillList(int lastIndex) {
        while (data.size() < lastIndex + 1) {
            data.add(null);
        }
    }
    
    public void add(T object) {
       data.add(object);
    }

    public final IntegerProperty currentFrameProperty() {
        return currentFrame;
    }

    public final int getCurrentFrame() {
        return currentFrame.get();
    }

    public final void setCurrentFrame(int newFrame) {
        currentFrame.set(newFrame);
    }

    /**
     * An invalidation listener that invalidates this data store
     */
    private InvalidationListener invalidationListener;

    /**
     * Creates a list and configures it to invalidate this data store when it
     * changes
     * <p/>
     * @param <T2> The type of element to store in the list
     * @return
     */
    private <T2> List<T2> createList() {
        return new ArrayList<>();
    }

    /**
     * Returns an iterator over all the lists of data for which this data
     * store has a value. This iterator supports all the optional operations.
     * <p/>
     * @return an iterator
     */
    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    @Override
    public MultiFrameDataStore getValue() {
        return this;
    }

    /**
     * 
     * @return The sets of data that this data store contains
     */
    protected List<T> getList() {
        return data;
    }
    

}
