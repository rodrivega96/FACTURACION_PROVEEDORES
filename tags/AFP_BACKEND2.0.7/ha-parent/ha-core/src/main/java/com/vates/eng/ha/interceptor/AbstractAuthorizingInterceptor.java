package com.vates.eng.ha.interceptor;

import javax.inject.Inject;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import com.vates.eng.ha.domain.Credential;
import com.vates.eng.ha.domain.Operation;
import com.vates.eng.ha.domain.Resource;
import com.vates.eng.ha.security.UriSecurityMatcher;
import com.vates.eng.ha.util.MessageResourceBundle;
import com.vates.eng.ha.util.impl.Utils;

/**
 * It defines an abstract base for CXF authorization interceptors. See {@link org.apache.cxf.phase.AbstractPhaseInterceptor} for more information.
 */
public abstract class AbstractAuthorizingInterceptor extends AbstractPhaseInterceptor<Message> {

    @Inject
    private UriSecurityMatcher uriMatcher;

    @Inject
    protected MessageResourceBundle messageResource;

    /**
     * Instantiates a new abstract authorizing interceptor.
     */
    public AbstractAuthorizingInterceptor() {

        super(Phase.INVOKE);

    }

    /**
     * Gets the credential.
     * 
     * @param message
     *            the message
     * @return the credential
     */
    protected Credential getCredential(Message message) {

        return null;

    }

    /**
     * Gets the operation.
     * 
     * @param message
     *            the message
     * @return the operation
     */
    protected Operation getOperation(Message message) {

        return null;

    }

    /**
     * Gets the resource.
     * 
     * @param message
     *            the message
     * @return the resource
     */
    protected Resource getResource(Message message) {

        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message .Message)
     */
    @Override
    public final void handleMessage(Message message) throws Fault {

        // Checks if the URI is public. In that case, it skips security process.
        if (!isPublicUri(message)) {

            // message preprocessing
            preprocess(message);

            // Message parsing
            Resource resource = getResource(message);

            Credential credential = getCredential(message);

            Operation operation = getOperation(message);

            // Checks if it is authorized
            if (!isAuthorized(credential, resource, operation)) {

                throw new AccessDeniedException(messageResource.getMessage("error.unauthorized.access"));

            }

            // message postprocessing
            postprocess(message);

        }

    }

    /**
     * Checks if an operation on a resource for a provided credential is authorized.
     * 
     * @param credential
     *            the credential
     * @param resource
     *            the resource
     * @param operation
     *            the operation
     * @return true, if is authorized
     */
    protected abstract boolean isAuthorized(Credential credential, Resource resource, Operation operation);

    /**
     * Checks if URL in a request is public (authentication not needed).
     * 
     * @param message
     *            the CXF message from which URL will be extracted in order to be checked.
     * @return <tt>true</tt> if URI is public, <tt>false</tt> otherwise.
     */
    protected final boolean isPublicUri(Message message) {

        return uriMatcher.isPublic(Utils.getUriFromMessage(message));

    }

    /**
     * Postprocess. <TBC>
     * 
     * @param message
     *            the CXF message to be prosprocessed.
     */
    protected void postprocess(Message message) {

        return;

    }

    /**
     * Preprocess. <TBC>
     * 
     * @param message
     *            the CXF message to be preprocessed.
     */
    protected void preprocess(Message message) {

        return;

    }

}
