import java.io.*;
public class fileClearing {
    public static void main(String path){
        try {
            new FileWriter(path,false).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
