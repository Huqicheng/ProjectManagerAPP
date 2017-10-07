package com.example.huqicheng.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class SslContextFactory {
    private static final String PROTOCOL = "TLS";
    private static final String CKMF_PATH = "app/resources/cChat.jks";
    private static final String SKMF_PATH = "app/resources/sChat.jks";
    private static final SSLContext SERVER_CONTEXT;
    private static final SSLContext CLIENT_CONTEXT;
    private static final TrustManagerFactory trustManagerFactory;

    static {

        SSLContext serverContext = null;
        SSLContext clientContext = null;
        InputStream in = null;
        InputStream trustIn = null;

        try {

            KeyStore ks = KeyStore.getInstance("JKS");
            in = new FileInputStream(new File(SKMF_PATH));
            trustIn = new FileInputStream(new File(CKMF_PATH));
            ks.load(in, "sNetty".toCharArray());

            // Set up key manager factory to use our key store
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, "sNetty".toCharArray());

            // truststore
            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(trustIn, "cNetty".toCharArray());

            // set up trust manager factory to use our trust store
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(ts);

            // Initialize the SSLContext to work with our key managers.
            serverContext = SSLContext.getInstance(PROTOCOL);
            serverContext.init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            clientContext = SSLContext.getInstance(PROTOCOL);
            clientContext.init(null, trustManagerFactory.getTrustManagers(), null);
        } catch (Exception e) {
            throw new Error("Failed to initialize the SSLContext", e);
        }finally {
        	if (in !=null) {
        		try {
					in.close();
				} catch (IOException e) {
				}
        	}
        	if (trustIn != null) {
        		try {
					trustIn.close();
				} catch (IOException e) {

				}
        	}
        }

        SERVER_CONTEXT = serverContext;
        CLIENT_CONTEXT = clientContext;
    }


    public static SSLContext getServerContext() {
        return SERVER_CONTEXT;
    }

    public static SSLContext getClientContext() {
        return CLIENT_CONTEXT;
    }

    public static TrustManagerFactory getTrustManagerFactory() {
        return trustManagerFactory;
    }

    private SslContextFactory() {
        // Unused
    }
}