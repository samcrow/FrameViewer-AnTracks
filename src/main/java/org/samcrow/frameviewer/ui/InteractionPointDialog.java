package org.samcrow.frameviewer.ui;

import java.util.ListIterator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.samcrow.frameviewer.trajectory.InteractionPoint;
import org.samcrow.frameviewer.trajectory.InteractionType;
import org.samcrow.frameviewer.trajectory.Point0;

/**
 * A dialog that allows a trajectory marker to be created or edited
 * @author Sam Crow
 */
public class InteractionPointDialog extends Stage {
    
    
    protected boolean succeeded = false;

    protected final IntegerField antIdField = new IntegerField();
    protected final IntegerField metAntIdField = new IntegerField(0);
    
    protected final RadioButtonGroup<Point0.Activity> activityBox = new RadioButtonGroup<>(Point0.Activity.values());
    
    protected final RadioButtonGroup<InteractionType> interactionTypeBox
            = new RadioButtonGroup<>(InteractionType.values());
    
    protected final RadioButtonGroup<Point0.Activity> metActivityBox = new RadioButtonGroup<>(Point0.Activity.values());
    

    private final VBox root = new VBox();

    private static final Insets PADDING = new Insets(10);
    
    public InteractionPointDialog(Window parent, InteractionPoint point) {
        this(parent);
        setFocalAntId(point.getFocalAntId());
        setMetAntId(point.getMetAntId());
        setFocalAntActivity(point.getFocalAntActivity());
        setMetAntActivity(point.getMetAntActivity());
        setInteractionType(point.getType());
    }
    
    public InteractionPointDialog(Window parent) {
        
        GridPane topBox = new GridPane();
        {
            
            antIdField.setPrefColumnCount(4);
            //Close when the return key is pressed
            antIdField.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    antIdField.setFieldFocused(false);
                    succeeded = true;
                    close();
                }
            });
            
            GridPane focalAntPane = new GridPane();
            {
                final Label label = new Label("Focal ant:");
                focalAntPane.add(label, 0, 0);
                GridPane.setMargin(label, PADDING);
                
                focalAntPane.add(antIdField, 1, 0);
                GridPane.setMargin(antIdField, PADDING);

                // Focus ant activity and location
                focalAntPane.add(activityBox, 0, 1, 2, 1);
                GridPane.setMargin(activityBox, PADDING);
            
            }

            // Checkbox and met ant selectors
            GridPane metAntPane = new GridPane();
            {
                // Interaction type to the right of the checkbox
                metAntPane.add(interactionTypeBox, 1, 0, 1, 1);
                GridPane.setMargin(interactionTypeBox, PADDING);

                Label metLabel = new Label("Met ant:");
                metAntPane.add(metLabel, 0, 1, 1, 1);
                GridPane.setMargin(metLabel, PADDING);

                // Met ant ID field
                metAntPane.add(metAntIdField, 1, 1, 1, 1);
                GridPane.setMargin(metAntIdField, PADDING);

                metAntPane.add(metActivityBox, 0, 2, 2, 1);
                GridPane.setMargin(metActivityBox, PADDING);
            
            }
            
            topBox.add(focalAntPane, 0, 0, 1, 1);
            topBox.add(metAntPane, 1, 0, 1, 1);
            
        }
        root.getChildren().add(topBox);
        
        
        HBox bottomBox = new HBox();
        {
            final Button cancelButton = new Button("Cancel");
            cancelButton.setCancelButton(true);
            cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    succeeded = false;
                    close();
                }
            });
            bottomBox.getChildren().add(cancelButton);
            HBox.setMargin(cancelButton, PADDING);
            
            final Button okButton = new Button("OK");
            okButton.setDefaultButton(true);
            okButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    succeeded = true;
                    close();
                }
            });
            bottomBox.getChildren().add(okButton);
            HBox.setMargin(okButton, PADDING);
        }
        root.getChildren().add(bottomBox);
        
        
        setTitle("Edit interaction");
        initOwner(parent);
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UTILITY);
        
        root.setPadding(PADDING);
        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
        setScene(scene);
    }
    
    public final void setFocalAntId(int id) {
        antIdField.setValue(id);
    }
    
    public int getFocalAntId() {
        return antIdField.getValue();
    }
    
    public final void setMetAntId(int id) {
        metAntIdField.setValue(id);
    }
    
    public int getMetAntId() {
        return metAntIdField.getValue();
    }
    
    public Point0.Activity getFocalAntActivity() {
        return activityBox.getValue();
    }
    
    public final void setFocalAntActivity(Point0.Activity activity) {
        activityBox.setValue(activity);
    }
    
    public Point0.Activity getMetAntActivity() {
        return metActivityBox.getValue();
    }
    
    public final void setMetAntActivity(Point0.Activity activity) {
        metActivityBox.setValue(activity);
    }
    
    public final void setInteractionType(InteractionType type) {
        interactionTypeBox.setValue(type);
    }
    
    public InteractionType getInteractionType() {
        return interactionTypeBox.getValue();
    }
    
    /**
     * 
     * @return True if the user entered valid values, otherwise false
     */
    public boolean success() {
        return succeeded;
    }
    
    
    
    /**
     * Adds a node to this window, below the controls and above the OK/cancel
     * buttons.
     * @param node 
     */
    protected void insertNode(Node node) {
        ObservableList<Node> children = root.getChildren();
        // Get an iterator with a cursor just before the last element
        ListIterator<Node> iterator = children.listIterator(children.size() - 1);
        // Insert just before the last element
        iterator.add(node);
        VBox.setMargin(node, PADDING);
    }
}
