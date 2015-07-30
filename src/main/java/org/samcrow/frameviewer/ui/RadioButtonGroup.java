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

import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Toggle;

/**
 * Manages a group of radio buttons and associated objects
 *
 * @param <T> The type of object to allow the user to choose between
 * @author Sam Crow
 */
public class RadioButtonGroup<T> extends GridPane {

    private ToggleGroup group = new ToggleGroup();

    private BiMap< T, Toggle> valueToButton = HashBiMap.create();
    private BiMap< Toggle, T> buttonToValue = valueToButton.inverse();

    private final ObjectProperty<T> selectedValue = new SimpleObjectProperty<>();

    private RadioButtonGroup() {

	group.selectedToggleProperty().addListener(
		(ObservableValue<? extends Toggle> observable, Toggle oldButton, Toggle newButton) -> {
		    // Set the new selectedValue to match the selected toggle
		    T newValue = buttonToValue.get(newButton);
		    if (newValue != getValue()) {
			setValue(newValue);
		    }
		});

	selectedValue.addListener(
		(ObservableValue<? extends T> observable, T oldValue, T newValue) -> {
		    // Set the selected toggle to match the new value
		    Toggle newButton = valueToButton.get(newValue);
		    if (newButton != group.getSelectedToggle()) {
			group.selectToggle(newButton);
		    }
		});

    }

    public RadioButtonGroup(T[] values) {
	// Call default constructor
	this();

	final Insets PADDING = new Insets(2);

	// Create a radio button for each selectedValue
	int i = 0;
	for (T value : values) {
	    RadioButton button = new RadioButton(value.toString());
	    button.setToggleGroup(group);
	    button.setAlignment(Pos.CENTER_LEFT);
	    // Add the button to the grid layout
	    add(button, 0, i);
	    GridPane.setMargin(button, PADDING);

	    valueToButton.put(value, button);

	    i++;
	}
    }

    public final ObjectProperty<T> valueProperty() {
	return selectedValue;
    }

    public final T getValue() {
	return selectedValue.get();
    }

    public final void setValue(T newValue) {
	selectedValue.set(newValue);
    }

}
