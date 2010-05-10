package akerigan.db;

import akerigan.fb2.domain.Container;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 06.05.2010
 * Time: 21:44:24
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class ContainerMapper implements RowMapper<Container> {

    private static ContainerMapper instance = new ContainerMapper();

    private ContainerMapper() {
    }

    public static ContainerMapper getInstance() {
        return instance;
    }

    public Container mapRow(ResultSet rs, int rowNum) throws SQLException {
        Container container = new Container();
        container.setId(rs.getInt("id"));
        container.setName(rs.getString("name"));
        container.setSize(rs.getLong("size"));
        container.setSaved(rs.getBoolean("saved"));
        return container;
    }
}
