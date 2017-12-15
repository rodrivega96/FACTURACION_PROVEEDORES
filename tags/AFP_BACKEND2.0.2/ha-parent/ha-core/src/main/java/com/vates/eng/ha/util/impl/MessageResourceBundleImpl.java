package com.vates.eng.ha.util.impl;

import org.springframework.context.support.MessageSourceAccessor;

import com.vates.eng.ha.util.MessageResourceBundle;

/**
 * 
 * This implementation of {@link com.vates.eng.ha.util.MessageResourceBundle} helps with Spring messages bundles. It is basically a wrapper of
 * {@link org.springframework.context.support.MessageSourceAccessor}.
 * 
 * @author Gaston Napoli
 * 
 */
public class MessageResourceBundleImpl implements MessageResourceBundle {

    private MessageSourceAccessor messageSourceAccessor;

    /**
     * Default constructor.
     */
    public MessageResourceBundleImpl() {
    }

    /**
     * Returns an initialized instance with a given message source accessor.
     * 
     * @param messageSourceAccessor
     */
    public MessageResourceBundleImpl(MessageSourceAccessor messageSourceAccessor) {

        this.messageSourceAccessor = messageSourceAccessor;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.util.MessageResourceBundle#getMessage(java.lang.String)
     */
    @Override
    public String getMessage(String code) {

        return this.getMessage(code, (String) null);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.util.MessageResourceBundle#getMessage(java.lang.String, java.lang.Long)
     */
    @Override
    public String getMessage(String code, Long arg) {

        return getMessage(code, (arg != null ? arg.toString() : null));

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.util.MessageResourceBundle#getMessage(java.lang.String, java.lang.Object)
     */
    @Override
    public String getMessage(String code, Object... args) {

        Object[] messageArgs = args;

        return messageSourceAccessor.getMessage(code, messageArgs);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.util.MessageResourceBundle#getMessage(java.lang.String, java.lang.String)
     */
    @Override
    public String getMessage(String code, String... args) {

        return getMessage(code, (Object[]) args);

    }

    /**
     * It sets the message resource accessor.
     * 
     * @param messageSourceAccessor
     *            the messageSourceAccessor to set.
     */
    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {

        this.messageSourceAccessor = messageSourceAccessor;

    }

}
