package com.example.demo.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/* loaded from: noMap.jar:BOOT-INF/classes/com/example/demo/util/MyObjectInputStream.class */
public class MyObjectInputStream extends ObjectInputStream {
    private static final String[] blacklist = {"bsh", "clojure", "java.util", "javax.management", "java.rmi", "com.sun.jndi", "sun.rmi", "org.apache", "org.hibernate", "org.springframework", "com.mchange.v2.c3p0", "com.rometools.rome.feed.impl", "com.alibaba.fastjson", "java.net.URL", "java.lang.reflect.Proxy", "javax.xml.transform.Templates", "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl", "org.apache.xalan.xsltc.trax.TemplatesImpl", "org.python.core", "com.mysql.jdbc", "org.jboss"};

    public MyObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    protected MyObjectInputStream() throws IOException, SecurityException {
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String[] strArr;
        String className = desc.getName();
        for (String forbiddenPackage : blacklist) {
            if (className.startsWith(forbiddenPackage)) {
                throw new InvalidClassException("Unauthorized deserialization attempt", className);
            }
        }
        return super.resolveClass(desc);
    }
}