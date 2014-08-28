package org.samcrow.frameviewer.trajectory.ui;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.samcrow.frameviewer.PaintableCanvas;
import org.samcrow.frameviewer.trajectory.InteractionPoint;
import org.samcrow.frameviewer.trajectory.Point;
import org.samcrow.frameviewer.trajectory.Trajectory;
import org.samcrow.frameviewer.ui.InteractionPointDialog;
import org.samcrow.frameviewer.ui.TrajectoryCreateDialog;
import org.samcrow.frameviewer.ui.TrajectoryEditDialog;

/**
 * Controller for create mode
 * <p>
 * @author samcrow
 */
public class CreateModeController extends FrameController {

    private Trajectory activeTrajectory;

    public CreateModeController(PaintableCanvas canvas) {
        super(canvas);
    }

    @Override
    protected void handleMouseClicked(MouseEvent event, Point2D framePosition) {
        if (activeTrajectory == null) {
            // Create a new trajectory
            createTrajectory(new Point2D(event.getScreenX(), event.getScreenY()), framePosition);
        }
        else {
            // Modify active trajectory
            if (event.isShiftDown()) {
                createInteractionMarker(framePosition);
            }
            else if (event.getButton() == MouseButton.SECONDARY) {
                createCustomMarker(new Point2D(event.getScreenX(), event.getScreenY()), framePosition);
            }
            else {
                createDefaultMarker(framePosition);
            }
        }
        repaint();
    }

    private void createInteractionMarker(Point2D frameLocation) {

        final Trajectory trajectoryWithNearbyPoint = getTrajectoryNear(frameLocation);
        Point nearbyPoint = null;
        if (trajectoryWithNearbyPoint != null) {
            try {
                nearbyPoint = trajectoryWithNearbyPoint.get(getCurrentFrame());
            }
            catch (IndexOutOfBoundsException ex) {
                return;
            }
        }

        // Now nearbyPoint is either null or an existing point on the current frame
        InteractionPoint newPoint;
        if (nearbyPoint != null) {
            // Make it an InteractionPoint, if it is not already one
            if (!(nearbyPoint instanceof InteractionPoint)) {
                nearbyPoint = new InteractionPoint(nearbyPoint);
                trajectoryWithNearbyPoint.set(getCurrentFrame(), nearbyPoint);
                nearbyPoint.setActivity(trajectoryWithNearbyPoint.getLastPoint().getActivity());
                ((InteractionPoint) nearbyPoint).setFocalAntId(trajectoryWithNearbyPoint.getId());
            }

            newPoint = InteractionPoint.inverted((InteractionPoint) nearbyPoint);
            newPoint.setX((int) Math.round(frameLocation.getX()));
            newPoint.setY((int) Math.round(frameLocation.getY()));
            // Propagate properties from the last point to the new point
            newPoint.setFocalAntId(activeTrajectory.getId());
            newPoint.setFocalAntActivity(activeTrajectory.getLastPoint().getActivity());
            newPoint.setMetAntId(trajectoryWithNearbyPoint.getId());

            newPoint.setOtherPoint((InteractionPoint) nearbyPoint);
        }
        else {
            // No nearby point; just create a new point
            newPoint = new InteractionPoint((int) Math.round(frameLocation.getX()), (int) Math.round(frameLocation.getY()));
            newPoint.setFocalAntId(activeTrajectory.getId());
            newPoint.setFocalAntActivity(activeTrajectory.getLastPoint().getActivity());
        }
        // Temporarily insert the point, so that it will be visible when the errorDialog appears
        activeTrajectory.set(getCurrentFrame(), newPoint);
        repaint();

        InteractionPointDialog dialog = new InteractionPointDialog(getScene().getWindow(), newPoint);
        dialog.showAndWait();
        if (dialog.success()) {

            newPoint.setFocalAntId(dialog.getFocalAntId());
            newPoint.setMetAntId(dialog.getMetAntId());
            newPoint.setFocalAntActivity(dialog.getFocalAntActivity());
            newPoint.setMetAntActivity(dialog.getMetAntActivity());
            newPoint.setType(dialog.getInteractionType());

            save(activeTrajectory);
            save(trajectoryWithNearbyPoint);
        }
        else {
            // Remove the point
            activeTrajectory.set(getCurrentFrame(), null);
        }

    }

    private void createCustomMarker(Point2D screenPosition, Point2D framePosition) {
        // Copy the point and edit it
        Point newPoint = activeTrajectory.copyLastPoint();
        newPoint.setX((int) Math.round(framePosition.getX()));
        newPoint.setY((int) Math.round(framePosition.getY()));

        // Temporarily add the point so that it will be displayed
        // while the errorDialog is visible
        activeTrajectory.set(getCurrentFrame(), newPoint);
        repaint();

        TrajectoryEditDialog dialog = new TrajectoryEditDialog(getScene().getWindow(), activeTrajectory, newPoint);
        dialog.setX(screenPosition.getX());
        dialog.setY(screenPosition.getY());
        dialog.showAndWait();

        if (dialog.succeeded()) {
            activeTrajectory.setId(dialog.getTrajectoryId());
            activeTrajectory.setMoveType(dialog.getMoveType());
            newPoint.setActivity(dialog.getActivity());

            activeTrajectory.set(getCurrentFrame(), newPoint);
            // Save the trajectory
            save(activeTrajectory);

            if (dialog.finalizeRequested()) {
                // Make this trajectory no longer active
                // The list still has a strong reference to it.
                activeTrajectory = null;
            }
        }
        else {
            // Not succeeded; Remove the new point
            activeTrajectory.set(getCurrentFrame(), null);
        }
    }

    private void createDefaultMarker(Point2D framePosition) {
        // Just add a new point with the same properties to the trajectory
        Point newPoint = activeTrajectory.copyLastPoint();
        newPoint.setX((int) Math.round(framePosition.getX()));
        newPoint.setY((int) Math.round(framePosition.getY()));

        activeTrajectory.set(getCurrentFrame(), newPoint);
        save(activeTrajectory);
    }

    private void createTrajectory(Point2D screenPosition, Point2D framePosition)    {
        TrajectoryCreateDialog dialog = new TrajectoryCreateDialog(getScene().getWindow());
        dialog.setX(screenPosition.getX());
        dialog.setY(screenPosition.getY());
        dialog.showAndWait();
        if (dialog.succeeded()) {

            activeTrajectory = new Trajectory(getCurrentFrame(), getCurrentFrame() + 1);
            activeTrajectory.setDataStore(getDataStore());
            getTrajectories().add(activeTrajectory);
            activeTrajectory.setId(dialog.getTrajectoryId());
            activeTrajectory.setMoveType(dialog.getMoveType());
            getDataStore().add(activeTrajectory);

            // Create a new Point at the mouse location
            Point newPoint = new Point((int) Math.round(framePosition.getX()), (int) Math.round(framePosition.getY()));
            newPoint.setActivity(dialog.getActivity());
            activeTrajectory.set(getCurrentFrame(), newPoint);
            save(activeTrajectory);
        }
    }

}
