package org.samcrow.frameviewer.ui;

import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * Displays a group of toggle buttons
 * @author samcrow
 */
public class ToggleButtonGroup extends HBox {

    public ToggleButtonGroup() {
        
        // Deal with buttons when they are added
        getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Node> change) {
                while(change.next()) {
                    if(change.wasAdded()) {
                        final List<? extends Node> addedNodes = change.getAddedSubList();

                        for(Node node : addedNodes) {
                            node.setStyle("-fx-background-radius: 0;");
                        }
                    }
                }
            }
        });
    }
    
    
    
}
