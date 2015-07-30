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

import java.text.ParseException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import jfxtras.labs.dialogs.MonologFX;
import org.samcrow.frameviewer.FrameIndexOutOfBoundsException;
import org.samcrow.frameviewer.util.TimeFormatter;

/**
 * A text field that displays a video timecode and allows it to be edited
 * @author Sam Crow
 */
public class TimeField extends TextField {
    
    private static final double FRAMES_PER_SECOND = 29.97;
    
    private final IntegerProperty currentFrame = new SimpleIntegerProperty();
    
    public TimeField() {
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
        
        currentFrame.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                
                int currentSecond = (int) Math.floor( newValue.intValue() / FRAMES_PER_SECOND );
                
                setText(TimeFormatter.formatDuration(currentSecond));
            }
        });
        
        
    }
    
    private void validateInput() {
        try {
            int secondsEntered = TimeFormatter.parseDuration(this.getText());
            
            int newFrame = (int) Math.round(secondsEntered * FRAMES_PER_SECOND);
            //Ensure that frame >= 1
            if(newFrame < 1) {
                newFrame = 1;
            }
            setCurrentFrame(newFrame);
        }
        catch (ParseException ex) {
            //Revert
            int currentSecond = (int) Math.round(getCurrentFrame() / FRAMES_PER_SECOND);
            
            setText(TimeFormatter.formatDuration(currentSecond));
        }
        catch (FrameIndexOutOfBoundsException ex) {
            //Invalid frame number
            //Revert
            int currentSecond = (int) Math.round(getCurrentFrame() / FRAMES_PER_SECOND);
            
            setText(TimeFormatter.formatDuration(currentSecond));
            
            //Alert
            MonologFX dialog = new MonologFX(MonologFX.Type.ERROR);
            dialog.setTitle("Invalid time");
            dialog.setMessage("Please enter a time between "
                    +TimeFormatter.formatDurationFromFrame(ex.getFirstFrame())
                    +" and "+TimeFormatter.formatDurationFromFrame(ex.getLastFrame()));
            dialog.setModal(true);
            dialog.showDialog();
            
        }
        
    }
    
    
    
    
    public final int getCurrentFrame() {
        return currentFrame.get();
    }
    
    public final void setCurrentFrame(int newValue) {
        currentFrame.set(newValue);
    }
    
    final IntegerProperty currentFrameProperty() {
        return currentFrame;
    }
}
