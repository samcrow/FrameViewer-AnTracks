package org.samcrow.frameviewer;

import org.samcrow.frameviewer.ui.FrameCanvas;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.samcrow.frameviewer.trajectory.Trajectory0;

public class DataStoringPlaybackControlModel extends PlaybackControlModel {

    private MultiFrameDataStore<Trajectory0> trajectoryDataStore;

    private FrameCanvas canvas;

    public DataStoringPlaybackControlModel(FrameFinder frameFinder,
            MultiFrameDataStore<Trajectory0> newTrajectoryDataStore) {
        
        super(frameFinder);
        trajectoryDataStore = newTrajectoryDataStore;
        
        // Bind frame number
        newTrajectoryDataStore.currentFrameProperty().bind(currentFrameProperty());

        currentFrameProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {

                if (canvas != null) {
                    int oldFrame = oldValue.intValue();
                    int newFrame = newValue.intValue();
                    
                    // TODO

                    canvas.repaint();
                }
                else {
                    Logger.getLogger(DataStoringPlaybackControlModel.class.getName()).warning("No canvas is set. This model will not update any markers.");
                }
            }
        });
    }

    /**
     * Sets up a canvas to have its markers be managed by this model
     * <p/>
     * @param canvas
     */
    public final void bindMarkers(FrameCanvas canvas) {
        this.canvas = canvas;
        
        this.canvas.currentFrameProperty().bind(this.currentFrameProperty());
    }
    
    public void setTrajectoryDataStore(MultiFrameDataStore<Trajectory0> trajectoryDataStore) {
        this.trajectoryDataStore = trajectoryDataStore;
        //Move to the first frame
        int firstFrame = getFirstFrame();
        setCurrentFrame(firstFrame);
       
        List<Trajectory0> trajectories = trajectoryDataStore.getObjectsForCurrentFrame();
        canvas.setTrajectories(trajectories);
        
        
        canvas.repaint();
    }

    /**
     * Stores the data from the current frame into the data store.
     * This should be called before a file is saved to ensure that data from the
     * current frame gets saved.
     */
    public void syncCurrentFrameData() {
        // TODO
    }

    public void undo() {
        
        // TODO
    }

}
