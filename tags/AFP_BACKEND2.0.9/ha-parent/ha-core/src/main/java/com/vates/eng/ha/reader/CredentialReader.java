package com.vates.eng.ha.reader;

import com.vates.eng.ha.domain.Credential;

/**
 * @author Gaston Napoli
 *
 */
public abstract class CredentialReader extends MessageTokenReader<Credential> {

    /**
     * 
     */
    public CredentialReader(){
        
        super(MessageTokenDescriptor.CREDENTIAL);
        
    }

}
