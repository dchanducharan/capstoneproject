package com.capstone.project.controller;

import org.apache.http.HttpHeaders;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLContext;

@RestController
public class RestconfController {

    @GetMapping("/getconfig")
    public String getConfig() throws Exception {
        // cofiguration settings for device IP, username, and password
        String deviceIp = "172.20.0.94";
        String username = "admin";
        String password = "cisco123";

        // Load SSL context with custom truststore 
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(/* your truststore, if any */ null, (chain, authType) -> true)
                .build();

        // Create an SSL socket factory with hostname verification enabled
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1.2", "TLSv1.3"}, // Supported TLS versions
                null,
                NoopHostnameVerifier.INSTANCE); // Allow all hostnames

        // Set up credentials provider for basic authentication
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(deviceIp, 443), //  HTTPS port
                new UsernamePasswordCredentials(username, password)
        );

        // Create an HTTP client with the credentials provider and SSL socket factory
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .setSSLSocketFactory(sslSocketFactory)
                .build();

        // Create the HTTP GET request
        String endpointUrl = "https://" + deviceIp + "/restconf/data/ietf-interfaces:interfaces";
        HttpGet request = new HttpGet(endpointUrl);
        request.setHeader(HttpHeaders.ACCEPT, "application/yang-data+json");
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/yang-data+json");
        String responseBody="";
        // Execute the request and process the response
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response: " + responseBody);
            } else {
                System.err.println("Failed with HTTP error code: " + status);
            }
        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
        } finally {
            // Close the HTTP client
            httpClient.close();
        }
        return responseBody;
    }
}
