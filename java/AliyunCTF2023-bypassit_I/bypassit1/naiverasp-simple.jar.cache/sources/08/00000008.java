package com.naiverasp.hook;

import java.io.IOException;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;

/* loaded from: naiverasp-simple.jar:com/naiverasp/hook/WatchHook.class */
public class WatchHook extends AbstractHook {
    public WatchHook(MethodDescriptor methodDesc) {
        super(methodDesc);
    }

    @Override // com.naiverasp.hook.AbstractHook
    public void hookMethod(CtClass ctClass) throws IOException, CannotCompileException, NotFoundException {
        CtBehavior[] methods;
        CtBehavior[] ctBehaviorArr;
        if (!this.methodDesc.isConstruct()) {
            methods = ctClass.getDeclaredMethods(this.methodDesc.getMethodName());
        } else {
            methods = ctClass.getConstructors();
        }
        for (CtBehavior method : methods) {
            method.insertBefore("com.naiverasp.Utils.printStackTrace();\nfor (int i = 0; i < $args.length; i++) {\n            System.out.println(i + $args[i].toString());\n        }\n");
            System.out.println("hooking method: " + method.getLongName());
        }
    }
}