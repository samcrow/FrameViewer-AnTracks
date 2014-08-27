package org.samcrow.frameviewer.ui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import jfxtras.labs.dialogs.MonologFX;
import org.samcrow.frameviewer.io3.Marker;
import org.samcrow.frameviewer.PaintableCanvas;
import org.samcrow.frameviewer.io3.DatabaseTrajectoryDataStore;
import org.samcrow.frameviewer.trajectory.InteractionPoint;
import org.samcrow.frameviewer.trajectory.Point;
import org.samcrow.frameviewer.trajectory.Trajectory;

/**
 * Displays a video frame and allows it to be clicked on
 * <p>
 * @author Sam Crow
 */
public class FrameCanvas extends PaintableCanvas {

    /**
     * The image to be displayed
     */
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();

    private final IntegerProperty currentFrame = new SimpleIntegerProperty();

    private List<Trajectory> trajectories = new LinkedList<>();

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

    private MouseEvent lastMouseMove;

    private DatabaseTrajectoryDataStore dataStore;

    /**
     * The trajectory that is currently being edited
     */
    private Trajectory activeTrajectory = null;

    public FrameCanvas() {

        setFocusTraversable(true);
        requestFocus();

        //Add a marker when clicked on
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Point2D markerPoint = getFrameLocation(event);

                    if (activeTrajectory == null) {
                        // Create a new trajectory

                        TrajectoryCreateDialog dialog = new TrajectoryCreateDialog(getScene().getWindow());
                        dialog.setX(event.getScreenX());
                        dialog.setY(event.getScreenY());
                        dialog.showAndWait();
                        if (dialog.succeeded()) {

                            activeTrajectory = new Trajectory(getCurrentFrame(), getCurrentFrame() + 1);
                            activeTrajectory.setDataStore(dataStore);
                            trajectories.add(activeTrajectory);
                            activeTrajectory.setId(dialog.getTrajectoryId());
                            activeTrajectory.setMoveType(dialog.getMoveType());
                            dataStore.add(activeTrajectory);

                            // Create a new Point at the mouse location
                            Point newPoint = new Point((int) Math.round(markerPoint.getX()), (int) Math.round(markerPoint.getY()));
                            newPoint.setActivity(dialog.getActivity());
                            activeTrajectory.set(getCurrentFrame(), newPoint);
                            saveActiveTrajectory();
                        }

                    }
                    else {
                        // An active trajectory already exists
                        try {
                            if (event.isShiftDown()) {
                                createInteractionMarker(markerPoint);
                            }
                            else if (event.getButton() == MouseButton.SECONDARY) {
                                // Copy the point and edit it
                                Point newPoint = activeTrajectory.copyLastPoint();
                                newPoint.setX((int) Math.round(markerPoint.getX()));
                                newPoint.setY((int) Math.round(markerPoint.getY()));

                                // Temporarily add the point so that it will be displayed
                                // while the errorDialog is visible
                                activeTrajectory.set(getCurrentFrame(), newPoint);
                                repaint();

                                TrajectoryEditDialog dialog = new TrajectoryEditDialog(getScene().getWindow(), activeTrajectory, newPoint);
                                dialog.setX(event.getScreenX());
                                dialog.setY(event.getScreenY());
                                dialog.showAndWait();

                                if (dialog.succeeded()) {
                                    activeTrajectory.setId(dialog.getTrajectoryId());
                                    activeTrajectory.setMoveType(dialog.getMoveType());
                                    newPoint.setActivity(dialog.getActivity());

                                    activeTrajectory.set(getCurrentFrame(), newPoint);
                                    // Save the trajectory
                                    saveActiveTrajectory();

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
                            else {
                                // Just add a new point with the same properties to the trajectory
                                Point newPoint = activeTrajectory.copyLastPoint();
                                newPoint.setX((int) Math.round(markerPoint.getX()));
                                newPoint.setY((int) Math.round(markerPoint.getY()));

                                activeTrajectory.set(getCurrentFrame(), newPoint);

                            }
                        }
                        catch (IllegalStateException ex) {
                            // Do nothing
                        }

                    }

                    repaint();

                }
                catch (NotInFrameException ex) {
                    Logger.getLogger(FrameCanvas.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        //Update cursor position when the mouse moves
        setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lastMouseMove = event;
                requestFocus();
            }
        });

        setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                // TODO
            }
        });

        //Repaint when the frame or the markers changes
        image.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {

                // Update trajectories 
                if (dataStore != null) {
                    trajectories = dataStore.getObjectsNearCurrentFrame(20);
                }

                requestFocus();
                repaint();
            }
        });
    }

    private void createInteractionMarker(Point2D frameLocation) {

        Point nearbyPoint = null;
        Trajectory trajectoryWithNearbyPoint = null;
        // Search for a nearby Point that has the same frame number as this frame
        for (Trajectory trajectory : trajectories) {
            // Ignore the trajectory that is currently being edited
            if (trajectory == activeTrajectory) {
                continue;
            }

            try {
                Point point = trajectory.get(getCurrentFrame());

                if (point != null) {
                    if (pointClicked(point.getX(), point.getY(), frameLocation)) {
                        nearbyPoint = point;
                        trajectoryWithNearbyPoint = trajectory;
                        break;
                    }
                }
            }
            catch (IndexOutOfBoundsException ex) {
                // No point from that trajectory
                // Do nothing
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
            newPoint.setOtherPoint((InteractionPoint) nearbyPoint);
        }
        else {
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

        }
        else {
            // Remove the point
            activeTrajectory.set(getCurrentFrame(), null);
        }

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
            }
            else {
                //Window is wider than image
                //If necessary, shrink image height to fit
                imageHeight = Math.min(targetImageHeight, canvasHeight);
                imageWidth = imageHeight * imageAspectRatio;
            }

            final double centerX = canvasWidth / 2;
            final double centerY = canvasHeight / 2;
            imageTopLeftX = centerX - imageWidth / 2;
            imageTopLeftY = centerY - imageHeight / 2;

            gc.drawImage(image.get(), imageTopLeftX, imageTopLeftY, imageWidth, imageHeight);

            gc.save();

            // Draw trajectories
            for (Trajectory trajectory : trajectories) {
                trajectory.paint(gc, image.get().getWidth(), image.get().getHeight(), imageWidth, imageHeight, imageTopLeftX, imageTopLeftY, getCurrentFrame());
            }

            gc.restore();
        }

    }

    private Marker createMarkerFromKey(MouseEvent location, KeyEvent keyEvent) throws NotInFrameException {
        Point2D screenPos = getFrameLocation(location);

        throw new IllegalArgumentException("No marker default corresponding to this key");

    }

    /**
     * Returns the location, in frame image coordinates, of a mouse event
     * <p>
     * @param event
     * @return
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
     * @throws org.samcrow.frameviewer.ui.FrameCanvas.NotInFrameException
     */
    private Point2D getFrameLocation(double x, double y) throws NotInFrameException {
        if ((x < imageTopLeftX || x > (imageTopLeftX + imageWidth))
                || (y < imageTopLeftY || y > (imageTopLeftY + imageHeight))) {
            throw new NotInFrameException("The provided click was outside the bounds of the frame");
        }

        final double xRatio = (x - imageTopLeftX) / imageWidth;
        final double yRatio = (y - imageTopLeftY) / imageHeight;

        assert xRatio <= 1;
        assert yRatio <= 1;

        return new Point2D(image.get().getWidth() * xRatio, image.get().getHeight() * yRatio);
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

    public void setTrajectories(List<Trajectory> trajectories) {
        if (trajectories == null) {
            throw new IllegalArgumentException("The trajectory list must not be null");
        }

        this.trajectories = trajectories;
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
        trajectories = dataStore.getObjectsNearCurrentFrame(20);
    }

    private void saveActiveTrajectory() {
        if (activeTrajectory != null && dataStore != null) {
            try {
                dataStore.persistTrajectory(activeTrajectory);
            }
            catch (SQLException ex) {
                MonologFX errorDialog = new MonologFX(MonologFX.Type.ERROR);
                errorDialog.setTitle("Error: Trajectory not saved");
                errorDialog.setMessage(ex.toString());
                errorDialog.showDialog();
                ex.printStackTrace();
            }
        }
    }

}
