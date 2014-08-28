package org.samcrow.frameviewer.ui;

import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Toggle;
import javafx.scene.layout.HBox;

/**
 * Displays a group of Toggles (and other nodes) and keeps track of one
 * active Toggle. Sets up nodes to look like split buttons.
 * <p>
 * @author samcrow
 */
public class SplitButtonBar extends HBox {
    
    private ObjectProperty<Toggle> selectedToggle = new SimpleObjectProperty<>();

    public SplitButtonBar() {

        // Deal with buttons when they are added
        getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Node> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        final List<? extends Node> addedNodes = change.getAddedSubList();
                        for (final Node node : addedNodes) {
                            node.setStyle("-fx-background-radius: 0;");

                            // When a button, or other toggleable thing, is selected, make every other button deselected
                            if (node instanceof Toggle) {
                                ((Toggle) node).selectedProperty().addListener(new ChangeListener<Boolean>() {
                                    @Override
                                    public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean newValue) {
                                        if(newValue) {
                                            selectedToggle.set((Toggle) node);
                                            // This is newly selected
                                            deselectEachExcept((Toggle) node);
                                        }
                                    }
                                });
                            }

                        }
                    }
                }
                // Restore the ususal radius for the outside corners
                // Not currently working
                final List<? extends Node> list = getChildren();
                if (!list.isEmpty()) {
                    list.get(0).setStyle("-fx-background-radius: 3 0 0 3, 2 0 0 2, 2 0 0 2;");
                    list.get(list.size() - 1).setStyle("-fx-background-radius: 0 3 3 0, 0 2 2 0, 0 2 2 0;");
                }
            }
        });
        
        selectedToggle.addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle newValue) {
                newValue.setSelected(true);
                deselectEachExcept(newValue);
            }
        });
    }

    /**
     * Deselects each Toggle in this group except the provided one
     * <p>
     * @param toggle
     */
    private void deselectEachExcept(Toggle toggle) {
        for (Node node : getChildrenUnmodifiable()) {
            if (node instanceof Toggle && node != toggle) {
                ((Toggle) node).setSelected(false);
            }
        }
    }

    public final Toggle getSelectedToggle() {
        return selectedToggle.get();
    }
    
    public final void setSelectedToggle(Toggle toggle) {
        selectedToggle.set(toggle);
    }
    
    public final ObjectProperty<Toggle> selectedToggleProperty() {
        return selectedToggle;
    }
    
}
