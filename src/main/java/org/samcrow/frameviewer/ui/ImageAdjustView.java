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

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Displays sliders for adjusting image properties
 * @author samcrow
 */
public class ImageAdjustView extends GridPane {
    
    private static final Insets MARGIN = new Insets(5, 5, 5, 5);
    
    private final Slider brightnessSlider;
    
    private final Slider contrastSlider;
    
    private final Slider hueSlider;
    
    private final Slider saturationSlider;
    
    public ImageAdjustView() {
	brightnessSlider = new Slider(-1, 1,0);
	contrastSlider = new Slider(-1, 1, 0);
	hueSlider = new Slider(-1, 1, 0);
	saturationSlider = new Slider(-1, 1, 0);

	{
	    final Text label = new Text("Brightness");
	    add(label, 0, 0);
	    setMargin(label, MARGIN);
	    add(brightnessSlider, 1, 0);
	    setMargin(brightnessSlider, MARGIN);
	}
	{
	    final Text label = new Text("Contrast");
	    add(label, 0, 1);
	    setMargin(label, MARGIN);
	    add(contrastSlider, 1, 1);
	    setMargin(contrastSlider, MARGIN);
	}
	{
	    final Text label = new Text("Hue");
	    add(label, 0, 2);
	    setMargin(label, MARGIN);
	    add(hueSlider, 1, 2);
	    setMargin(hueSlider, MARGIN);
	}
	{
	    final Text label = new Text("Saturation");
	    add(label, 0, 3);
	    setMargin(label, MARGIN);
	    add(saturationSlider, 1, 3);
	    setMargin(saturationSlider, MARGIN);
	}
    }
    
    public DoubleProperty brightnessProperty() {
	return brightnessSlider.valueProperty();
    }
    public DoubleProperty contrastProperty() {
	return contrastSlider.valueProperty();
    }
    public DoubleProperty hueProperty() {
	return hueSlider.valueProperty();
    }
    public DoubleProperty saturationProperty() {
	return saturationSlider.valueProperty();
    }
}
