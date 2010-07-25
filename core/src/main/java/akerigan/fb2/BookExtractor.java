package akerigan.fb2;

import akerigan.fb2.domain.Book;
import akerigan.fb2.domain.Container;
import akerigan.fb2.service.DbService;
import akerigan.utils.file.FileUtils;
import de.schlichtherle.util.zip.ZipEntry;
import de.schlichtherle.util.zip.ZipFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static akerigan.utils.StringUtils.capitalize;
import static akerigan.utils.StringUtils.trim;
import static akerigan.utils.file.FileUtils.makeDirs;

/**
 * Date: 27.05.2010
 * Time: 21:12:54
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class BookExtractor {

/*
    public static final String[] SEARCH_PATTERNS = new String[]{
            "%ивадный%", "%space%", "sf_humor", "%антастический боевик%", "%tale%", "%егионер%"
    };
*/

    public static final String[] SEARCH_PATTERNS = new String[]{
            "Абвалов", "Басов"
    };

    private static Log log = LogFactory.getLog(LibraryIndexer.class);

    public static void main(String[] args) {
        AppProperties appProperties = null;
        try {
            appProperties = new AppProperties();
        } catch (IOException e) {
            log.error("Cant initialize application properties", e);
        }
        if (appProperties != null) {
            ApplicationContext ctx = new ClassPathXmlApplicationContext("/appContext.xml");
            DbService service = (DbService) ctx.getBean("dbService");
            for (String searchPattern : SEARCH_PATTERNS) {
                extractBooks(searchPattern, appProperties, service);
            }
        }
    }

    public static void extractBooks(String searchPattern, AppProperties appProperties, DbService service) {
        File workDir = appProperties.getWorkDir();
        File destDir = new File(workDir, searchPattern);
        makeDirs(destDir);
        log.info("Get book by pattern: " + searchPattern);
        List<Book> books = service.getBooksByDescriptionPattern(searchPattern);
        log.info("Get descriptions by pattern: " + searchPattern);
        Map<Integer, Map<String, String>> booksDescription = service.getBooksDescription(searchPattern);
        Map<Integer, Set<Book>> containerBooks = new HashMap<Integer, Set<Book>>();
        for (Book book : books) {
            Map<String, String> description = booksDescription.get(book.getId());
            if (description == null) {
                description = service.getDescription(book.getId());
                booksDescription.put(book.getId(), description);
            }
            log.info("Book found: " + description.get("title-info.author.first-name")
                    + " " + description.get("title-info.author.last-name") + "\""
                    + description.get("title-info.book-title") + "\"");
            Set<Book> booksEntries = containerBooks.get(book.getContainer());
            if (booksEntries == null) {
                booksEntries = new HashSet<Book>();
                containerBooks.put(book.getContainer(), booksEntries);
            }
            booksEntries.add(book);
        }
        for (Integer containerId : containerBooks.keySet()) {
            Container container = service.getContainer(containerId);
            Map<String, Book> booksByName = new HashMap<String, Book>();
            for (Book book : containerBooks.get(containerId)) {
                booksByName.put(book.getName(), book);
            }
            for (File baseDir : appProperties.getBaseDirs()) {
                File containerFile = new File(baseDir, container.getName());
                if (containerFile.exists()) {
                    log.info("Container found: " + containerFile.toString());
                    try {
                        ZipFile zippedFb2 = new ZipFile(containerFile, "IBM-866");
                        Enumeration<ZipEntry> entries = zippedFb2.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            Book book = booksByName.get(entry.getName());
                            if (book != null) {
                                Map<String, String> description = booksDescription.get(book.getId());
                                String firstName = capitalize(trim(description.get("title-info.author.first-name")));
                                String lastName = capitalize(trim(description.get("title-info.author.last-name")));
                                StringBuilder builder = new StringBuilder();
                                if (lastName != null) {
                                    builder.append(lastName);
                                }
                                if (firstName != null) {
                                    builder.append(" ");
                                    builder.append(firstName);
                                }
                                String author = trim(builder.toString());
                                if (author == null) {
                                    author = "Не задан";
                                }
                                File authorDir = new File(destDir, author.substring(0, 1) + "/" + author);
                                makeDirs(authorDir);
                                File newContainer = new File(authorDir, book.getSha1() + ".zip");
                                FileUtils.copyToZip(zippedFb2.getInputStream(entry),
                                        newContainer, "book.fb2");
                                log.info("Book copied: " + newContainer.toString());
                            }
                        }
                    } catch (IOException e) {
                        log.error("Error while reading container: " + container);
                    }
                }
            }
        }
    }

}
