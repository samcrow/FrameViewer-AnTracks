package org.samcrow.frameviewer.trajectory.ui;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.samcrow.frameviewer.trajectory.Point;

/**
 *
 * @author samcrow
 */
public class PointEditDialog extends Stage {
    
    public PointEditDialog(Point point, Window owner) {
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UTILITY);
        
    }
    
    private void setUpLayout() {
        
    }
}
