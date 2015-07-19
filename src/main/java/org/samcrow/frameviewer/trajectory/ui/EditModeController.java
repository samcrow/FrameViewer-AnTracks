package org.samcrow.frameviewer.trajectory.ui;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import org.samcrow.frameviewer.PaintableCanvas;
import org.samcrow.frameviewer.track.Tracker;
import org.samcrow.frameviewer.trajectory.InteractionPoint;
import org.samcrow.frameviewer.trajectory.Point0;
import org.samcrow.frameviewer.trajectory.Trajectory0;

/**
 * Controller for edit mode
 * <p>
 * @author samcrow
 */
public class EditModeController extends FrameController {

    private boolean dragging = false;

    private Point0 activePoint;

    public EditModeController(PaintableCanvas canvas, Tracker tracker) {
        super(canvas, tracker);
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
	final Trajectory0 activeTrajectory = getActiveTrajectory();
        if (activeTrajectory != null) {
            
            PointEditDialog dialog = new PointEditDialog(activeTrajectory, activePoint, getScene().getWindow());
            dialog.setX(event.getScreenX());
            dialog.setY(event.getScreenY());
            dialog.showAndWait();

            if(dialog.result == PointEditDialog.Result.Save) {
                
                activeTrajectory.setId(dialog.getTrajectoryId());
                activeTrajectory.setFromAction(dialog.getFromAction());
                activeTrajectory.setToAction(dialog.getToAction());
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
                        activePoint = new Point0(activePoint);
                    }
                }
                
                activeTrajectory.put(getCurrentFrame(), activePoint);
                save(activeTrajectory);
                repaint();
            }
            else if(dialog.result == PointEditDialog.Result.DeletePoint) {
                delete(activePoint, getCurrentFrame(), activeTrajectory.getId());
                activeTrajectory.put(getCurrentFrame(), null);
                repaint();
            }
            else if(dialog.result == PointEditDialog.Result.DeleteTrajectory) {
                getTrajectories().remove(activeTrajectory);
                delete(activeTrajectory);
                setActiveTrajectory(null);
                repaint();
            }
        }

    }

    @Override
    protected void handleMouseReleased(MouseEvent event, Point2D framePosition) {
        
        dragging = false;
        if(activePoint != null && getActiveTrajectory() != null) {
            save(activePoint, getActiveTrajectory().getId());
        }
    }

    @Override
    protected void handleMousePressed(MouseEvent event, Point2D framePosition) {
        
        // Look for a point to drag
        final Trajectory0 activeTrajectory = getTrajectoryNear(framePosition);
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
