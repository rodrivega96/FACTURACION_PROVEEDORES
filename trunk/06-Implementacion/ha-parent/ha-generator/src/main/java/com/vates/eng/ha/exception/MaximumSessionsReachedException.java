package com.vates.eng.ha.exception;

/**
 * The Class MaximumSessionsReachedException.
 */
public class MaximumSessionsReachedException extends IdGenerationException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6550299994639073122L;

    /**
     * Instantiates a new hA maximum sessions reached exception.
     *
     * @param message the message
     */
    public MaximumSessionsReachedException(String message) {
        super(message);
    }

}
