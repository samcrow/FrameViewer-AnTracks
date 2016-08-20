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
package org.samcrow.frameviewer.ui;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A window that contains an ImageAdjustView
 * @author samcrow
 */
public class ImageAdjustWindow extends Stage {
    
    private final ImageAdjustView view;
    
    public ImageAdjustWindow() {
	super(StageStyle.UTILITY);
	initModality(Modality.NONE);
	
	view = new ImageAdjustView();
	final Scene scene = new Scene(view);
	setScene(scene);
	setTitle("Image adjustment");
    }
    
    public ImageAdjustView getView() {
	return view;
    }
}
