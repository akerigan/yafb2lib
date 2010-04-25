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

package com.jgoodies.binding.tutorial;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.TableModel;

import com.jgoodies.binding.adapter.AbstractTableAdapter;


/**
 * Consists only of static methods that return instances
 * reused in multiple examples of the JGoodies Binding tutorial.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.17 $
 */
public final class TutorialUtils {
    
   
    private TutorialUtils() { 
        // Suppresses default constructor, ensuring non-instantiability.
    }
    
    
    /**
     * Creates and returns a renderer for Albums in a list.
     *  
     * @return a renderer for Albums in lists.
     */
    public static ListCellRenderer createAlbumListCellRenderer() {
        return new AlbumListCellRenderer();
    }
    
    
    /**
     * Creates and returns a TableModel for Albums with columns
     * for the title, artist, classical and composer.
     *  
     * @param listModel   the ListModel of Albums to display in the table
     * @return a TableModel on the list of Albums
     */
    public static TableModel createAlbumTableModel(ListModel listModel) {
        return new AlbumTableModel(listModel);
    }
    
    
    /**
     * Returns a listener that writes bean property changes to the console.
     * The log entry includes the PropertyChangeEvent's source, property name, 
     * old value, and new value.
     * 
     * @return a debug listener that logs bean changes to the console
     */
    public static PropertyChangeListener createDebugPropertyChangeListener() {
        PropertyChangeListener listener = new DebugPropertyChangeListener();
        debugListeners.add(listener);
        return listener;
    }
    
    
    /**
     * Creates and returns an Action that exists the system if performed.
     *  
     * @return an Action that exists the system if performed
     * 
     * @see System#exit(int)
     */
    public static Action getCloseAction() {
        return new CloseAction();
    }
    
    
    /**
     * Locates the given component on the screen's center.
     * 
     * @param component   the component to be centered
     */
    public static void locateOnOpticalScreenCenter(Component component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation(
            (screenSize.width  - paneSize.width)  / 2,
            (int) ((screenSize.height - paneSize.height) *0.45));
    }


    // Renderer ***************************************************************

    /**
     * Used to renders Albums in JLists and JComboBoxes. If the combobox 
     * selection is null, an empty text <code>""</code> is rendered.
     */
    private static final class AlbumListCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);

            Album album = (Album) value;
            setText(album == null ? "" : (" " + album.getTitle()));
            return component;
        }
    }
    
    
    // TableModel *************************************************************
    
    /**
     * Describes how to present an Album in a JTable.
     */
    private static final class AlbumTableModel extends AbstractTableAdapter {
        
        private static final String[] COLUMNS = {"Artist", "Title", "Classical", "Composer"};
        
        private AlbumTableModel(ListModel listModel) {
            super(listModel, COLUMNS);
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            Album album = (Album) getRow(rowIndex);
            switch (columnIndex) {
                case 0 : return album.getArtist();
                case 1 : return album.getTitle();
                case 2 : return Boolean.valueOf(album.isClassical());
                case 3 : return album.isClassical() ? album.getComposer() : "";
                default :
                    throw new IllegalStateException("Unknown column");
            }
        }
        
    }
    
    
    // Debug Listener *********************************************************
    
    /**
     * Used to hold debug listeners, so they won't be removed by 
     * the garbage collector, even if registered by a listener list 
     * that is based on weak references.
     * 
     * @see #createDebugPropertyChangeListener()
     * @see WeakReference
     */ 
    private static List debugListeners = new LinkedList();
    
    
    /**
     * Writes the source, property name, old/new value to the system console.
     */
    private static final class DebugPropertyChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println();
            System.out.println("The source: " + evt.getSource());
            System.out.println(
                    "changed '" + evt.getPropertyName() 
                  + "' from '" + evt.getOldValue() 
                  + "' to '" + evt.getNewValue() + "'.");
        }
    }
    
    
    // Actions ****************************************************************
    
    /**
     * An Action that exists the System.
     */
    private static final class CloseAction extends AbstractAction {
        
        private CloseAction() {
            super("Close");
        }
        
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    
}
