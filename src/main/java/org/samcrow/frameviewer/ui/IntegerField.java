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

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import jfxtras.labs.dialogs.MonologFX;
import org.samcrow.frameviewer.FrameIndexOutOfBoundsException;

/**
 * A text field that contains and displays an integer value
 * @author Sam Crow
 */
public class IntegerField extends TextField {
    
    /**
     * The current numerical value
     */
    private final IntegerProperty value = new SimpleIntegerProperty();
    
    public IntegerField(int initialValue) {
        setPrefColumnCount(5);
        
        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                validateInput();
            }
        });
        
        //Validate the number when the field loses focus
        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                if(newValue == false) {
                    validateInput();
                }
            }
        });
        
        value.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number newValue) {
                setText(String.valueOf(newValue.intValue()));
            }
        });
        
        value.set(initialValue);
        
        
    }
    
    public IntegerField() {
        this(1);
    }
    
    private void validateInput() {
        String text = getText();
        
        try {
            int newValue = Integer.valueOf(text);
            value.set(newValue);
        }
        catch (NumberFormatException ex) {
            //Revert to the last numerical value
            setText(String.valueOf(getValue()));
        }
        catch (FrameIndexOutOfBoundsException ex) {
            //Invalid frame number
            //Revert
            setText(String.valueOf(getValue()));
            
            //Alert
            MonologFX dialog = new MonologFX(MonologFX.Type.ERROR);
            dialog.setTitle("Invalid frame number");
            dialog.setMessage("Please enter a frame number between "+ex.getFirstFrame()+" and "+ex.getLastFrame());
            dialog.setModal(true);
            dialog.showDialog();
            
        }
        
    }
    
    
    public final int getValue() {
        return value.get();
    }
    
    public final void setValue(int newValue) {
        value.set(newValue);
    }
    
    public final IntegerProperty valueProperty() {
        return value;
    }
    
    public void setFieldFocused(boolean focused) {
        setFocused(focused);
    }
}
