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
package org.samcrow.frameviewer.trajectory.ui;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.samcrow.frameviewer.PaintableCanvas;
import org.samcrow.frameviewer.track.Tracker;
import org.samcrow.frameviewer.trajectory.InteractionPoint;
import org.samcrow.frameviewer.trajectory.Point.Source;
import org.samcrow.frameviewer.trajectory.Point0;
import org.samcrow.frameviewer.trajectory.Trajectory0;
import org.samcrow.frameviewer.ui.InteractionPointDialog;
import org.samcrow.frameviewer.ui.TrajectoryCreateDialog;
import org.samcrow.frameviewer.ui.TrajectoryEditDialog;

/**
 * Controller for create mode
 * <p>
 * @author samcrow
 */
public class CreateModeController extends FrameController {

    public CreateModeController(PaintableCanvas canvas, Tracker tracker) {
	super(canvas, tracker);
    }

    @Override
    protected void handleMouseClicked(MouseEvent event, Point2D framePosition) {
	if (getActiveTrajectory() == null) {
	    // Create a new trajectory
	    createTrajectory(new Point2D(event.getScreenX(), event.getScreenY()),
		    framePosition);
	} else {
	    // Modify active trajectory
	    if (event.isShiftDown()) {
		createInteractionMarker(framePosition);
	    } else if (event.getButton() == MouseButton.SECONDARY) {
		createCustomMarker(new Point2D(event.getScreenX(), event
			.getScreenY()), framePosition);
	    } else {
		createDefaultMarker(framePosition);
	    }
	}
	repaint();
    }

    private void createInteractionMarker(Point2D frameLocation) {

	final Trajectory0 trajectoryWithNearbyPoint = getTrajectoryNear(
		frameLocation);
	Point0 nearbyPoint = null;
	if (trajectoryWithNearbyPoint != null) {
	    try {
		nearbyPoint = trajectoryWithNearbyPoint.get(getCurrentFrame());
	    } catch (IndexOutOfBoundsException ex) {
		return;
	    }
	}

	// Now nearbyPoint is either null or an existing point on the current frame
	InteractionPoint newPoint;
	if (nearbyPoint != null) {
	    // Make it an InteractionPoint, if it is not already one
	    if (!(nearbyPoint instanceof InteractionPoint)) {
		nearbyPoint = new InteractionPoint(nearbyPoint);
		trajectoryWithNearbyPoint.put(getCurrentFrame(), nearbyPoint);
		nearbyPoint.setActivity(trajectoryWithNearbyPoint.get(
			trajectoryWithNearbyPoint.getLastFrame()).getActivity());
		((InteractionPoint) nearbyPoint).setFocalAntId(
			trajectoryWithNearbyPoint.getId());
	    }

	    newPoint = InteractionPoint.inverted((InteractionPoint) nearbyPoint);
	    newPoint.setX((int) Math.round(frameLocation.getX()));
	    newPoint.setY((int) Math.round(frameLocation.getY()));
	    newPoint.setSource(Source.User);
	    // Propagate properties from the last point to the new point
	    newPoint.setFocalAntId(getActiveTrajectory().getId());
	    newPoint.setFocalAntActivity(getActiveTrajectory().get(getActiveTrajectory()
		    .getLastFrame()).getActivity());
	    newPoint.setMetAntId(trajectoryWithNearbyPoint.getId());

	    newPoint.setOtherPoint((InteractionPoint) nearbyPoint);
	} else {
	    // No nearby point; just create a new point
	    newPoint = new InteractionPoint((int) Math.round(frameLocation
		    .getX()), (int) Math.round(frameLocation.getY()));
	    newPoint.setSource(Source.User);
	    newPoint.setFocalAntId(getActiveTrajectory().getId());
	    newPoint.setFocalAntActivity(getActiveTrajectory().get(getActiveTrajectory()
		    .getLastFrame()).getActivity());
	}
	// Temporarily insert the point, so that it will be visible when the errorDialog appears
	getActiveTrajectory().put(getCurrentFrame(), newPoint);
	repaint();

	InteractionPointDialog dialog = new InteractionPointDialog(getScene()
		.getWindow(), newPoint);
	dialog.showAndWait();
	if (dialog.success()) {

	    newPoint.setFocalAntId(dialog.getFocalAntId());
	    newPoint.setMetAntId(dialog.getMetAntId());
	    newPoint.setFocalAntActivity(dialog.getFocalAntActivity());
	    newPoint.setMetAntActivity(dialog.getMetAntActivity());
	    newPoint.setType(dialog.getInteractionType());
	    newPoint.setSource(Source.User);

	    save(getActiveTrajectory());
	    save(trajectoryWithNearbyPoint);
	} else {
	    // Remove the point
	    getActiveTrajectory().remove(getCurrentFrame());
	}

    }

