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

    private Trajectory0 activeTrajectory;

    public CreateModeController(PaintableCanvas canvas, Tracker tracker) {
	super(canvas, tracker);
    }

    @Override
    protected void handleMouseClicked(MouseEvent event, Point2D framePosition) {
	if (activeTrajectory == null) {
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
	    newPoint.setFocalAntId(activeTrajectory.getId());
	    newPoint.setFocalAntActivity(activeTrajectory.get(activeTrajectory
		    .getLastFrame()).getActivity());
	    newPoint.setMetAntId(trajectoryWithNearbyPoint.getId());

	    newPoint.setOtherPoint((InteractionPoint) nearbyPoint);
	} else {
	    // No nearby point; just create a new point
	    newPoint = new InteractionPoint((int) Math.round(frameLocation
		    .getX()), (int) Math.round(frameLocation.getY()));
	    newPoint.setSource(Source.User);
	    newPoint.setFocalAntId(activeTrajectory.getId());
	    newPoint.setFocalAntActivity(activeTrajectory.get(activeTrajectory
		    .getLastFrame()).getActivity());
	}
	// Temporarily insert the point, so that it will be visible when the errorDialog appears
	activeTrajectory.put(getCurrentFrame(), newPoint);
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

	    save(activeTrajectory);
	    save(trajectoryWithNearbyPoint);
	} else {
	    // Remove the point
	    activeTrajectory.remove(getCurrentFrame());
	}

    }

    private void createCustomMarker(Point2D screenPosition,
	    Point2D framePosition) {
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
		activeTrajectory = null;
	    }
	} else {
	    // Not succeeded; Remove the new point
	    activeTrajectory.put(getCurrentFrame(), null);
	}
    }

    private void createDefaultMarker(Point2D framePosition) {
	// Just add a new point with the same properties to the trajectory
	Point0 newPoint = activeTrajectory.copyLastPoint();
	newPoint.setX((int) Math.round(framePosition.getX()));
	newPoint.setY((int) Math.round(framePosition.getY()));
	newPoint.setSource(Source.User);

	activeTrajectory.put(getCurrentFrame(), newPoint);
	save(activeTrajectory);
    }

    private void createTrajectory(Point2D screenPosition, Point2D framePosition) {
	TrajectoryCreateDialog dialog = new TrajectoryCreateDialog(getScene()
		.getWindow());
	dialog.setX(screenPosition.getX());
	dialog.setY(screenPosition.getY());
	dialog.showAndWait();
	if (dialog.succeeded()) {

	    activeTrajectory = new Trajectory0(getTracker());
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
