package com.ctf.sleepwalker.Controllers;

import com.ctf.sleepwalker.Utils.MessageReader;
import com.ctf.sleepwalker.Utils.Request;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
/* loaded from: SleepWalker.jar:BOOT-INF/classes/com/ctf/sleepwalker/Controllers/MainController.class */
public class MainController {
    @RequestMapping({"/"})
    @ResponseBody
    public String index() {
        return "Welcome to SCTF!";
    }

    @RequestMapping({"/client/status"})
    @ResponseBody
    public String status() throws IOException, ClassNotFoundException {
        String res = Request.sendGetRequest("http://client:5000/status");
        MessageReader messageReader = new MessageReader();
        System.out.println(res);
        Object readres = messageReader.read(res);
        return readres.toString();
    }

    @RequestMapping({"/server/status"})
    @ResponseBody
    public String server_status() {
        return "OK!";
    }
}