package akerigan.fb2;

import akerigan.fb2.domain.Book;
import akerigan.utils.Fb2Utils;
import de.schlichtherle.util.zip.ZipEntry;
import de.schlichtherle.util.zip.ZipFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Date: 08.05.2010
 * Time: 14:44:27
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class Fb2ContainerIterator implements Iterator<Book> {

    private ZipFile zipFile;
    private Enumeration<ZipEntry> entries;
    private ZipEntry currentEntry;

    private Log log = LogFactory.getLog(getClass());

    public Fb2ContainerIterator(File zippedFb2File, String encoding) throws IOException {
        zipFile = new ZipFile(zippedFb2File, encoding);
        entries = zipFile.entries();
        nextEntry();
    }

    private void nextEntry() {
        currentEntry = null;
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().toLowerCase().endsWith(".fb2")) {
                currentEntry = entry;
                break;
            }
        }
    }

    public boolean hasNext() {
        return currentEntry != null;
    }

    public Book next() {
        Book book = null;
        try {
            book = Fb2Utils.getBookInfo(zipFile.getInputStream(currentEntry), true);
        } catch (Exception e) {
            log.error("Cant fetch book", e);
        }
        if (book != null) {
            book.setName(currentEntry.getName());
            book.setSize(currentEntry.getSize());
        }
        nextEntry();
        return book;
    }

    public void remove() {
        //do nothing
    }

    public void close() throws IOException {
        zipFile.close();
    }

    public String getEntryName() {
        if (currentEntry != null) {
            return currentEntry.getName();
        } else {
            return null;
        }
    }

    public long getEntrySize() {
        if (currentEntry != null) {
            return currentEntry.getSize();
        } else {
            return -1;
        }
    }
}
