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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.samcrow.frameviewer.ui;

import junit.framework.TestCase;
import org.samcrow.frameviewer.util.TimeFormatter;

/**
 *
 * @author samcrow
 */
public class TimeFormatterTest extends TestCase {
    
    public TimeFormatterTest(String testName) {
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

    public void testParseDuration1() throws Exception {
        
        System.out.println("parseDuration");
        String time = "00:05";
        int expResult = 5;
        int result = TimeFormatter.parseDuration(time);
        assertEquals(expResult, result);
        
    }
    
    public void testParseDuration2() throws Exception {
        
        System.out.println("parseDuration");
        String time = "01:05";
        int expResult = 65;
        int result = TimeFormatter.parseDuration(time);
        assertEquals(expResult, result);
        
    }

    
}
