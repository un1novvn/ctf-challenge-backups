package com.ctf.sleepwalker.Utils;

import java.io.File;
import java.io.IOException;

/* loaded from: SleepWalker.jar:BOOT-INF/classes/com/ctf/sleepwalker/Utils/MessageReader.class */
public class MessageReader {
    private SerialUtil serialUtil = new SerialUtil();

    public Object read(String data) throws IOException, ClassNotFoundException {
        File file = new File("/tmp/tmp.ser");
        if (file.exists()) {
            Object unserialize = this.serialUtil.unserialize("/tmp/tmp.ser");
            return unserialize;
        }
        this.serialUtil.serialize(data);
        return "the first message";
    }
}