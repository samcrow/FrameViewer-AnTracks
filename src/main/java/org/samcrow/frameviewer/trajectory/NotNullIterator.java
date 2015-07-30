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

/**
 * An iterator that iterates over the non-null elements of something
 * @author samcrow
 * @param <T> The type of element to iterate over
 */
class NotNullIterator<T> implements Iterator<T> {

    private final Iterator<T> underlying;

    public NotNullIterator(Iterator<T> underlying) {
        this.underlying = underlying;
        
        findNext();
    }

    T next;

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public T next() {
        T value = next;
        findNext();
        return value;
    }

    @Override
    public void remove() {
        underlying.remove();
        findNext();
    }
    
    private void findNext() {
        next = null;
        while(underlying.hasNext()) {
            next = underlying.next();
            
            if(next != null) {
                break;
            }
        }
    }
    
}
