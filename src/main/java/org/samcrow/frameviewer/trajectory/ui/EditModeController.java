package org.samcrow.frameviewer.trajectory.ui;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import org.samcrow.frameviewer.PaintableCanvas;
import org.samcrow.frameviewer.trajectory.InteractionPoint;
import org.samcrow.frameviewer.trajectory.Point;
import org.samcrow.frameviewer.trajectory.Trajectory;

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
            
            PointEditDialog dialog = new PointEditDialog(activeTrajectory, activePoint, getScene().getWindow());
            dialog.setX(event.getScreenX());
            dialog.setY(event.getScreenY());
            dialog.showAndWait();

            if(dialog.result == PointEditDialog.Result.Save) {
                
                activeTrajectory.setId(dialog.getTrajectoryId());
                activeTrajectory.setStartAction(dialog.getStartAction());
                activeTrajectory.setEndAction(dialog.getEndAction());
                activePoint.setActivity(dialog.getActivity());
                if(dialog.isInteraction()) {
                    // Promote point if needed
                    if(!(activePoint instanceof InteractionPoint)) {
                        activePoint = new InteractionPoint(activePoint);
                    }
                    // Propagate
                    final InteractionPoint iPoint = (InteractionPoint) activePoint;
                    iPoint.setType(dialog.getInteractionType());
                    iPoint.setMetAntId(dialog.getMetTrajectoryId());
                    iPoint.setMetAntActivity(dialog.getMetTrajectoryActivity());
                }
                else {
                    // Demote point if needed
                    if(activePoint instanceof InteractionPoint) {
                        activePoint = new Point(activePoint);
                    }
                }
                
                activeTrajectory.set(getCurrentFrame(), activePoint);
                save(activePoint, activeTrajectory.getId());
                repaint();
            }
            else if(dialog.result == PointEditDialog.Result.DeletePoint) {
                delete(activePoint, activeTrajectory.getId());
                activeTrajectory.set(getCurrentFrame(), null);
                repaint();
            }
            else if(dialog.result == PointEditDialog.Result.DeleteTrajectory) {
                getTrajectories().remove(activeTrajectory);
                delete(activeTrajectory);
                activeTrajectory = null;
                repaint();
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
