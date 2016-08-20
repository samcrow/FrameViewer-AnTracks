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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.samcrow.frameviewer.PlaybackControlModel;
import org.samcrow.frameviewer.trajectory.TrajectoryDisplayMode;
import org.samcrow.frameviewer.trajectory.TrajectoryTool;

import static javafx.scene.layout.HBox.setMargin;
import javafx.scene.layout.VBox;

/**
 * Displays playback controls
 * <p>
 * @author Sam Crow
 */
public class PlaybackControlPane extends HBox {

    private final Button jumpBackwardsButton;

    private final Button jumpForwardButton;

    private final PlaybackControlModel model;

    private final Button refreshButton;
    
    private final Button imageButton;

    private final ObjectProperty<TrajectoryTool> trajectoryTool = new SimpleObjectProperty<>();

    private final ObjectProperty<TrajectoryDisplayMode> trajectoryDisplayMode = new SimpleObjectProperty<>();

    private final DoubleProperty trajectoryAlpha = new SimpleDoubleProperty(1);

    private final DoubleProperty activeTrajectoryAlpha = new SimpleDoubleProperty(
	    1);

    public PlaybackControlPane(PlaybackControlModel model) {
	this.model = model;

	final Insets PADDING = new Insets(10);

	setPadding(PADDING);
	setAlignment(Pos.CENTER);

	// Refresh button
	{
	    refreshButton = new Button("Refresh");
	    getChildren().add(refreshButton);
	    setMargin(refreshButton, PADDING);
	}
	{
	    imageButton = new Button("Image");
	    getChildren().add(imageButton);
	    setMargin(imageButton, PADDING);
	}

	// Left spacer
	{
	    final Region spacer = new Region();
	    getChildren().add(spacer);
	    setHgrow(spacer, Priority.ALWAYS);
	}

	{
	    jumpBackwardsButton = new Button("|<");
	    jumpBackwardsButton.setOnAction(model
		    .getJumpBackwardsButtonHandler());
	    jumpBackwardsButton.disableProperty().bind(model
		    .jumpBackwardsEnabledProperty().not());

	    getChildren().add(jumpBackwardsButton);
	    setMargin(jumpBackwardsButton, PADDING);
	}

	{

	    final Label timeLabel = new Label("Time: ");

	    getChildren().add(timeLabel);
	    setMargin(timeLabel, PADDING);

	}

	{
	    final TimeField timeField = new TimeField();
	    timeField.currentFrameProperty().bindBidirectional(model
		    .currentFrameProperty());

	    getChildren().add(timeField);
	    setMargin(timeField, PADDING);
	}

	{

	    final Label frameLabel = new Label("Frame: ");

	    getChildren().add(frameLabel);
	    setMargin(frameLabel, PADDING);

	}

	{
	    final IntegerField frameField = new IntegerField(1);
	    frameField.valueProperty().bindBidirectional(model
		    .currentFrameProperty());

	    getChildren().add(frameField);
	    setMargin(frameField, PADDING);
	}

	{
	    jumpForwardButton = new Button(">|");
	    jumpForwardButton.setOnAction(model.getJumpForwardButtonHandler());
	    jumpForwardButton.disableProperty().bind(model
		    .jumpForwardEnabledProperty().not());

	    getChildren().add(jumpForwardButton);
	    setMargin(jumpForwardButton, PADDING);
	}

	// Right spacer
	{
	    final Region spacer = new Region();
	    getChildren().add(spacer);
	    setHgrow(spacer, Priority.ALWAYS);
	}

	// Mode select
	{

	    final SplitButtonBar<TrajectoryTool> toolChoice = new SplitButtonBar<>(
		    TrajectoryTool.values(), TrajectoryTool.Create);
	    trajectoryTool.bindBidirectional(toolChoice.selectedItemProperty());

	    getChildren().add(toolChoice);
	    setMargin(toolChoice, PADDING);
	}

	// Display mode select
	{
	    final SplitButtonBar<TrajectoryDisplayMode> displayChoice = new SplitButtonBar<>(
		    TrajectoryDisplayMode.values(), TrajectoryDisplayMode.Full);
	    trajectoryDisplayMode.bindBidirectional(displayChoice
		    .selectedItemProperty());

	    // Not in user interface
	}

	// Trajectory opacity
	{
	    final Slider activeTrajectorySlider = new Slider(0, 1, 1);
	    final Slider slider = new Slider(0, 1, 1);
	    activeTrajectoryAlpha.bindBidirectional(activeTrajectorySlider.valueProperty());
	    trajectoryAlpha.bindBidirectional(slider.valueProperty());

	    final VBox box = new VBox(activeTrajectorySlider, slider);
	    box.setSpacing(5);
	    
	    getChildren().add(box);
	    setMargin(box, PADDING);
	}

    }

    public final TrajectoryTool getTrajectoryTool() {
	return trajectoryTool.get();
    }

    public final void setTrajectoryTool(TrajectoryTool tool) {
	trajectoryTool.set(tool);
    }

    public final ObjectProperty<TrajectoryTool> trajectoryToolProperty() {
	return trajectoryTool;
    }

    public final TrajectoryDisplayMode getTrajectoryDisplayMode() {
	return trajectoryDisplayMode.get();
    }

    public final void setTrajectoryDisplayMode(TrajectoryDisplayMode mode) {
	trajectoryDisplayMode.set(mode);
    }

    public final ObjectProperty<TrajectoryDisplayMode> trajectoryDisplayModeProperty() {
	return trajectoryDisplayMode;
    }

    public final DoubleProperty trajectoryAlphaProperty() {
	return trajectoryAlpha;
    }

    public final DoubleProperty activeTrajectoryAlphaProperty() {
	return activeTrajectoryAlpha;
    }

    public final void setOnRefreshRequested(EventHandler<ActionEvent> handler) {
	refreshButton.setOnAction(handler);
    }
    
    public final void setOnImageControlsRequested(EventHandler<ActionEvent> handler) {
	imageButton.setOnAction(handler);
    }

    /**
     * Sets up global keyboard shortcuts. Must be called after this pane
     * is attached to a scene.
     */
    public void setupAccelerators() {
	final Scene scene = getScene();

	scene.getAccelerators().put(new KeyCodeCombination(KeyCode.LEFT),
		(Runnable) () -> {
		    if (!jumpBackwardsButton.isDisabled()) {
			jumpBackwardsButton.fire();
		    }
		});
	scene.getAccelerators().put(new KeyCodeCombination(KeyCode.RIGHT),
		(Runnable) () -> {
		    if (!jumpForwardButton.isDisabled()) {
			jumpForwardButton.fire();
		    }
		});
    }

}
