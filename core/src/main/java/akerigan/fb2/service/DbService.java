package akerigan.fb2.service;

import akerigan.db.BookMapper;
import akerigan.db.ContainerMapper;
import akerigan.db.StringMapper;
import akerigan.fb2.Fb2ContainerIterator;
import akerigan.fb2.domain.Book;
import akerigan.fb2.domain.Container;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 26.04.2010
 * Time: 22:23:56
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public abstract class DbService {

    protected SimpleJdbcTemplate template;
    private Log log = LogFactory.getLog(getClass());

    public void setTemplate(SimpleJdbcTemplate template) {
        this.template = template;
    }

    public abstract void init();

    public Container getContainer(String containerName) {
        try {
            return template.queryForObject("select * from container where name=?",
                    ContainerMapper.getInstance(), containerName);
        } catch (DataAccessException e) {
            log.error("Container not found: " + containerName + ", Exception: " + e.getMessage());
            return null;
        }
    }

    protected abstract int getGeneratedContainerId(String containerName);

    public List<Book> getContainerBooks(int container) {
        return template.query("select * from book where container=?", BookMapper.getInstance(), container);
    }

    public void mergeBook(Book book) {
        if (book.getId() == 0) {
            template.update("insert into book (container, sha1, name, size) values (?,?,?,?)",
                    book.getContainer(), book.getSha1(), book.getName(), book.getSize());
            book.setId(getGeneratedBookId(book.getContainer(), book.getName()));
        } else {
            template.update("update book set saved=false, container=?, sha1=?, name=?, size=? where id=?",
                    book.getContainer(), book.getSha1(), book.getName(), book.getSize(), book.getId());
            deleteDescription(book.getId());
        }
        if (book.getDescription() != null) {
            for (Map.Entry<String, String> entry : book.getDescription().entrySet()) {
                template.update("insert into description (book, name, value) values (?,?,?)",
                        book.getId(), entry.getKey(), entry.getValue());
            }
        }
        template.update("update book set saved=true where id=?", book.getId());
        log.info("book stored: " + book.getName());
    }

    public Book getBook(int container, String name) {
        try {
            return template.queryForObject("select * from book where container=? and name=?",
                    BookMapper.getInstance(), container, name);
        } catch (DataAccessException e) {
            log.error("Book not found: " + container + "," + name + ", Exception: " + e.getMessage());
            return null;
        }
    }

    protected abstract int getGeneratedBookId(int container, String name);

    public Book getBook(int book) {
        try {
            return template.queryForObject("select * from book where id=?",
                    BookMapper.getInstance(), book);
        } catch (DataAccessException e) {
            log.error("Book not found: " + book + ", Exception: " + e.getMessage());
            return null;
        }
    }

    public void mergeContainer(Container container, File zipFb2Container, String encoding) throws IOException {
        Fb2ContainerIterator iterator = new Fb2ContainerIterator(zipFb2Container, encoding);
        Map<String, Book> dbBooksMap = new HashMap<String, Book>();
        if (container.getId() == 0) {
            template.update("insert into container (name, size) values (?,?)",
                    container.getName(), container.getSize());
            container.setId(getGeneratedContainerId(container.getName()));
        } else {
            template.update("update container set size=?, saved=false where id=?",
                    container.getSize(), container.getId());
            for (Book dbBook : getContainerBooks(container.getId())) {
                dbBooksMap.put(dbBook.getName(), dbBook);
            }
        }
        while (iterator.hasNext()) {
            Book book = iterator.next();
            Book dbBook = dbBooksMap.get(iterator.getEntryName());
            if (dbBook == null || dbBook.getSize() != iterator.getEntrySize() || !dbBook.isSaved()) {
                if (book != null) {
                    if (dbBook != null) {
                        book.setId(dbBook.getId());
                    }
                    book.setContainer(container.getId());
                    mergeBook(book);
                    dbBooksMap.remove(book.getName());
                }
            } else {
                log.info("book skipped: " + iterator.getEntryName());
            }
        }
        iterator.close();
        for (Book dbBook : dbBooksMap.values()) {
            deleteBook(dbBook.getId());
        }
        template.update("update container set saved=true where id=?", container.getId());
        log.info("container stored: " + container.getName());
    }

    public abstract void deleteBook(int book);

    public abstract void deleteDescription(int book);

    public List<String> getAuthorsLastNames() {

        return template.query("select distinct value from description " +
                "where name='title-info.author.last-name' order by value", StringMapper.getInstance());
    }


}
