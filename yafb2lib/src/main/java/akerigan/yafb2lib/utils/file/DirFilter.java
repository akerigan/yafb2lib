package akerigan.yafb2lib.utils.file;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 18.03.2008
 *         Time: 20:51:13
 */
public class DirFilter implements FileFilter {
    public boolean accept(File file) {
        return file.isDirectory();
    }
}
