/*
 * Copyright (c) 2002-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jgoodies.binding.tutorial.basics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.tutorial.Album;
import com.jgoodies.binding.tutorial.TutorialUtils;
import com.jgoodies.binding.value.*;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates how to bind different value types to Swing components.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.18 $
 * 
 * @see com.jgoodies.binding.adapter.BasicComponentFactory
 * @see com.jgoodies.binding.adapter.Bindings
 */

public final class ComponentsExample {

    // Holds an ExampleBean and vends ValueModels that adapt its properties.
    private final ExamplePresentationModel presentationModel;

    // Text Components
    private JTextField     textField;
    private JTextArea      textArea;
    private JPasswordField passwordField;
    private JLabel         textLabel;

    // Formatted Input
    private JFormattedTextField dateField;
    private JFormattedTextField integerField;
    private JFormattedTextField longField;

    // Lists
    private JComboBox comboBox;
    private JList     list;
    private JTable    table;

    // Choice
    private JRadioButton leftIntRadio;
    private JRadioButton centerIntRadio;
    private JRadioButton rightIntRadio;
    private JComboBox    alignmentIntCombo;
    private JRadioButton leftObjectRadio;
    private JRadioButton centerObjectRadio;
    private JRadioButton rightObjectRadio;
    private JComboBox    alignmentObjectCombo;

