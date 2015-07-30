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
                activeTrajectory.remove(getCurrentFrame());
		activePoint = null;
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
	    getActiveTrajectory().put(getCurrentFrame(), activePoint);
            save(activePoint, getActiveTrajectory().getId());
	    repaint();
        }
    }

    @Override
    protected void handleMousePressed(MouseEvent event, Point2D framePosition) {
        
        // Look for a point to drag
        final Trajectory0 activeTrajectory = getTrajectoryNear(framePosition);
	setActiveTrajectory(activeTrajectory);
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
