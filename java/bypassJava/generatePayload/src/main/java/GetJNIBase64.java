import java.io.RandomAccessFile;
import java.util.Base64;

public class GetJNIBase64 {
    public static void main(String[] args) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("libcmd.so", "r");
            byte[] content = new byte[(int) randomAccessFile.length()];
            randomAccessFile.read(content);
            String base64Content = Base64.getEncoder().encodeToString(content);
            System.out.println(base64Content);
            randomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
