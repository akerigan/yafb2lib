package akerigan.db;

import akerigan.fb2.domain.BookInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 06.05.2010
 * Time: 22:03:43
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class BookInfoMapper implements RowMapper<BookInfo> {

    private static BookInfoMapper instance = new BookInfoMapper();

    private BookInfoMapper() {
    }

    public static BookInfoMapper getInstance() {
        return instance;
    }

    public BookInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        BookInfo result = new BookInfo();
        result.setId(rs.getInt("id"));
        result.setContainer(rs.getInt("container"));
        result.setSha1(rs.getString("sha1"));
        result.setName(rs.getString("name"));
        result.setSize(rs.getLong("size"));
        return result;
    }
}
