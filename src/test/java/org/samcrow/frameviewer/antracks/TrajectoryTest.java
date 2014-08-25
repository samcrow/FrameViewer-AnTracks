package org.samcrow.frameviewer.antracks;

import java.util.Iterator;
import junit.framework.TestCase;

/**
 *
 * @author samcrow
 */
public class TrajectoryTest extends TestCase {
    
    public TrajectoryTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testSetGet() {
        Trajectory t = new Trajectory(0, 1);
        
        final Trajectory.Point point1 = new Trajectory.Point(1, 2);
        final Trajectory.Point point2 = new Trajectory.Point(3, 4);
        
        t.set(0, point1);
        t.set(1, point2);
        
        assertEquals(t.get(0), point1);
        assertEquals(t.get(1), point2);
        
    }
    
    public void testIterator() {
        
        Trajectory t = new Trajectory(100, 200);
        
        final Trajectory.Point point1 = new Trajectory.Point(3, 9);
        final Trajectory.Point point2 = new Trajectory.Point(53, 90);
        
        t.set(120, point1);
        t.set(200, point2);
        
        Iterator<Trajectory.Point> iter = t.iterator();
        assertTrue(iter.hasNext());
        assertEquals(iter.next(), point1);
        assertTrue(iter.hasNext());
        assertEquals(iter.next(), point2);
        
        assertFalse(iter.hasNext());
        
    }
    
    public void testAppend() {
        
        Trajectory t1 = new Trajectory(1, 5);
        t1.set(1, new Trajectory.Point(1, 1));
        t1.set(2, new Trajectory.Point(1, 2));
        t1.set(3, new Trajectory.Point(1, 3));
        t1.set(4, new Trajectory.Point(1, 4));
        t1.set(5, new Trajectory.Point(1, 5));
        
        Trajectory t2 = new Trajectory(5, 10);
        t2.set(5, new Trajectory.Point(2, 5));
        t2.set(6, new Trajectory.Point(2, 6));
        t2.set(7, new Trajectory.Point(2, 7));
        t2.set(8, new Trajectory.Point(2, 8));
        t2.set(9, new Trajectory.Point(2, 9));
        t2.set(10, new Trajectory.Point(2, 10));
        
        t1.append(t2);
        
        Iterator<Trajectory.Point> iter = t1.iterator();
        
        assertTrue(iter.hasNext());
        assertEquals(iter.next(), new Trajectory.Point(1, 1));
        assertTrue(iter.hasNext());
        assertEquals(iter.next(), new Trajectory.Point(1, 2));
        assertTrue(iter.hasNext());
        assertEquals(iter.next(), new Trajectory.Point(1, 3));
        assertTrue(iter.hasNext());
        assertEquals(iter.next(), new Trajectory.Point(1, 4));
        
        assertTrue(iter.hasNext());
        assertEquals(iter.next(), new Trajectory.Point(2, 5));
        assertTrue(iter.hasNext());
        assertEquals(iter.next(), new Trajectory.Point(2, 6));
        assertTrue(iter.hasNext());
        assertEquals(iter.next(), new Trajectory.Point(2, 7));
        assertTrue(iter.hasNext());
        assertEquals(iter.next(), new Trajectory.Point(2, 8));
        assertTrue(iter.hasNext());
        assertEquals(iter.next(), new Trajectory.Point(2, 9));
        assertTrue(iter.hasNext());
        assertEquals(iter.next(), new Trajectory.Point(2, 10));
        
        assertFalse(iter.hasNext());
    }
    
}
