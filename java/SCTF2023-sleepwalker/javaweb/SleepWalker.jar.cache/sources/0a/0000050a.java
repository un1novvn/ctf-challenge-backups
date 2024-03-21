package com.ctf.sleepwalker.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/* loaded from: SleepWalker.jar:BOOT-INF/classes/com/ctf/sleepwalker/Utils/Request.class */
public class Request {
    public static String sendGetRequest(String url) throws IOException {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    response.append(line);
                } else {
                    reader.close();
                    return response.toString();
                }
            }
        } else {
            throw new IOException("GET request failed with response code: " + responseCode);
        }
    }
}