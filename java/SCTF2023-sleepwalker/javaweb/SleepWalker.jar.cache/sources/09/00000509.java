package com.ctf.sleepwalker.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import javax.swing.AbstractAction;

/* loaded from: SleepWalker.jar:BOOT-INF/classes/com/ctf/sleepwalker/Utils/MyOwnObjectInputStream.class */
public class MyOwnObjectInputStream extends ObjectInputStream {
    private static int count = 0;
    private static final String[] blacklist = {"java.util", "javax.management", "java.rmi", "sun.rmi", "org.apache", "org.hibernate", "org.springframework", "com.mchange.v2.c3p0", "com.rometools.rome.feed.impl", "java.net.URL", "java.lang.reflect.Proxy", "javax.xml.transform.Templates", "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl", "org.apache.xalan.xsltc.trax.TemplatesImpl", "org.python.core", "com.mysql.jdbc", "org.jboss", "com.fasterxml.jackson", "com.sun.jndi", "com.alibaba.fastjson.JSONObject"};

    public MyOwnObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    protected MyOwnObjectInputStream() throws IOException, SecurityException {
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String className = desc.getName();
        String[] var3 = blacklist;
        if (count == 0) {
            if (isParentClass(Class.forName(desc.getName()))) {
                throw new InvalidClassException("fuckaway", className);
            }
            count++;
            return super.resolveClass(desc);
        }
        for (String forbiddenPackage : var3) {
            if (className.startsWith(forbiddenPackage) && !className.equals("java.util.ArrayList")) {
                throw new InvalidClassException("Unauthorized deserialization attempt", className);
            }
        }
        return super.resolveClass(desc);
    }

    private static boolean isParentClass(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        if (clazz == AbstractAction.class) {
            return true;
        }
        Class<?> superClass = clazz.getSuperclass();
        return isParentClass(superClass);
    }
}