    // Misc
    private JCheckBox     checkBox;
    private JPanel        colorPreview;
    private JSlider       slider;
    private JLabel        floatLabel;
    private JSpinner      spinner;

    
    // Launching **************************************************************

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Binding Tutorial :: Components");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ComponentsExample example = new ComponentsExample();
        JComponent panel = example.buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        TutorialUtils.locateOnOpticalScreenCenter(frame);
        frame.setVisible(true);
    }
    

    // Instance Creation ******************************************************

    /**
     * Constructs the 'Components' example on an instance of ExampleBean.
     */
    public ComponentsExample() {
        presentationModel = new ExamplePresentationModel(new ExampleBean());
    }
    

    // Component Creation and Initialization **********************************

    /**
     * Creates, binds and configures the UI components.<p>
     * 
     * If possible, the components are created using the BasicComponentFactory,
     * or the Bindings class.
     */
    private void initComponents() {
        // Text Components
        textField = BasicComponentFactory.createTextField(
                presentationModel.getModel(ExampleBean.PROPERTYNAME_TEXT));
        textArea = BasicComponentFactory.createTextArea(
                presentationModel.getModel(ExampleBean.PROPERTYNAME_TEXT));
        passwordField = BasicComponentFactory.createPasswordField(
                presentationModel.getModel(ExampleBean.PROPERTYNAME_TEXT));
        textLabel = BasicComponentFactory.createLabel(presentationModel
                .getModel(ExampleBean.PROPERTYNAME_TEXT));

        // Formatted Input
        dateField = BasicComponentFactory.createDateField(
                presentationModel.getModel(ExampleBean.PROPERTYNAME_DATE));
        integerField = BasicComponentFactory.createIntegerField(
                presentationModel.getModel(ExampleBean.PROPERTYNAME_INT_VALUE));
        longField = BasicComponentFactory.createLongField(
                presentationModel.getModel(ExampleBean.PROPERTYNAME_LONG_VALUE));

        // Choice
        ValueModel intChoiceModel = presentationModel.getModel(ExampleBean.PROPERTYNAME_INT_CHOICE);
        leftIntRadio = BasicComponentFactory.createRadioButton(
                intChoiceModel, ExampleBean.LEFT_INTEGER, "Left");
        centerIntRadio = BasicComponentFactory.createRadioButton(
                intChoiceModel, ExampleBean.CENTER_INTEGER, "Center");
        rightIntRadio = BasicComponentFactory.createRadioButton(
                intChoiceModel, ExampleBean.RIGHT_INTEGER, "Right");
        alignmentIntCombo = BasicComponentFactory.createComboBox(
                new SelectionInList(ExampleBean.INTEGER_CHOICES, intChoiceModel));
        
        ValueModel objectChoiceModel = presentationModel.getModel(ExampleBean.PROPERTYNAME_OBJECT_CHOICE);
        leftObjectRadio = BasicComponentFactory.createRadioButton(
                objectChoiceModel, ExampleBean.LEFT, "Left");
        centerObjectRadio = BasicComponentFactory.createRadioButton(
                objectChoiceModel, ExampleBean.CENTER, "Center");
        rightObjectRadio = BasicComponentFactory.createRadioButton(
                objectChoiceModel, ExampleBean.RIGHT, "Right");
        alignmentObjectCombo = BasicComponentFactory.createComboBox(
                new SelectionInList(ExampleBean.OBJECT_CHOICES, objectChoiceModel));
        
      
        // Lists
        comboBox = BasicComponentFactory.createComboBox(
                presentationModel.getSelectionInList(),
                TutorialUtils.createAlbumListCellRenderer());
        
        list = BasicComponentFactory.createList(
                presentationModel.getSelectionInList(),
                TutorialUtils.createAlbumListCellRenderer());
        
        table = new JTable();
        table.setModel(TutorialUtils.createAlbumTableModel(
                presentationModel.getSelectionInList()));
        table.setSelectionModel(new SingleListSelectionAdapter(
                presentationModel.getSelectionInList().getSelectionIndexHolder()));
        
        // Misc
        checkBox = BasicComponentFactory.createCheckBox(
                presentationModel.getModel(ExampleBean.PROPERTYNAME_BOOLEAN_VALUE),
                "available");
        colorPreview = new JPanel();
        colorPreview.setBorder(new LineBorder(Color.GRAY));
        updatePreviewPanel();
        
        ValueModel floatModel = presentationModel.getModel(ExampleBean.PROPERTYNAME_FLOAT_VALUE);
        slider = new JSlider();
        slider.setModel(new BoundedRangeAdapter(
                ConverterFactory.createFloatToIntegerConverter(floatModel, 100),
                0, 0, 100));
        floatLabel = BasicComponentFactory.createLabel(
                ConverterFactory.createStringConverter(
                        floatModel,
                        NumberFormat.getPercentInstance()));
        spinner = new JSpinner();
        spinner.setModel(SpinnerAdapterFactory.createNumberAdapter(
                presentationModel.getModel(ExampleBean.PROPERTYNAME_INT_LIMITED),
                0,   // defaultValue
                0,   // minValue
                100, // maxValue
                5)); // step
    }
    
    private void initEventHandling() {
        presentationModel.getModel(ExampleBean.PROPERTYNAME_COLOR).addValueChangeListener(
                new ColorUpdateHandler());
    }
    
    private void updatePreviewPanel() {
        colorPreview.setBackground(((ExampleBean) presentationModel.getBean()).getColor());
    }
    

    // Building ***************************************************************

    /**
     * Builds and returns the panel.
     * 
     * @return the built panel
     */
    public JComponent buildPanel() {
        initComponents();
        initEventHandling();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.addTab("Text",      buildTextPanel());
        tabbedPane.addTab("Formatted", buildFormattedPanel());
        tabbedPane.addTab("Choices",   buildChoicesPanel());
        tabbedPane.addTab("List",      buildListPanel());
        tabbedPane.addTab("Misc",      buildMiscPanel());
        return tabbedPane;
    }

    private JPanel buildTextPanel() {
        FormLayout layout = new FormLayout(
                "right:max(50dlu;pref), 3dlu, 50dlu",
                "p, 3dlu, p, 3dlu, p, 14dlu, 3dlu, p");
        layout.setRowGroups(new int[][]{{1, 3, 5}});

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addLabel("JTextField",          cc.xy  (1, 1));
        builder.add(textField,                  cc.xy  (3, 1));
        builder.addLabel("JPasswordField",      cc.xy  (1, 3));
        builder.add(passwordField,              cc.xy  (3, 3));
        builder.addLabel("JTextArea",           cc.xy  (1, 5));
        builder.add(new JScrollPane(textArea),  cc.xywh(3, 5, 1, 2));
        builder.addLabel("JLabel",              cc.xy  (1, 8));
        builder.add(textLabel,                  cc.xy  (3, 8));
        return builder.getPanel();
    }

    private JPanel buildFormattedPanel() {
        FormLayout layout = new FormLayout(
                "right:max(50dlu;pref), 3dlu, 50dlu", 
                "p, 3dlu, p, 3dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addLabel("Date",                cc.xy(1, 1));
        builder.add(dateField,                  cc.xy(3, 1));
        builder.addLabel("Integer",             cc.xy(1, 3));
        builder.add(integerField,               cc.xy(3, 3));
        builder.addLabel("Long",                cc.xy(1, 5));
        builder.add(longField,                  cc.xy(3, 5));
        return builder.getPanel();
    }
    
    private JPanel buildChoicesPanel() {
        FormLayout layout = new FormLayout(
                "right:max(50dlu;pref), 3dlu, p, 6dlu, p, 6dlu, p, 0:grow", 
                "p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, p, 3dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addSeparator("Integer Choice",  cc.xyw(1,  1, 8));
        builder.addLabel("JRadioButton",        cc.xy (1,  3));
        builder.add(leftIntRadio,               cc.xy (3,  3));
        builder.add(centerIntRadio,             cc.xy (5,  3));
        builder.add(rightIntRadio,              cc.xy (7,  3));
        builder.addLabel("JComboBox",           cc.xy (1,  5));
        builder.add(alignmentIntCombo,          cc.xyw(3,  5, 3));
        
        builder.addSeparator("Object Choice",   cc.xyw(1,  7, 8));
        builder.addLabel("JRadioButton",        cc.xy (1,  9));
        builder.add(leftObjectRadio,            cc.xy (3,  9));
        builder.add(centerObjectRadio,          cc.xy (5,  9));
        builder.add(rightObjectRadio,           cc.xy (7,  9));
        builder.addLabel("JComboBox",           cc.xy (1, 11));
        builder.add(alignmentObjectCombo,       cc.xyw(3, 11, 3));
        return builder.getPanel();
    }
    
    private JPanel buildListPanel() {
        FormLayout layout = new FormLayout(
                "right:max(50dlu;pref), 3dlu, 150dlu", 
                "fill:60dlu, 6dlu, fill:60dlu, 6dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addLabel("JList",               cc.xy(1, 1, "right, top"));
        builder.add(new JScrollPane(list),      cc.xy(3, 1));
        builder.addLabel("JTable",              cc.xy(1, 3, "right, top"));
        builder.add(new JScrollPane(table),     cc.xy(3, 3));
        builder.addLabel("JComboBox",           cc.xy(1, 5));
        builder.add(comboBox,                   cc.xy(3, 5));
        return builder.getPanel();
    }
    
    private JPanel buildMiscPanel() {
        FormLayout layout = new FormLayout(
                "right:max(50dlu;pref), 3dlu, 50dlu, 3dlu, 50dlu", 
                "p, 3dlu, p, 3dlu, p, 3dlu, p");
        layout.setRowGroups(new int[][]{{1, 3, 5}});

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        Action chooseAction = new ChooseColorAction(
                builder.getPanel(), 
                presentationModel.getModel(ExampleBean.PROPERTYNAME_COLOR));
        
        CellConstraints cc = new CellConstraints();
        builder.addLabel("JCheckBox",          cc.xy(1, 1));
        builder.add(checkBox,                  cc.xy(3, 1));
        builder.addLabel("JSlider",            cc.xy(1, 3));
        builder.add(slider,                    cc.xy(3, 3));
        builder.add(floatLabel,                cc.xy(5, 3));
        builder.addLabel("JSpinner",           cc.xy(1, 5));
        builder.add(spinner,                   cc.xy(3, 5));
        builder.addLabel("JColorChooser",      cc.xy(1, 7));
        builder.add(colorPreview,              cc.xy(3, 7, "fill, fill"));
        builder.add(new JButton(chooseAction), cc.xy(5, 7, "left, center"));
        return builder.getPanel();
    }
    
    
    // Helper Code ************************************************************
    
    public static final class ExampleBean extends Model {

        // Names of the Bound Bean Properties *************************************

        public static final String PROPERTYNAME_BOOLEAN_VALUE  = "booleanValue";
        public static final String PROPERTYNAME_COLOR          = "color";
        public static final String PROPERTYNAME_DATE           = "date";
        public static final String PROPERTYNAME_FLOAT_VALUE    = "floatValue";
        public static final String PROPERTYNAME_INT_CHOICE     = "intChoice";
        public static final String PROPERTYNAME_INT_LIMITED    = "intLimited";
        public static final String PROPERTYNAME_INT_VALUE      = "intValue";
        public static final String PROPERTYNAME_LIST_SELECTION = "listSelection";
        public static final String PROPERTYNAME_LONG_VALUE     = "longValue";
        public static final String PROPERTYNAME_OBJECT_CHOICE  = "objectChoice";
        public static final String PROPERTYNAME_TEXT           = "text";

        
        // Constants **************************************************************

        // An int based enumeration.
        public static final Integer LEFT_INTEGER   = new Integer(0);
        public static final Integer CENTER_INTEGER = new Integer(1);
        public static final Integer RIGHT_INTEGER  = new Integer(2);
               static final Integer[] INTEGER_CHOICES = 
            { LEFT_INTEGER, CENTER_INTEGER, RIGHT_INTEGER };

        // An object based enumeration (using an enum from the JGoodies Forms)
        public static final Object LEFT   = ColumnSpec.LEFT;
        public static final Object CENTER = ColumnSpec.CENTER;
        public static final Object RIGHT  = ColumnSpec.RIGHT;
               static final Object[] OBJECT_CHOICES = 
            { LEFT, CENTER, RIGHT };
               
               
        private static final int NO_DATE = -1;

        
        // Fields *****************************************************************

        private boolean booleanValue;
        private Color color;
        private long  date;
        private float floatValue;
        private int intChoice;
        private int intLimited; // for a spinner
        private int intValue;
        private long longValue;
        private Object objectChoice;
        private String text;
        private ObservableList listModel;
        private Object listSelection;

        
        // Instance Creation ******************************************************
        
        public ExampleBean() {
            booleanValue = true;
            color = Color.WHITE;
            date = new GregorianCalendar(1967, 11, 5).getTime().getTime();
            floatValue = 0.5f;
            intChoice = LEFT_INTEGER.intValue();
            intLimited = 15;
            intValue = 42;
            longValue = 42L;
            objectChoice = LEFT;
            text = "Text";
            listModel = new ArrayListModel();
            listModel.addAll(Album.ALBUMS);
            listSelection = listModel.get(0);
        }

        
        // Accessors **************************************************************

        public boolean getBooleanValue() {
            return booleanValue;
        }

        public void setBooleanValue(boolean newBooleanValue) {
            boolean oldBooleanValue = getBooleanValue();
            booleanValue = newBooleanValue;
            firePropertyChange(PROPERTYNAME_BOOLEAN_VALUE, oldBooleanValue,
                    newBooleanValue);
        }
        
        
        public Color getColor() {
            return color;
        }
        
        
        public void setColor(Color newColor) {
            Color oldColor = getColor();
            color = newColor;
            firePropertyChange(PROPERTYNAME_COLOR, oldColor, newColor);
        }

        
        public Date getDate() {
            return date == NO_DATE ? null : new Date(date);
        }

        public void setDate(Date newDate) {
            Date oldDate = getDate();
            date = newDate == null ? NO_DATE : newDate.getTime();
            firePropertyChange(PROPERTYNAME_DATE, oldDate, newDate);
        }


        public float getFloatValue() {
            return floatValue;
        }

        public void setFloatValue(float newFloatValue) {
            float oldFloatValue = getFloatValue();
            floatValue = newFloatValue;
            firePropertyChange(PROPERTYNAME_FLOAT_VALUE, oldFloatValue, newFloatValue);
        }

        
        public int getIntChoice() {
            return intChoice;
        }

        public void setIntChoice(int newIntChoice) {
            int oldIntChoice = getIntChoice();
            intChoice = newIntChoice;
            firePropertyChange(PROPERTYNAME_INT_CHOICE, oldIntChoice,
                    newIntChoice);
        }

        
        public int getIntLimited() {
            return intLimited;
        }

        public void setIntLimited(int newIntLimited) {
            int oldIntLimited = getIntLimited();
            intLimited = newIntLimited;
            firePropertyChange(PROPERTYNAME_INT_LIMITED, oldIntLimited, newIntLimited);
        }

        
        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int newIntValue) {
            int oldIntValue = getIntValue();
            intValue = newIntValue;
            firePropertyChange(PROPERTYNAME_INT_VALUE, oldIntValue, newIntValue);
        }

        
        public long getLongValue() {
            return longValue;
        }

        public void setLongValue(long newLongValue) {
            long oldLongValue = getLongValue();
            longValue = newLongValue;
            firePropertyChange(PROPERTYNAME_LONG_VALUE, oldLongValue,
                    newLongValue);
        }

        
        public Object getObjectChoice() {
            return objectChoice;
        }

        public void setObjectChoice(Object newObjectChoice) {
            Object oldObjectChoice = getObjectChoice();
            objectChoice = newObjectChoice;
            firePropertyChange(PROPERTYNAME_OBJECT_CHOICE, oldObjectChoice,
                    newObjectChoice);
        }
        
        
        public String getText() {
            return text;
        }

        public void setText(String newText) {
            String oldText = getText();
            text = newText;
            firePropertyChange(PROPERTYNAME_TEXT, oldText, newText);
        }
        
        
        public ListModel getListModel() {
            return listModel;
        }
        
        
        public Object getListSelection() {
            return listSelection;
        }
        
        public void setListSelection(Object newListSelection) {
            Object oldListSelection = getListSelection();
            listSelection = newListSelection;
            firePropertyChange(PROPERTYNAME_LIST_SELECTION, oldListSelection, newListSelection);
        }

    }
    
    
    // A custom PresentationModel that provides a SelectionInList
    // for the bean's ListModel and the bean's list selection.
    private static final class ExamplePresentationModel extends PresentationModel {
        
        /**
         * Holds the bean's list model plus a selection.
         */
        private final SelectionInList selectionInList;
        
        // Instance Creation -----------------------------------------
        
        private ExamplePresentationModel(ExampleBean exampleBean) {
            super(exampleBean);
            selectionInList = new SelectionInList(
                    exampleBean.getListModel(),
                    getModel(ExampleBean.PROPERTYNAME_LIST_SELECTION));
        }
        
        
        // Custom Models ---------------------------------------------
        
        public SelectionInList getSelectionInList() {
            return selectionInList;
        }
        
    }
    
    private final class ColorUpdateHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            updatePreviewPanel();
        }
    }
    
    private static final class ChooseColorAction extends AbstractAction {
        
        private final Component parent;
        private final ValueModel bufferedColorModel;
        private final Trigger    trigger;
        
        private ChooseColorAction(Component parent, ValueModel colorModel) {
            super("\u2026");
            this.parent = parent;
            this.trigger = new Trigger();
            this.bufferedColorModel = new BufferedValueModel(colorModel, trigger);
        }
        
        public void actionPerformed(ActionEvent e) {
            JColorChooser colorChooser = BasicComponentFactory.createColorChooser(bufferedColorModel);
            ActionListener okHandler = new OKHandler(trigger);
            JDialog dialog = JColorChooser.createDialog(parent, "Choose Color", true, colorChooser, okHandler, null);
            dialog.addWindowListener(new Closer());
            dialog.addComponentListener(new DisposeOnClose());

            dialog.setVisible(true); // blocks until user brings dialog down...
        }
        
        private static final class Closer extends WindowAdapter implements Serializable {
            public void windowClosing(WindowEvent e) {
                Window w = e.getWindow();
                w.setVisible(false);
            }
        }

        private static final class DisposeOnClose extends ComponentAdapter implements Serializable {
            public void componentHidden(ComponentEvent e) {
                Window w = (Window) e.getComponent();
                w.dispose();
            }
        }
        
        private static final class OKHandler implements ActionListener {
            private final Trigger trigger;
            private OKHandler(Trigger trigger) {
                this.trigger = trigger;
            }
            public void actionPerformed(ActionEvent e) {
                trigger.triggerCommit();
            }
        }
        
    }
    
    

}
