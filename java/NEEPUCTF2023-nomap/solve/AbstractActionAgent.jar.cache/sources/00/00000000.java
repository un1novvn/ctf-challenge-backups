package defpackage;

import java.lang.instrument.Instrumentation;

/* renamed from: AgentMain  reason: default package */
/* loaded from: AbstractActionAgent.jar:AgentMain.class */
public class AgentMain {
    public static final String ClassName = "javax.swing.ActionMap";

    public static void premain(String agentArgs, Instrumentation ins) {
        ins.addTransformer(new DefineTransformer(), true);
        Class[] classes = ins.getAllLoadedClasses();
        for (Class clazz : classes) {
            if (clazz.getName().equals(ClassName)) {
                try {
                    ins.retransformClasses(new Class[]{clazz});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void agentmain(String agentArgs, Instrumentation ins) {
        ins.addTransformer(new DefineTransformer(), true);
        Class[] classes = ins.getAllLoadedClasses();
        for (Class clazz : classes) {
            if (clazz.getName().equals(ClassName)) {
                try {
                    ins.retransformClasses(new Class[]{clazz});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}