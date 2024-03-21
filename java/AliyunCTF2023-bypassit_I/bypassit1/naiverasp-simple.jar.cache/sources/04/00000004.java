package com.naiverasp.hook;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

/* loaded from: naiverasp-simple.jar:com/naiverasp/hook/AbstractHook.class */
public abstract class AbstractHook implements Hook {
    protected MethodDescriptor methodDesc;

    public abstract void hookMethod(CtClass ctClass) throws IOException, CannotCompileException, NotFoundException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractHook(MethodDescriptor methodDesc) {
        this.methodDesc = methodDesc;
    }

    public boolean isClassMatched(String className, ClassLoader classLoader, Instrumentation inst) {
        if (className == null) {
            return false;
        }
        String className2 = className.replace("/", ".");
        String hookClassName = this.methodDesc.getClassName();
        return className2.equals(hookClassName);
    }
}