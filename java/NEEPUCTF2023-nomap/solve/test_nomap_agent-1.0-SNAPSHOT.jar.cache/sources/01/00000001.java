package defpackage;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/* renamed from: MyClassFileTransformer  reason: default package */
/* loaded from: test_nomap_agent-1.0-SNAPSHOT.jar:MyClassFileTransformer.class */
public class MyClassFileTransformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        String className2 = className.replace("/", ".");
        if (className2.equals("javax.swing.AbstractAction")) {
            System.out.println("Find the Inject Class: javax.swing.AbstractAction");
            System.out.println(1);
            try {
                String a = null;
                a.toCharArray();
                ClassPool pool = ClassPool.getDefault();
                CtClass c = pool.getCtClass(className2);
                System.out.println(3);
                System.out.println("hhhh");
                CtMethod ctMethod = c.getDeclaredMethod("writeObject");
                ctMethod.setBody("{System.out.println(\"hhhhwriteObject\");}");
                byte[] bytes = c.toBytecode();
                c.detach();
                return bytes;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }
}