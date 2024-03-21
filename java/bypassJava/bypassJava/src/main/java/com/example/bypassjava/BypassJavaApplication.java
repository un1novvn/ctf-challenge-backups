package com.example.bypassjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


@SpringBootApplication
@ServletComponentScan
public class BypassJavaApplication {

    public static void initSelf() throws IOException {
        URL url = new URL("http://127.0.0.1:8080");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        connection.connect();
        connection.connect();
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(BypassJavaApplication.class, args);
        initSelf();
    }

}
