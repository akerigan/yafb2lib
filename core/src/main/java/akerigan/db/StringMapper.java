package akerigan.db;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Vlad Vinichenko (akerigan@gmail.com)
 * Date: 27.04.2010
 * Time: 22:55:45
 */
public class StringMapper implements RowMapper<String> {

    private static StringMapper instance = new StringMapper();

    public StringMapper() {
    }

    public static StringMapper getInstance() {
        return instance;
    }

    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString(1);
    }
}
