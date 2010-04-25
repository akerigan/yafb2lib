package akerigan.yafb2lib.persist.service;

import akerigan.yafb2lib.domain.fb2.Book;
import akerigan.yafb2lib.persist.dao.BookDao;

import java.util.List;
import java.util.Collection;

/**
 * @author Влад Виниченко (akerigan@gmail.com)
 *         Date: 21.09.2008
 *         Time: 21:05:50
 */
public class BookServiceImpl implements BookService {

    private BookDao bookDao;

    public BookDao getBookDao() {
        return bookDao;
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void processBooks(List<Book> books) {
        bookDao.create(books);
        List<Book> list = bookDao.findAll();
        System.out.println("The saved books are --> " + list);
    }

    public int addBook(Book book) {
        return bookDao.addBook(book);
    }

    public List<Book> getBooksBySequenceName(String sequenceName) {
        return bookDao.getBooksBySequenceName(sequenceName);
    }
}
