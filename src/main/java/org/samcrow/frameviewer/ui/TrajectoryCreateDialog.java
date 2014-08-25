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
import org.samcrow.frameviewer.trajectory.Point;
import org.samcrow.frameviewer.trajectory.Trajectory;

/**
 * A dialog that allows the user to set the properties of a 
 * @author samcrow
 */
public class TrajectoryCreateDialog extends Stage {
    
    protected final IntegerField antIdField = new IntegerField();
    
    protected final RadioButtonGroup<Trajectory.MoveType> moveTypeBox = new RadioButtonGroup<>(Trajectory.MoveType.values());
    
    protected final RadioButtonGroup<Point.Activity> activityBox = new RadioButtonGroup<>(Point.Activity.values());
    
    private final VBox root = new VBox();

    private static final Insets PADDING = new Insets(10);
    
    protected boolean succeeded = false;
    
    /**
     * Creates a dialog and copies its initial values from the given trajectory and point
     * @param parent
     * @param trajectory
     * @param point 
     */
    public TrajectoryCreateDialog(Window parent, Trajectory trajectory, Point point) {
        this(parent);
        
        antIdField.setValue(trajectory.getId());
        moveTypeBox.setValue(trajectory.getMoveType());
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
        
        moveTypeBox.setPadding(PADDING);
        root.getChildren().add(moveTypeBox);
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
        moveTypeBox.setValue(Trajectory.MoveType.Unknown);
        activityBox.setValue(Point.Activity.NotCarrying);
        
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
    
    public Trajectory.MoveType getMoveType() {
        return moveTypeBox.getValue();
    }
    
    public Point.Activity getActivity() {
        return activityBox.getValue();
    }
}
