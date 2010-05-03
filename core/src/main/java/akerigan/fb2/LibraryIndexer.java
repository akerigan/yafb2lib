package akerigan.fb2;

import akerigan.fb2.service.DbService;
import akerigan.utils.file.FileLister;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Date: 03.05.2010
 * Time: 20:47:30
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class LibraryIndexer {

    private static Log log = LogFactory.getLog(LibraryIndexer.class);

    public static void main(String[] args) {
        AppProperties appProperties = null;
        try {
            appProperties = new AppProperties();
        } catch (IOException e) {
            log.error("Cant initialize application properties", e);
        }
        if (appProperties != null) {
            ApplicationContext ctx = new ClassPathXmlApplicationContext("/appContext.xml");
            DbService service = (DbService) ctx.getBean("dbService");
            service.init();
            FileLister lister = new FileLister();
            List<File> baseDirs = appProperties.getBaseDirs();
            if (baseDirs != null) {
                lister.getStartDirs().addAll(baseDirs);
                List<String> baseDirsString = new LinkedList<String>();
                for (File baseDir : baseDirs) {
                    baseDirsString.add(baseDir.toString() + "/");
                }
                Map<String, Integer> containers = service.getContainers();
                for (File file : lister.findFiles(new Fb2FilenameFilter())) {
                    log.info("book container: " + file);
                    String fullFileName = file.toString();
                    String lowerContainerName = null;
                    String containerName = null;
                    for (String baseDir : baseDirsString) {
                        if (fullFileName.startsWith(baseDir)) {
                            containerName = fullFileName.substring(baseDir.length());
                            lowerContainerName = containerName.toLowerCase();
                            break;
                        }
                    }
                    log.info("container name: " + containerName);
                    if (containerName != null) {
                        long containerSize = file.length();
                        if (!containers.containsKey(containerName) || containers.get(containerName) != containerSize) {
                            if (lowerContainerName.endsWith(".zip")) {

                            } else if (lowerContainerName.endsWith(".fb2")) {

                            }
                        }
                    }
                }
            }
        }
    }

}
