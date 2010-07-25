package akerigan.utils.file;

import de.schlichtherle.util.zip.ZipEntry;
import de.schlichtherle.util.zip.ZipOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Date: 27.05.2010
 * Time: 21:25:28
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class FileUtils {

    private static Log log = LogFactory.getLog(FileUtils.class);

    public static void makeDirs(File file) {
        if (file == null) {
            throw new IllegalStateException("File is null");
        }
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new IllegalStateException("Not a directory: " + file.toString());
            }
        } else {
            if (!file.mkdirs()) {
                throw new IllegalStateException("Cant create directory: " + file.toString());
            }
            log.info("Directory created: " + file.toString());
        }
    }

    public static void copyToZip(InputStream in, File file, String entryName) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(file));
        zipOut.setLevel(9);
        ZipEntry zipEntry = new ZipEntry(entryName);
        zipOut.putNextEntry(zipEntry);
        byte[] buffer = new byte[8192];
        int count;
        while ((count = in.read(buffer)) > 0) {
            zipOut.write(buffer, 0, count);
        }
        zipOut.flush();
        zipOut.close();
    }
}
