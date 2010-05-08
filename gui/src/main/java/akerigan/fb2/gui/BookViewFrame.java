package akerigan.fb2.gui;

import akerigan.fb2.domain.BookInfo;
import akerigan.utils.Fb2Utils;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

/**
 * Date: 17.04.2010
 * Time: 22:54:18
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class BookViewFrame extends JFrame {

    public static final String BOOK_INFO = "BookInfo info :: ";

    private JLabel bookPathLabel;
    private JButton bookSelectButton;
    private JTable bookInfoTable;
    private BookInfoTableModel bookInfoTableModel;

    private static Log log = LogFactory.getLog(BookViewFrame.class);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
            log.error("", e);
        }
        BookViewFrame frame = new BookViewFrame();
        frame.setVisible(true);
    }

    public BookViewFrame() {
        super(BOOK_INFO + "Select file");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(buildPanel());
        pack();
        Dimension paneSize = getSize();
        Dimension screenSize = getToolkit().getScreenSize();
        setLocation((screenSize.width - paneSize.width) / 2,
                (int) ((screenSize.height - paneSize.height) * 0.45));
    }

    private void initComponents() {
        bookPathLabel = new JLabel("Not selected");
        bookSelectButton = new JButton("...");

        bookInfoTableModel = new BookInfoTableModel();
        bookSelectButton.addActionListener(new BookSelectActionListener(this));
        bookInfoTable = new JTable(bookInfoTableModel);
    }

    public JComponent buildPanel() {
        // Separating the component initialization and configuration
        // from the layout code makes both parts easier to read.
        initComponents();

        // Create a FormLayout instance on the given column and row specs.
        // For almost all forms you specify the columns; sometimes rows are
        // created dynamically. In this case the labels are right aligned.
        FormLayout layout = new FormLayout(
                "pref, 3dlu, fill:pref:grow", // cols
                "pref, 3dlu, pref, 3dlu, pref, 3dlu, fill:p:grow");      // rows

        // Create a builder that assists in adding components to the container.
        // Wrap the panel with a standardized border.
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        // Obtain a reusable constraints object to place components in the grid.
        CellConstraints cc = new CellConstraints();

        // Fill the grid with components; the builder offers to create
        // frequently used components, e.g. separators and labels.

        // Add a titled separator to cell (1, 1) that spans 7 columns.
        builder.addSeparator("BookInfo path", cc.xyw(1, 1, 3));
        builder.add(bookSelectButton, cc.xy(1, 3));
        builder.add(bookPathLabel, cc.xy(3, 3));

        builder.addSeparator("BookInfo info", cc.xyw(1, 5, 3));
        builder.add(new JScrollPane(bookInfoTable), cc.xyw(1, 7, 3));

        // The builder holds the layout container that we now return.
        return builder.getPanel();
    }

    private class BookSelectActionListener implements ActionListener {

        private final JFileChooser fileChooser = new JFileChooser();
        private BookViewFrame frame;

        public BookSelectActionListener(BookViewFrame frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent event) {
            //Handle open button action.
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                int rows = bookInfoTableModel.getRowCount();
                if (rows > 0) {
                    bookInfoTableModel.clearAll();
                    bookInfoTableModel.fireTableRowsDeleted(0, rows);
                }
                File file = fileChooser.getSelectedFile();
                frame.setTitle(BOOK_INFO + file.getName());
                bookPathLabel.setText(file.toString());
                //This is where a real application would open the file.
                log.info("Opening: " + file.getName() + ".");
                try {
                    for (BookInfo booksInfo : Fb2Utils.getBooksInfo(file, "IBM-866", true)) {
                        for (Map.Entry<String, String> entry : booksInfo.getDescription().entrySet()) {
                            bookInfoTableModel.addEntry(entry.getKey(), entry.getValue());
                        }
                        bookInfoTableModel.addEntry("----------", "----------");
                        bookInfoTableModel.fireTableRowsInserted(0, Integer.MAX_VALUE);
                    }
                } catch (Exception ex) {
                    log.error("Error reading file", ex);
                }
            } else {
                log.info("Open command cancelled by user.");
            }
        }
    }
}
