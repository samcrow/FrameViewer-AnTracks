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

import jfxtras.labs.dialogs.MonologFX;
import jfxtras.labs.dialogs.MonologFXButton;

/**
 * A dialog that asks the user if he/she wants to save a document or not
 * @author Sam Crow
 */
public class SaveDialog extends MonologFX {
    
    public SaveDialog() {
        super(Type.QUESTION);
        
        setTitle("Save file");
        setMessage("A file may be unsaved. Would you like to save it?");
        setModal(true);
        
        MonologFXButton saveButton = new MonologFXButton();
        saveButton.setType(MonologFXButton.Type.YES);
        saveButton.setLabel("Save");
        saveButton.setDefaultButton(true);
        
        
        MonologFXButton dontSaveButton = new MonologFXButton();
        dontSaveButton.setType(MonologFXButton.Type.NO);
        dontSaveButton.setLabel("Don't Save");
        
        
        MonologFXButton cancelButton = new MonologFXButton();
        cancelButton.setType(MonologFXButton.Type.CANCEL);
        cancelButton.setLabel("Cancel");
        cancelButton.setCancelButton(true);
        
        addButton(cancelButton);
        addButton(saveButton);
        addButton(dontSaveButton);
    }
    
}
