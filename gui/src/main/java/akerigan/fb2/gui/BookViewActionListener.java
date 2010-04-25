package akerigan.fb2.gui;

import akerigan.fb2.Fb2Utils;
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
import java.util.Map;

import de.schlichtherle.util.zip.ZipFile;
import de.schlichtherle.util.zip.ZipEntry;

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
                for (Map<String, String> description: Fb2Utils.getDescriptions(file, "windows-1251", true)) {
                    for (Map.Entry<String, String> entry : description.entrySet()) {
                        tableModel.addEntry(entry.getKey(), entry.getValue());
                        tableModel.fireTableRowsInserted(0, Integer.MAX_VALUE);
                    }
                }
            } catch (Exception ex) {
                log.error("Error reading file", ex);
            }

        } else {
            log.info("Open command cancelled by user.");
        }
    }

}
