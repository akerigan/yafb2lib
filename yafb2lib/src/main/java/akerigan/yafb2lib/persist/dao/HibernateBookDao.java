package akerigan.yafb2lib.persist.dao;

import akerigan.yafb2lib.domain.fb2.Book;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;
import java.util.Collection;

/**
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 21.09.2008
 *         Time: 21:01:16
 */
public class HibernateBookDao extends HibernateDaoSupport implements BookDao {

    public void create(List<Book> books) {
        HibernateTemplate ht = getHibernateTemplate();
        for (Book book : books) {
            ht.save(book);
        }
    }

    public List<Book> findAll() {
        HibernateTemplate ht = getHibernateTemplate();
        return ht.find("From Book");
    }

    public int addBook(Book book) {
//        HibernateTemplate ht = getHibernateTemplate();
//        return ht.save(book) != null;
        return 0;
    }

    public List<Book> getBooksBySequenceName(String sequenceName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
