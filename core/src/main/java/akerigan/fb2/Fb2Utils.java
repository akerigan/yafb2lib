package akerigan.fb2;

import akerigan.StringUtils;
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

    public static List<Map<String, String>> getDescriptions(File zippedFb2File, String encoding) throws IOException, XMLStreamException, NoSuchAlgorithmException {
        return getDescriptions(zippedFb2File, encoding, false);
    }

    public static List<Map<String, String>> getDescriptions(File zippedFb2File, String encoding, boolean addDigest) throws IOException, XMLStreamException, NoSuchAlgorithmException {
        List<Map<String, String>> result = new LinkedList<Map<String, String>>();
        ZipFile zipFile = new ZipFile(zippedFb2File, encoding);
        Enumeration<ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().toLowerCase().endsWith(".fb2")) {
                Map<String, String> description = getDescription(zipFile.getInputStream(entry), addDigest);
                if (description != null) {
                    result.add(description);
                }
            }
        }
        return result;
    }

    public static Map<String, String> getDescription(InputStream fb2InputStream, boolean addDigest) throws XMLStreamException, NoSuchAlgorithmException, IOException {
        if (fb2InputStream != null) {
            Map<String, String> description = new LinkedHashMap<String, String>();
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
                description.put("SHA1", StringUtils.hexencode(digestInputStream.getMessageDigest().digest()));
            } else {
                reader = factory.createXMLEventReader(fb2InputStream);
                fillDescription("", reader, description);
            }
            return description;
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
