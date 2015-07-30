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
package org.samcrow.frameviewer.ui.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.labs.dialogs.MonologFX;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 *
 * @author samcrow
 */
public class DatabaseConnectionDialog extends Stage {

    private final String connectionTypeName;

    private DatabaseConnectionView connectionView;

    private DataSetSelectionView selectionView = null;

    private final Button cancelButton = new Button("Cancel");

    private final Button nextButton = new Button("Next");

    private Connection connection = null;

    private boolean success = false;
    private String pointsTableName;
    private String trajectoriesTableName;

    public DatabaseConnectionDialog(String connectionTypeName) {
	this.connectionTypeName = connectionTypeName;
	setTitle("Database connection");
	// Placeholder
	root.getChildren().add(new Region());

	// Buttons
	{
	    final HBox buttonBox = new HBox();
	    buttonBox.setPadding(new Insets(10));

	    cancelButton.setCancelButton(true);
	    buttonBox.getChildren().add(cancelButton);
	    cancelButton.setOnAction((ActionEvent t) -> {
		hide();
	    });

	    final Region spacer = new Region();
	    buttonBox.getChildren().add(spacer);
	    HBox.setHgrow(spacer, Priority.ALWAYS);

	    nextButton.setDefaultButton(true);
	    buttonBox.getChildren().add(nextButton);

	    root.getChildren().add(buttonBox);
	}

	switchToConnection();

	final Scene scene = new Scene(root, 220, root.getPrefHeight());
	setScene(scene);
    }

    private final VBox root = new VBox();

    private void openConnection() {
	try {
	    if (connectionView.getAddress().isEmpty()) {
		showDialog("Server address required",
			"Please specify an address");
		return;
	    }
	    if (connectionView.getUsername().isEmpty()) {
		showDialog("Username required", "Please specify a username");
		return;
	    }

	    DriverManager.setLoginTimeout(5);
	    final String url = "jdbc:" + connectionTypeName + "://"
		    + connectionView.getAddress() + "/"
		    + connectionView.getDatabase();
	    connection = DriverManager
		    .getConnection(url,
			    connectionView.getUsername(),
			    connectionView.getPassword());

	    switchToDataSetSelection();

	} catch (Throwable ex) {
	    MonologFX dialog = new MonologFX(MonologFX.Type.ERROR);
	    dialog.setTitle("Could not connect to database");
	    dialog.setMessage(ex.getMessage()
		    + "\nPlease ensure that the address, username, password, and database are correct.");
	    dialog.showDialog();
	    ex.printStackTrace();
	}
    }

    private void selectDataSet() {
	final String dataSetName = selectionView.getSelectedDataSet();
	if (dataSetName.isEmpty()) {
	    showDialog("Data set required", "Please enter a data set name");
	    return;
	}
	try {
	    pointsTableName = dataSetName + "_points";
	    trajectoriesTableName = dataSetName + "_trajectories";

	    // Verify that the tables exist
	    try (ResultSet tables = connection.getMetaData().getTables(
		    connection.getCatalog(), null, pointsTableName, null)) {
		if (!tables.next()) {
		    throw new SQLException("No points table exists");
		}
	    }
	    try (ResultSet tables = connection.getMetaData().getTables(
		    connection.getCatalog(), null, trajectoriesTableName, null)) {
		if (!tables.next()) {
		    throw new SQLException("No points table exists");
		}
	    }

	    success = true;
	    pointsTableName = dataSetName + "_points";
	    trajectoriesTableName = dataSetName + "_trajectories";
	    hide();
	} catch (SQLException ex) {
	    // Tables do not exist. Try to create them.
	    try (Statement statement = connection.createStatement()) {

		statement.executeUpdate("DROP TABLE IF EXISTS "
			+ pointsTableName);
		statement.executeUpdate("DROP TABLE IF EXISTS "
			+ trajectoriesTableName);

		statement.executeUpdate("CREATE TABLE " + pointsTableName
			+ " ( `id` INT(11) PRIMARY KEY AUTO_INCREMENT ) ");
		statement.executeUpdate("CREATE TABLE " + trajectoriesTableName
			+ " ( `id` INT(11) PRIMARY KEY AUTO_INCREMENT ) ");

                // Now drop the tables so that the other class can create them
		statement.executeUpdate("DROP TABLE " + pointsTableName);
		statement.executeUpdate("DROP TABLE " + trajectoriesTableName);

		success = true;
		hide();
	    } catch (SQLException ex1) {
		showDialog("The data set could not be created", ex1.getMessage()
			+ "\nTry using a name containing only letters, numbers, and underscores that does not start with a number..");
	    }
	}
    }

    private void switchToDataSetSelection() {
	try {
	    selectionView = new DataSetSelectionView(connection);
	    root.getChildren().set(0, selectionView);
	    connectionView = null;

	    nextButton.setOnAction((ActionEvent t) -> {
		selectDataSet();
	    });
	} catch (Throwable ex) {
	    showDialog(ex);
	    switchToConnection();
	}
    }

    private void switchToConnection() {
	connectionView = new DatabaseConnectionView();
	root.getChildren().set(0, connectionView);
	selectionView = null;

	nextButton.setOnAction((ActionEvent t) -> {
	    openConnection();
	});
    }

    private void showDialog(Throwable ex) {
	ex.printStackTrace();
	showDialog(ex.getClass().getSimpleName(), ExceptionUtils
		.getFullStackTrace(ex));
    }

    private void showDialog(String title, String body) {
	MonologFX dialog = new MonologFX(MonologFX.Type.ERROR);
	dialog.setTitle(title);
	dialog.setMessage(body);
	dialog.showDialog();
    }

    public Connection getConnection() {
	return connection;
    }

    public boolean succeeded() {
	return success;
    }

    public String getPointsTableName() {
	return pointsTableName;
    }

    public String getTrajectoriesTableName() {
	return trajectoriesTableName;
    }

}
