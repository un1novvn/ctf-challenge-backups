package org.example.hook;

import javassist.*;

public class HookJNI {
    public static byte[] transformed() {
        ClassPool pool = ClassPool.getDefault();
        CtClass clazz = null;
        try {
            System.out.println("start convert java.lang.ClassLoader");
            clazz = pool.getCtClass("java.lang.ClassLoader");
            if(clazz.isFrozen()) {
                clazz.defrost();
            }
            CtMethod method = clazz.getDeclaredMethod("loadLibrary0");
            System.out.println("modify method loadLibrary0");
            method.insertBefore("throw new RuntimeException(\"RASP hooked loadLibrary0\");");
            System.out.println("has modified method loadLibrary0");
            return clazz.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
