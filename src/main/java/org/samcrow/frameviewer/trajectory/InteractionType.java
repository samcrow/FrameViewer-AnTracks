package org.samcrow.frameviewer.trajectory;

/**
 * Types of interactions, and the ants that participated in them
 */
public enum InteractionType {

    /**
     * Focal ant started interaction, met ant did not participate
     */
    Performed,
    /**
     * Met ant started interaction, focal ant did not participate
     */
    Received,
    /**
     * Both ants participated in interaction
     */
    TwoWay("2-Way"), Unknown;

    private String shortName = null;

    private InteractionType() {
    }

    private InteractionType(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        if (shortName == null) {
            return super.toString();
        }
        else {
            return shortName;
        }
    }
    
    /**
     * Returns the inverse of this interaction type.
     * If one ant performed interaction type A, the other ant
     * performed interaction type A.invert().
     * @return 
     */
    public InteractionType invert() {
        switch(this) {
            case Performed:
                return Received;
            case Received:
                return Performed;
            case TwoWay:
                return TwoWay;
            case Unknown:
                return Unknown;
            
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the type corresponding to a name, but never
     * throws an exception. Returns Unknown if a valid value
     * could not be found.
     * @param name
     * @return 
     */
    public static InteractionType safeValueOf(String name) {
        try {
            return valueOf(name);
        }
        catch(Exception ex) {
            return Unknown;
        }
    }
}
