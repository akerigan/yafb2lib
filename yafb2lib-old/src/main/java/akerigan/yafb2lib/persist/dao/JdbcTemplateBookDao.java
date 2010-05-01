package akerigan.yafb2lib.persist.dao;

import akerigan.yafb2lib.domain.fb2.*;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Date: 24.09.2008
 * Time: 21:17:03
 *
 * @author Vlad Vinichenko(akerigan@gmail.com)
 */
public class JdbcTemplateBookDao implements BookDao {

    SimpleJdbcTemplate jdbcTemplate;

    private static final String AUTHORS_COUNT =
            "select count(*) from authors where firstname=? and middlename=? and lastname=? and nickname=?";
    private static final String AUTHOR_SELECT =
            "select id from authors where firstname=? and middlename=? and lastname=? and nickname=?";
    private static final String AUTHOR_INSERT =
            "insert into authors (firstname, middlename, lastname, nickname, fb2_id) values (?,?,?,?,?)";
    private static final String AUTHOR_HOMEPAGES_INSERT =
            "insert into author_homepages (author_id, homepage) values (?,?)";
    private static final String AUTHOR_EMAILS_INSERT =
            "insert into author_emails (author_id, email) values (?,?)";

    private static final String TEXTFIELDS_COUNT =
            "select count(*) from textfields where lang=? and value=?";
    private static final String TEXTFIELD_SELECT =
            "select id from textfields where lang=? and value=?";
    private static final String TEXTFIELD_INSERT =
            "insert into textfields (lang, value) values (?,?)";

    private static final String BOOK_AUTHOR_TITLE_COUNT =
            "select count(b.id) from books b, books_authors ba where b.title=? and ba.author_id=? and ba.book_id = b.id";
    private static final String BOOK_INSERT =
            "insert into books (title, annotation, keywords, date, lang, src_lang, filename) values (?,?,?,?,?,?,?)";
    private static final String BOOK_AUTHOR_TITLE_SELECT =
            "select b.id from books b, books_authors ba where b.title=? and ba.author_id=? and ba.book_id = b.id";
    private static final String BOOK_GENRE_INSERT =
            "insert into books_genres (book_id, genre_id) values (?,?)";
    private static final String BOOK_AUTHOR_INSERT =
            "insert into books_authors (book_id, author_id) values (?,?)";
    private static final String BOOK_TRANSLATOR_INSERT =
            "insert into books_authors (book_id, author_id) values (?,?)";
    private static final String BOOK_SEQUENCE_INSERT =
            "insert into books_sequences (book_id, sequence_id) values (?,?)";

    private static final String BOOK_PUBLISH_COUNT =
            "select count(*) from books_publish where book_id=? and book_name=? and publisher=?";
    private static final String BOOK_PUBLISH_SELECT =
            "select id from books_publish where book_id=? and book_name=? and publisher=?";
    private static final String BOOK_PUBLISH_INSERT =
            "insert into books_publish (book_id, book_name, publisher, city, year, isbn) values (?,?,?,?,?,?)";


    private static final String GENRE_COUNT =
            "select count(*) from genres where genre=?";
    private static final String GENRE_SELECT =
            "select id from genres where genre=?";
    private static final String GENRE_INSERT =
            "insert into genres (genre) values (?)";

    private static final String SEQUENCE_COUNT =
            "select count(*) from sequences where name=? and number=? and lang=? and type=?";
    private static final String SEQUENCE_SELECT =
            "select id from sequences where name=? and number=? and lang=? and type=?";
    private static final String SEQUENCE_INSERT =
            "insert into sequences (name, number, lang, type) values (?,?,?,?)";

    private static final String BOOKS_BY_SEQUENCE_NAME =
            "select b.id, b.filename, b.annotation from books b, books_sequences bs, sequences s where s.name=? and bs.sequence_id=s.id and bs.book_id = b.id";


