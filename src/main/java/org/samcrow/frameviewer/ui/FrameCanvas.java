package org.samcrow.frameviewer.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.samcrow.frameviewer.io3.Marker;
import org.samcrow.frameviewer.PaintableCanvas;
import org.samcrow.frameviewer.trajectory.Trajectory;
import org.samcrow.frameviewer.io3.AntActivity;
import org.samcrow.frameviewer.io3.AntLocation;
import org.samcrow.frameviewer.io3.InteractionMarker;

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

    public FrameCanvas() {

        setFocusTraversable(true);
        requestFocus();

        //Add a marker when clicked on
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Point2D markerPoint = getFrameLocation(event);

                    // TODO
                    
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
                requestFocus();
                repaint();
            }
        });
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
            
            // TODO: Draw things

            gc.restore();
        }

    }

    private Marker createMarkerFromKey(MouseEvent location, KeyEvent keyEvent) throws NotInFrameException {
        Point2D screenPos = getFrameLocation(location);
        if (keyEvent.getCharacter().equals("i")) {
            // Interaction: entrance chamber, both ants walking and 2 way interaction
            InteractionMarker marker = new InteractionMarker(screenPos, AntActivity.Walking, AntLocation.EntranceChamber, AntActivity.Walking, AntLocation.EntranceChamber);
            marker.setType(InteractionMarker.InteractionType.TwoWay);
            marker.setAntId(MarkerDialog.getLastAntId());

            return marker;
        }
        else if (keyEvent.getCharacter().equals("x")) {
            // Not an interaction: Focal ant, walking, exit
            Marker marker = new Marker(screenPos, AntActivity.Walking, AntLocation.AtExit);
            marker.setAntId(MarkerDialog.getLastAntId());
            return marker;
        }
        else if (keyEvent.getCharacter().equals("e")) {
            // Not an interaction: Focal ant, walking, entrance chamber
            Marker marker = new Marker(screenPos, AntActivity.Walking, AntLocation.EntranceChamber);
            marker.setAntId(MarkerDialog.getLastAntId());
            return marker;
        }
        else if (keyEvent.getCharacter().equals("t")) {
            // Not an interaction: Focal ant, walking, tunnel
            Marker marker = new Marker(screenPos, AntActivity.Walking, AntLocation.AtTunnel);
            marker.setAntId(MarkerDialog.getLastAntId());
            return marker;
        }
        else if(keyEvent.getCharacter().equals("g")) {
            // Not an interaction: Focal ant walking, edge
            Marker marker = new Marker(screenPos, AntActivity.Walking, AntLocation.Edge);
            marker.setAntId(MarkerDialog.getLastAntId());
            return marker;
        }
        else if(keyEvent.getCharacter().equals("o")) {
            // Not an interaction: Focal ant walking, outside
            Marker marker = new Marker(screenPos, AntActivity.Walking, AntLocation.Outside);
            marker.setAntId(MarkerDialog.getLastAntId());
            return marker;
        }
        else {
            throw new IllegalArgumentException("No marker default corresponding to this key");
        }

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

    private boolean markerClicked(Marker marker, Point2D frameLocation) {
        final int radius = 6;
        return radius >= frameLocation.distance(marker.getX(), marker.getY());

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
        if(trajectories == null) {
            throw new IllegalArgumentException("The trajectory list must not be null");
        }
        
        this.trajectories = trajectories;
    }

}
