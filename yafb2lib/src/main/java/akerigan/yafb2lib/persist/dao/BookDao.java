package akerigan.yafb2lib.persist.dao;

import akerigan.yafb2lib.domain.fb2.Book;

import java.util.List;
import java.util.Collection;

/**
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 21.09.2008
 *         Time: 21:00:21
 */
public interface BookDao {

    public abstract void create(List<Book> books);

    public abstract List<Book> findAll();

    public int addBook(Book book);

    List<Book> getBooksBySequenceName(String sequenceName);
}
