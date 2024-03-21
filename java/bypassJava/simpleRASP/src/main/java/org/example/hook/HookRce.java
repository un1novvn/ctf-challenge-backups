package org.example.hook;

import javassist.*;

public class HookRce {
    public static byte[] transformed() {
        ClassPool pool = ClassPool.getDefault();
        CtClass clazz = null;
        try {
            System.out.println("start convert java.lang.UNIXProcess");
            clazz = pool.getCtClass("java.lang.UNIXProcess");
            if(clazz.isFrozen()) {
                clazz.defrost();
            }
//            CtMethod method = CtNewMethod.make("int Wrapping_forkAndExec(int var1, byte[] var2, byte[] var3, byte[] var4, int var5, byte[] var6, int var7, byte[] var8, int[] var9, boolean var10);", clazz);
//            method.setModifiers(Modifier.PRIVATE|Modifier.NATIVE);
//            System.out.println("add new native method Wrapping_forkAndExec");
//            clazz.addMethod(method);
            CtMethod method1 = clazz.getDeclaredMethod("forkAndExec");
            System.out.println("remove old native method forkAndExec");
            clazz.removeMethod(method1);
            CtMethod method2 = CtNewMethod.make("int forkAndExec(int var1, byte[] var2, byte[] var3, byte[] var4, int var5, byte[] var6, int var7, byte[] var8, int[] var9, boolean var10) { throw new RuntimeException(\"RASP hooked forkAndExec\"); }", clazz);
            System.out.println("add new method forkAndExec");
            clazz.addMethod(method2);
            return clazz.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
