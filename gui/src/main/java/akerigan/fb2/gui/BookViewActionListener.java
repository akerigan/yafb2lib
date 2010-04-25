package akerigan.fb2.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Date: 18.04.2010
 * Time: 18:11:50
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class BookViewActionListener implements ActionListener {

    private Component parent;
    private BookInfoTableModel tableModel;
    private final JFileChooser fileChooser = new JFileChooser();
    private Log log = LogFactory.getLog(getClass());
    private XMLInputFactory factory = XMLInputFactory.newInstance();

    public BookViewActionListener(Component parent, BookInfoTableModel tableModel) {
        this.parent = parent;
        this.tableModel = tableModel;
    }

    public void actionPerformed(ActionEvent event) {
        //Handle open button action.
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //This is where a real application would open the file.
            log.info("Opening: " + file.getName() + ".");
            try {
                ZipFile zipFile = new ZipFile(fileChooser.getSelectedFile());
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = entries.nextElement();
                    log.info("Extracting: " + zipEntry.getName());
                    XMLEventReader reader = factory.createXMLEventReader(zipFile.getInputStream(zipEntry));
                    fillModel("", reader);
                    tableModel.fireTableRowsInserted(0, Integer.MAX_VALUE);
                }
            } catch (Exception ex) {
                log.error("Error reading file", ex);
            }

        } else {
            log.info("Open command cancelled by user.");
        }
    }

    private boolean fillModel(String parentName, XMLEventReader reader) throws XMLStreamException {
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
                if (!"FictionBook".equals(qName.getLocalPart())) {
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
                    tableModel.addEntry(fullName.toString() + "@" + attribute.getName().getLocalPart(), attribute.getValue());
                }
                if (startElement.isEndElement() || !fillModel(fullName.toString(), reader)) {
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
                tableModel.addEntry(parentName, characters);
            }
        }
        return true;
    }

}
