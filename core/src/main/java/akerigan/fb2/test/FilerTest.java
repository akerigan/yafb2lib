package akerigan.fb2.test;

import akerigan.utils.file.FileLister;
import akerigan.utils.file.FilePatternType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import static akerigan.fb2.CoreConstants.BASE_DIRS_PROP;
import static akerigan.fb2.CoreConstants.PROPS_FILE;

/**
 * Date: 18.03.2008
 * Time: 21:15:38
 *
 * @author Vlad Vinichenko(akerigan@gmail.com)
 */
public class FilerTest {


    private static Log log = LogFactory.getLog(FilerTest.class);

    public static void main(String[] args) throws IOException {
        String userHome = System.getProperty("user.home");
        File propsFile = new File(userHome + PROPS_FILE);
        if (propsFile.exists()) {
            Properties props = new Properties();
            Reader propsReader = new FileReader(propsFile);
            props.load(propsReader);
            propsReader.close();
            if (props.containsKey(BASE_DIRS_PROP)) {
                String baseDirs = (String) props.get(BASE_DIRS_PROP);
                FileLister lister = new FileLister();
                for (String baseDir : baseDirs.split("\\s*;\\s*")) {
                    lister.addStartDir(new File(baseDir));
                }
                for (File file : lister.findFiles(FilePatternType.postfix, ".zip")) {
                    log.info("file: " + file);
                }
            } else {
                log.error("Property not found: " + BASE_DIRS_PROP);
            }
        } else {
            log.error("File doesn't exist: " + propsFile.toString());
        }
    }

}
