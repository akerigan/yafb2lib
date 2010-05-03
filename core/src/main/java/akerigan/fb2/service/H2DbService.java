package akerigan.fb2.service;

import akerigan.db.DescriptionCallbackHandler;
import akerigan.db.StringMapper;

import java.util.Map;
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
        if (!tables.contains("FILES")) {
            template.update("create table files(book_id identity primary key, sha1 char(40)," +
                    " container varchar(256), file varchar(256), container_size bigint)");
        }
        if (!tables.contains("DESCRIPTION")) {
            template.update("create table description(book_id int, name varchar(256), value varchar(4096))");
        }

        Set<String> indexes = new TreeSet<String>(
                template.query("select index_name from information_schema.indexes", StringMapper.getInstance()));
        if (!indexes.contains("SHA1_IDX")) {
            template.update("create index sha1_idx ON files(sha1)");
        }
        if (!indexes.contains("CONTAINER_IDX")) {
            template.update("create index container_idx ON files(container, container_size)");
        }
        if (!indexes.contains("DESCRIPTION_BOOK_ID_IDX")) {
            template.update("create index description_book_id_idx ON description(book_id)");
        }
    }

    public Map<String, Integer> getContainers() {
        DescriptionCallbackHandler handler = new DescriptionCallbackHandler();
        template.getJdbcOperations().query("select distinct container, container_size from files", handler);
        return handler.getResult();
    }


}
