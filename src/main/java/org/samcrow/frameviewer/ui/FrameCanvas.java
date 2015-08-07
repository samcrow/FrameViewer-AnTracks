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

import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import org.samcrow.frameviewer.PaintableCanvas;
import org.samcrow.frameviewer.io3.DatabaseTrajectoryDataStore;
import org.samcrow.frameviewer.track.Tracker;
import org.samcrow.frameviewer.trajectory.Trajectory0;
import org.samcrow.frameviewer.trajectory.TrajectoryDisplayMode;
import org.samcrow.frameviewer.trajectory.TrajectoryTool;
import org.samcrow.frameviewer.trajectory.ui.CreateModeController;
import org.samcrow.frameviewer.trajectory.ui.EditModeController;
import org.samcrow.frameviewer.trajectory.ui.FrameController;

/**
 * Displays a video frame and allows it to be clicked on
 * 
 * @author Sam Crow
 */
public class FrameCanvas extends PaintableCanvas {

    /**
     * The image to be displayed
     */
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();

    private final IntegerProperty currentFrame = new SimpleIntegerProperty();

    private final ObjectProperty<TrajectoryTool> tool = new SimpleObjectProperty<>();

    private final ObjectProperty<List<Trajectory0>> trajectories = createTrajectoriesProperty();

    /**
     * Local coordinate X position of the frame's top left corner
     */
    private double imageTopLeftX;

    /**
     * Local coordinate Y position of the frame's top left corner
     */
    private double imageTopLeftY;

    /**
     * Local coordinate displayed width of the frame
     */
    private double imageWidth;

    /**
     * Local coordinate displayed width of the frame
     */
    private double imageHeight;

    private final ObjectProperty<TrajectoryDisplayMode> displayMode = new SimpleObjectProperty<>(
	    TrajectoryDisplayMode.Full);

    private final DoubleProperty trajectoryAlpha = new SimpleDoubleProperty(1);

    private DatabaseTrajectoryDataStore dataStore;

    private final CreateModeController createController;
    private final EditModeController editController;

    private FrameController activeController;

    /**
     * The tracker used for tracking
     */
    private final Tracker tracker;

