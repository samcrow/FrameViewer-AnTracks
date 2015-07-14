package org.samcrow.frameviewer.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.samcrow.frameviewer.trajectory.Point0;
import org.samcrow.frameviewer.trajectory.Trajectory0;

/**
 * A dialog that allows the user to set the properties of a 
 * @author samcrow
 */
public class TrajectoryCreateDialog extends Stage {
    
    protected final IntegerField antIdField = new IntegerField();
    
    protected final RadioButtonGroup<Trajectory0.FromAction> startActionBox = new RadioButtonGroup<>(Trajectory0.FromAction.values());
    
    protected final RadioButtonGroup<Point0.Activity> activityBox = new RadioButtonGroup<>(Point0.Activity.values());
    
    private final VBox root = new VBox();

    private static final Insets PADDING = new Insets(10);
    
    protected boolean succeeded = false;
    
    /**
     * Creates a dialog and copies its initial values from the given trajectory and point
     * @param parent
     * @param trajectory
     * @param point 
     */
    public TrajectoryCreateDialog(Window parent, Trajectory0 trajectory, Point0 point) {
        this(parent);
        
        antIdField.setValue(trajectory.getId());
        startActionBox.setValue(trajectory.getFromAction());
        activityBox.setValue(point.getActivity());
    }
    
    public TrajectoryCreateDialog(Window parent) {
        setTitle("Edit trajectory point");
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UTILITY);
        initOwner(parent);
        
        // Layout

        HBox antIdBox = new HBox();
        {
            antIdField.setPrefColumnCount(4);
            HBox.setMargin(antIdField, PADDING);
            final Label label = new Label("Ant ID");
            HBox.setMargin(label, PADDING);
            antIdBox.getChildren().add(label);
            antIdBox.getChildren().add(antIdField);
            antIdBox.setFillHeight(true);
        }
        root.getChildren().add(antIdBox);
        
        startActionBox.setPadding(PADDING);
        root.getChildren().add(startActionBox);
        activityBox.setPadding(PADDING);
        root.getChildren().add(activityBox);
        
        HBox buttonBox = new HBox();
        {
            final Button cancelButton = new Button("Cancel");
            cancelButton.setCancelButton(true);
            cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    onCancelButtonPressed();
                }
            });
            buttonBox.getChildren().add(cancelButton);
            HBox.setMargin(cancelButton, PADDING);
            
            final Button okButton = new Button("OK");
            okButton.setDefaultButton(true);
            okButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    onOkButtonPressed();
                }
            });
            buttonBox.getChildren().add(okButton);
            HBox.setMargin(okButton, PADDING);
        }
        root.getChildren().add(buttonBox);
        
        // Set default values
        antIdField.setValue(0);
        startActionBox.setValue(Trajectory0.FromAction.Other);
        activityBox.setValue(Point0.Activity.NotCarrying);
        
        // Focus on ID field
        antIdField.requestFocus();
        
        final Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
        setScene(scene);
    }
    
    /**
     * Adds a node to this dialog, above the OK and cancel buttons
     * @param node 
     */
    protected void addNode(Node node) {
        VBox.setMargin(node, PADDING);
        root.getChildren().add(root.getChildren().size() - 1, node);
    }
    
    private void onOkButtonPressed() {
        succeeded = true;
        close();
    }
    
    private void onCancelButtonPressed() {
        succeeded = false;
        close();
    }
    
    public boolean succeeded() {
        return succeeded;
    }
    
    public int getTrajectoryId() {
        return antIdField.getValue();
    }
    
    public Trajectory0.FromAction getStartAction() {
        return startActionBox.getValue();
    }
    
    public Point0.Activity getActivity() {
        return activityBox.getValue();
    }
}
