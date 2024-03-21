package com.naiverasp;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.AccessibleObject;

/* loaded from: naiverasp-simple.jar:com/naiverasp/Agent.class */
public class Agent {
    static {
        try {
            File nativeRaspFile = new File("/opt/rasp/libnativerasp.so");
            if (nativeRaspFile.exists()) {
                System.load("/opt/rasp/libnativerasp.so");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        JarFileHelper.addJarToBootstrap(inst);
        ClassFileTransformer transformer = new NaiveRaspClassFileTransformer(inst);
        inst.addTransformer(transformer, true);
        inst.retransformClasses(new Class[]{AccessibleObject.class});
    }
}