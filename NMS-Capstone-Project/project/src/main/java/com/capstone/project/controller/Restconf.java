package com.capstone.project.controller;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class Restconf {
    public static void main(String[] args) throws Exception {
        // Replace with your device IP, username, and password
        String deviceIp = "172.20.0.94";
        String username = "admin";
        String password = "cisco123";

        // Create a custom SSL context that trusts all certificates
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, TrustAllManager.get(), null);

        // Create an SSL socket factory with our custom SSL context
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                NoopHostnameVerifier.INSTANCE);

        // Create a HttpClient with the custom SSL socket factory
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .setDefaultCredentialsProvider(new CustomCredentialsProvider(username, password))
                .build();

        // Create the HTTP GET request
        HttpGet request = new HttpGet("https://" + deviceIp + "/restconf/data/ietf-interfaces:interfaces");
        request.addHeader("Accept", "application/yang-data+xml");
        request.addHeader("Content-Type", "application/yang-data+xml");

        // Execute the request and process the response
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response: " + responseBody);
            } else {
                System.err.println("Failed with HTTP error code: " + status);
            }
        } finally {
            // Close the HTTP client
            httpClient.close();
        }
    }

    // TrustAllManager to trust all certificates
    static class TrustAllManager {
        public static javax.net.ssl.TrustManager[] get() {
            return new javax.net.ssl.TrustManager[]{
                    new javax.net.ssl.X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };
        }
    }

    // Custom CredentialsProvider to handle basic authentication
    static class CustomCredentialsProvider extends BasicCredentialsProvider {
        public CustomCredentialsProvider(String username, String password) {
            setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        }
    }
}
