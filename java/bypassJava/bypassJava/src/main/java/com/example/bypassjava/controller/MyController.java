package com.example.bypassjava.controller;

import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.Map;

@RestController
public class MyController {
    @GetMapping("/")
    public String index() {
        return "hello CTFer!!";
    }
    @PostMapping("/read")
    public String serialize(@RequestBody String data) {

        try {
            byte[] payload = Base64.getDecoder().decode(data);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payload);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            objectInputStream.readObject();
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
}
