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

        if (!tables.contains("TEST")) {
            template.update("create table test(id identity primary key, name varchar(255))");
        }
    }

}
