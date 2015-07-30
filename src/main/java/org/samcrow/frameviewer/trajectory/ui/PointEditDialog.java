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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.samcrow.frameviewer.trajectory.InteractionPoint;
import org.samcrow.frameviewer.trajectory.InteractionType;
import org.samcrow.frameviewer.trajectory.Point0;
import org.samcrow.frameviewer.trajectory.Trajectory0;
import org.samcrow.frameviewer.ui.IntegerField;
import org.samcrow.frameviewer.ui.RadioButtonGroup;

/**
 *
 * @author samcrow
 */
public class PointEditDialog extends Stage {
    
    public enum Result {
        Save,
        Cancel,
        DeletePoint,
        DeleteTrajectory,
    }
    
    protected Result result = Result.Cancel;
    
    // Elements
    // Trajectory
    private final IntegerField trajectoryIdField = new IntegerField();
    
    private final RadioButtonGroup<Trajectory0.FromAction> fromAction = new RadioButtonGroup<>(Trajectory0.FromAction.values());
    private final RadioButtonGroup<Trajectory0.ToAction> toAction = new RadioButtonGroup<>(Trajectory0.ToAction.values());
    
    // Point
    private final RadioButtonGroup<Point0.Activity> focalActivityGroup = new RadioButtonGroup<>(Point0.Activity.values());
    
    // InteractionPoint
    private final CheckBox interactionBox = new CheckBox("Interaction");
    private final RadioButtonGroup<InteractionType> interactionTypeGroup = new RadioButtonGroup<>(InteractionType.values());
    private final IntegerField metTrajectoryIdField = new IntegerField();
    private final RadioButtonGroup<Point0.Activity> metActivityGroup = new RadioButtonGroup<>(Point0.Activity.values());
    
    public PointEditDialog(Trajectory0 trajectory, Point0 point, Window owner) {
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UTILITY);
        
        setUpBindings();
        setUpLayout();
        
        // Set up values based on the provided trajectory and point
        trajectoryIdField.setValue(trajectory.getId());
        focalActivityGroup.setValue(point.getActivity());
        fromAction.setValue(trajectory.getFromAction());
        toAction.setValue(trajectory.getToAction());
        if(point instanceof InteractionPoint) {
            final InteractionPoint iPoint = (InteractionPoint) point;
            
            interactionBox.setSelected(true);
            interactionTypeGroup.setValue(iPoint.getType());
            metTrajectoryIdField.setValue(iPoint.getMetAntId());
            metActivityGroup.setValue(iPoint.getMetAntActivity());
        }
        else {
            interactionBox.setSelected(false);
            // Default values for interaction fields
            interactionTypeGroup.setValue(InteractionType.Unknown);
            metActivityGroup.setValue(Point0.Activity.NotCarrying);
        }
    }
    
    private void setUpBindings() {
        // Associate trajectory box with enabling of trajectory fields
        interactionTypeGroup.disableProperty().bind(interactionBox.selectedProperty().not());
        metTrajectoryIdField.disableProperty().bind(interactionBox.selectedProperty().not());
        metActivityGroup.disableProperty().bind(interactionBox.selectedProperty().not());
        
    }
    
    private void setUpLayout() {
        final VBox root = new VBox();
        root.setPadding(new Insets(2));
        
        root.getChildren().addAll(trajectoryIdField, new Label("Start action"), fromAction, new Label("End action"), toAction, focalActivityGroup, interactionBox, interactionTypeGroup, metTrajectoryIdField, metActivityGroup);
        for(Node child : root.getChildren()) {
            VBox.setMargin(child, new Insets(5));
        }
        
        // Buttons
        {
            final GridPane buttonPane = new GridPane();
            final Insets BUTTON_MARGIN = new Insets(2);
            
            final Button deletePointButton = new Button("Delete point");
            deletePointButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    result = Result.DeletePoint;
                    hide();
                }
            });
            buttonPane.add(deletePointButton, 0, 0, 2, 1);
            GridPane.setMargin(deletePointButton, BUTTON_MARGIN);
            GridPane.setHgrow(deletePointButton, Priority.ALWAYS);
            
            final Button deleteTrajectoryButton = new Button("Delete trajectory");
            deleteTrajectoryButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    result = Result.DeleteTrajectory;
                    hide();
                }
            });
            buttonPane.add(deleteTrajectoryButton, 0, 1, 2, 1);
            GridPane.setMargin(deleteTrajectoryButton, BUTTON_MARGIN);
            
            final Button cancelButton = new Button("Cancel");
            cancelButton.setCancelButton(true);
            cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    result = Result.Cancel;
                    hide();
                }
            });
            buttonPane.add(cancelButton, 0, 2);
            GridPane.setMargin(cancelButton, BUTTON_MARGIN);
            
            final Button okButton = new Button("OK");
            okButton.setDefaultButton(true);
            okButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    result = Result.Save;
                    hide();
                }
            });
            buttonPane.add(okButton, 1, 2);
            GridPane.setMargin(okButton, BUTTON_MARGIN);
            
            
            root.getChildren().add(buttonPane);
        }
        
        final Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
        setScene(scene);
    }
    
    
    public final Result getResult() {
        return result;
    }
    
    public final int getTrajectoryId() {
        return trajectoryIdField.getValue();
    }
    
    public final Point0.Activity getActivity() {
        return focalActivityGroup.getValue();
    }
    
    public final boolean isInteraction() {
        return interactionBox.isSelected();
    }
    
    public final InteractionType getInteractionType() {
        return interactionTypeGroup.getValue();
    }
    
    public final int getMetTrajectoryId() {
        return metTrajectoryIdField.getValue();
    }
    
    public final Point0.Activity getMetTrajectoryActivity() {
        return metActivityGroup.getValue();
    }
    
    public final Trajectory0.FromAction getFromAction() {
        return fromAction.getValue();
    }
    
    public final Trajectory0.ToAction getToAction() {
        return toAction.getValue();
    }
    
}
