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

import java.util.Iterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.samcrow.frameviewer.trajectory.Point.Source;
import org.samcrow.frameviewer.trajectory.Trajectory.Entry;

/**
 *
 * @author samcrow
 */
public class TrajectoryTest {
    
    @Test
    public void testSetGet() {
        Trajectory0 t = new Trajectory0(null);
        
        final Point0 point1 = new Point0(1, 2, Point.Source.User);
        final Point0 point2 = new Point0(3, 4, Point.Source.User);
        
        t.put(0, point1);
        t.put(1, point2);
        
        assertEquals(t.get(0), point1);
        assertEquals(t.get(1), point2);
        
    }
    
    @Test
    public void testIterator() {
        
        Trajectory0 t = new Trajectory0(null);
        
        final Point0 point1 = new Point0(3, 9, Point.Source.User);
        final Point0 point2 = new Point0(53, 90, Point.Source.User);
        
        t.put(120, point1);
        t.put(200, point2);
        
        Iterator<Entry<Point0>> iter = t.iterator();
        assertTrue(iter.hasNext());
        assertEquals(iter.next().point, point1);
        assertTrue(iter.hasNext());
        assertEquals(iter.next().point, point2);
        
        assertFalse(iter.hasNext());
        
    }
    
    @Test
    public void testAppend() {
        
        Trajectory0 t1 = new Trajectory0(null);
        t1.put(1, new Point0(1, 1, Source.User));
        t1.put(2, new Point0(1, 2, Source.User));
        t1.put(3, new Point0(1, 3, Source.User));
        t1.put(4, new Point0(1, 4, Source.User));
        t1.put(5, new Point0(1, 5, Source.User));
        
        Trajectory0 t2 = new Trajectory0(null);
        t2.put(5, new Point0(2, 5, Source.User));
        t2.put(6, new Point0(2, 6, Source.User));
        t2.put(7, new Point0(2, 7, Source.User));
        t2.put(8, new Point0(2, 8, Source.User));
        t2.put(9, new Point0(2, 9, Source.User));
        t2.put(10, new Point0(2, 10, Source.User));
        
        t1.putAll(t2);
        
        Iterator<Entry<Point0>> iter = t1.iterator();
        
        assertTrue(iter.hasNext());
        assertEquals(iter.next().point, new Point0(1, 1, Source.User));
        assertTrue(iter.hasNext());
        assertEquals(iter.next().point, new Point0(1, 2, Source.User));
        assertTrue(iter.hasNext());
        assertEquals(iter.next().point, new Point0(1, 3, Source.User));
        assertTrue(iter.hasNext());
        assertEquals(iter.next().point, new Point0(1, 4, Source.User));
        
        assertTrue(iter.hasNext());
        assertEquals(iter.next().point, new Point0(2, 5, Source.User));
        assertTrue(iter.hasNext());
        assertEquals(iter.next().point, new Point0(2, 6, Source.User));
        assertTrue(iter.hasNext());
        assertEquals(iter.next().point, new Point0(2, 7, Source.User));
        assertTrue(iter.hasNext());
        assertEquals(iter.next().point, new Point0(2, 8, Source.User));
        assertTrue(iter.hasNext());
        assertEquals(iter.next().point, new Point0(2, 9, Source.User));
        assertTrue(iter.hasNext());
        assertEquals(iter.next().point, new Point0(2, 10, Source.User));
        
        assertFalse(iter.hasNext());
    }
    
}
