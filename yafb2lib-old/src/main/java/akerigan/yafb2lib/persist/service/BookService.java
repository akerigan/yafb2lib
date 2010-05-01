package akerigan.yafb2lib.persist.service;

import akerigan.yafb2lib.domain.fb2.Book;

import java.util.List;

/**
 * Date: 21.09.2008
 * Time: 21:04:44
 *
 * @author Vlad Vinichenko(akerigan@gmail.com)
 */
public interface BookService {

    public abstract void processBooks(List<Book> books);

    public int addBook(Book book);

    public List<Book> getBooksBySequenceName(String sequenceName);

}
