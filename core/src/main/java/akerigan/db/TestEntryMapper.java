package akerigan.db;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Vlad Vinichenko (akerigan@gmail.com)
 * Date: 27.04.2010
 * Time: 22:49:22
 */
public class TestEntryMapper implements RowMapper<TestEntry> {

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
