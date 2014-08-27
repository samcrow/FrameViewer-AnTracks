package org.samcrow.frameviewer.trajectory;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A point that marks an interaction
 * <p>
 * @author samcrow
 */
public class InteractionPoint extends Point {
    
    private int focalAntId = 0;
    
    private int metAntId = 0;

    // focalAntActivity refers to the activity field of Point
    
    private Activity metAntActivity = Activity.NotCarrying;
    
    private InteractionType type = InteractionType.Unknown;

    /**
     * The point in the other trajectory that also corresponds to this
     * interaction
     */
    private InteractionPoint otherPoint;

    public InteractionPoint(int x, int y) {
        super(x, y);
    }

    public InteractionPoint(Point other) {
        super(other);
    }
    
    /**
     * Creates and returns a new InteractionPoint that is an inverted,
     * complementary version of the provided original point.
     * @param original
     * @return 
     */
    public static InteractionPoint inverted(InteractionPoint original) {
        InteractionPoint instance = new InteractionPoint(0, 0);
        
        instance.setFocalAntActivity(original.metAntActivity);
        instance.metAntId = original.focalAntId;
        
        instance.setFocalAntActivity(original.metAntActivity);
        instance.metAntActivity = original.getFocalAntActivity();
        
        instance.type = original.type.invert();
        
        return instance;
    }

    public InteractionPoint(InteractionPoint other) {
        super(other);
        this.setFocalAntActivity(other.getFocalAntActivity());
        this.metAntActivity = other.metAntActivity;
        this.type = other.type;
    }

    public Activity getFocalAntActivity() {
        return getActivity();
    }

    public final void setFocalAntActivity(Activity focalAntActivity) {
        setActivity(focalAntActivity);
        if(otherPoint != null) {
            otherPoint.metAntActivity = focalAntActivity;
        }
    }

    public Activity getMetAntActivity() {
        return metAntActivity;
    }

    public void setMetAntActivity(Activity metAnActivity) {
        this.metAntActivity = metAnActivity;
        if(otherPoint != null) {
            otherPoint.activity = metAnActivity;
        }
    }

    public InteractionType getType() {
        return type;
    }

    public void setType(InteractionType type) {
        this.type = type;
        if(otherPoint != null) {
            // Set the type of the other marker
            // Don't call setType() because that will cause stack overflow
            otherPoint.type = type.invert();
        }
    }

    public InteractionPoint getOtherPoint() {
        return otherPoint;
    }

    public void setOtherPoint(InteractionPoint otherPoint) {
        this.otherPoint = otherPoint;
    }

    public int getFocalAntId() {
        return focalAntId;
    }

    public void setFocalAntId(int focalAntId) {
        this.focalAntId = focalAntId;
        if(otherPoint != null) {
            otherPoint.metAntId = focalAntId;
        }
    }

    public int getMetAntId() {
        return metAntId;
    }

    public void setMetAntId(int metAntId) {
        this.metAntId = metAntId;
        if(otherPoint != null) {
            otherPoint.focalAntId = metAntId;
        }
    }

    @Override
    public void paint(GraphicsContext gc, double canvasX, double canvasY, boolean hilighted) {
        gc.save();
        gc.setFill(Color.YELLOW);
        final int RADIUS = 7;
        gc.fillOval(canvasX - RADIUS, canvasY - RADIUS, 2 * RADIUS, 2 * RADIUS);
        if(hilighted) {
            final int HILIGHT_RADIUS = RADIUS + 1;
            gc.setLineWidth(1);
            gc.setStroke(Color.RED);
            gc.strokeOval(canvasX - HILIGHT_RADIUS , canvasY - HILIGHT_RADIUS, 2 * HILIGHT_RADIUS, 2 * HILIGHT_RADIUS);
        }
        
        gc.restore();
    }
    
    
    

}