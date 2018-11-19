package com.example.dsouchon.myapplication;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.ServiceConnectionSE;


import org.ksoap2.HeaderProperty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Connection for J2SE environments.
 */
public class ServiceConnectionSE2 implements ServiceConnection {

    private HttpURLConnection connection;

    /**
     * Constructor taking the url to the endpoint for this soap communication
     * @param url the url to open the connection to.
     * @throws IOException
     */

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }

    private TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
    };



    public ServiceConnectionSE2(Proxy proxy, String url) throws IOException {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.getMessage();
        }
        trustEveryone();
        connection = (HttpsURLConnection) new URL(url).openConnection();
        ((HttpsURLConnection) connection).setHostnameVerifier(new AllowAllHostnameVerifier());

        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
    }


    public ServiceConnectionSE2(String url) throws IOException {
        this(null, url, ServiceConnection.DEFAULT_TIMEOUT);
    }

    //public ServiceConnectionSE2(Proxy proxy, String url) throws IOException {
     //   this(proxy, url, ServiceConnection.DEFAULT_TIMEOUT);
   // }

    /**
     * Constructor taking the url to the endpoint for this soap communication
     * @param url the url to open the connection to.
     * @param timeout the connection and read timeout for the http connection in milliseconds
     * @throws IOException                            // 20 seconds
     */
    public ServiceConnectionSE2(String url, int timeout) throws IOException {
        this(null, url, timeout);
    }

    public ServiceConnectionSE2(Proxy proxy, String url, int timeout) throws IOException {
        trustEveryone();
        connection = (proxy == null)
                ? (HttpURLConnection) new URL(url).openConnection()
                : (HttpURLConnection) new URL(url).openConnection(proxy);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout); // even if we connect fine we want to time out if we cant read anything..
    }

    public void connect() throws IOException {
        connection.connect();
    }

    public void disconnect() {
        connection.disconnect();
    }

    public List getResponseProperties() throws IOException {
        List retList = new LinkedList();

        Map properties = connection.getHeaderFields();
        if(properties != null) {
            Set keys = properties.keySet();
            for (Iterator i = keys.iterator(); i.hasNext();) {
                String key = (String) i.next();
                List values = (List) properties.get(key);

                for (int j = 0; j < values.size(); j++) {
                    retList.add(new HeaderProperty(key, (String) values.get(j)));
                }
            }
        }

        return retList;
    }

    public int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }

    public void setRequestProperty(String string, String soapAction) {
        connection.setRequestProperty(string, soapAction);
    }

    public void setRequestMethod(String requestMethod) throws IOException {
        connection.setRequestMethod(requestMethod);
    }

    /**
     * If the length of a HTTP request body is known ahead, sets fixed length
     * to enable streaming without buffering. Sets after connection will cause an exception.
     *
     * @param contentLength the fixed length of the HTTP request body
     * @see http://developer.android.com/reference/java/net/HttpURLConnection.html
     **/
    public void setFixedLengthStreamingMode(int contentLength) {
        connection.setFixedLengthStreamingMode(contentLength);
    }

    public void setChunkedStreamingMode() {
        connection.setChunkedStreamingMode(0);
    }

    public OutputStream openOutputStream() throws IOException {
        return connection.getOutputStream();
    }

    public InputStream openInputStream() throws IOException {
        return connection.getInputStream();
    }

    public InputStream getErrorStream() {
        return connection.getErrorStream();
    }

    public String getHost() {
        return connection.getURL().getHost();
    }

    public int getPort() {
        return connection.getURL().getPort();
    }

    public String getPath() {
        return connection.getURL().getPath();
    }
}