    private void createCustomMarker(Point2D screenPosition,
	    Point2D framePosition) {
	final Trajectory0 activeTrajectory = getActiveTrajectory();
	// Copy the point and edit it
	Point0 newPoint = new Point0(activeTrajectory.get(activeTrajectory.getLastFrame()));
	newPoint.setX((int) Math.round(framePosition.getX()));
	newPoint.setY((int) Math.round(framePosition.getY()));
	newPoint.setSource(Source.User);

        // Temporarily add the point so that it will be displayed
	// while the errorDialog is visible
	activeTrajectory.put(getCurrentFrame(), newPoint);
	repaint();

	TrajectoryEditDialog dialog = new TrajectoryEditDialog(getScene()
		.getWindow(), activeTrajectory, newPoint);
	dialog.setX(screenPosition.getX());
	dialog.setY(screenPosition.getY());
	dialog.showAndWait();

	if (dialog.succeeded()) {
	    activeTrajectory.setId(dialog.getTrajectoryId());
	    activeTrajectory.setFromAction(dialog.getStartAction());
	    activeTrajectory.setToAction(dialog.getToAction());
	    newPoint.setActivity(dialog.getActivity());

	    activeTrajectory.put(getCurrentFrame(), newPoint);
	    // Save the trajectory
	    save(activeTrajectory);

	    if (dialog.finalizeRequested()) {
                // Make this trajectory no longer active
		// The list still has a strong reference to it.
		setActiveTrajectory(null);
	    }
	} else {
	    // Not succeeded; Remove the new point
	    activeTrajectory.remove(getCurrentFrame());
	}
    }

    private void createDefaultMarker(Point2D framePosition) {
	// Just add a new point with the same properties to the trajectory
	Point0 newPoint = getActiveTrajectory().copyLastPoint();
	newPoint.setX((int) Math.round(framePosition.getX()));
	newPoint.setY((int) Math.round(framePosition.getY()));
	newPoint.setSource(Source.User);

	getActiveTrajectory().put(getCurrentFrame(), newPoint);
	save(getActiveTrajectory());
    }

    private void createTrajectory(Point2D screenPosition, Point2D framePosition) {
	TrajectoryCreateDialog dialog = new TrajectoryCreateDialog(getScene()
		.getWindow());
	dialog.setX(screenPosition.getX());
	dialog.setY(screenPosition.getY());
	dialog.showAndWait();
	if (dialog.succeeded()) {

	    final Trajectory0 activeTrajectory = new Trajectory0(getTracker());
	    setActiveTrajectory(activeTrajectory);
	    activeTrajectory.setDataStore(getDataStore());
	    getTrajectories().add(activeTrajectory);
	    activeTrajectory.setId(dialog.getTrajectoryId());
	    activeTrajectory.setFromAction(dialog.getStartAction());

	    getDataStore().add(activeTrajectory);

	    // Create a new Point at the mouse location
	    Point0 newPoint = new Point0((int) Math.round(framePosition.getX()),
		    (int) Math.round(framePosition.getY()), Source.User);
	    newPoint.setActivity(dialog.getActivity());
	    activeTrajectory.put(getCurrentFrame(), newPoint);
	    save(activeTrajectory);
	}
    }

}
