package pl.edu.agh.ztis.helper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Michal on 2015-05-23.
 * Helper class for file operations
 */
public class FileHelper {
    public static InputStream loadResource(String fileName) throws FileNotFoundException {
        InputStream stream = FileHelper.class.getResourceAsStream(fileName);

        if (stream == null) {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            URL fileUrl = classLoader.getResource(fileName);
            if (fileUrl == null) {
                throw new FileNotFoundException(fileName);
            }

            return new FileInputStream(fileUrl.getFile());
        }

        return stream;
    }
}
