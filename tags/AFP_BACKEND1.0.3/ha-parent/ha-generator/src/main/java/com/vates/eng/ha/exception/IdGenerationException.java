package com.vates.eng.ha.exception;

/**
 * The Class IdGenerationException.
 */
public class IdGenerationException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 9137118686174557138L;

    /**
     * Instantiates a new id generation exception.
     * 
     * @param message
     *            the message
     */
    public IdGenerationException(String message) {
        super(message);
    }

    /**
     * Instantiates a new id generation exception.
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public IdGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

}
