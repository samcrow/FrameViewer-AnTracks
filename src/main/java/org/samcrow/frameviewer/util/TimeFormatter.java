package org.samcrow.frameviewer.util;

import java.text.ParseException;

/**
 * Provides utilities for formatting time
 * @author Sam Crow
 */
public class TimeFormatter {
    
    private static final double FRAMES_PER_SECOND = 29.97;

    private static final int SECONDS_PER_MINUTE = 60;
    
    /**
     * Returns a formatted duration with hours, minutes, and seconds
     * @param seconds The number of seconds to format
     * @return 
     */
    public static String formatDuration(int seconds) {
        
        int minutes = 0;
        
        while(seconds >= SECONDS_PER_MINUTE) {
            minutes++;
            seconds -= SECONDS_PER_MINUTE;
        }
        
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    public static String formatDurationFromFrame(int frame) {
        return formatDuration((int) Math.floor(frame / FRAMES_PER_SECOND));
    }
    
    /**
     * Parses a time string that contains a minutes value and a seconds value,
     * separated by a colon character
     * @param time The duration string to parse
     * @return The number of seconds represented by the value
     * @throws ParseException If the duration string was not in a valid format 
     */
    public static int parseDuration(String time) throws ParseException {
        try {
        
        int firstColonIndex = time.indexOf(':');
        int min = Integer.valueOf(time.substring(0, firstColonIndex));
        int sec = Integer.valueOf(time.substring(firstColonIndex + 1));
        
        return (min * SECONDS_PER_MINUTE) + sec;
        
        
        }
        catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
            ParseException exception = new ParseException("Invalid duration string", 0);
            exception.initCause(ex);
            throw exception;
        }
    }
}
