// <editor-fold defaultstate="collapsed" desc="License">
/*
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author samcrow
 */
public class NotNullIteratorTest {
    
    @Test
    public void testNoNulls() {
        final List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("hello");
        list.add("I");
        list.add("don't");
        list.add("know");
        list.add("why");
        list.add("you");
        list.add("say");
        list.add("goodbye");
        list.add("I");
        list.add("say");
        list.add("hello");
        
        NotNullIterator<String> iter = new NotNullIterator<>(list.iterator());
        
        Iterator<String> defaultIter = list.iterator();
        
        while(defaultIter.hasNext()) {
            assertTrue("NotNullIterator has next", iter.hasNext());
            
            assertEquals(defaultIter.next(), iter.next());
        }
    }
    
    @Test
    public void testAllNulls() {
        final List<String> list = new ArrayList<>();
        
        for(int i = 0; i < 10; i++) {
            list.add(null);
        }
        
        final NotNullIterator<String> iter = new NotNullIterator<>(list.iterator());
        
        assertFalse("No next element for all-null list", iter.hasNext());
        
    }
    
    @Test
    public void testMixed() {
        
        System.out.println("testMixed");
        
        final List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("hello");
        list.add(null);
        list.add(null);
        list.add("I");
        list.add("don't");
        list.add(null);
        list.add("know");
        list.add(null);
        list.add(null);
        list.add(null);
        list.add("why");
        list.add("you");
        list.add("say");
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add("goodbye");
        list.add(null);
        list.add("I");
        list.add("say");
        list.add("hello");
        list.add(null);
        
        final NotNullIterator<String> iter = new NotNullIterator<>(list.iterator());
        final Iterator<String> defaultIter = list.iterator();
        
        while(defaultIter.hasNext()) {
            String value = defaultIter.next();
            if(value != null) {
                // It should equal the NNI's next() value
                assertEquals(value, iter.next());
            }
            // If null, the NNI should not provide it
        }
        
        assertFalse(iter.hasNext());
        
    }
    
}
