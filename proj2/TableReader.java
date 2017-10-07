import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Created by jhinukbarman on 2/26/17.
 */
public class TableReader {
    //borrowed code: https://docs.oracle.com/javase/tutorial/essential/io/file.html
    String s;

    public TableReader(String name) {
        s = name;
    }

    public static void main(String a[]) {
        File tempFile = null;
        BufferedWriter writer = null;
        try {
            tempFile = File.createTempFile("MyTempFile", ".tmp");
            writer = new BufferedWriter(new FileWriter(tempFile));
            writer.write("Writing data into temp file!!!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (Exception ex) {
            }
        }
        System.out.println("Stored data in temporary file.");
    }
}

