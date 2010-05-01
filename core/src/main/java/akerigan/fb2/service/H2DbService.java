package akerigan.fb2.service;

import akerigan.db.StringMapper;

import java.util.Set;
import java.util.TreeSet;

/**
 * Date: 26.04.2010
 * Time: 21:52:01
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class H2DbService extends DbService {

    @Override
    public void init() {
        Set<String> tables = new TreeSet<String>(
                template.query("select table_name from information_schema.tables", StringMapper.getInstance()));
        if (!tables.contains("books")) {
            template.update("create table books(book_id identity primary key, sha1 char(40)," +
                    " container varchar(256), file varchar(256), bigint size)");
            template.update("create index books_sha1_idx ON books(sha1)");
        }
        if (!tables.contains("books_description")) {
            template.update("create table books_description(int book_id, name varchar(256), value varchar(4096))");
            template.update("create index books_id_idx ON books_description(book_id)");
        }
    }


}
