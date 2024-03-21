package defpackage;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/* renamed from: DefineTransformer  reason: default package */
/* loaded from: AbstractActionAgent.jar:DefineTransformer.class */
public class DefineTransformer implements ClassFileTransformer {
    public static final String ClassName = "javax.swing.AbstractAction";

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        String className2 = className.replace("/", ".");
        if (className2.equals(ClassName)) {
            System.out.println("Find the Inject Class: javax.swing.AbstractAction");
            ClassPool pool = ClassPool.getDefault();
            try {
                CtClass c = pool.getCtClass(className2);
                CtMethod ctMethod = c.getDeclaredMethod("writeObject");
                ctMethod.setBody("{$1.defaultWriteObject();java.lang.Object keys[] = arrayTable.getKeys(null);int validCount = keys.length;$1.writeInt(validCount);    for (int i=0; i<validCount; i++) {\n        if (keys != null) {\n            $1.writeObject(\"ricky\");\n            $1.writeObject(arrayTable.get(keys[i]));\n        }\n    }\n}");
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