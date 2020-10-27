package File;
import java.io.File;

public class FileUtil {
    public static File getFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists() || !file.isFile())
            throw new IllegalArgumentException("not a file: " + file);
        return file;
    }
}