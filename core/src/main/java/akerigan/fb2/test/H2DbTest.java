package akerigan.fb2.test;

import akerigan.db.StringMapper;
import akerigan.db.TestEntry;
import akerigan.db.TestEntryMapper;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Date: 26.04.2010
 * Time: 22:44:58
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class H2DbTest {

    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:~/.yafb2lib/db");
        dataSource.setUsername("sa");
        dataSource.setPassword("sa");
        dataSource.setMaxActive(10);
        dataSource.setMinIdle(3);

        SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);

        Set<String> tables = new TreeSet<String>(template.query("select table_name from information_schema.tables", StringMapper.getInstance()));
        System.out.println("tables = " + tables);

        if (!tables.contains("TEST")) {
            template.update("create table test(id identity primary key, name varchar(255))");
        }

        for (String name : Arrays.asList("abc", "def", "ghi")) {
            template.update("insert into test (name) values (?)", name);
        }

        List<TestEntry> entries = template.query("select id, name from test", TestEntryMapper.getInstance());

        System.out.println("entries = " + entries);

        Set<String> indexes = new TreeSet<String>(template.query("select index_name from information_schema.indexes", StringMapper.getInstance()));
        System.out.println("indexes = " + indexes);

    }
}