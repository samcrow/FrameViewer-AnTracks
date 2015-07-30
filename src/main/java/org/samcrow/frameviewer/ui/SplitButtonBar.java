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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;

/**
 * Displays a group of ToggleButtons that allow a value to be selected.
 * 
 * 
 * 
 * <p>
 * @param <T> The type of element to choose
 * @author samcrow
 */
public class SplitButtonBar<T> extends HBox {
    
    private final ObjectProperty<T> selection = new SimpleObjectProperty<>();
    
    private final BiMap < T, Toggle > valueToButton = HashBiMap.create();
    private final BiMap < Toggle, T > buttonToValue = valueToButton.inverse();

    public SplitButtonBar(T[] values, T initialSelection) {

        for(T value : values) {
            // Create a ToggleButton
            final ToggleButton button = new ToggleButton(value.toString());
            
            button.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue == true) {
                        setSelectedItem(buttonToValue.get(button));
                    }
                }
            });
            
            valueToButton.put(value, button);
            getChildren().add(button);
        }
        
        // Set up initial selection
        setSelectedItem(initialSelection);
        valueToButton.get(initialSelection).setSelected(true);
        
        selection.addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> ov, T oldValue, T newValue) {
                // Deselect the Toggle corresponding to the old value
                valueToButton.get(oldValue).setSelected(false);
                // Select the Toggle corresponding to the new value
                valueToButton.get(newValue).setSelected(true);
            }
        });
        
        setCorners();
    }

    
    public final T getSelectedItem() {
        return selection.get();
    }
    public final void setSelectedItem(T item) {
        selection.set(item);
    }
    public final ObjectProperty<T> selectedItemProperty() {
        return selection;
    }
    
    private void setCorners() {
        final List<Node> children = getChildrenUnmodifiable();
        if(children.size() <= 1) {
            // Up to 1 buttons: Do nothing
        }
        else if(children.size() == 2) {
            // Set corners for buttons 0 and 1
            children.get(0).setStyle("-fx-background-radius: 3 0 0 3, 2 0 0 2, 2 0 0 2;");
            children.get(1).setStyle("-fx-background-radius: 0 3 3 0, 0 2 2 0, 0 2 2 0;");
        }
        else {
            // 3 or more. Set corners for first, middle, and last
            children.get(0).setStyle("-fx-background-radius: 3 0 0 3, 2 0 0 2, 2 0 0 2;");
            
            for(int i = 1; i < children.size() - 1; i++) {
                children.get(i).setStyle("-fx-background-radius: 0;");
            }
            
            children.get(children.size() - 1).setStyle("-fx-background-radius: 0 3 3 0, 0 2 2 0, 0 2 2 0;");
        }
    }
}
