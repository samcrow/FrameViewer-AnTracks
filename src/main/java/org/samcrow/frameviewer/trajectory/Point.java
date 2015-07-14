package org.samcrow.frameviewer.trajectory;

import java.util.Objects;

/**
 * A point in a video frame.
 *
 * Coordinates are in pixels from the upper left corner of the frame, in the
 * native resolution of the frame.
 *
 * @author Sam Crow
 */
public class Point {
    
    /**
     * Sources that can create points
     */
    public enum Source {
	/**
	 * The point was entered by the user
	 */
	User,
	/**
	 * The point was interpolated between adjacent points, without using
	 * image-based tracking
	 */
	Interpolation,
	/**
	 * The point was calculated using image-based tracking
	 */
	Tracking,
    }

    /**
     * The X location
     */
    protected int x;
    /**
     * The Y location
     */
    protected int y;

    /**
     * The source that created this point
     */
    private Source source;
    
    /**
     * Creates a new point
     *
     * @param x the X location
     * @param y the Y location
     * @param source the source that created this point
     */
    public Point(int x, int y, Source source) {
	this.x = x;
	this.y = y;
	this.source = source;
    }
    
    /**
     * Creates a new Point as a copy of another
     * @param other the point to copy from
     */
    public Point(Point other) {
	this.x = other.x;
	this.y = other.y;
	this.source = other.source;
    }

    /**
     *
     * @return The X location of this point
     */
    public int getX() {
	return x;
    }

    /**
     *
     * @return the Y location of this point
     */
    public int getY() {
	return y;
    }

    /**
     * 
     * @return the source of this point
     */
    public Source getSource() {
	return source;
    }

    public void setSource(Source source) {
	this.source = source;
    }

    @Override
    public String toString() {
	return "Point{" + "x=" + x + ", y=" + y + ", source=" + source + '}';
    }

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 67 * hash + this.x;
	hash = 67 * hash + this.y;
	hash = 67 * hash + Objects.hashCode(this.source);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Point other = (Point) obj;
	if (this.x != other.x) {
	    return false;
	}
	if (this.y != other.y) {
	    return false;
	}
	if (this.source != other.source) {
	    return false;
	}
	return true;
    }
    
    
}
