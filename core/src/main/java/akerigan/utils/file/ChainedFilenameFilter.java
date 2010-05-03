package akerigan.utils.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Date: 03.05.2010
 * Time: 20:14:06
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class ChainedFilenameFilter implements FilenameFilter {

    private FilenameFilter[] filters;

    public ChainedFilenameFilter(FilenameFilter... filters) {
        this.filters = filters;
    }

    public boolean accept(File dir, String name) {
        for (FilenameFilter filter : filters) {
            boolean accept = filter.accept(dir, name);
            if (accept) {
                return true;
            }
        }
        return false;
    }
}
