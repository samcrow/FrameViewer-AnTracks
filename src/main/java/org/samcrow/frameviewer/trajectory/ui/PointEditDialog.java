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
import org.samcrow.frameviewer.trajectory.Point;
import org.samcrow.frameviewer.trajectory.Trajectory;
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
    
    private final RadioButtonGroup<Trajectory.FromAction> fromAction = new RadioButtonGroup<>(Trajectory.FromAction.values());
    private final RadioButtonGroup<Trajectory.ToAction> toAction = new RadioButtonGroup<>(Trajectory.ToAction.values());
    
    // Point
    private final RadioButtonGroup<Point.Activity> focalActivityGroup = new RadioButtonGroup<>(Point.Activity.values());
    
    // InteractionPoint
    private final CheckBox interactionBox = new CheckBox("Interaction");
    private final RadioButtonGroup<InteractionType> interactionTypeGroup = new RadioButtonGroup<>(InteractionType.values());
    private final IntegerField metTrajectoryIdField = new IntegerField();
    private final RadioButtonGroup<Point.Activity> metActivityGroup = new RadioButtonGroup<>(Point.Activity.values());
    
    public PointEditDialog(Trajectory trajectory, Point point, Window owner) {
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
            metActivityGroup.setValue(Point.Activity.NotCarrying);
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
    
    public final Point.Activity getActivity() {
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
    
    public final Point.Activity getMetTrajectoryActivity() {
        return metActivityGroup.getValue();
    }
    
    public final Trajectory.FromAction getFromAction() {
        return fromAction.getValue();
    }
    
    public final Trajectory.ToAction getToAction() {
        return toAction.getValue();
    }
    
}
