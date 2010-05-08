package akerigan.fb2.gui;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Date: 08.05.2010
 * Time: 10:51:20
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class LibraryFrame extends JFrame {

    public LibraryFrame() {
        super("Yet another fb2 library");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(buildPanel());
        pack();
        Dimension paneSize = getSize();
        Dimension screenSize = getToolkit().getScreenSize();
        setLocation((screenSize.width - paneSize.width) / 2,
                (int) ((screenSize.height - paneSize.height) * 0.45));
    }

    public JComponent buildPanel() {
        initComponents();

        FormLayout layout = new FormLayout(
                "pref, 3dlu, fill:pref:grow", // cols
                "pref, 3dlu, pref, 3dlu, pref, 3dlu, fill:p:grow");      // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();

        return builder.getPanel();
    }

    private void initComponents() {

    }

}
