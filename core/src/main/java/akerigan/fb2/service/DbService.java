package akerigan.fb2.service;

import akerigan.fb2.domain.BookInfo;
import akerigan.fb2.domain.BooksContainer;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import java.util.List;

/**
 * Date: 26.04.2010
 * Time: 22:23:56
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public abstract class DbService {

    protected SimpleJdbcTemplate template;

    public void setTemplate(SimpleJdbcTemplate template) {
        this.template = template;
    }

    public abstract void init();

    public abstract BooksContainer getBooksContainer(String containerName);

    public abstract List<BookInfo> getBooksInfo(int container);

    public abstract BookInfo getBookInfo(int container, String name);

    public abstract void storeBookInfo(BookInfo bookInfo);

    public abstract void storeBooksContainer(BooksContainer booksContainer);

}
