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
// </editor-fold>package org.samcrow.frameviewer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

/**
 * Keeps track of the changes of an observable and if it has been saved
 * @author samcrow
 */
public class SaveStatusController {
    
    /**
     * If the value has any unsaved data
     */
    private final BooleanProperty unsavedData = new SimpleBooleanProperty();

    /**
     * Constructor
     * @param value The value to keep track of
     */
    public SaveStatusController(ObservableValue<?> value) {
        
        value.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                unsavedData.set(true);
            }
        });
    }
    
    
    public final ReadOnlyBooleanProperty unsavedDataProperty() {
        return unsavedData;
    }
    
    public final boolean hasUnsavedData() {
        return unsavedData.get();
    }
    
    /**
     * Marks the value as saved
     */
    public final void markSaved() {
        unsavedData.set(false);
    }
}
