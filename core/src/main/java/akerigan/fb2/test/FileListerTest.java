package akerigan.fb2.test;

import akerigan.fb2.AppProperties;
import akerigan.utils.file.FileLister;
import akerigan.utils.file.FilePatternType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

/**
 * Date: 18.03.2008
 * Time: 21:15:38
 *
 * @author Vlad Vinichenko(akerigan@gmail.com)
 */
public class FileListerTest {

    private static Log log = LogFactory.getLog(FileListerTest.class);

    public static void main(String[] args) throws IOException {
        AppProperties appProperties = new AppProperties();
        FileLister lister = new FileLister();
        lister.getStartDirs().addAll(appProperties.getBaseDirs());
        for (File file : lister.findFiles(FilePatternType.postfix, ".zip")) {
            log.info("file: " + file);
        }
    }

}
