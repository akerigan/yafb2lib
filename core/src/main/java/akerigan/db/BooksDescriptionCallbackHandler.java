package akerigan.db;

import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 03.05.2010
 * Time: 21:17:58
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class BooksDescriptionCallbackHandler implements RowCallbackHandler {

    private Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String, String>>();

    public Map<Integer, Map<String, String>> getResult() {
        return result;
    }

    public void processRow(ResultSet rs) throws SQLException {
        int book = rs.getInt(1);
        Map<String, String> descriptions = result.get(book);
        if (descriptions == null) {
            descriptions = new LinkedHashMap<String, String>();
            result.put(book, descriptions);
        }
        descriptions.put(rs.getString(2), rs.getString(3));
    }
}