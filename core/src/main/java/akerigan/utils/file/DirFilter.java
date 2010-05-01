package akerigan.utils.file;

import java.io.File;
import java.io.FileFilter;

/**
 * Date: 18.03.2008
 * Time: 20:51:13
 *
 * @author Vlad Vinichenko(akerigan@gmail.com)
 */
public class DirFilter implements FileFilter {
    public boolean accept(File file) {
        return file.isDirectory();
    }
}
