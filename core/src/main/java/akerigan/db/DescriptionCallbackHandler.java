package akerigan.db;

import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Date: 03.05.2010
 * Time: 21:17:58
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class DescriptionCallbackHandler implements RowCallbackHandler {

    private Map<String, Integer> result = new TreeMap<String, Integer>();

    public Map<String, Integer> getResult() {
        return result;
    }

    public void processRow(ResultSet rs) throws SQLException {
        result.put(rs.getString(1), rs.getInt(2));
    }
}
