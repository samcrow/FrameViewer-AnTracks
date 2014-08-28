package org.samcrow.frameviewer.ui.db;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * A dialog that allows a user to choose parameters and open a database connection
 * @author Sam Crow
 */
public class DatabaseConnectionView extends VBox {
    
    private final TextField addressField = new TextField();
    
    private final TextField usernameField = new TextField();
    
    private final TextField passwordField = new PasswordField();
    
    public DatabaseConnectionView() {
        final Insets MARGIN = new Insets(5);
        
        addressField.setPromptText("Address");
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");
        
        getChildren().addAll(addressField, usernameField, passwordField);
        for(Node node : getChildrenUnmodifiable()) {
            setMargin(node, MARGIN);
        }
    }
    
    public String getAddress() {
        return addressField.getText();
    }
    
    public String getUsername() {
        return usernameField.getText();
    }
    
    public String getPassword() {
        return passwordField.getText();
    }
    
}
