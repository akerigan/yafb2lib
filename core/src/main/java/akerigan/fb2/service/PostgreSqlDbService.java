package akerigan.fb2.service;

import akerigan.db.StringMapper;

import java.util.Set;
import java.util.TreeSet;

/**
 * Date: 08.05.2010
 * Time: 22:06:05
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class PostgreSqlDbService extends DbService {

    @Override
    public void init() {
        Set<String> tables = new TreeSet<String>(
                template.query("select tablename from pg_tables where schemaname='public'", StringMapper.getInstance()));
        if (!tables.contains("container")) {
            template.update("create table container(" +
                    "id serial primary key, " +
                    "name varchar(256), " +
                    "size bigint, " +
                    "saved bool)");
        }
        if (!tables.contains("book")) {
            template.update("create table book(" +
                    "id serial primary key, " +
                    "container int references container(id) on update cascade on delete cascade, " +
                    "sha1 char(40), name varchar(256), size bigint, saved bool)");
        }
        if (!tables.contains("description")) {
            template.update("create table description(" +
                    "id serial primary key, " +
                    "book int references book(id) on update cascade on delete cascade, " +
                    "name varchar(256), value text)");
        }

        Set<String> indexes = new TreeSet<String>(
                template.query("select indexname from pg_indexes where schemaname='public'", StringMapper.getInstance()));
        if (!indexes.contains("container_name_idx")) {
            template.update("create unique index container_name_idx ON container(name)");
        }
        if (!indexes.contains("book_container_idx")) {
            template.update("create index book_container_idx ON book(container)");
        }
        if (!indexes.contains("book_container_name_idx")) {
            template.update("create index book_container_name_idx ON book(container,name)");
        }
        if (!indexes.contains("description_book_idx")) {
            template.update("create index description_book_idx ON description(book)");
        }
        if (!indexes.contains("description_name_idx")) {
            template.update("create index description_name_idx on description(name)");
        }
    }

    @Override
    protected int getGeneratedContainerId(String containerName) {
        return template.queryForInt("select currval(pg_get_serial_sequence('container', 'id'))");
    }

    @Override
    protected int getGeneratedBookId(int container, String name) {
        return template.queryForInt("select currval(pg_get_serial_sequence('book', 'id'));");
    }
}
