package com.mangospice.gems.security;

/**
 * @author Oliver Henlich
 */

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class can be invoked from command line and extract certificates provided by remote party at an SSL/TLS port.
 * Certificates are stored both as .cer files and imported into a .jks Java keystore.
 */
public class ExtractCertificates {

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.out.println("Usage: java com.mangospice.gems.security.ExtractCertificates host port");
            System.exit(1);
        }

        String host = args[0];
        Integer port = Integer.valueOf(Integer.parseInt(args[1]));

        final List certificates = new ArrayList();

        X509TrustManager trust = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                System.out.println(s);
            }

            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                for (int i = 0; i < x509Certificates.length; i++) {
                    X509Certificate cert = x509Certificates[i];
                    System.out.println("Loading certificate " + cert.getSubjectDN() + " issued by: " + cert.getIssuerDN());
                    certificates.add(x509Certificates[i]);
                }
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trust}, null);
        SSLSocket socket = (SSLSocket) sslContext.getSocketFactory().createSocket(host, port.intValue());

        socket.getInputStream();
        socket.getSession().getPeerCertificates();
        socket.close();

        for (Object certificate : certificates) {
            X509Certificate cert = (X509Certificate) certificate;

            System.out.println("fingerprint " + getFingerPrint(cert));

            String outputFile = cert.getSubjectDN().getName().replaceAll("[^a-zA-Z0-9-=_\\.]", "_") + ".cer";
            System.out.println("Serializing certificate to: " + outputFile);


            try (FileOutputStream os = new FileOutputStream(outputFile)) {
                os.write(cert.getEncoded());
            }
        }
    }


    public static String getFingerPrint(X509Certificate cert) throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] der = cert.getEncoded();
        md.update(der);

        StringBuilder sb = new StringBuilder();

        byte[] digest = md.digest();

        for (int i = 0; i < digest.length; i++) {
            sb.append(String.format("%02X", digest[i]));
            if (i + 1 < digest.length) {
                sb.append(":");
            }

        }

        return sb.toString();

    }


}