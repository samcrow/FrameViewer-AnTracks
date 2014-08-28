package org.samcrow.frameviewer.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.samcrow.frameviewer.PlaybackControlModel;
import org.samcrow.frameviewer.trajectory.TrajectoryDisplayMode;
import org.samcrow.frameviewer.trajectory.TrajectoryTool;

import static javafx.scene.layout.HBox.setMargin;

/**
 * Displays playback controls
 * <p>
 * @author Sam Crow
 */
public class PlaybackControlPane extends HBox {

    private final Button playBackwardsButton;

    private final Button jumpBackwardsButton;

    private final Button pauseButton;

    private final Button jumpForwardButton;

    private final Button playForwardButton;

    private final PlaybackControlModel model;

    
    private final ObjectProperty<TrajectoryTool> trajectoryTool = new SimpleObjectProperty<>();
    
    private final ObjectProperty<TrajectoryDisplayMode> trajectoryDisplayMode = new SimpleObjectProperty<>();

    public PlaybackControlPane(PlaybackControlModel model) {
        this.model = model;

        final Insets PADDING = new Insets(10);

        setPadding(PADDING);
        setAlignment(Pos.CENTER);
        
        // Left spacer
        {
            final Region spacer = new Region();
            getChildren().add(spacer);
            setHgrow(spacer, Priority.ALWAYS);
        }

        {
            playBackwardsButton = new Button("<");
            playBackwardsButton.setOnAction(model.getPlayBackwardsButtonHandler());
            playBackwardsButton.disableProperty().bind(model.playBackwardsEnabledProperty().not());

            getChildren().add(playBackwardsButton);
            setMargin(playBackwardsButton, PADDING);
        }

        {
            jumpBackwardsButton = new Button("|<");
            jumpBackwardsButton.setOnAction(model.getJumpBackwardsButtonHandler());
            jumpBackwardsButton.disableProperty().bind(model.jumpBackwardsEnabledProperty().not());

            getChildren().add(jumpBackwardsButton);
            setMargin(jumpBackwardsButton, PADDING);
        }

        {

            final Label timeLabel = new Label("Time: ");

            getChildren().add(timeLabel);
            setMargin(timeLabel, PADDING);

        }

        {
            final TimeField timeField = new TimeField();
            timeField.currentFrameProperty().bindBidirectional(model.currentFrameProperty());

            getChildren().add(timeField);
            setMargin(timeField, PADDING);
        }

        {

            final Label frameLabel = new Label("Frame: ");

            getChildren().add(frameLabel);
            setMargin(frameLabel, PADDING);

        }

        {
            final IntegerField frameField = new IntegerField(1);
            frameField.valueProperty().bindBidirectional(model.currentFrameProperty());

            getChildren().add(frameField);
            setMargin(frameField, PADDING);
        }

        {
            pauseButton = new Button("||");
            pauseButton.setOnAction(model.getPauseButtonHandler());
            pauseButton.disableProperty().bind(model.pauseEnabledProperty().not());

            getChildren().add(pauseButton);
            setMargin(pauseButton, PADDING);
        }

        {
            jumpForwardButton = new Button(">|");
            jumpForwardButton.setOnAction(model.getJumpForwardButtonHandler());
            jumpForwardButton.disableProperty().bind(model.jumpForwardEnabledProperty().not());

            getChildren().add(jumpForwardButton);
            setMargin(jumpForwardButton, PADDING);
        }

        {
            playForwardButton = new Button(">");
            playForwardButton.setOnAction(model.getPlayForwardButtonHandler());
            playForwardButton.disableProperty().bind(model.playForwardEnabledProperty().not());

            getChildren().add(playForwardButton);
            setMargin(playForwardButton, PADDING);
        }

        
        // Right spacer
        {
            final Region spacer = new Region();
            getChildren().add(spacer);
            setHgrow(spacer, Priority.ALWAYS);
        }

        // Mode select
        {
            
            final SplitButtonBar<TrajectoryTool> toolChoice = new SplitButtonBar<>(TrajectoryTool.values(), TrajectoryTool.Create);
            trajectoryTool.bindBidirectional(toolChoice.selectedItemProperty());

            getChildren().add(toolChoice);
            setMargin(toolChoice, PADDING);
        }
        
        // Display mode select
        {
            final SplitButtonBar<TrajectoryDisplayMode> displayChoice = new SplitButtonBar<>(TrajectoryDisplayMode.values(), TrajectoryDisplayMode.Full);
            trajectoryDisplayMode.bindBidirectional(displayChoice.selectedItemProperty());
            
            getChildren().add(displayChoice);
            setMargin(displayChoice, PADDING);
        }


    }


    public final TrajectoryTool getTrajectoryTool() {
        return trajectoryTool.get();
    }
    
    public final void setTrajectoryTool(TrajectoryTool tool) {
        trajectoryTool.set(tool);
    }
    
    public final ObjectProperty<TrajectoryTool> trajectoryToolProperty() {
        return trajectoryTool;
    }
    
    public final TrajectoryDisplayMode getTrajectoryDisplayMode() {
        return trajectoryDisplayMode.get();
    }
    
    public final void setTrajectoryDisplayMode(TrajectoryDisplayMode mode) {
        trajectoryDisplayMode.set(mode);
    }
    
    public final ObjectProperty<TrajectoryDisplayMode> trajectoryDisplayModeProperty() {
        return trajectoryDisplayMode;
    }
    
    /**
     * Sets up global keyboard shortcuts. Must be called after this pane
     * is attached to a scene.
     */
    public void setupAccelerators() {
        final Scene scene = playBackwardsButton.getScene();

        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.LEFT), new Runnable() {
            @Override
            public void run() {
                if (!jumpBackwardsButton.isDisabled()) {
                    jumpBackwardsButton.fire();
                }
            }
        });
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.RIGHT), new Runnable() {
            @Override
            public void run() {
                if (!jumpForwardButton.isDisabled()) {
                    jumpForwardButton.fire();
                }
            }
        });
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.SPACE), new Runnable() {
            @Override
            public void run() {
                PlaybackControlModel.State state = model.getState();
                if (state == PlaybackControlModel.State.Paused) {
                    model.setState(PlaybackControlModel.State.PlayingForward);
                }
                else {
                    model.setState(PlaybackControlModel.State.Paused);
                }
            }
        });
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.LEFT, KeyCodeCombination.SHORTCUT_DOWN), new Runnable() {
            @Override
            public void run() {
                if (!playBackwardsButton.isDisabled()) {
                    playBackwardsButton.fire();
                }
            }
        });
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.RIGHT, KeyCodeCombination.SHORTCUT_DOWN), new Runnable() {
            @Override
            public void run() {
                if (!playForwardButton.isDisabled()) {
                    playForwardButton.fire();
                }
            }
        });
    }

}
