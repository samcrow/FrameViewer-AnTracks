package org.samcrow.frameviewer.ui.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author samcrow
 */
public class DatabaseSelectionView extends VBox {
 
    private final ComboBox<String> databaseBox = new ComboBox<>();
    
    private final Connection connection;
    
    public DatabaseSelectionView(Connection connection) throws SQLException {
        this.connection = connection;
        
        databaseBox.setEditable(true);
        
        getChildren().add(new Label("Select a database to open"));
        getChildren().add(databaseBox);
        for(Node node : getChildrenUnmodifiable()) {
            VBox.setMargin(node, new Insets(10));
        }
        
        // Find existing databases
        final DatabaseMetaData data = connection.getMetaData();
        try (ResultSet tableResults = data.getCatalogs()) {
            while(tableResults.next()) {
                databaseBox.getItems().add(tableResults.getString("TABLE_CAT"));
            }
        }
    } 
    
    public final String getSelectedDatabase() {
        return databaseBox.getValue();
    }
    
}
