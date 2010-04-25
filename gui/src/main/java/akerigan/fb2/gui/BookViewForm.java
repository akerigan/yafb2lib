package akerigan.fb2.gui;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Date: 17.04.2010
 * Time: 22:54:18
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class BookViewForm {

    private JTextField bookPathField;
    private JButton bookSelectButton;
    private JTable bookInfoTable;
    private BookInfoTableModel bookInfoTableModel;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Book info :: Select file");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        JComponent panel = new BookViewForm().buildPanel(frame);
        frame.getContentPane().add(panel);
        frame.pack();

        Dimension paneSize = frame.getSize();
        Dimension screenSize = frame.getToolkit().getScreenSize();
        frame.setLocation(
                (screenSize.width - paneSize.width) / 2,
                (int) ((screenSize.height - paneSize.height) * 0.45));

        frame.setVisible(true);
    }

    private void initComponents(Component parent) {
        bookPathField = new JTextField(40);
        bookSelectButton = new JButton("...");

        bookInfoTableModel = new BookInfoTableModel();
        bookSelectButton.addActionListener(new BookViewActionListener(parent, bookInfoTableModel));
        bookInfoTable = new JTable(bookInfoTableModel);
    }

    public JComponent buildPanel(Component parent) {
        // Separating the component initialization and configuration
        // from the layout code makes both parts easier to read.
        initComponents(parent);

        // Create a FormLayout instance on the given column and row specs.
        // For almost all forms you specify the columns; sometimes rows are
        // created dynamically. In this case the labels are right aligned.
        FormLayout layout = new FormLayout(
                "pref, 3dlu, pref, fill:pref:grow", // cols
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
        builder.addSeparator("Book path", cc.xyw(1, 1, 4));
        builder.add(bookPathField, cc.xy(1, 3));
        builder.add(bookSelectButton, cc.xy(3, 3));

        builder.addSeparator("Book info", cc.xyw(1, 5, 4));
        builder.add(new JScrollPane(bookInfoTable), cc.xyw(1, 7, 4));

        // The builder holds the layout container that we now return.
        return builder.getPanel();
    }
}
