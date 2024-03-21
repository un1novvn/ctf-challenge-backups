package com.ctf.sleepwalker.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.Base64;

/* loaded from: SleepWalker.jar:BOOT-INF/classes/com/ctf/sleepwalker/Utils/SerialUtil.class */
public class SerialUtil {
    public void serialize(String o) throws IOException {
        byte[] base64decodedBytes = Base64.getDecoder().decode(o);
        Files.write(Paths.get("/tmp/tmp.ser", new String[0]), base64decodedBytes, new OpenOption[0]);
        System.out.println("success");
    }

    public Object unserialize(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        MyOwnObjectInputStream objectInputStream = new MyOwnObjectInputStream(fileInputStream);
        Object o = objectInputStream.readObject();
        return o;
    }
}