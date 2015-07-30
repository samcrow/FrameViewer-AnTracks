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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

/**
 * Handles the logic of playback controls.
 * Supports a pause button, a play forwards button, a play backwards button,
 * a jump forwards button, and a jump backwards button. The jump buttons
 * are used to jump one frame.
 * @author Sam Crow
 */
public class PlaybackControlModel implements CurrentFrameProvider {
    /**
     * If the jump forward button should be enabled
     */
    private final BooleanProperty jumpForwardEnabled = new SimpleBooleanProperty(true);
    /**
     * If the jump backwards button should be enabled
     */
    private final BooleanProperty jumpBackwardsEnabled = new SimpleBooleanProperty(true);
    
    
    /**
     * The frame that is currently displayed
     */
    private final ObjectProperty<Image> currentFrameImage = new SimpleObjectProperty<>();
    
    /**
     * The frame number that is currently displayed
     */
    private final IntegerProperty currentFrame = new SimpleIntegerProperty();
    
    /**
     * Loads frames
     */
    private final FrameFinder finder;
    
    public PlaybackControlModel(FrameFinder frameFinder) {
        this.finder = frameFinder;
        
        currentFrame.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int frame = newValue.intValue();
                if(frame < 1 || frame > getMaximumFrame()) {
                    throw new FrameIndexOutOfBoundsException(getFirstFrame(), frame, getMaximumFrame());
                }
                
                currentFrameImage.set(finder.getImage(frame));
                
                //Disable backwards buttons if the first frame has been reached
                if(frame <= getFirstFrame()) {
                    jumpBackwardsEnabled.set(false);
                }
                else {
                    jumpBackwardsEnabled.set(true);
                }
                //Disable forwards buttons if the last frame has been reached
                if(frame >= getMaximumFrame()) {
                    jumpForwardEnabled.set(false);
                }
                else {
                    jumpForwardEnabled.set(true);
                }
            }
        });
        
        
        //Display the first frame
        currentFrame.set(getFirstFrame());
    }
    
    /**
     * @return if the jump forward button should be enabled
     */
    public final ReadOnlyBooleanProperty jumpForwardEnabledProperty() {
        return jumpForwardEnabled;
    }
    /**
     * @return if the jump backwards button should be enabled
     */
    public final ReadOnlyBooleanProperty jumpBackwardsEnabledProperty() {
        return jumpBackwardsEnabled;
    }
    
    private void jumpForwardButtonClicked() {
        if(getCurrentFrame() < getMaximumFrame()) {
            currentFrame.set(currentFrame.get() + 1);
        }
    }
    
    private void jumpBackwardsButtonClicked() {
        if(getCurrentFrame() > getFirstFrame()) {
            currentFrame.set(currentFrame.get() - 1);
        }
    }
    
    private final EventHandler<ActionEvent> jumpForwardHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
            jumpForwardButtonClicked();
        }
    };
    
    public final EventHandler<ActionEvent> getJumpForwardButtonHandler() {
        return jumpForwardHandler;
    }
    
    private final EventHandler<ActionEvent> jumpBackwardsHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
            jumpBackwardsButtonClicked();
        }
    };
    
    public final EventHandler<ActionEvent> getJumpBackwardsButtonHandler() {
        return jumpBackwardsHandler;
    }
    
    public final int getFirstFrame() {
        return finder.getFirstFrame();
    }
    
    public final IntegerProperty currentFrameProperty() {
        return currentFrame;
    }
    
    @Override
    public final int getCurrentFrame() {
        return currentFrame.get();
    }
    
    public final void setCurrentFrame(int newFrame) {
        currentFrame.set(newFrame);
    }
    
    public final int getMaximumFrame() {
        return finder.getMaximumFrame();
    }
    
    public final ReadOnlyObjectProperty<Image> currentFrameImageProperty() {
        return currentFrameImage;
    }
    
    public Image getCurrentFrameImage() {
        return currentFrameImage.get();
    }
}
