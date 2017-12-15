package com.vates.eng.ha.reader;

import org.apache.cxf.message.Message;

import com.vates.eng.ha.domain.Token;

/**
 * @author Gaston Napoli
 * 
 * @param <T>
 */
public abstract class MessageTokenReader<T extends Token> implements TokenReader<T, Message, MessageTokenDescriptor> {

    private MessageTokenDescriptor descriptor;

    protected MessageTokenReader(MessageTokenDescriptor descriptor) {

        this.descriptor = descriptor;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.reader.TokenReader#getDescriptor()
     */
    public MessageTokenDescriptor getDescriptor() {

        return descriptor;

    }

}
