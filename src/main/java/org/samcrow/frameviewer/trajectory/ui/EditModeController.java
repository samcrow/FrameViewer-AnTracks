package org.samcrow.frameviewer.trajectory.ui;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import org.samcrow.frameviewer.PaintableCanvas;
import org.samcrow.frameviewer.trajectory.Point;
import org.samcrow.frameviewer.trajectory.Trajectory;
import org.samcrow.frameviewer.ui.TrajectoryCreateDialog;

/**
 * Controller for edit mode
 * <p>
 * @author samcrow
 */
public class EditModeController extends FrameController {

    private Trajectory activeTrajectory;

    private boolean dragging = false;

    private Point activePoint;

    public EditModeController(PaintableCanvas canvas) {
        super(canvas);
    }

    @Override
    protected void handleMouseDragged(MouseEvent event, Point2D framePosition) {
        
        if (dragging && activePoint != null) {
            activePoint.setX((int) Math.round(framePosition.getX()));
            activePoint.setY((int) Math.round(framePosition.getY()));
            repaint();
        }
    }

    @Override
    protected void handleMouseClicked(MouseEvent event, Point2D framePosition) {
        
        dragging = false;

        // Look for a point to edit
        if (activePoint != null) {
            TrajectoryCreateDialog dialog = new TrajectoryCreateDialog(getScene().getWindow(), activeTrajectory, activePoint);
            dialog.setX(event.getScreenX());
            dialog.setY(event.getScreenY());
            dialog.showAndWait();

            if (dialog.succeeded()) {
                activeTrajectory.setId(dialog.getTrajectoryId());
                activeTrajectory.setMoveType(dialog.getMoveType());
                activePoint.setActivity(dialog.getActivity());

                
                // Save the trajectory
                save(activeTrajectory);

            }
        }

    }

    @Override
    protected void handleMouseReleased(MouseEvent event, Point2D framePosition) {
        
        dragging = false;
        if(activePoint != null && activeTrajectory != null) {
            save(activePoint, activeTrajectory.getId());
        }
    }

    @Override
    protected void handleMousePressed(MouseEvent event, Point2D framePosition) {
        
        // Look for a point to drag
        activeTrajectory = getTrajectoryNear(framePosition);
        if (activeTrajectory != null) {
            try {
                activePoint = activeTrajectory.get(getCurrentFrame());
                if (activePoint != null) {
                    dragging = true;
                }
            }
            catch (IndexOutOfBoundsException ex) {
                activePoint = null;
                dragging = false;
            }
        }
    }

}
