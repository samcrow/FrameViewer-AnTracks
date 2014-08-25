package org.samcrow.frameviewer.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Window;
import org.samcrow.frameviewer.trajectory.Point;
import org.samcrow.frameviewer.trajectory.Trajectory;

/**
 * A Trajectory dialog that allows the user to
 * finalize the trajectory
 * @author Sam Crow
 */
public class TrajectoryEditDialog extends TrajectoryCreateDialog {
    
    private boolean finalizeRequested = false;

    public TrajectoryEditDialog(Window parent, Trajectory trajectory, Point point) {
        super(parent, trajectory, point);
        addFinalizeButtion();
    }

    public TrajectoryEditDialog(Window parent) {
        super(parent);
        addFinalizeButtion();
    }
    
    private void addFinalizeButtion() {
        final Button button = new Button("Finalize trajectory");
        
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                succeeded = true;
                finalizeRequested = true;
                close();
            }
        });
        
        addNode(button);
    }
    
    public boolean finalizeRequested() {
        return finalizeRequested;
    }
}
