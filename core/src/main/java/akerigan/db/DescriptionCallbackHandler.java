package akerigan.db;

import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 03.05.2010
 * Time: 21:17:58
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class DescriptionCallbackHandler implements RowCallbackHandler {

    private Map<String, String> result = new LinkedHashMap<String, String>();

    public Map<String, String> getResult() {
        return result;
    }

    public void processRow(ResultSet rs) throws SQLException {
        result.put(rs.getString(1), rs.getString(2));
    }
}
