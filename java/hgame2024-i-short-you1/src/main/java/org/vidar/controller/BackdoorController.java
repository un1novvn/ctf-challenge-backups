package org.vidar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

@Controller
public class BackdoorController {

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping({"/backdoor"})
    @ResponseBody
    public String hack(@RequestParam String payload) throws IOException, SAXException, ClassNotFoundException {
        if (payload.length() > 220) {
            return "hacker!!!";
        }
        byte[] bytes = Base64.getDecoder().decode(payload);
        new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        return "success";
    }
}
