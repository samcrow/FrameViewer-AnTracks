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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author samcrow
 */
public class DataSetSelectionView extends VBox {
 
    private final ComboBox<String> dataSetBox = new ComboBox<>();
    
    public DataSetSelectionView(Connection connection) throws SQLException {
        
        dataSetBox.setEditable(true);
        dataSetBox.setPrefWidth(200);
        
        getChildren().add(new Label("Select a database to open"));
        getChildren().add(dataSetBox);
        for(Node node : getChildrenUnmodifiable()) {
            VBox.setMargin(node, new Insets(10));
        }
        
        // Find existing pairs of tables named <name>_trajectories and <name>_points
        final List<String> tableNames = new ArrayList<>();
        final DatabaseMetaData data = connection.getMetaData();
        try (ResultSet tableResults = data.getTables(connection.getCatalog(), null, null, null)) {
            while(tableResults.next()) {
                tableNames.add(tableResults.getString("TABLE_NAME"));
            }
        }
        // Sort the list so that it is in order, and <name>_trajectories follows <name>_points for every <name>
        Collections.sort(tableNames);
        
        final Pattern pointsPattern = Pattern.compile("^([a-zA-Z0-9]\\w*)_points$");
        final Pattern trajectoriesPattern = Pattern.compile("^([a-zA-Z0-9]\\w*)_trajectories$");
        
        final Iterator<String> iter = tableNames.iterator();
        while(iter.hasNext()) {
            final String firstTableName = iter.next();
            final Matcher firstTableMatcher = pointsPattern.matcher(firstTableName);
            if(firstTableMatcher.matches()) {
                final String dataSetName = firstTableMatcher.group(1);
                
                if(iter.hasNext()) {
                    final String secondTableName = iter.next();
                    final Matcher secondTableMatcher = trajectoriesPattern.matcher(secondTableName);
                    if(secondTableMatcher.matches()) {
                        final String secondDataSetName = secondTableMatcher.group(1);
                        
                        if(!dataSetName.equals(secondDataSetName)) {
                            Logger.getLogger(DataSetSelectionView.class.getName()).log(Level.WARNING, "Found two tables {0} and {1} with non-matching dataset names. Ignoring both.", new Object[]{firstTableName, secondTableName});
                            continue;
                        }
                        
                        // Got two tables
                        dataSetBox.getItems().add(dataSetName);
                        
                    }
                    else {
                        Logger.getLogger(DataSetSelectionView.class.getName()).log(Level.WARNING, "Found unexpected table {0}", firstTableName);
                    }
                }
                else {
                    // No more tables!
                    break;
                }
            }
            else {
                Logger.getLogger(DataSetSelectionView.class.getName()).log(Level.WARNING, "Found unexpected table {0}", firstTableName);
            }
        }
        
    } 
    
    public final String getSelectedDataSet() {
        return dataSetBox.getValue();
    }
    
}
