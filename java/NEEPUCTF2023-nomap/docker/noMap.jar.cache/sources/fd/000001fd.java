package com.example.demo.controller;

import com.example.demo.util.MyObjectInputStream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
/* loaded from: noMap.jar:BOOT-INF/classes/com/example/demo/controller/MainController.class */
public class MainController {
    @RequestMapping({"/"})
    @ResponseBody
    public String status() {
        return "Welcome to NEEPUCTF 2023 :D";
    }

    @RequestMapping({"/nomap"})
    @ResponseBody
    public String noMap(HttpServletRequest request) {
        try {
            MyObjectInputStream ois = new MyObjectInputStream(request.getInputStream());
            String name = ois.readUTF();
            int year = ois.readInt();
            if (name.equals("NEEPU") && year == 1949) {
                ois.readObject();
            }
            return "No map to deserialize XD";
        } catch (Exception var3) {
            var3.printStackTrace();
            return "No map to deserialize XD";
        }
    }
}