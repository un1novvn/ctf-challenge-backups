package org.example.agent;

import org.example.transformer.MyAgentTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class MyAgent {
    public static void agentmain(String args, Instrumentation inst) throws  UnmodifiableClassException {

        System.out.println("start agentmain");
        MyAgentTransformer agentTransformer = new MyAgentTransformer();
        String prefix = "myPrefix_";
        String className;
        Class<?>[] clazz = inst.getAllLoadedClasses();
        inst.addTransformer(agentTransformer, true);
        inst.setNativeMethodPrefix(agentTransformer, prefix);
        for (Class<?> c: clazz) {
            className = c.getName();
            if(className.equals("java.lang.UNIXProcess") || className.equals("java.lang.ClassLoader")) {
                inst.retransformClasses(c);
            }
        }
    }
    public static void premain(String args, Instrumentation inst) {}
}
