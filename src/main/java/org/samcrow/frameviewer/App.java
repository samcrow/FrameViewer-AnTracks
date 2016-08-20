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
package org.samcrow.frameviewer;

import org.samcrow.frameviewer.ui.CanvasPane;
import org.samcrow.frameviewer.ui.FrameCanvas;
import org.samcrow.frameviewer.ui.PlaybackControlPane;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import jfxtras.labs.dialogs.MonologFX;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.samcrow.frameviewer.io3.DatabaseTrajectoryDataStore;
import org.samcrow.frameviewer.track.Tracker;
import org.samcrow.frameviewer.ui.ImageAdjustWindow;
import org.samcrow.frameviewer.ui.db.DatabaseConnectionDialog;
import org.samcrow.tracking.TemplateTracker;

/**
 * Hello world!
 *
 */
public class App extends Application {

    private DatabaseTrajectoryDataStore trajectoryDataStore;

    private DataStoringPlaybackControlModel model;

    @Override
    public void start(final Stage stage) {
        
        try {
            // Check for command-line frame directory
            File frameDir;
            if (getParameters().getNamed().containsKey("frame-directory")) {
                frameDir = new File(getParameters().getNamed().get("frame-directory"));
                if (!frameDir.isDirectory()) {
                    throw new IllegalArgumentException("The provided image directory path must be a folder");
                }
            }
            else {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Choose a directory with images to process");
                frameDir = chooser.showDialog(stage);
            }
            // Exit if no directory selected
            if (frameDir == null) {
                stop();
            }

            VBox box = new VBox();

            MenuBar bar = createMenuBar();
            bar.setUseSystemMenuBar(true);
            box.getChildren().add(bar);

            // Ask the user for a connection
            DatabaseConnectionDialog dialog = new DatabaseConnectionDialog("mysql");
            dialog.showAndWait();
            if(!dialog.succeeded()) {
                stop();
            }
            
            trajectoryDataStore = new DatabaseTrajectoryDataStore(dialog.getDataSource(), dialog.getPointsTableName(), dialog.getTrajectoriesTableName());
            FrameSource finder = new FrameFinder(frameDir);
            model = new DataStoringPlaybackControlModel(finder, trajectoryDataStore);

	    Tracker tracker = new Tracker(finder, new TemplateTracker.Config(20, 20));
            FrameCanvas canvas = new FrameCanvas(tracker);
            canvas.imageProperty().bind(model.currentFrameImageProperty());
            canvas.setDataStore(trajectoryDataStore);
            model.bindMarkers(canvas);

            box.getChildren().add(new CanvasPane<>(canvas));

            final PlaybackControlPane controls = new PlaybackControlPane(model);
            box.getChildren().add(controls);
	    
	    // Create image options window
	    final ImageAdjustWindow imageAdjust = new ImageAdjustWindow();
	    imageAdjust.initOwner(stage);
	    canvas.brightnessProperty().bindBidirectional(imageAdjust.getView().brightnessProperty());
	    canvas.contrastProperty().bindBidirectional(imageAdjust.getView().contrastProperty());
	    canvas.hueProperty().bindBidirectional(imageAdjust.getView().hueProperty());
	    canvas.saturationProperty().bindBidirectional(imageAdjust.getView().saturationProperty());
	    
	    controls.setOnImageControlsRequested((ActionEvent event) -> {
		imageAdjust.show();
	    });

            // Hook up the trajectory display options
            canvas.displayModeProperty().bindBidirectional(controls.trajectoryDisplayModeProperty());
            canvas.trajectoryAlphaProperty().bindBidirectional(controls.trajectoryAlphaProperty());
	    canvas.activeTrajectoryAlphaProperty().bindBidirectional(controls.activeTrajectoryAlphaProperty());
            // Hook up trajectory tool select
            canvas.trajectoryToolProperty().bindBidirectional(controls.trajectoryToolProperty());
            // Hook up refresh action
            controls.setOnRefreshRequested((ActionEvent t) -> {
		try {
		    trajectoryDataStore.refresh();
		}
		catch (IOException ex) {
		    showDialog(ex);
		}
	    });

            //Assemble the root StackPane
            StackPane root = new StackPane();
            root.getChildren().add(box);

            stage.setTitle("Frame Viewer");
            Scene scene = new Scene(root);

            controls.setupAccelerators();

            stage.setScene(scene);
            stage.show();

        }
        catch (Exception ex) {
            showDialog(ex);
            stop();
        }
    }

    private void showDialog(Exception ex) {
        MonologFX dialog = new MonologFX(MonologFX.Type.ERROR);
        dialog.setTitle(ex.toString());
        dialog.setMessage(ExceptionUtils.getFullStackTrace(ex));
        dialog.showDialog();
        ex.printStackTrace();
    }

    private MenuBar createMenuBar() {
        MenuBar bar = new MenuBar();

        return bar;
    }

    @Override
    public void stop() {
        try {
            // Close the database connection
            if (trajectoryDataStore != null) {
                trajectoryDataStore.close();
            }
        }
        catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
