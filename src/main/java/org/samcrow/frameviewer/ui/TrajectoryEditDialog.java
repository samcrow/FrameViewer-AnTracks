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
package org.samcrow.frameviewer.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Window;
import org.samcrow.frameviewer.trajectory.Point0;
import org.samcrow.frameviewer.trajectory.Trajectory0;

/**
 * A Trajectory dialog that allows the user to
 * finalize the trajectory
 * @author Sam Crow
 */
public class TrajectoryEditDialog extends TrajectoryCreateDialog {
    
    private boolean finalizeRequested = false;
    
    private final RadioButtonGroup<Trajectory0.ToAction> toAction = new RadioButtonGroup<>(Trajectory0.ToAction.values());

    public TrajectoryEditDialog(Window parent, Trajectory0 trajectory, Point0 point) {
        super(parent, trajectory, point);
        addEndActionBox(trajectory.getToAction());
        addFinalizeButtion();
    }

    public TrajectoryEditDialog(Window parent) {
        super(parent);
        addFinalizeButtion();
    }
    
    private void addEndActionBox(Trajectory0.ToAction initialValue) {
        toAction.setValue(initialValue);
        addNode(new Label("Trajectory end action"));
        addNode(toAction);
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
    
    public Trajectory0.ToAction getToAction() {
        return toAction.getValue();
    }
}
