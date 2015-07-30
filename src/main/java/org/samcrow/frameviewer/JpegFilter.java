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

import java.io.File;
import java.io.FileFilter;

/**
 * Finds JPEG image files. Checks extensions.
 * @author Sam Crow
 */
class JpegFilter implements FileFilter {


    @Override
    public boolean accept(File file) {
        String extension = getExtension(file);
        if (!(extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg"))) {
            //Not a jpg or jpeg -extensioned file
            return false;
        }
        // Ignore dot-prefixed files
        if(file.getName().startsWith(".")) {
            return false;
        }
        return true;
    }

    private String getExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) {
            //No dot in the name
            return "";
        }
        if (dotIndex == name.length() - 1) {
            //Dot is last character in the name
            return "";
        }
        //Dot in the name
        return name.substring(dotIndex + 1);
    }
    
}
