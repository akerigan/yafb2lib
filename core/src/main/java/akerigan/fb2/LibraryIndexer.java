package akerigan.fb2;

import akerigan.fb2.domain.Book;
import akerigan.fb2.domain.Container;
import akerigan.fb2.service.DbService;
import akerigan.utils.Fb2Utils;
import akerigan.utils.file.FileLister;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
            FileLister lister = new FileLister();
            List<File> baseDirs = appProperties.getBaseDirs();
            if (baseDirs != null) {
                lister.getStartDirs().addAll(baseDirs);
                List<String> baseDirsString = new LinkedList<String>();
                for (File baseDir : baseDirs) {
                    baseDirsString.add(baseDir.toString() + "/");
                }
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
                        try {
                            if (lowerContainerName.endsWith(".zip")) {
                                Container container = service.getContainer(containerName);
                                if (container == null) {
                                    container = new Container();
                                    container.setName(containerName);
                                    container.setSize(file.length());
                                }
                                if (container.getId() == 0 || container.getSize() != file.length() || !container.isSaved()) {
                                    container.setSize(file.length());
                                    service.mergeContainer(container, file, "IBM-866");
                                } else {
                                    log.info("container skipped: " + containerName);
                                }
                            } else if (lowerContainerName.endsWith(".fb2")) {
                                Book dbBook = service.getBook(0, containerName);
                                if (dbBook == null || dbBook.getSize() != file.length() || !dbBook.isSaved()) {
                                    Book book = Fb2Utils.getBookInfo(new FileInputStream(file), true);
                                    if (dbBook != null) {
                                        book.setId(dbBook.getId());
                                    }
                                    book.setName(containerName);
                                    book.setSize(file.length());
                                    service.mergeBook(book);
                                } else {
                                    log.info("book skipped: " + containerName);
                                }
                            }
                        } catch (Exception e) {
                            log.error("Cant add book(s): " + file.toString(), e);
                        }
                    }
                }
            }
        }
    }

}
