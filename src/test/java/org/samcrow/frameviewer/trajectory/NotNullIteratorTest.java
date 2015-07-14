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
