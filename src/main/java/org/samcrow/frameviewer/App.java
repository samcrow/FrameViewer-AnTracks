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
            if(getParameters().getNamed().containsKey("frame-directory")) {
                frameDir = new File(getParameters().getNamed().get("frame-directory"));
                if(!frameDir.isDirectory()) {
                    throw new IllegalArgumentException("The provided image directory path must be a folder");
                }
            }
            else {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Choose a directory with images to process");
                frameDir = chooser.showDialog(stage);
            }
            // Exit if no directory selected
            if(frameDir == null) {
                stop();
            }

            VBox box = new VBox();

            MenuBar bar = createMenuBar();
            bar.setUseSystemMenuBar(true);
            box.getChildren().add(bar);

            trajectoryDataStore = DatabaseTrajectoryDataStore.readFrom("192.168.3.100", "FrameViewer", "FrameViewer", "FrameViewer");
            FrameFinder finder = new FrameFinder(frameDir);
            model = new DataStoringPlaybackControlModel(finder, trajectoryDataStore);

            FrameCanvas canvas = new FrameCanvas();
            canvas.imageProperty().bind(model.currentFrameImageProperty());
            canvas.setDataStore(trajectoryDataStore);
            model.bindMarkers(canvas);

            box.getChildren().add(new CanvasPane<>(canvas));

            final PlaybackControlPane controls = new PlaybackControlPane(model);
            box.getChildren().add(controls);
            
            // Hook up the trajectory display options
            canvas.displayModeProperty().bindBidirectional(controls.trajectoryDisplayModeProperty());
            canvas.trajectoryAlphaProperty().bindBidirectional(controls.trajectoryAlphaProperty());
            // Hook up trajectory tool select
            canvas.trajectoryToolProperty().bindBidirectional(controls.trajectoryToolProperty());

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
            MonologFX dialog = new MonologFX(MonologFX.Type.ERROR);
            dialog.setTitle(ex.toString());
            dialog.setMessage(ExceptionUtils.getFullStackTrace(ex));
            dialog.showDialog();
            ex.printStackTrace();
            stop();
        }
    }

    private MenuBar createMenuBar() {
        MenuBar bar = new MenuBar();

        final Menu fileMenu = new Menu("File");


        final MenuItem openItem = new MenuItem("Open...");
        openItem.setAccelerator(KeyCombination.keyCombination("Shortcut+O"));
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                // TODO
            }
        });

        fileMenu.getItems().add(openItem);

        bar.getMenus().add(fileMenu);

        final Menu editMenu = new Menu("Edit");
        final MenuItem undoItem = new MenuItem("Undo");
        undoItem.setAccelerator(KeyCombination.keyCombination("Shortcut+Z"));
        undoItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                model.undo();
            }
        });

        editMenu.getItems().add(undoItem);
        bar.getMenus().add(editMenu);

        return bar;
    }


    @Override
    public void stop() {
        try {
            // Close the database connection
            if(trajectoryDataStore != null) {
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
