package akerigan;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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

        template.update("create table test(id identity primary key, name varchar(255))");

        for (String name : Arrays.asList("abc", "def", "ghi")) {
            template.update("insert into test (name) values (?)", name);
        }

        List<TestEntry> entries = template.query("select id, name from test", TestEntryMapper.getInstance());

        System.out.println("entries = " + entries);
    }
}

class TestEntry {
    private int id;
    private String name;

    TestEntry() {
    }

    public TestEntry(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestEntry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

class TestEntryMapper implements RowMapper<TestEntry> {

    private static TestEntryMapper instance = new TestEntryMapper();

    private TestEntryMapper() {
    }

    public static TestEntryMapper getInstance() {
        return instance;
    }

    public TestEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        TestEntry result = new TestEntry();
        result.setId(rs.getInt(1));
        result.setName(rs.getString(2));
        return result;
    }
}

