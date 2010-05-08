package akerigan.db;

import akerigan.fb2.domain.BooksContainer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 06.05.2010
 * Time: 21:44:24
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class BooksContainerMapper implements RowMapper<BooksContainer> {

    private static BooksContainerMapper instance = new BooksContainerMapper();

    private BooksContainerMapper() {
    }

    public static BooksContainerMapper getInstance() {
        return instance;
    }

    public BooksContainer mapRow(ResultSet rs, int rowNum) throws SQLException {
        BooksContainer container = new BooksContainer();
        container.setId(rs.getInt("id"));
        container.setName(rs.getString("name"));
        container.setSize(rs.getLong("size"));
        container.setSaved(rs.getBoolean("saved"));
        return container;
    }
}
