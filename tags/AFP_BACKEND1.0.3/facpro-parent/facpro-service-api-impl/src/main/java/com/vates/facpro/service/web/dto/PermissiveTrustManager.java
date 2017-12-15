package com.vates.facpro.service.web.dto;


import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
/**
 * Trust Manager that accepts SSL server certificates from unknown authorities.
 *
 * @author Eric Biderbost
 */
public class PermissiveTrustManager implements X509TrustManager {
	
	private static final String IBM_PROVIDER = "IbmX509";
	private static final String SUN_PROVIDER="SunX509";
	private static String provider = SUN_PROVIDER;
	
	public static void selectProvider() {
		try {
			@SuppressWarnings("unused")
			PermissiveTrustManager test = new PermissiveTrustManager();
		} catch (NoSuchAlgorithmException e) {
			PermissiveTrustManager.provider = IBM_PROVIDER;
		}
	}
	
    X509TrustManager baseTrustManager;
    PermissiveTrustManager ()
        throws java.security.NoSuchAlgorithmException {
        TrustManagerFactory trustManagerFactory;
			trustManagerFactory = TrustManagerFactory.getInstance(provider);
        java.security.KeyStore keyStore = null;
        try {
                trustManagerFactory.init(keyStore);
                baseTrustManager = (X509TrustManager)trustManagerFactory.getTrustManagers()[0];
        } catch (java.security.KeyStoreException e) {
                        e.printStackTrace();
        }
    }
    public X509Certificate[] getAcceptedIssuers()
    {
        return baseTrustManager.getAcceptedIssuers();
    }
    public void checkClientTrusted(X509Certificate chain[], String authType)
            throws CertificateException
    {
        baseTrustManager.checkClientTrusted(chain, authType);
    }
    @SuppressWarnings("unused")
	private X509Certificate getCACert(X509Certificate chain[])
    {
        X509Certificate ca = chain[chain.length - 1];
        // check that root certificate is self-signed.
        if (ca != null && ca.getSubjectDN().equals(ca.getIssuerDN()))
            return ca;
        else
            return null;
    }
    /**
     * <p>Returns whether a particular certificate is one of the
     * accepted issuers</p>
     * @param caCert
     * @return
     */
    @SuppressWarnings("unused")
	private boolean rootCertIsKnown(X509Certificate caCert)
    {
        X509Certificate certificates[] = getAcceptedIssuers();
        if (certificates == null)
            return false;
        for (int i = 0; i < certificates.length; i++)
            if (caCert.equals(certificates[i]))
                return true;
        return false;
    }
    /**
     * Accept all valid certificates, regardless of the CA.
     *
     * @param chain a list of certificates forming an certificate chain - these are the certs being checked.
     * @param authType the type of authentication being used; usually 'RSA' I believe\u2026
     * @throws java.security.cert.CertificateException if the certificate is not valid.
     */
    public void checkServerTrusted(X509Certificate chain[], String authType)
            throws CertificateException
    {
    }
}
