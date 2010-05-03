package akerigan.yafb2lib;

import akerigan.utils.file.FileLister;
import akerigan.utils.file.SuffixFilenameFilter;
import akerigan.yafb2lib.domain.fb2.Book;
import akerigan.yafb2lib.persist.service.BookService;
import de.schlichtherle.util.zip.ZipEntry;
import de.schlichtherle.util.zip.ZipFile;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Enumeration;

/**
 * Date: 13.08.2008
 * Time: 21:47:10
 *
 * @author Vlad Vinichenko(akerigan@gmail.com)
 */
public class BookIndexer {

    public static void main(String[] args) {

        try {

            ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");
            BookService service = (BookService) ctx.getBean("jdbcBookService");

//            String path = "c:/library/fb2/LibRusEc/�� �������/�";
//            String path = "c:/Mobile/givc/download/fb2";
            String path = "c:/library/fb2";

            JAXBContext context = JAXBContext.newInstance(Book.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            FileLister lister = new FileLister();
            for (String dirPath : path.split(";")) {
                lister.addStartDir(new File(dirPath));
            }
            for (File file : lister.findFiles(new SuffixFilenameFilter(".zip"))) {
                System.out.println(file);
                try {
                    ZipFile zipFile = new ZipFile(file, "windows-1251");
                    Enumeration<ZipEntry> entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        if (entry.getName().toLowerCase().endsWith(".fb2")) {
                            Book book = (Book) unmarshaller.unmarshal(zipFile.getInputStream(entry));
                            book.setFileName(file.toString());
                            service.addBook(book);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("error in file: " + file);
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
