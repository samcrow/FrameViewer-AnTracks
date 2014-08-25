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
