package akerigan.fb2.service;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * Date: 26.04.2010
 * Time: 22:23:56
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public abstract class DbService {

    public SimpleJdbcTemplate template;

    public void setTemplate(SimpleJdbcTemplate template) {
        this.template = template;
    }    

}
