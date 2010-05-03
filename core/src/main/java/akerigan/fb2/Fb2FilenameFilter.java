package akerigan.fb2;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Date: 03.05.2010
 * Time: 20:31:04
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class Fb2FilenameFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        String lowerName = name.toLowerCase();
        return lowerName.endsWith(".zip") || lowerName.endsWith("fb2");
    }
}
