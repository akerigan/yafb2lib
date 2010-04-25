package akerigan.yafb2lib;

import akerigan.yafb2lib.persist.service.BookService;
import akerigan.yafb2lib.domain.fb2.Book;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 27.09.2008
 *         Time: 15:37:14
 */
public class BookCopy {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");
        BookService service = (BookService) ctx.getBean("jdbcBookService");
        String sequenceName = "Фантастический боевик";
        List<Book> books = service.getBooksBySequenceName(sequenceName);
        String base = "c:/tmp";
        File baseDir = new File(base, sequenceName);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        for (Book book : books) {
            System.out.println("path = " + book.getFileName());
            File file = new File(book.getFileName());
            if (file.exists()) {
                File newFile = new File(baseDir, file.getName());
                try {
                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
                    int i;
                    while ((i = in.read()) != -1) {
                        out.write(i);
                    }
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
