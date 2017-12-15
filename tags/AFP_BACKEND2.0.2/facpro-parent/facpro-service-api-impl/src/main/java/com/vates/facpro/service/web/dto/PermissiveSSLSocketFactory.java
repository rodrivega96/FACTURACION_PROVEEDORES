package com.vates.facpro.service.web.dto;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
/**
 * SSL Socket Factory that uses a permissive trust manager accepting SSL server certificates from unknown authorities.
 *
 * @author Eric Biderbost
 */
public class PermissiveSSLSocketFactory extends SSLSocketFactory {
      private static SSLSocketFactory permissiveFactory = null;
       /**
         * Initializes a permissive ssl socket factory.
         */
    public void init() {
                try {
                        TrustManager[] trustManager = new TrustManager[] { new PermissiveTrustManager() };
                        SSLContext sc = SSLContext.getInstance("SSL");
                        
                        sc.init(null, trustManager, new java.security.SecureRandom());
                        permissiveFactory = sc.getSocketFactory();
                } catch (java.security.NoSuchAlgorithmException e) {
                        e.printStackTrace();
                } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                }
    }
   /**
         * @see javax.net.SocketFactory#getDefault()
         */
        public static SocketFactory getDefault() {
                PermissiveSSLSocketFactory factory = new PermissiveSSLSocketFactory();
                factory.init();
                return factory;
        }

    /**
         * @see javax.net.SocketFactory#createSocket(java.lang.String, int)
     */
    public Socket createSocket(String host, int port)
        throws IOException, UnknownHostException
    {
        return permissiveFactory.createSocket(host, port);
    }
    /**
         * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int)
     */
    public Socket createSocket(InetAddress host, int port)
       throws IOException, UnknownHostException
    {
        return permissiveFactory.createSocket(host, port);
    }

    /**
         * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int, java.net.InetAddress, int)
     */
    public Socket createSocket(InetAddress host, int port,
                             InetAddress client_host, int client_port)
       throws IOException, UnknownHostException
    {
        return permissiveFactory.createSocket(host, port, client_host, client_port);
    }

    /**
         * @see javax.net.SocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int)
     */
    public Socket createSocket(String host, int port,
                             InetAddress client_host, int client_port)
       throws IOException, UnknownHostException
    {
        return permissiveFactory.createSocket(host, port, client_host, client_port);
    }
    /**
         * @see javax.net.SocketFactory#createSocket(java.net.Socket, java.lang.String, int, boolean)
     */
    public Socket createSocket(Socket socket, String host, int port, boolean autoclose)
       throws IOException, UnknownHostException
    {
        return permissiveFactory.createSocket(socket, host, port, autoclose);
    }
    /**
     * Return supported cipher suites.
     */
    public String[] getSupportedCipherSuites()
    {
        return permissiveFactory.getSupportedCipherSuites();
    }
    /**
     * Return default cipher suites.
     */
    public String[] getDefaultCipherSuites()
    {
            return permissiveFactory.getDefaultCipherSuites();
    }
}
