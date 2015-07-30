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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import jfxtras.labs.dialogs.MonologFX;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.samcrow.frameviewer.PaintableCanvas;
import org.samcrow.frameviewer.io3.DatabaseTrajectoryDataStore;
import org.samcrow.frameviewer.track.Tracker;
import org.samcrow.frameviewer.trajectory.Point0;
import org.samcrow.frameviewer.trajectory.Trajectory0;

/**
 * Manages user interactions with the currently displayed frame
 * <p>
 * @author samcrow
 */
public class FrameController {

    private final ObjectProperty<Scene> scene = new SimpleObjectProperty<>();

    private final PaintableCanvas canvas;

    private final ObjectProperty<List<Trajectory0>> trajectories = createTrajectoriesProperty();

    private final IntegerProperty frame = new SimpleIntegerProperty();

    private final ObjectProperty<DatabaseTrajectoryDataStore> dataStore = new SimpleObjectProperty<>();
    
    /**
     * The trajectory selected for creation or editing, or null
     */
    private final ObjectProperty<Trajectory0> activeTrajectory = new SimpleObjectProperty<>();
    
    private final Tracker tracker;

    public FrameController(PaintableCanvas canvas, Tracker tracker) {
        this.canvas = canvas;
	this.tracker = tracker;
    }

    /**
     * Called when a mouse event is received. Subclasses should override
     * this method. The default implementation does nothing.
     * <p>
     * @param event The mouse event
     * @param framePosition The position of the event in frame coordinates
     */
    public void handleMouseEvent(MouseEvent event, Point2D framePosition) {
        final EventType<? extends Event> type = event.getEventType();
        if (type == MouseEvent.MOUSE_PRESSED) {
            handleMousePressed(event, framePosition);
        }
        else if (type == MouseEvent.MOUSE_RELEASED) {
            handleMouseReleased(event, framePosition);
        }
        else if (type == MouseEvent.MOUSE_CLICKED && event.isStillSincePress()) {
            handleMouseClicked(event, framePosition);
        }
        else if (type == MouseEvent.MOUSE_MOVED) {
            handleMouseMoved(event, framePosition);
        }
        else if(type == MouseEvent.MOUSE_DRAGGED) {
            handleMouseDragged(event, framePosition);
        }
    }

    protected void handleMousePressed(MouseEvent event, Point2D framePosition) {

    }

    protected void handleMouseReleased(MouseEvent event, Point2D framePosition) {

    }

    protected void handleMouseClicked(MouseEvent event, Point2D framePosition) {

    }

    protected void handleMouseMoved(MouseEvent event, Point2D framePosition) {

    }
    
    protected void handleMouseDragged(MouseEvent event, Point2D framePosition) {
        
    }

    public final List<Trajectory0> getTrajectories() {
        return trajectories.get();
    }

    public final void setTrajectories(List<Trajectory0> trajectories) {
        this.trajectories.set(trajectories);
    }
    
    public final ObjectProperty<List<Trajectory0>> trajectoriesProperty() {
        return trajectories;
    }

    /**
     * Returns the trajectory near the requested frame position, or null
     * if none exists
     * <p>
     * @param framePosition
     * @return
     */
    protected final Trajectory0 getTrajectoryNear(Point2D framePosition) {
        for (Trajectory0 trajectory : getTrajectories()) {
            try {
                final Point0 point = trajectory.get(getCurrentFrame());
                if (point != null) {
                    // Find distance
                    final double MAX_RADIUS = 6;
                    final double MAX_RADIUS_SQUARED = MAX_RADIUS * MAX_RADIUS;

                    final double dx = framePosition.getX() - point.getX();
                    final double dy = framePosition.getY() - point.getY();

                    final double distanceSquared = dx * dx + dy * dy;

                    if (distanceSquared <= MAX_RADIUS_SQUARED) {
                        return trajectory;
                    }

                }
                // else continue
            }
            catch (IndexOutOfBoundsException ex) {
                // Continue
            }
        }
        return null;
    }

    /**
     * Returns the point near the requested frame position, or null
     * if none exists
     * <p>
     * @param framePosition
     * @return
     */
    protected final Point0 getPointNear(Point2D framePosition) {
        final Trajectory0 trajectory = getTrajectoryNear(framePosition);
        if (trajectory != null) {
            try {
                return trajectory.get(getCurrentFrame());
            }
            catch (IndexOutOfBoundsException ex) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public final int getCurrentFrame() {
        return frame.get();
    }

    public final void setCurrentFrame(int frame) {
        this.frame.set(frame);
    }

    public final IntegerProperty currentFrameProperty() {
        return frame;
    }

    public final DatabaseTrajectoryDataStore getDataStore() {
        return dataStore.get();
    }

    public final void setDataStore(DatabaseTrajectoryDataStore store) {
        dataStore.set(store);
    }

    public final ObjectProperty<DatabaseTrajectoryDataStore> dataStoreProperty() {
        return dataStore;
    }

    protected final Scene getScene() {
        return scene.get();
    }
    
    public final ObjectProperty<Scene> sceneProperty() {
        return scene;
    }

    protected final void save(Trajectory0 trajectory) {
        try {
            getDataStore().persistTrajectory(trajectory);
        }
        catch (SQLException ex) {
            MonologFX dialog = new MonologFX(MonologFX.Type.ERROR);
            dialog.setTitle(ex.toString());
            dialog.setMessage(ExceptionUtils.getFullStackTrace(ex));
            dialog.showDialog();
            ex.printStackTrace();
        }
    }

    protected final void save(Point0 point, int trajectoryId) {
        try {
            getDataStore().persistPoint(point, getCurrentFrame(), trajectoryId);
        }
        catch (SQLException ex) {
            MonologFX dialog = new MonologFX(MonologFX.Type.ERROR);
            dialog.setTitle(ex.toString());
            dialog.setMessage(ExceptionUtils.getFullStackTrace(ex));
            dialog.showDialog();
            ex.printStackTrace();
        }
    }
    
    protected final void delete(Trajectory0 trajectory) {
        try {
            getDataStore().deleteTrajectory(trajectory);
        }
        catch (SQLException ex) {
            MonologFX dialog = new MonologFX(MonologFX.Type.ERROR);
            dialog.setTitle(ex.toString());
            dialog.setMessage(ExceptionUtils.getFullStackTrace(ex));
            dialog.showDialog();
            ex.printStackTrace();
        }
    }
    
    protected final void delete(Point0 point, int frame, int trajectoryId) {
        try {
            getDataStore().deletePoint(trajectoryId, frame);
        }
        catch (SQLException ex) {
            MonologFX dialog = new MonologFX(MonologFX.Type.ERROR);
            dialog.setTitle(ex.toString());
            dialog.setMessage(ExceptionUtils.getFullStackTrace(ex));
            dialog.showDialog();
            ex.printStackTrace();
        }
    }

    protected final void repaint() {
        canvas.repaint();
    }

    private static ObjectProperty<List<Trajectory0>> createTrajectoriesProperty() {
        final List<Trajectory0> initialList = new ArrayList<>();
        return new SimpleObjectProperty<>(initialList);
    }

    public Tracker getTracker() {
	return tracker;
    }
    

    public Trajectory0 getActiveTrajectory() {
	return activeTrajectory.get();
    }
    
    public void setActiveTrajectory(Trajectory0 trajectory) {
	activeTrajectory.set(trajectory);
    }
    
    public final ObjectProperty<Trajectory0> activeTrajectoryProperty() {
	return activeTrajectory;
    }
    
}
