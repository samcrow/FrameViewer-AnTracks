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
package org.samcrow.frameviewer.trajectory;

import java.io.IOException;
import java.sql.SQLException;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.samcrow.frameviewer.io3.DatabaseTrajectoryDataStore;
import org.samcrow.frameviewer.track.Tracker;

/**
 * Stores a path followed by an ant.
 *
 * A trajectory is defined by a start frame and position, an end frame and
 * position,
 * and zero or more intermediate frames and positions.
 *
 * A trajectory's start frame is defined when the trajectory is created, based
 * on
 * input from a human user.
 *
 * Intermediate frames and points are defined based on input from the user.
 *
 * The end frame is defined based on input from the user. A trajectory may
 * not have an end frame.
 *
 * In between these user-defined frames and positions, a position is
 * automatically
 * generated for each frame. If a trajectory does not have an end frame,
 * positions are automatically generated for each frame that the user views.
 *
 * If the user adds, edits, or deletes a point, the automatically generated
 * points
 * in between that point and the adjacent user-defined points are recalculated.
 *
 * @author Sam Crow
 */
public class Trajectory0 extends TrackingTrajectory<Point0> {

    public static enum FromAction {

	Other,
	OutOfTunnel,;

	/**
	 * Returns the type corresponding to a name, but never
	 * throws an exception. Returns Unknown if a valid value
	 * could not be found.
	 * <p>
	 * @param name
	 * @return
	 */
	public static FromAction safeValueOf(String name) {
	    try {
		return valueOf(name);
	    } catch (Exception ex) {
		return Other;
	    }
	}
    }

    public static enum ToAction {

	Other,
	IntoTunnel,;

	/**
	 * Returns the type corresponding to a name, but never
	 * throws an exception. Returns Unknown if a valid value
	 * could not be found.
	 * <p>
	 * @param name
	 * @return
	 */
	public static ToAction safeValueOf(String name) {
	    try {
		return valueOf(name);
	    } catch (Exception ex) {
		return Other;
	    }
	}
    }

    private int id;

    private FromAction fromAction = FromAction.Other;
    private ToAction toAction = ToAction.Other;

    public Trajectory0(Tracker tracker) {
	super(tracker,
		(int x, int y, Point.Source source, Point0 previous, Point0 next) -> {
		    Point0 point = new Point0(x, y, source);
		    point.setActivity(previous.getActivity());
		    return point;
		});
    }

    public void setId(int newId) {
	id = newId;
    }

    public int getId() {
	return id;
    }

    public FromAction getFromAction() {
	return fromAction;
    }

    public void setFromAction(FromAction startAction) {
	this.fromAction = startAction;
    }

    public ToAction getToAction() {
	return toAction;
    }

    public void setToAction(ToAction endAction) {
	this.toAction = endAction;
    }

    /**
     * Creates and returns a copy of the Point corresponding to the highest
     * numbered frame.
     * <p>
     * @throws IllegalStateException if this trajectory does not contain any
     * points
     * @return
     */
    public Point0 copyLastPoint() {
	return new Point0(get(getLastFrame()));
    }

    public void paint(GraphicsContext gc, double nativeImageWidth,
	    double nativeImageHeight, double actualImageWidth,
	    double actualImageHeight, double imageTopLeftX, double imageTopLeftY,
	    int currentFrame, TrajectoryDisplayMode mode, boolean showPastEnd) {
	switch (mode) {
	    case Full:
		paintFull(gc, nativeImageWidth, nativeImageHeight,
			actualImageWidth, actualImageHeight, imageTopLeftX,
			imageTopLeftY, currentFrame, showPastEnd);
		break;
	    case Hidden:
		// Do nothing
		break;
	    // TOOD: Restore others
	}
    }

    private void paintFull(GraphicsContext gc, double nativeImageWidth,
	    double nativeImageHeight, double actualImageWidth,
	    double actualImageHeight, double imageTopLeftX, double imageTopLeftY,
	    int currentFrame, boolean showPastEnd) {
	Point2D lastLocation = null;
	for (Entry<Point0> entry : this) {
	    Point0 point = entry.point;
	    final Point2D canvasPos = imageToCanvasPosition(new Point2D(point
		    .getX(), point.getY()), nativeImageWidth, nativeImageHeight,
		    actualImageWidth, actualImageHeight, imageTopLeftX,
		    imageTopLeftY);

	    // Draw a line from the last point to this one
	    if (lastLocation != null) {
		gc.setStroke(Color.LIGHTGREEN);
		gc.strokeLine(lastLocation.getX(), lastLocation.getY(),
			canvasPos.getX(), canvasPos.getY());
	    }

	    final boolean hilighted = currentFrame == entry.frame;
	    if (point.getSource() == Point.Source.User) {
		// Draw the marker
		point.paint(gc, canvasPos.getX(), canvasPos.getY(), hilighted);
	    }

	    lastLocation = canvasPos;
	}
	// Past-end tracking
	if (showPastEnd) {

	    if (lastLocation != null && currentFrame > getLastFrame()) {
		for (int frame = getLastFrame() + 1; frame <= currentFrame; frame++) {
		    Point0 point = getWithTracking(frame);
		    final Point2D canvasPos = imageToCanvasPosition(new Point2D(
			    point
			    .getX(), point.getY()), nativeImageWidth,
			    nativeImageHeight,
			    actualImageWidth, actualImageHeight, imageTopLeftX,
			    imageTopLeftY);
		    // Draw a line
		    if (lastLocation != null) {
			gc.setStroke(Color.YELLOW);
			gc.strokeLine(lastLocation.getX(), lastLocation.getY(),
				canvasPos.getX(), canvasPos.getY());
		    }

		    if (frame == currentFrame) {
			point.paint(gc, canvasPos.getX(), canvasPos.getY(),
				false);
		    }
		    lastLocation = canvasPos;
		}
	    }
	}
    }

    private Point2D imageToCanvasPosition(Point2D imagePosition,
	    double nativeImageWidth, double nativeImageHeight,
	    double actualImageWidth, double actualImageHeight,
	    double imageTopLeftX, double imageTopLeftY) {
	final double xRatio = imagePosition.getX() / nativeImageWidth;
	final double yRatio = imagePosition.getY() / nativeImageHeight;
	final double canvasX = imageTopLeftX + xRatio * actualImageWidth;
	final double canvasY = imageTopLeftY + yRatio * actualImageHeight;

	return new Point2D(canvasX, canvasY);
    }

    // Persistence section
    private DatabaseTrajectoryDataStore dataStore;

    public DatabaseTrajectoryDataStore getDataStore() {
	return dataStore;
    }

    public void setDataStore(DatabaseTrajectoryDataStore dataStore) {
	this.dataStore = dataStore;
    }

    /**
     * Saves this trajectory and all its points
     * <p>
     * @throws java.io.IOException
     */
    public void save() throws IOException {
	if (dataStore != null) {
	    try {
		dataStore.persistTrajectory(this);
	    } catch (SQLException ex) {
		throw new IOException(ex);
	    }
	} else {
	    throw new IllegalStateException(
		    "Can't call save() on a Trajectory with no datastore defined");
	}
    }

}
