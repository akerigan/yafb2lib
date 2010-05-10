package akerigan.fb2.service;

import akerigan.db.StringMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

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
            template.update("create table container(id identity primary key, name varchar(256), size bigint, saved bool)");
        }
        if (!tables.contains("BOOK")) {
            template.update("create table book(id identity primary key, container int, sha1 char(40)," +
                    " name varchar(256), size bigint, saved bool)");
        }
        if (!tables.contains("DESCRIPTION")) {
            template.update("create table description(id identity primary key, book int, name varchar(256), value varchar(16384))");
        } else {
            template.update("alter table description alter column value varchar(16384)");
        }

        Set<String> indexes = new TreeSet<String>(
                template.query("select indexname from pg_indexes where schemaname='public'", StringMapper.getInstance()));
/*
        if (!indexes.contains("SHA1_IDX")) {
            template.update("create index sha1_idx ON files(sha1)");
        }
*/
        if (!indexes.contains("CONTAINER_NAME_IDX")) {
            template.update("create unique index container_name_idx ON container(name)");
        }
        if (!indexes.contains("CONTAINER_BOOKS_IDX")) {
            template.update("create index container_books_idx ON book(container)");
        }
        if (!indexes.contains("BOOK_NAME_IDX")) {
            template.update("create index book_name_idx ON book(container,name)");
        }
        if (!indexes.contains("BOOK_DESCRIPTION_IDX")) {
            template.update("create index book_description_idx ON description(book)");
        }
/*
        if (!indexes.contains("DESCRIPTION_BOOK_ID_IDX")) {
            template.update("create index description_book_id_idx ON description(book_id)");
        }
*/
    }

    @Override
    protected int getGeneratedContainerId(String containerName) {
        try {
            return template.queryForInt("select id from container where name=?", containerName);
        } catch (DataAccessException e) {
            log.error("Container not found: " + containerName + ", Exception: " + e.getMessage());
            return 0;
        }
    }

    @Override
    protected int getGeneratedBookId(int container, String name) {
        try {
            return template.queryForInt("select id from book where container=? and name=?", container, name);
        } catch (DataAccessException e) {
            log.error("Book not found: " + container + "," + name + ", Exception: " + e.getMessage());
            return 0;
        }
    }

}
