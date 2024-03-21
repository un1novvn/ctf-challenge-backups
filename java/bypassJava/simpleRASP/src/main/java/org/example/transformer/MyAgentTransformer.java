package org.example.transformer;

import org.example.hook.HookJNI;
import org.example.hook.HookRce;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MyAgentTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String name = className.replace("/", ".");
        if(name.equals("java.lang.UNIXProcess")) {
            return HookRce.transformed();
        } else if (name.equals("java.lang.ClassLoader")) {
            return HookJNI.transformed();
        }
        return classfileBuffer;
    }
}
