package org.example;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("start my RASP........");
        String target;
        if(args.length == 1) {
            target = args[0];
        } else {
            System.out.println("need target jar arg");
            return;
        }
        try {
            Class<?> virtualMachine;
            Class<?> virtualMachineDescriptor;
            String toolsJarPath = System.getProperty("java.home").replace("jre", "lib") + "/tools.jar";
            File toolsJar = new File(toolsJarPath);
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{toolsJar.toURI().toURL()});
            virtualMachine = urlClassLoader.loadClass("com.sun.tools.attach.VirtualMachine");
            virtualMachineDescriptor = urlClassLoader.loadClass("com.sun.tools.attach.VirtualMachineDescriptor");
            Method listMethod = virtualMachine.getDeclaredMethod("list");
            Method attachMethod = virtualMachine.getDeclaredMethod("attach",String.class);
            Method displayMethod = virtualMachineDescriptor.getDeclaredMethod("displayName");
            Method idMethod = virtualMachineDescriptor.getDeclaredMethod("id");
            Method loadAgentMethod = virtualMachine.getDeclaredMethod("loadAgent", String.class);
            Method detachMethod = virtualMachine.getDeclaredMethod("detach");
            List<?> pidList = (List<?>) listMethod.invoke(null);
            for (Object vir: pidList) {
                String name = (String) displayMethod.invoke(vir);
                String pid = (String) idMethod.invoke(vir);
                System.out.println(name + ":" + pid);
                if(name.equals(target)) {
                    Object attach = attachMethod.invoke(null, pid);
                    String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                    loadAgentMethod.invoke(attach, jarPath);
                    detachMethod.invoke(attach);
                }
            }
            urlClassLoader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}