package akerigan.utils;

import akerigan.fb2.domain.BookInfo;
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

    public static List<BookInfo> getBooksInfo(File zippedFb2File, String encoding) throws IOException, XMLStreamException, NoSuchAlgorithmException {
        return getBooksInfo(zippedFb2File, encoding, false);
    }

    public static List<BookInfo> getBooksInfo(File zippedFb2File, String encoding, boolean addDigest) throws IOException, XMLStreamException, NoSuchAlgorithmException {
        List<BookInfo> result = new LinkedList<BookInfo>();
        ZipFile zipFile = new ZipFile(zippedFb2File, encoding);
        Enumeration<ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().toLowerCase().endsWith(".fb2")) {
                BookInfo bookInfo = getBookInfo(zipFile.getInputStream(entry), addDigest);
                if (bookInfo != null) {
                    bookInfo.setName(entry.getName());
                    bookInfo.setSize(entry.getSize());
                }
            }
        }
        return result;
    }

    public static BookInfo getBookInfo(InputStream fb2InputStream, boolean addDigest) throws XMLStreamException, NoSuchAlgorithmException, IOException {
        if (fb2InputStream != null) {
            BookInfo bookInfo = new BookInfo();
            Map<String, String> description = new LinkedHashMap<String, String>();
            bookInfo.setDescription(description);
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
                bookInfo.setSha1(StringUtils.hexencode(digestInputStream.getMessageDigest().digest()));
            } else {
                reader = factory.createXMLEventReader(fb2InputStream);
                fillDescription("", reader, description);
            }
            return bookInfo;
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
                result.put(parentName, characters);
            }
        }
        return true;
    }


}
