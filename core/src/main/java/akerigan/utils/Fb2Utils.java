package akerigan.utils;

import akerigan.fb2.domain.Book;
import de.schlichtherle.util.zip.ZipEntry;
import de.schlichtherle.util.zip.ZipFile;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Date: 25.04.2010
 * Time: 20:04:41
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class Fb2Utils {

    private static XMLInputFactory factory = XMLInputFactory.newInstance();

    public static List<Book> getBooksInfo(File zippedFb2File, String encoding) throws IOException, XMLStreamException, NoSuchAlgorithmException {
        return getBooksInfo(zippedFb2File, encoding, false);
    }

    public static List<Book> getBooksInfo(File zippedFb2File, String encoding, boolean addDigest) throws IOException, XMLStreamException, NoSuchAlgorithmException {
        List<Book> result = new LinkedList<Book>();
        ZipFile zipFile = new ZipFile(zippedFb2File, encoding);
        Enumeration<ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().toLowerCase().endsWith(".fb2")) {
                Book book = getBookInfo(zipFile.getInputStream(entry), addDigest);
                if (book != null) {
                    book.setName(entry.getName());
                    book.setSize(entry.getSize());
                    result.add(book);
                }
            }
        }
        zipFile.close();
        return result;
    }

    public static Book getBookInfo(InputStream fb2InputStream, boolean addDigest) throws XMLStreamException, NoSuchAlgorithmException, IOException {
        if (fb2InputStream != null) {
            Book book = new Book();
            Map<String, String> description = new LinkedHashMap<String, String>();
            book.setDescription(description);
            XMLEventReader reader;
            if (addDigest) {
                MessageDigest sha1Hash = MessageDigest.getInstance("SHA1");
                DigestInputStream digestInputStream = new DigestInputStream(new BufferedInputStream(fb2InputStream, 8192), sha1Hash);
                reader = factory.createXMLEventReader(digestInputStream);
                fillDescription("", reader, description);
                int ch = digestInputStream.read();
                while (ch != -1) {
                    ch = digestInputStream.read();
                }
                book.setSha1(StringUtils.hexencode(digestInputStream.getMessageDigest().digest()));
                reader.close();
                digestInputStream.close();
            } else {
                reader = factory.createXMLEventReader(fb2InputStream);
                fillDescription("", reader, description);
                reader.close();
                fb2InputStream.close();
            }
            return book;
        } else {
            return null;
        }
    }

    private static boolean fillDescription(String parentName, XMLEventReader reader, Map<String, String> result) throws XMLStreamException {
        StringBuilder value = null;
        while (true) {
            XMLEvent event = reader.nextEvent();
            if (event == null) {
                break;
            }
            if (event.isStartElement()) {
                StartElement startElement = (StartElement) event;
                QName qName = startElement.getName();
                StringBuilder fullName = new StringBuilder(parentName);
                if (!"FictionBook".equals(qName.getLocalPart()) && !"description".equals(qName.getLocalPart())) {
                    if (parentName.length() > 0) {
                        fullName.append(".");
                    }
                    fullName.append(qName.getLocalPart());
                }
                if ("body".equals(qName.getLocalPart())) {
                    return false;
                }
                Iterator attributes = startElement.getAttributes();
                while (attributes.hasNext()) {
                    Attribute attribute = (Attribute) attributes.next();
                    result.put(fullName.toString() + "@" + attribute.getName().getLocalPart(), attribute.getValue());
                }
                if (startElement.isEndElement() || !fillDescription(fullName.toString(), reader, result)) {
                    return false;
                }
            } else if (event.isCharacters()) {
                Characters characters = (Characters) event;
                if (value == null) {
                    value = new StringBuilder();
                }
                value.append(characters.getData());
            } else if (event.isEndElement()) {
                break;
            }
        }
        if (value != null) {
            String characters = value.toString().trim();
            if (characters != null && characters.length() > 0) {
                if (parentName.startsWith("title-info.author.")) {
                    result.put(parentName, StringUtils.capitalize3(characters.trim(), " "));
                } else {
                    result.put(parentName, characters);
                }
            }
        }
        return true;
    }


}
