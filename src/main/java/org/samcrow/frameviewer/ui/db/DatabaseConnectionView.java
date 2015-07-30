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
// </editor-fold>package org.samcrow.frameviewer.ui.db;

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
    
    private final TextField databaseField = new TextField();
    
    public DatabaseConnectionView() {
        final Insets MARGIN = new Insets(5);
        
        addressField.setPromptText("Address");
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");
        databaseField.setPromptText("Database");
        
        getChildren().addAll(addressField, usernameField, passwordField, databaseField);
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
    
    public String getDatabase() {
        return databaseField.getText();
    }
    
}
