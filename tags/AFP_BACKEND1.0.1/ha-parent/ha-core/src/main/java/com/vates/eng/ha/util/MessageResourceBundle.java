package com.vates.eng.ha.util;

/**
 * This interface defines a helper for easy access to messages in resource bundles.
 * 
 * @author Gaston Napoli
 * 
 */
public interface MessageResourceBundle {

    /**
     * It returns an formatted string based on message format given by a code. For this operation, default Locale is used. Given arguments are used in
     * the formatting operation. The applied format will implementation-dependant.
     * 
     * @param code
     *            message's code.
     * @param arguments
     *            argumentos para el mensaje
     * @return el mensaje correspondiente
     * @throws ar.com.osde.framework.exception.FrameworkRuntimeException
     *             if there is not a meesage associated with the given code.
     * 
     */
    public abstract String getMessage(String code, Object... arguments);

    /**
     * It returns an formatted string based on message format given by a code. For this operation, default Locale is used. Given arguments are used in
     * the formatting operation. The applied format will implementation-dependant.
     * 
     * @param code
     *            message's code
     * @param arguments
     *            argumentos para el mensaje
     * @return the formatted message.
     * @throws ar.com.osde.framework.exception.FrameworkRuntimeException
     *             if there is not a meesage associated with the given code.
     * 
     */
    public abstract String getMessage(String code, String... arguments);

    /**
     * It returns an formatted string based on message format given by a code. For this operation, default Locale is used. Given arguments are used in
     * the formatting operation. The applied format will implementation-dependant.
     * 
     * @param code
     *            message's code.
     * @param arg
     *            argumento para el mensaje
     * @return the formatted message.
     * @throws ar.com.osde.framework.exception.FrameworkRuntimeException
     *             if there is not a meesage associated with the given code.
     * 
     */
    public abstract String getMessage(String code, Long arg);

    /**
     * It returns an formatted string based on message format given by a code. For this operation, default Locale is used.
     * 
     * @param code
     *            message's code.
     * @return the formatted message.
     * @throws ar.com.osde.framework.exception.FrameworkRuntimeException
     *             if there is not a meesage associated with the given code.
     * 
     */
    public abstract String getMessage(String code);

}