package akerigan.fb2;

import akerigan.fb2.service.DbService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Date: 08.05.2010
 * Time: 13:29:25
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class ServiceTest {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/appContext.xml");
        DbService service = (DbService) ctx.getBean("dbService");

        List<String> authorsLastNames = service.getAuthorsLastNames();
        System.out.println("authorsLastNames = " + authorsLastNames);
    }
}