    public SimpleJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(List<Book> books) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Book> findAll() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int addBook(final Book book) {
        int id = 0;
        if (book != null) {
            Description description = book.getDescription();
            if (description != null) {
                final TitleInfo titleInfo = description.getTitleInfo();
                if (titleInfo != null) {
                    final int bookTitle = addTextField(titleInfo.getBookTitle());
                    if (bookTitle != 0) {
                        titleInfo.getBookTitle().setId(bookTitle);
                    }
                    int count = 0;
                    for (Author author : titleInfo.getAuthors()) {
                        if (author.getId() == 0) {
                            author.setId(addAuthor(author));
                        }
                        int count2 = jdbcTemplate.queryForInt(BOOK_AUTHOR_TITLE_COUNT, bookTitle, author.getId());
                        count += count2;
                        if (count2 > 0) {
                            id = jdbcTemplate.queryForInt(BOOK_AUTHOR_TITLE_SELECT, bookTitle, author.getId());
                        }
                    }
                    if (count == 0) {
                        final int keywords = addTextField(titleInfo.getKeywords());
                        if (keywords != 0) {
                            titleInfo.getKeywords().setId(keywords);
                        }
                        final int date = addTextField(titleInfo.getDate());
                        if (date != 0) {
                            titleInfo.getDate().setId(date);
                        }
                        KeyHolder keyHolder = new GeneratedKeyHolder();
                        jdbcTemplate.getJdbcOperations().update(
                                new PreparedStatementCreator() {
                                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                                        PreparedStatement ps = con.prepareStatement(BOOK_INSERT, new String[]{"id"});
                                        ps.setInt(1, bookTitle);
                                        ps.setString(2, titleInfo.getAnnotation2());
                                        ps.setInt(3, keywords);
                                        ps.setInt(4, date);
                                        ps.setString(5, titleInfo.getLanguage());
                                        ps.setString(6, titleInfo.getSourceLanguage());
                                        ps.setString(7, book.getFileName());
                                        return ps;
                                    }
                                }, keyHolder
                        );
                        id = keyHolder.getKey().intValue();
                        for (String genre : titleInfo.getGenres()) {
                            int genreId = addGenre(genre);
                            jdbcTemplate.update(BOOK_GENRE_INSERT, id, genreId);
                        }
                        for (Author author : titleInfo.getAuthors()) {
                            jdbcTemplate.update(BOOK_AUTHOR_INSERT, id, author.getId());
                        }
                        for (Author author : titleInfo.getTranslators()) {
                            int authorId = addAuthor(author);
                            jdbcTemplate.update(BOOK_TRANSLATOR_INSERT, id, authorId);
                        }
                        for (Sequence sequence : titleInfo.getSequences()) {
                            int sequenceId = addSequence(sequence, 1);
                            jdbcTemplate.update(BOOK_SEQUENCE_INSERT, id, sequenceId);
                        }
                    } else {

                    }
                }
                final PublishInfo publishInfo = description.getPublishInfo();
                if (id != 0) {
                    int publish = addPublishInfo(id, publishInfo);
                    if (publish != 0) {
                        for (Sequence sequence : publishInfo.getSequences()) {
                            int sequenceId = addSequence(sequence, 2);
                            jdbcTemplate.update(BOOK_SEQUENCE_INSERT, id, sequenceId);
                        }
                    }
                }
            }
        }
        return id;
    }

    private int addAuthor(final Author author) {
        int id = 0;
        if (author != null) {

            final int firstName = addTextField(author.getFirstName());
            if (firstName != 0) {
                author.getFirstName().setId(firstName);
            }
            final int middleName = addTextField(author.getMiddleName());
            if (middleName != 0) {
                author.getMiddleName().setId(middleName);
            }
            final int lastName = addTextField(author.getLastName());
            if (lastName != 0) {
                author.getLastName().setId(lastName);
            }
            final int nickName = addTextField(author.getNickName());
            if (nickName != 0) {
                author.getNickName().setId(nickName);
            }
            if (author.getFb2Id() == null) {
                author.setFb2Id("");
            } else {
                author.setFb2Id(author.getFb2Id().trim());
            }

            int count = jdbcTemplate.queryForInt(AUTHORS_COUNT, firstName, middleName, lastName, nickName);
            if (count > 0) {
                id = jdbcTemplate.queryForInt(AUTHOR_SELECT, firstName, middleName, lastName, nickName);
            }
            if (id == 0) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.getJdbcOperations().update(
                        new PreparedStatementCreator() {
                            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                                PreparedStatement ps = con.prepareStatement(AUTHOR_INSERT, new String[]{"id"});
                                ps.setInt(1, firstName);
                                ps.setInt(2, middleName);
                                ps.setInt(3, lastName);
                                ps.setInt(4, nickName);
                                ps.setString(5, author.getFb2Id());
                                return ps;
                            }
                        }, keyHolder
                );
                id = keyHolder.getKey().intValue();
                for (String homepage : author.getHomePages()) {
                    jdbcTemplate.update(AUTHOR_HOMEPAGES_INSERT, id, homepage);
                }
                for (String email : author.getEmails()) {
                    jdbcTemplate.update(AUTHOR_EMAILS_INSERT, id, email);
                }
            }
        }
        return id;
    }

    private int addTextField(final TextField textField) {
        int id = 0;
        if (textField != null) {
            if (textField.getLang() == null) {
                textField.setLang("");
            } else {
                textField.setLang(textField.getLang().trim());
            }
            if (textField.getValue() == null) {
                textField.setValue("");
            } else {
                textField.setValue(textField.getValue().trim());
            }
            if (!"".equals(textField.getLang()) || !"".equals(textField.getValue())) {
                int count = jdbcTemplate.queryForInt(TEXTFIELDS_COUNT, textField.getLang(), textField.getValue());
                if (count > 0) {
                    id = jdbcTemplate.queryForInt(TEXTFIELD_SELECT, textField.getLang(), textField.getValue());
                }
                if (id == 0) {
                    KeyHolder keyHolder = new GeneratedKeyHolder();
                    jdbcTemplate.getJdbcOperations().update(
                            new PreparedStatementCreator() {
                                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                                    PreparedStatement ps = con.prepareStatement(TEXTFIELD_INSERT, new String[]{"id"});
                                    ps.setString(1, textField.getLang());
                                    ps.setString(2, textField.getValue());
                                    return ps;
                                }
                            }, keyHolder
                    );
                    id = keyHolder.getKey().intValue();
                }
            }
        }
        return id;
    }

    private int addSequence(final Sequence sequence, final int type) {
        int id = 0;
        if (sequence != null) {
            if (sequence.getName() == null) {
                sequence.setName("");
            } else {
                sequence.setName(sequence.getName().trim());
            }
            if (sequence.getLanguage() == null) {
                sequence.setLanguage("");
            } else {
                sequence.setLanguage(sequence.getLanguage().trim());
            }
            int count = jdbcTemplate.queryForInt(SEQUENCE_COUNT, sequence.getName(), sequence.getNumber(), sequence.getLanguage(), type);
            if (count > 0) {
                id = jdbcTemplate.queryForInt(SEQUENCE_SELECT, sequence.getName(), sequence.getNumber(), sequence.getLanguage(), type);
            }
            if (id == 0) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.getJdbcOperations().update(
                        new PreparedStatementCreator() {
                            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                                PreparedStatement ps = con.prepareStatement(SEQUENCE_INSERT, new String[]{"id"});
                                ps.setString(1, sequence.getName());
                                ps.setInt(2, sequence.getNumber());
                                ps.setString(3, sequence.getLanguage());
                                ps.setInt(4, type);
                                return ps;
                            }
                        }, keyHolder
                );
                id = keyHolder.getKey().intValue();
            }
        }
        return id;
    }

    private int addGenre(final String genre) {
        int id = 0;
        if (genre != null) {
            int count = jdbcTemplate.queryForInt(GENRE_COUNT, genre.trim());
            if (count > 0) {
                id = jdbcTemplate.queryForInt(GENRE_SELECT, genre.trim());
            }
            if (id == 0) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.getJdbcOperations().update(
                        new PreparedStatementCreator() {
                            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                                PreparedStatement ps = con.prepareStatement(GENRE_INSERT, new String[]{"id"});
                                ps.setString(1, genre.trim());
                                return ps;
                            }
                        }, keyHolder
                );
                id = keyHolder.getKey().intValue();
            }
        }
        return id;
    }

    private int addPublishInfo(final int bookId, final PublishInfo publishInfo) {
        int id = 0;
        if (publishInfo != null) {
            final int bookName = addTextField(publishInfo.getBookName());
            final int city = addTextField(publishInfo.getCity());
            final int isbn = addTextField(publishInfo.getIsbn());
            final int publisher = addTextField(publishInfo.getPublisher());
            if (publishInfo.getYear() == null) {
                publishInfo.setYear("");
            } else {
                publishInfo.setYear(publishInfo.getYear().trim());
            }
            int count = jdbcTemplate.queryForInt(BOOK_PUBLISH_COUNT, bookId, bookName, publisher);
            if (count > 0) {
                id = jdbcTemplate.queryForInt(BOOK_PUBLISH_SELECT, bookId, bookName, publisher);
            }
            if (id == 0) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.getJdbcOperations().update(
                        new PreparedStatementCreator() {
                            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                                PreparedStatement ps = con.prepareStatement(BOOK_PUBLISH_INSERT, new String[]{"id"});
                                ps.setInt(1, bookId);
                                ps.setInt(2, bookName);
                                ps.setInt(3, publisher);
                                ps.setInt(4, city);
                                ps.setString(5, publishInfo.getYear());
                                ps.setInt(6, isbn);
                                return ps;
                            }
                        }, keyHolder
                );
                id = keyHolder.getKey().intValue();
            }
        }
        return id;
    }

    public List<Book> getBooksBySequenceName(String sequenceName) {
        return jdbcTemplate.query(
                BOOKS_BY_SEQUENCE_NAME,
                new ParameterizedRowMapper<Book>() {
                    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Book book = new Book();
                        book.setId(rs.getInt(1));
                        book.setFileName(rs.getString(2));
                        book.getDescription().getTitleInfo().setAnnotation2(rs.getString(3));
                        return book;
                    }
                },
                sequenceName);
    }
}
