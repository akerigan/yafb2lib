package akerigan.fb2;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static akerigan.fb2.CoreConstants.BASE_DIRS_PROP;
import static akerigan.fb2.CoreConstants.PROPS_FILE;

/**
 * Date: 01.05.2010
 * Time: 13:50:56
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class AppProperties {

    private Properties props;

    private List<File> baseDirs;

    public AppProperties() throws IOException {
        String userHome = System.getProperty("user.home");
        File propsFile = new File(userHome + PROPS_FILE);
        if (propsFile.exists()) {
            props = new Properties();
            Reader propsReader = new FileReader(propsFile);
            props.load(propsReader);
            propsReader.close();
        } else {
            throw new IllegalStateException("File doesn't exist: " + propsFile.toString());
        }
    }

    public List<File> getBaseDirs() {
        if (baseDirs == null) {
            if (props.containsKey(BASE_DIRS_PROP)) {
                baseDirs = new LinkedList<File>();
                String baseDirsProp = (String) props.get(BASE_DIRS_PROP);
                for (String baseDir : baseDirsProp.split("\\s*;\\s*")) {
                    File baseDirFile = new File(baseDir);
                    if (baseDirFile.exists() && baseDirFile.isDirectory()) {
                        baseDirs.add(baseDirFile);
                    }
                }
            } else {
                throw new IllegalStateException("Property not found: " + BASE_DIRS_PROP);
            }
        }
        return baseDirs;
    }
}
