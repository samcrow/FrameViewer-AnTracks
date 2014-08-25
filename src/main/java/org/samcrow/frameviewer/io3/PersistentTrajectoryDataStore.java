package org.samcrow.frameviewer.io3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.samcrow.frameviewer.MultiFrameDataStore;
import org.samcrow.frameviewer.trajectory.Point;
import org.samcrow.frameviewer.trajectory.Trajectory;

/**
 *
 * @author samcrow
 * @param <T> The type of trajectory to operate on
 */
public class PersistentTrajectoryDataStore<T extends Trajectory> extends MultiFrameDataStore<T> {
    
    public void writeTo(File file) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    public static PersistentTrajectoryDataStore<Trajectory> readFrom(File file) throws IOException, ParseException {
        PersistentTrajectoryDataStore<Trajectory> instance = new PersistentTrajectoryDataStore<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        
            // Ignore version line and header
            reader.readLine();
            reader.readLine();
            
            final Pattern linePattern = Pattern.compile("^(?<id>\\d+),(?<frame>\\d+),(?<x>\\d+),(?<y>\\d+),*$");
            
            Trajectory lastTrajectory = null;
            
            while(true) {
                final String line = reader.readLine();
                
                if(line == null) {
                    break;
                }
                
                final Matcher matcher = linePattern.matcher(line);
                if(matcher.find()) {
                    try {
                        final int trajectoryId = Integer.parseInt(matcher.group("id"));
                        final int frame = Integer.parseInt(matcher.group("frame"));
                        final int x = Integer.parseInt(matcher.group("x"));
                        final int y = Integer.parseInt(matcher.group("y"));
                        
                        if(lastTrajectory != null && lastTrajectory.getId() == trajectoryId) {
                            // Add to the current trajectory
                            lastTrajectory.set(frame, new Point(x, y));
                        }
                        else {
                            // New trajectory
                            // Add the old one to the list
                            if(lastTrajectory != null) {
                                instance.getList().add(lastTrajectory);
                            }
                            // Create a new trajectory
                            // Use a default last frame that makes some sense
                            lastTrajectory = new Trajectory(frame, frame + 1, trajectoryId);
                            lastTrajectory.set(frame, new Point(x, y));
                        }
                        
                    }
                    catch (IllegalArgumentException ex) {
                        ParseException parseEx = new ParseException("Unrecognized marker or in line \"" + line + "\"", 0);
                        parseEx.initCause(ex);
                        throw parseEx;
                    }
                }
                
            }
        
        }
        return instance;
    }
    
}
