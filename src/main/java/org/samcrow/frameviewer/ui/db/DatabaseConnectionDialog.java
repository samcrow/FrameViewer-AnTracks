package org.samcrow.frameviewer.ui.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
            cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    hide();
                }
            });

            final Region spacer = new Region();
            buttonBox.getChildren().add(spacer);
            HBox.setHgrow(spacer, Priority.ALWAYS);

            nextButton.setDefaultButton(true);
            buttonBox.getChildren().add(nextButton);

            root.getChildren().add(buttonBox);
        }

        switchToConnection();

        final Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
        setScene(scene);
    }

    private final VBox root = new VBox();

    private void openConnection() {
        try {
            if (connectionView.getAddress().isEmpty()) {
                showDialog("Server address required", "Please specify an address");
                return;
            }
            if (connectionView.getUsername().isEmpty()) {
                showDialog("Username required", "Please specify a username");
                return;
            }

            DriverManager.setLoginTimeout(5);
            connection = DriverManager
                    .getConnection("jdbc:" + connectionTypeName + "://" 
                            + connectionView.getAddress() + "/" 
                            + connectionView.getDatabase(), 
                            connectionView.getUsername(), 
                            connectionView.getPassword());

            switchToDatabaseSelection();

        }
        catch (SQLException ex) {
            MonologFX dialog = new MonologFX(MonologFX.Type.ERROR);
            dialog.setTitle("Could not connect to database");
            dialog.setMessage(ex.getMessage() + "\nPlease ensure that the address, username, password, and database are correct.");
            dialog.showDialog();
            ex.printStackTrace();
        }
    }

    private void selectDatabase() {
        final String dbName = selectionView.getSelectedDataSet();
        if (dbName.isEmpty()) {
            showDialog("Database required", "Please enter a database name");
            return;
        }
        try {
            connection.setCatalog(dbName);
            success = true;
            hide();
        }
        catch (SQLException ex) {
            // Database does not exist. Try to create it
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE DATABASE " + dbName);
                connection.setCatalog(dbName);
                success = true;
                hide();
            }
            catch (SQLException ex1) {
                showDialog("Database could not be created", ex1.getMessage() + "\nTry using a name containing only letters, numbers, and underscores that does not start with a number..");
            }
        }
    }

    private void switchToDatabaseSelection() {
        try {
            selectionView = new DataSetSelectionView(connection);
            root.getChildren().set(0, selectionView);
            connectionView = null;

            nextButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    selectDatabase();
                }
            });
        }
        catch (SQLException ex) {
            showDialog(ex);
            switchToConnection();
        }
    }

    private void switchToConnection() {
        connectionView = new DatabaseConnectionView();
        root.getChildren().set(0, connectionView);
        selectionView = null;

        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                openConnection();
            }
        });
    }

    private void showDialog(Exception ex) {
        ex.printStackTrace();
        showDialog(ex.getClass().getSimpleName(), ExceptionUtils.getFullStackTrace(ex));
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

}