    public FrameCanvas(Tracker tracker) {
	this.tracker = tracker;

	setFocusTraversable(true);
	requestFocus();

	setEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		try {
		    final Point2D imagePosition = getFrameLocation(event);
		    activeController.handleMouseEvent(event, imagePosition);
		} catch (NotInFrameException ex) {

		}
		requestFocus();
	    }
	});

	//Repaint when the frame or the markers changes
	image.addListener(new InvalidationListener() {
	    @Override
	    public void invalidated(Observable o) {

		// Update trajectories 
		if (dataStore != null) {
		    trajectories.set(dataStore.getObjectsNearCurrentFrame(20));
		}

		requestFocus();
		repaint();
	    }
	});
	trajectoryAlpha.addListener(new InvalidationListener() {
	    @Override
	    public void invalidated(Observable o) {

		// Update trajectories 
		if (dataStore != null) {
		    trajectories.set(dataStore.getObjectsNearCurrentFrame(20));
		}

		requestFocus();
		repaint();
	    }
	});

	// Repaint when the trajectory display state changes
	displayMode.addListener(new InvalidationListener() {
	    @Override
	    public void invalidated(Observable o) {
		// Update trajectories 
		if (dataStore != null) {
		    trajectories.set(dataStore.getObjectsNearCurrentFrame(20));
		}

		requestFocus();
		repaint();
	    }
	});

	// Set up controllers
	createController = new CreateModeController(this, tracker);
	createController.currentFrameProperty().bind(currentFrameProperty());
	createController.trajectoriesProperty().bind(trajectories);
	createController.sceneProperty().bind(sceneProperty());

	editController = new EditModeController(this, tracker);
	editController.currentFrameProperty().bind(currentFrameProperty());
	editController.trajectoriesProperty().bind(trajectories);
	editController.sceneProperty().bind(sceneProperty());

	// Set up initial state and bindings for active controller
	activeController = createController;
	trajectoryToolProperty().addListener(
		new ChangeListener<TrajectoryTool>() {
		    @Override
		    public void changed(
			    ObservableValue<? extends TrajectoryTool> ov,
			    TrajectoryTool t, TrajectoryTool newValue) {
				switch (newValue) {
				    case Create:
					editController.setActiveTrajectory(null);
					activeController = createController;
					break;
				    case Edit:
					createController.setActiveTrajectory(null);
					activeController = editController;
					break;
				}
			    }
		});

	// Set tracker on trajectories when new trajectories arrive
	trajectories.addListener(new ChangeListener<List<Trajectory0>>() {
	    @Override
	    public void changed(
		    ObservableValue<? extends List<Trajectory0>> observable,
		    List<Trajectory0> oldValue, List<Trajectory0> newValue) {
		for (Trajectory0 trajectory : newValue) {
		    trajectory.setTracker(FrameCanvas.this.tracker);
		}
	    }
	});
    }

    @Override
    protected void paint() {

	GraphicsContext gc = getGraphicsContext2D();
	gc.clearRect(0, 0, getWidth(), getHeight());

	//Draw image
	if (image.get() != null) {

	    //Scale image to fit this canvas, but preserve its aspect ratio
	    final double canvasWidth = getWidth();
	    final double canvasHeight = getHeight();

	    final double targetImageWidth = image.get().getWidth();
	    final double targetImageHeight = image.get().getHeight();
	    final double imageAspectRatio = targetImageWidth / targetImageHeight;

	    final double widthRatio = targetImageWidth / canvasWidth;
	    final double heightRatio = targetImageHeight / canvasHeight;

	    if (heightRatio < widthRatio) {
                //Window is taller than image
		//If necessary, shrink image width to fit
		imageWidth = Math.min(targetImageWidth, canvasWidth);
		imageHeight = imageWidth / imageAspectRatio;
	    } else {
                //Window is wider than image
		//If necessary, shrink image height to fit
		imageHeight = Math.min(targetImageHeight, canvasHeight);
		imageWidth = imageHeight * imageAspectRatio;
	    }

	    final double centerX = canvasWidth / 2;
	    final double centerY = canvasHeight / 2;
	    imageTopLeftX = centerX - imageWidth / 2;
	    imageTopLeftY = centerY - imageHeight / 2;

	    gc.drawImage(image.get(), imageTopLeftX, imageTopLeftY, imageWidth,
		    imageHeight);

	    if (getDisplayMode() != TrajectoryDisplayMode.Hidden) {
		gc.save();
		gc.setGlobalAlpha(trajectoryAlpha.get());

		// Draw trajectories
		for (Trajectory0 trajectory : trajectories.get()) {
		    trajectory.paint(gc, image.get().getWidth(), image.get()
			    .getHeight(), imageWidth, imageHeight, imageTopLeftX,
			    imageTopLeftY, getCurrentFrame(), getDisplayMode(),
			    false);
		}
		gc.setGlobalAlpha(1);
		// Always draw active trajectory with 100% opacity
		final Trajectory0 active = activeController
			.getActiveTrajectory();
		if (active != null) {
		    active.paint(gc, image.get().getWidth(), image.get()
			    .getHeight(), imageWidth, imageHeight, imageTopLeftX,
			    imageTopLeftY, getCurrentFrame(), getDisplayMode(),
			    true);
		}

		gc.restore();
	    }
	}

    }

    /**
     * Returns the location, in frame image coordinates, of a mouse event
     * <p>
     * @param event
     * @return
     *
     * @throws org.samcrow.frameviewer.ui.FrameCanvas.NotInFrameException If the
     * mouse
     * event was not on the displayed frame
     */
    private Point2D getFrameLocation(MouseEvent event) throws NotInFrameException {
	return getFrameLocation(event.getX(), event.getY());
    }

    /**
     * Returns the location, in frame image coordinates, of a location in local
     * canvas coordinates
     * <p>
     * @param x
     * @param y
     * @return
     *
     * @throws org.samcrow.frameviewer.ui.FrameCanvas.NotInFrameException
     */
    private Point2D getFrameLocation(double x, double y) throws NotInFrameException {
	if ((x < imageTopLeftX || x > (imageTopLeftX + imageWidth))
		|| (y < imageTopLeftY || y > (imageTopLeftY + imageHeight))) {
	    throw new NotInFrameException(
		    "The provided click was outside the bounds of the frame");
	}

	final double xRatio = (x - imageTopLeftX) / imageWidth;
	final double yRatio = (y - imageTopLeftY) / imageHeight;

	assert xRatio <= 1;
	assert yRatio <= 1;

	return new Point2D(image.get().getWidth() * xRatio, image.get()
		.getHeight() * yRatio);
    }

    private boolean pointClicked(double x, double y, Point2D frameLocation) {
	final int radius = 6;
	return radius >= frameLocation.distance(x, y);

    }

    /**
     * Thrown when a mouse event is not inside the frame
     */
    private static class NotInFrameException extends Exception {

	public NotInFrameException(String message) {
	    super(message);
	}

    }

    public final ObjectProperty<Image> imageProperty() {
	return image;
    }

    public void setTrajectories(List<Trajectory0> trajectories) {
	if (trajectories == null) {
	    throw new IllegalArgumentException(
		    "The trajectory list must not be null");
	}

	this.trajectories.set(trajectories);
    }

    public final int getCurrentFrame() {
	return currentFrame.get();
    }

    public final void setCurrentFrame(int newFrame) {
	currentFrame.set(newFrame);
    }

    public final IntegerProperty currentFrameProperty() {
	return currentFrame;
    }

    public DatabaseTrajectoryDataStore getDataStore() {
	return dataStore;
    }

    public void setDataStore(DatabaseTrajectoryDataStore dataStore) {
	this.dataStore = dataStore;
	createController.setDataStore(dataStore);
	editController.setDataStore(dataStore);
	trajectories.set(dataStore.getObjectsNearCurrentFrame(20));
    }

    public final TrajectoryDisplayMode getDisplayMode() {
	return displayMode.get();
    }

    public final void setDisplayMode(TrajectoryDisplayMode mode) {
	displayMode.set(mode);
    }

    public final ObjectProperty<TrajectoryDisplayMode> displayModeProperty() {
	return displayMode;
    }

    public final TrajectoryTool getTrajectoryTool() {
	return tool.get();
    }

    public final void setTrajectoryTool(TrajectoryTool tool) {
	this.tool.set(tool);
    }

    public final ObjectProperty<TrajectoryTool> trajectoryToolProperty() {
	return tool;
    }

    public final DoubleProperty trajectoryAlphaProperty() {
	return trajectoryAlpha;
    }

    private static ObjectProperty<List<Trajectory0>> createTrajectoriesProperty() {
	final List<Trajectory0> initialList = new ArrayList<>();
	return new SimpleObjectProperty<>(initialList);
    }
}
