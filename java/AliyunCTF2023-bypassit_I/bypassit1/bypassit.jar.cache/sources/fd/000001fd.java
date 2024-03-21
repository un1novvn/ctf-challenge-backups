package com.ctf.bypassit;

import java.io.ObjectInputStream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
/* loaded from: bypassit.jar:BOOT-INF/classes/com/ctf/bypassit/IndexController.class */
public class IndexController {
    @RequestMapping({"/bypassit"})
    @ResponseBody
    public String bypassIt(HttpServletRequest request) {
        try {
            ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
            ois.readObject();
            return "bypass it and rce it";
        } catch (Exception e) {
            e.printStackTrace();
            return "bypass it and rce it";
        }
    }
}