package com.example.istv_andorid.util;

import org.json.JSONException;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import info.guardianproject.netcipher.NetCipher;


public class HttpUtil {

    static final String BACKEND_URL = "http://127.0.0.1:8080/";
    static final String SECURITY_HEADER = "Authorization";
    static final String SECURITY_PREFIX = "Bearer ";

    public static String doPost(String path, String body, String token) throws Exception {
        URL url = new URL(BACKEND_URL + path);
        HttpURLConnection urlConnection = NetCipher.getHttpURLConnection(url);
        try {
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            if (token != null) {
                urlConnection.setRequestProperty(SECURITY_HEADER, SECURITY_PREFIX + token);
            }

            urlConnection.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));

            if (urlConnection.getResponseCode() == 401) {
                throw new JSONException("");
            } else if (urlConnection.getResponseCode() == 400) {
                throw new IOException("");
            } else if (urlConnection.getResponseCode() != 200) {
                throw new Exception("");
            }

            InputStream in = urlConnection.getInputStream();

            StringBuilder sb = new StringBuilder();
            try (Reader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                int c = 0;
                while ((c = reader.read()) != -1) {
                    sb.append((char) c);
                }
            }

            return sb.toString();
        } finally {
            urlConnection.disconnect();
        }
    }

    public static InputStream doGet(String path, String token) throws Exception {
        URL url = new URL(BACKEND_URL + path);
        HttpURLConnection urlConnection = NetCipher.getHttpURLConnection(url);

        urlConnection.setDoOutput(false);
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "application/json");

        if (token != null) {
            urlConnection.setRequestProperty(SECURITY_HEADER, SECURITY_PREFIX + token);
        }

        if (urlConnection.getResponseCode() != 200) {
            throw new Exception("");
        }

        return urlConnection.getInputStream();
    }

    public static boolean doDelete(String path, String token) {
        try {
            URL url = new URL(BACKEND_URL + path);
            HttpURLConnection urlConnection = NetCipher.getHttpURLConnection(url);
            try {
                urlConnection.setDoOutput(false);
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setRequestProperty("Accept", "text/plain");

                if (token != null) {
                    urlConnection.setRequestProperty(SECURITY_HEADER, SECURITY_PREFIX + token);
                }

                if (urlConnection.getResponseCode() == 200) {
                    return true;
                }
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static String doPut(String path, String body, String token) throws Exception {
        URL url = new URL(BACKEND_URL + path);
        HttpURLConnection urlConnection = NetCipher.getHttpURLConnection(url);
        try {
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            if (token != null) {
                urlConnection.setRequestProperty(SECURITY_HEADER, SECURITY_PREFIX + token);
            }

            urlConnection.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));

            if (urlConnection.getResponseCode() == 400) {
                throw new IOException("");
            } else if (urlConnection.getResponseCode() != 200) {
                throw new Exception("");
            }

            InputStream in = urlConnection.getInputStream();

            StringBuilder sb = new StringBuilder();
            try (Reader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                int c = 0;
                while ((c = reader.read()) != -1) {
                    sb.append((char) c);
                }
            }

            return sb.toString();
        } finally {
            urlConnection.disconnect();
        }
    }

}
