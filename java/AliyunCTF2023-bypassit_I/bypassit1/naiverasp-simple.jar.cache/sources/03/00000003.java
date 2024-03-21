package com.naiverasp;

import java.util.Arrays;
import java.util.Random;
import javassist.bytecode.Opcode;
import sun.reflect.Reflection;

/* loaded from: naiverasp-simple.jar:com/naiverasp/Utils.class */
public class Utils {
    public static String generateRandomString(int targetStringLength) {
        Random random = new Random();
        String generatedString = ((StringBuilder) random.ints(97, Opcode.ISHR + 1).limit(targetStringLength).collect(StringBuilder::new, (v0, v1) -> {
            v0.appendCodePoint(v1);
        }, (v0, v1) -> {
            v0.append(v1);
        })).toString();
        return generatedString;
    }

    public static void dumpArgs(Object[] args) {
        System.out.println("dumpArgs");
        for (Object arg : args) {
            if (arg instanceof String[]) {
                System.out.println(Arrays.toString((String[]) arg));
            } else {
                System.out.println(arg);
            }
        }
    }

    public static void printStackTrace() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stack.length; i++) {
            System.out.println(stack[i].getClassName() + "." + stack[i].getMethodName());
        }
        System.out.println("--------------------------------------------------");
    }

    public static boolean checkTrust() {
        for (int i = 4; i < 8; i++) {
            try {
                Class clazz = Reflection.getCallerClass(i);
                ClassLoader loader = clazz.getClassLoader();
                if (loader != null && !loader.toString().startsWith("org.springframework.boot.loader.LaunchedURLClassLoader")) {
                    System.out.println("class name " + clazz.getName() + " loader " + loader);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }
        return true;
    }
}