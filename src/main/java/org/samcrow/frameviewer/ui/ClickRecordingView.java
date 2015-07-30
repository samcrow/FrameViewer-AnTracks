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
// </editor-fold>package org.samcrow.frameviewer.ui;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.samcrow.frameviewer.CurrentFrameProvider;

/**
 * Records clicks on the image
 * @author Sam Crow
 */
public class ClickRecordingView extends ImageView {
    
    private final CurrentFrameProvider provider;

    public ClickRecordingView(CurrentFrameProvider provider) {
        this.provider = provider;
    }

    public ClickRecordingView(CurrentFrameProvider provider, String string) {
        super(string);
        this.provider = provider;
    }

    public ClickRecordingView(CurrentFrameProvider provider, Image image) {
        super(image);
        this.provider = provider;
    }
    
    {
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Get click information
                int x = (int) Math.round(event.getX());
                int y = (int) Math.round(event.getY());
                int frame = provider.getCurrentFrame();
                
                System.out.println("Clicked during frame "+frame+" at ("+x+", "+y+")");
            }
        });
    }
    
    
    
}
