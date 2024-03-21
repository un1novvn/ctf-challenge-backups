package defpackage;

import java.lang.instrument.Instrumentation;

/* renamed from: MyAgent  reason: default package */
/* loaded from: test_nomap_agent.jar:MyAgent.class */
public class MyAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new MyClassFileTransformer(), true);
    }
}