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
import org.samcrow.frameviewer.trajectory.Point;
import org.samcrow.frameviewer.trajectory.Trajectory;

/**
 * Manages user interactions with the currently displayed frame
 * <p>
 * @author samcrow
 */
public class FrameController {

    private ObjectProperty<Scene> scene = new SimpleObjectProperty<>();

    private final PaintableCanvas canvas;

    private final ObjectProperty<List<Trajectory>> trajectories = createTrajectoriesProperty();

    private final IntegerProperty frame = new SimpleIntegerProperty();

    private final ObjectProperty<DatabaseTrajectoryDataStore> dataStore = new SimpleObjectProperty<>();

    public FrameController(PaintableCanvas canvas) {
        this.canvas = canvas;
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

    public final List<Trajectory> getTrajectories() {
        return trajectories.get();
    }

    public final void setTrajectories(List<Trajectory> trajectories) {
        this.trajectories.set(trajectories);
    }
    
    public final ObjectProperty<List<Trajectory>> trajectoriesProperty() {
        return trajectories;
    }

    /**
     * Returns the trajectory near the requested frame position, or null
     * if none exists
     * <p>
     * @param framePosition
     * @return
     */
    protected final Trajectory getTrajectoryNear(Point2D framePosition) {
        for (Trajectory trajectory : getTrajectories()) {
            try {
                final Point point = trajectory.get(getCurrentFrame());
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
    protected final Point getPointNear(Point2D framePosition) {
        final Trajectory trajectory = getTrajectoryNear(framePosition);
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

    protected final void save(Trajectory trajectory) {
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

    protected final void save(Point point, int trajectoryId) {
        try {
            getDataStore().persistPoint(point, trajectoryId);
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

    private static ObjectProperty<List<Trajectory>> createTrajectoriesProperty() {
        final List<Trajectory> initialList = new ArrayList<>();
        return new SimpleObjectProperty<>(initialList);
    }

}
