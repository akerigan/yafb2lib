package akerigan.fb2.service;

import akerigan.db.BookInfoMapper;
import akerigan.db.BooksContainerMapper;
import akerigan.db.StringMapper;
import akerigan.fb2.domain.BookInfo;
import akerigan.fb2.domain.BooksContainer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Date: 26.04.2010
 * Time: 21:52:01
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class H2DbService extends DbService {

    private Log log = LogFactory.getLog(getClass());

    @Override
    public void init() {
        Set<String> tables = new TreeSet<String>(
                template.query("select table_name from information_schema.tables", StringMapper.getInstance()));
        if (!tables.contains("CONTAINER")) {
            template.update("create table container(id identity primary key, name varchar(256), size bigint)");
        }
        if (!tables.contains("BOOK")) {
            template.update("create table book(id identity primary key, container int, sha1 char(40)," +
                    " name varchar(256), size bigint)");
        }
        if (!tables.contains("DESCRIPTION")) {
            template.update("create table description(book int, name varchar(256), value varchar(4096))");
        }

        Set<String> indexes = new TreeSet<String>(
                template.query("select index_name from information_schema.indexes", StringMapper.getInstance()));
/*
        if (!indexes.contains("SHA1_IDX")) {
            template.update("create index sha1_idx ON files(sha1)");
        }
*/
        if (!indexes.contains("CONTAINER_NAME_IDX")) {
            template.update("create unique index container_name_idx ON container(name)");
        }
        if (!indexes.contains("CONTAINER_BOOKS_IDX")) {
            template.update("create unique index container_books_idx ON book(container)");
        }
/*
        if (!indexes.contains("DESCRIPTION_BOOK_ID_IDX")) {
            template.update("create index description_book_id_idx ON description(book_id)");
        }
*/
    }

    @Override
    public BooksContainer getBooksContainer(String containerName) {
        try {
            return template.queryForObject("select * from container where name=?",
                    BooksContainerMapper.getInstance(), containerName);
        } catch (DataAccessException e) {
            log.error("Container not found: " + containerName + ", Exception: " + e.getMessage());
            return null;
        }
    }

    public int getBooksContainerId(String containerName) {
        try {
            return template.queryForInt("select id from container where name=?", containerName);
        } catch (DataAccessException e) {
            log.error("Container not found: " + containerName + ", Exception: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public List<BookInfo> getBooksInfo(int container) {
        return template.query("select * from book where container=?", BookInfoMapper.getInstance(), container);
    }

    @Override
    public void storeBookInfo(BookInfo bookInfo) {
        log.info(bookInfo);
    }

    @Override
    public void storeBooksContainer(BooksContainer container) {
        List<BookInfo> dbBooksInfo = null;
        if (container.getId() == 0) {
            template.update("insert into container (name, size) values (?,?)", container.getName(), container.getSize());
            container.setId(getBooksContainerId(container.getName()));
        } else {
            dbBooksInfo = getBooksInfo(container.getId());
        }
        if (container.getBooksInfo() != null) {
            if (dbBooksInfo == null) {
                for (BookInfo bookInfo : container.getBooksInfo()) {
                    bookInfo.setContainer(container.getId());
                    storeBookInfo(bookInfo);
                }
            }
        }
    }

}
