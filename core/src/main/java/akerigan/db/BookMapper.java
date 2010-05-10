package akerigan.db;

import akerigan.fb2.domain.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 06.05.2010
 * Time: 22:03:43
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class BookMapper implements RowMapper<Book> {

    private static BookMapper instance = new BookMapper();

    private BookMapper() {
    }

    public static BookMapper getInstance() {
        return instance;
    }

    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book result = new Book();
        result.setId(rs.getInt("id"));
        result.setContainer(rs.getInt("container"));
        result.setSha1(rs.getString("sha1"));
        result.setName(rs.getString("name"));
        result.setSize(rs.getLong("size"));
        result.setSaved(rs.getBoolean("saved"));
        return result;
    }
}
