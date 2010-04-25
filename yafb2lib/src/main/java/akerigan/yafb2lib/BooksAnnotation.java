package akerigan.yafb2lib;

import akerigan.yafb2lib.domain.fb2.Book;
import akerigan.yafb2lib.persist.service.BookService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

/**
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 30.09.2008
 *         Time: 22:39:51
 */
public class BooksAnnotation {

    public static void main(String[] args) {
        try {
            String sequenceName = "Фантастический боевик";
            String base = "c:/tmp";

            File baseDir = new File(base, sequenceName);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            File index = new File(baseDir, "index.html");

            Properties props = new Properties();
            props.put("resource.loader", "class");
            props.put("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

            Velocity.init(props);
            Template template = Velocity.getTemplate("annotations.vm");
            VelocityContext context = new VelocityContext();

            ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");
            BookService service = (BookService) ctx.getBean("jdbcBookService");
            List<Book> books = service.getBooksBySequenceName(sequenceName);
            Collections.sort(books, new Comparator<Book>() {
                public int compare(Book book1, Book book2) {
                    return book1.getFileName().compareToIgnoreCase(book2.getFileName());
                }
            });

            context.put("title", sequenceName);
            context.put("books", books);

            Writer writer = new FileWriter(index);

            template.merge(context, writer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
