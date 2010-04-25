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

import java.util.List;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.tutorial.Album;
import com.jgoodies.binding.tutorial.TutorialUtils;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates an elegant way how to connect a master list with a bound 
 * details view. It builds a JList of Albums with an attached details panel 
 * that presents the current Album selection. The details panel's components 
 * are bound to the domain using ValueModels returned by a PresentationModel.<p>
 * 
 * This example does not require a custom ListSelectionListener to handle
 * selection changes in the list of Albums. Instead it uses the SelectionInList 
 * as bean channel for the details PresentationModel. And so, whenever the
 * SelectionInList's selection changes, the details PresentationModel will
 * automatically update the bean used to display the details.<p>
 * 
 * In contrast, the MasterDetailsBoundExample uses a ListSelectionListener
 * to handle selection changes in the Album list. This listener then sets
 * the current selection as the details PresentationModel's new bean.<p>
 * 
 * Another variant of this example is the MasterDetailsDelayedReadExample
 * that deferres selection changes for a specified delay. This can be useful
 * if bean changes in the details PresentationModel require time consuming
 * additional operations, for example a database lookup, a heavy computation,
 * or a remote access.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.14 $
 * 
 * @see com.jgoodies.binding.PresentationModel
 * @see com.jgoodies.binding.tutorial.basics.MasterDetailsBoundExample
 * @see com.jgoodies.binding.tutorial.basics.MasterDetailsDelayedReadExample
 */

public final class MasterDetailsSelectionInListExample {
    
    /**
     * Holds the list of Albums plus a single selection.
     */
    private final SelectionInList albumSelection;

    /**
     * Holds the edited Album and vends ValueModels that adapt Album properties.
     */
    private final PresentationModel detailsModel;

    private JList          albumsList;
    private JTextComponent titleField;
    private JTextComponent artistField;
    private JTextComponent classicalField;
    private JTextComponent composerField;
    private JButton        closeButton;

 
    // Launching **************************************************************
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Binding Tutorial :: Master/Details (SelectionInList)");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent panel = new MasterDetailsSelectionInListExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        TutorialUtils.locateOnOpticalScreenCenter(frame);
        frame.setVisible(true);
    }
    
    
    // Instance Creation ******************************************************
    
    /**
     * Constructs a list editor using a example Album list.
     */
    public MasterDetailsSelectionInListExample() {
        this(Album.ALBUMS);
    }
    
    /**
     * Constructs a list editor for editing the given list of Albums.
     * 
     * @param albums   the list of Albums to edit
     */
    public MasterDetailsSelectionInListExample(List albums) {
        this.albumSelection = new SelectionInList(albums);
        
        // The PresentationModel uses the SelectionInList as bean channel.
        // In other words, the list's selection is the adapter's bean.
        // This aproach ensures that the bean is in the SelectionInList's list.
        // See below for a slightly different approach.
        detailsModel = new PresentationModel(albumSelection);
        
        // The details model uses the SelectionInList's selection holder
        // as bean channel. In some cases the bean may not be contained
        // in the SelectionInList's list - which may be desired or not.
        // detailsModel = new PresentationModel(albumSelection.getSelectionHolder(), true);
    }
    

    // Component Creation and Initialization **********************************

    /**
     *  Creates and intializes the UI components.
     */
    private void initComponents() {
        albumsList = BasicComponentFactory.createList(
                albumSelection,
                TutorialUtils.createAlbumListCellRenderer());
        
        titleField = BasicComponentFactory.createTextField(
                detailsModel.getModel(Album.PROPERTYNAME_TITLE));
        titleField.setEditable(false);
        artistField = BasicComponentFactory.createTextField(
                detailsModel.getModel(Album.PROPERTYNAME_ARTIST));
        artistField.setEditable(false);
        classicalField = BasicComponentFactory.createTextField(
                ConverterFactory.createBooleanToStringConverter(
                        detailsModel.getModel(Album.PROPERTYNAME_CLASSICAL), 
                        "Yes",
                        "No"));
        classicalField.setEditable(false);
        composerField = BasicComponentFactory.createTextField(
                detailsModel.getModel(Album.PROPERTYNAME_COMPOSER));
        composerField.setEditable(false);
        closeButton = new JButton(TutorialUtils.getCloseAction());
    }
    
    
    // Building ***************************************************************

    /**
     * Builds and returns a panel that consists of 
     * a master list and a details form.
     * 
     * @return the built panel
     */
    public JComponent buildPanel() {
        initComponents();

        FormLayout layout = new FormLayout(
                "right:pref, 3dlu, 150dlu:grow",
                "p, 1dlu, p, 9dlu, p, 1dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, p");
                
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        builder.addSeparator("Albums",  cc.xyw(1,  1, 3));
        builder.add(new JScrollPane(
                        albumsList),    cc.xy (3,  3));

        builder.addSeparator("Details", cc.xyw(1,  5, 3));
        builder.addLabel("Artist",      cc.xy (1,  7));
        builder.add(artistField,        cc.xy (3,  7));
        builder.addLabel("Title",       cc.xy (1,  9));
        builder.add(titleField,         cc.xy (3,  9));
        builder.addLabel("Classical",   cc.xy (1, 11));
        builder.add(classicalField,     cc.xy (3, 11));
        builder.addLabel("Composer",    cc.xy (1, 13));
        builder.add(composerField,      cc.xy (3, 13));
        builder.add(buildButtonBar(),   cc.xyw(1, 15, 3));
        
        return builder.getPanel();
    }
    
    
    private JComponent buildButtonBar() {
        return ButtonBarFactory.buildRightAlignedBar(closeButton);
    }
    
    
}
