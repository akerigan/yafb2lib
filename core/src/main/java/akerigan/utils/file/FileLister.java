package akerigan.utils.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 18.03.2008
 * Time: 20:25:11
 *
 * @author Vlad Vinichenko(akerigan@gmail.com)
 */
public class FileLister {

    private ArrayList<File> startDirs = new ArrayList<File>();

    public FileLister() {
    }

    public ArrayList<File> getStartDirs() {
        return startDirs;
    }

    public void addStartDir(File dir) {
        if (dir.exists()) {
            startDirs.add(dir);
        }
    }

    public List<File> findFiles(FilenameFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Filename filter is empty");
        }
        ArrayList<File> dirsFound = new ArrayList<File>();
        ArrayList<File> dirsCurrent = new ArrayList<File>(startDirs);
        ArrayList<File> dirsPrevious;
        FileFilter dirFilter = new DirFilter();
        while (dirsCurrent.size() != 0) {
            dirsFound.addAll(dirsCurrent);
            dirsPrevious = dirsCurrent;
            dirsCurrent = new ArrayList<File>();
            for (File dir : dirsPrevious) {
                dirsCurrent.addAll(Arrays.asList(dir.listFiles(dirFilter)));
            }
        }

        ArrayList<File> filesFound = new ArrayList<File>();
        for (File dir : dirsFound) {
            filesFound.addAll(Arrays.asList(dir.listFiles(filter)));
        }
        return filesFound;
    }
}
