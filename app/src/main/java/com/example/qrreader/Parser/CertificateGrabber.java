package com.example.qrreader.Parser;

import android.os.AsyncTask;

import com.example.qrreader.Models.CertRes;

import org.apache.commons.io.IOUtils;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * class for ssl certificates validation
 */

public class CertificateGrabber extends AsyncTask<String, Integer, CertRes> {

    @Override
    protected CertRes doInBackground(String... parameter) {
        CertRes certificates = new CertRes(parameter[0]);
        Certificate[] certs = new Certificate[0];
        URL destinationURL = null;

        try {
            destinationURL = new URL(parameter[0]);
            HttpsURLConnection conn = null;
            conn = (HttpsURLConnection) destinationURL.openConnection();
            conn.connect();
            certs = conn.getServerCertificates();
        } catch (SSLPeerUnverifiedException e) {
            e.printStackTrace();
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            certificates.setCert("There is no endpoint for this site");
            certificates.setStatus(false);
            return certificates;
        }

        String regex1 = "Full\\s+Name:\\n.+\\n";
        String regex2 = "Authority\\s+Information\\s+Access:\\s+\\n.+\\n+..+\\n";
        Pattern pattern = Pattern.compile(regex1);


            for (Certificate cert : certs) {
                if(cert instanceof X509Certificate){
                    try {
                        ( (X509Certificate) cert).checkValidity();

                        Matcher matcher = pattern.matcher(cert.toString());
                       if(!matcher.find())
                       {
                           pattern = Pattern.compile(regex2);
                           matcher = pattern.matcher(cert.toString());
                           matcher.find();

                       }
                       certificates.setCert(cert.toString().substring(matcher.start(), matcher.end()));
                       certificates.setStatus(true);

                    } catch(CertificateExpiredException cee) {
                        Matcher matcher = pattern.matcher(cert.toString());
                        matcher.find();
                        certificates.setCert(cert.toString().substring(matcher.start(), matcher.end()));
                        certificates.setStatus(false);
                    }
                     catch (CertificateNotYetValidException e) {
                        e.printStackTrace();
                    }

                } else {
                    Matcher matcher = pattern.matcher(cert.toString());
                    matcher.find();
                    certificates.setCert(cert.toString().substring(matcher.start(), matcher.end()));
                    certificates.setStatus(false);
                }

            }
    return certificates;
    }

    @Override
    protected void onPostExecute(CertRes certRes) {
        super.onPostExecute(certRes);
    }
}
