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

package com.jgoodies.binding.tests;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import junit.framework.TestCase;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.extras.SelectionInListModel;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.LinkedListModel;
import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.tests.event.ListDataReport;
import com.jgoodies.binding.tests.event.PropertyChangeReport;
import com.jgoodies.binding.tests.value.ValueHolderWithOldValueNull;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

/**
 * A test case for class {@link SelectionInListModel}.
 * 
 * @author Karsten Lentzsch
 * @author Jeanette Winzenburg
 * @version $Revision: 1.8 $
 */
public final class SelectionInListModelTest extends TestCase {
    
    private static final Object[] AN_ARRAY = { "one", "two", "three" };
    
    private DefaultListModel listModel;

    
    // Initialization *********************************************************
    
    protected void setUp() throws Exception {
        super.setUp();
        listModel = createListModel(AN_ARRAY);
    }
    

    // Testing Setup **********************************************************
    
    public void testRejectNullSelectionIndex() {
        ValueHolder indexHolder = new ValueHolder(0);
        SelectionInListModel sil = new SelectionInListModel(listModel);
        sil.setSelectionIndexHolder(indexHolder);
        try {
            indexHolder.setValue(null);
            fail("The SelectionInListModel must reject null selection index values.");
        } catch (Exception e) {
            // The expected behavior.
        }
    }
    
    public void testRejectNewNullSelectionHolder() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        try {
            sil.setSelectionHolder(null);
            fail("The SelectionInListModel must reject a null selection holder.");
        } catch (Exception e) {
            // The expected behavior.
        }
    }
    
    public void testRejectNewNullSelectionIndexHolder() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        try {
            sil.setSelectionIndexHolder(null);
            fail("The SelectionInListModel must reject a null selection index holder.");
        } catch (Exception e) {
            // The expected behavior.
        }
    }
    
    public void testRejectNewSelectionIndexHolderWithNullValue() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        try {
            sil.setSelectionIndexHolder(new ValueHolder(null));
            fail("The SelectionInListModel must reject selection index holder with null values.");
        } catch (Exception e) {
            // The expected behavior.
        }
    }
    
    public void testSelectionIndexAfterInitialization() {
        Object value = "two";
        ValueModel selectionHolder = new ValueHolder(value);
        SelectionInListModel sil = new SelectionInListModel(listModel, selectionHolder);
        int initialSelectionIndex = sil.getSelectionIndex();
        assertEquals(
                "The initial selection index reflects the index of the selection holder's initial value.",
                listModel.indexOf(value), 
                initialSelectionIndex);
        selectionHolder.setValue(null);
        assertEquals(
                "The selection index has been updated to indicate no selection.",
                -1, 
                sil.getSelectionIndex());
    }
    
    
    public void testDefaultSelectionHolderChecksIdentity() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        // The following line will fail if the default selection holder
        // doesn't check the identity.
        BeanAdapter adapter = new BeanAdapter(sil.getSelectionHolder());
        assertEquals("The BeanAdapter's bean is the selection.",
                sil.getSelection(),
                adapter.getBean());
    }
    
    
    // Testing Bean Spec Compliance *******************************************
    
    public void testAcceptsNullOldValueInSelectionIndexPropertyChangeEvent() {
        int selectionIndex = 0;
        ValueHolder indexHolder = new ValueHolder(selectionIndex);
        SelectionInListModel sil = new SelectionInListModel(listModel);
        sil.setSelectionIndexHolder(indexHolder);
        indexHolder.fireValueChange(null, new Integer(selectionIndex));
        assertEquals("Selection index", 
                selectionIndex,
                sil.getSelectionIndex());
        Object selection = listModel.get(selectionIndex);
        assertEquals("Selection",
                selection,
                sil.getSelection());
    }
    
    public void testAcceptsNullNewValueInSelectionIndexPropertyChangeEvent() {
        int selectionIndex = 0;
        ValueHolder indexHolder = new ValueHolder(selectionIndex);
        SelectionInListModel sil = new SelectionInListModel(listModel);
        sil.setSelectionIndexHolder(indexHolder);
        indexHolder.fireValueChange(new Integer(selectionIndex), null);
        assertEquals("Selection index", 
                selectionIndex,
                sil.getSelectionIndex());
        Object selection = listModel.get(selectionIndex);
        assertEquals("Selection",
                selection,
                sil.getSelection());
    }
    
    public void testAcceptsNullOldAndNewValueInSelectionIndexPropertyChangeEvent() {
        int selectionIndex = 0;
        ValueHolder indexHolder = new ValueHolder(selectionIndex);
        SelectionInListModel sil = new SelectionInListModel(listModel);
        sil.setSelectionIndexHolder(indexHolder);
        indexHolder.fireValueChange(null, null);
        assertEquals("Selection index", 
                selectionIndex,
                sil.getSelectionIndex());
        Object selection = listModel.get(selectionIndex);
        assertEquals("Selection",
                selection,
                sil.getSelection());
    }
    
    
    // ************************************************************************
    
    public void testFiresSelectionChangeOnlyForSelectionChanges() {
        int selectionIndex = 0;
        ValueHolder indexHolder = new ValueHolder(selectionIndex);
        SelectionInListModel sil = new SelectionInListModel(listModel);
        sil.setSelectionIndexHolder(indexHolder);
        
        // Create change reports.
        PropertyChangeReport selectionReport      = new PropertyChangeReport();
        PropertyChangeReport selectionEmptyReport = new PropertyChangeReport();
        PropertyChangeReport selectionIndexReport = new PropertyChangeReport();
        
        // Register change reports for selection, selection index,
        // and selectionEmpty
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION, selectionReport);
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION_EMPTY, selectionEmptyReport);
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION_INDEX, selectionIndexReport);
        
        indexHolder.fireValueChange(null, new Integer(selectionIndex));
        indexHolder.fireValueChange(new Integer(selectionIndex), null);
        indexHolder.fireValueChange(null, null);
        assertEquals("No selection change event fired",
                0,
                selectionReport.eventCount());    
        assertEquals("No selectionEmpty change event fired",
                0,
                selectionEmptyReport.eventCount());    
        assertEquals("No selectionIndex change event fired",
                0,
                selectionIndexReport.eventCount());    
    }
    
    
    public void testIndexChangeFiresChangesWithNonNullOldValue() {
        int initialSelectionIndex = 0;
        int newSelectionIndex = 1;
        AbstractValueModel indexHolder = new ValueHolderWithOldValueNull(initialSelectionIndex);
        SelectionInListModel sil = new SelectionInListModel(listModel);
        sil.setSelectionIndexHolder(indexHolder);
        
        // Create change reports.
        PropertyChangeReport selectionReport      = new PropertyChangeReport();
        PropertyChangeReport selectionIndexReport = new PropertyChangeReport();
        
        // Register change reports for selection, selection index,
        // and selectionEmpty
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION, selectionReport);
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION_INDEX, selectionIndexReport);

        // We change the selection index holder's value to the new index.
        // The ValueModel used for the selectionIndexHolder fires no old value.
        indexHolder.setValue(newSelectionIndex);
        Object oldSelection      = selectionReport.lastEvent().getOldValue();
        Object oldSelectionIndex = selectionIndexReport.lastEvent().getOldValue();
        assertTrue("Non-null old value in selection change event",
                oldSelection != null);    
        assertTrue("Non-null old value in selectionIndex change event",
                oldSelectionIndex != null);    
    }
    
    
    public void testSelectionForDirectSelectionIndexChanges() {
        SelectionInListModel sil = new SelectionInListModel(listModel);

        // Create change reports.
        PropertyChangeReport selectionReport      = new PropertyChangeReport();
        PropertyChangeReport selectionEmptyReport = new PropertyChangeReport();
        PropertyChangeReport selectionIndexReport = new PropertyChangeReport();
        
        // Register change reports for selection, selection index,
        // and selectionEmpty
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION, selectionReport);
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION_EMPTY, selectionEmptyReport);
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION_INDEX, selectionIndexReport);
        
        assertEquals("The initial selection is null.",
                null,
                sil.getSelection());
        assertTrue("The initial selection is empty.",
                sil.isSelectionEmpty());
        assertEquals("The initial selection index is -1.",
                -1,
                sil.getSelectionIndex());
        
        sil.setSelectionIndex(0);
        assertEquals("The new selection is the first list element.",
                listModel.getElementAt(0),
                sil.getSelection());
        assertFalse("The selection is not empty.",
                sil.isSelectionEmpty());
        assertEquals("The new selection index is 0.",
                0,
                sil.getSelectionIndex());
        
        assertEquals("selectionEmpty changed from true to false.",
                1,
                selectionEmptyReport.eventCount());    
        assertTrue("1) selectionEmpty change event oldValue == true.",
                selectionEmptyReport.lastOldBooleanValue());    
        assertFalse("1) selectionEmpty change event newValue == false.",
                selectionEmptyReport.lastNewBooleanValue());    

        sil.setSelectionIndex(1);
        assertFalse("The selection index is 1 and not empty.",
                sil.isSelectionEmpty());
        
        assertEquals("No selectionEmpty change event fired",
                1,
                selectionEmptyReport.eventCount());    

        sil.clearSelection();
        assertTrue("The selection index is empty.",
                sil.isSelectionEmpty());
        
        assertEquals("selectionEmpty changed from false to true.",
                2,
                selectionEmptyReport.eventCount());    
        assertFalse("2) selectionEmpty change event oldValue == false.",
                selectionEmptyReport.lastOldBooleanValue());    
        assertTrue("2) selectionEmpty change event newValue == true.",
                selectionEmptyReport.lastNewBooleanValue());    

    }
    
    
    public void testSelectionForIndirectSelectionIndexChanges() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        ValueHolder selectionIndexHolder = new ValueHolder(-1);
        sil.setSelectionIndexHolder(selectionIndexHolder);
        PropertyChangeReport selectionEmptyReport = new PropertyChangeReport();
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION_EMPTY, selectionEmptyReport);

        assertTrue("The initial selection is empty.",
                sil.isSelectionEmpty());
        
        selectionIndexHolder.setValue(0);
        assertFalse("The selection index is 0 and not empty.",
                sil.isSelectionEmpty());
        
        assertEquals("selectionEmpty changed from true to false.",
                1,
                selectionEmptyReport.eventCount());    
        assertTrue("1) selectionEmpty change event oldValue == true.",
                selectionEmptyReport.lastOldBooleanValue());    
        assertFalse("1) selectionEmpty change event newValue == false.",
                selectionEmptyReport.lastNewBooleanValue());    

        selectionIndexHolder.setValue(1);
        assertFalse("The selection index is 1 and not empty.",
                sil.isSelectionEmpty());
        
        assertEquals("No selectionEmpty change event fired",
                1,
                selectionEmptyReport.eventCount());    

        selectionIndexHolder.setValue(-1);
        assertTrue("The selection index is empty.",
                sil.isSelectionEmpty());
        
        assertEquals("selectionEmpty changed from false to true.",
                2,
                selectionEmptyReport.eventCount());    
        assertFalse("2) selectionEmpty change event oldValue == false.",
                selectionEmptyReport.lastOldBooleanValue());    
        assertTrue("2) selectionEmpty change event newValue == true.",
                selectionEmptyReport.lastNewBooleanValue());    

    }
    
    
    public void testSelectionForDirectSelectionChanges() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        PropertyChangeReport selectionEmptyReport = new PropertyChangeReport();
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION_EMPTY, selectionEmptyReport);

        sil.setSelection(listModel.getElementAt(0));
        assertFalse("The selection index is 0 and not empty.",
                sil.isSelectionEmpty());
        
        assertEquals("selectionEmpty changed from true to false.",
                1,
                selectionEmptyReport.eventCount());    
        assertTrue("1) selectionEmpty change event oldValue == true.",
                selectionEmptyReport.lastOldBooleanValue());    
        assertFalse("1) selectionEmpty change event newValue == false.",
                selectionEmptyReport.lastNewBooleanValue());    

        sil.setSelection(listModel.getElementAt(1));
        assertFalse("The selection index is 1 and not empty.",
                sil.isSelectionEmpty());
        
        assertEquals("No selectionEmpty change event fired",
                1,
                selectionEmptyReport.eventCount());    

        sil.setSelection(null);
        assertTrue("The selection index is empty.",
                sil.isSelectionEmpty());
        
        assertEquals("selectionEmpty changed from false to true.",
                2,
                selectionEmptyReport.eventCount());    
        assertFalse("2) selectionEmpty change event oldValue == false.",
                selectionEmptyReport.lastOldBooleanValue());    
        assertTrue("2) selectionEmpty change event newValue == true.",
                selectionEmptyReport.lastNewBooleanValue());    

    }
    
    public void testSelectionForIndirectSelectionChanges() {
        ValueModel selectionHolder = new ValueHolder();
        SelectionInListModel sil = new SelectionInListModel(new ValueHolder(listModel, true), selectionHolder);
        PropertyChangeReport selectionEmptyReport = new PropertyChangeReport();
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION_EMPTY, selectionEmptyReport);

        assertTrue("The selection index is -1 and empty.",
                sil.isSelectionEmpty());
        selectionHolder.setValue(listModel.getElementAt(0));
        assertFalse("The selection index is 0 and not empty.",
                sil.isSelectionEmpty());
        
        assertEquals("selectionEmpty changed from true to false.",
                1,
                selectionEmptyReport.eventCount());    
        assertTrue("1) selectionEmpty change event oldValue == true.",
                selectionEmptyReport.lastOldBooleanValue());    
        assertFalse("1) selectionEmpty change event newValue == false.",
                selectionEmptyReport.lastNewBooleanValue());    

        selectionHolder.setValue(listModel.getElementAt(1));
        assertFalse("The selection index is 1 and not empty.",
                sil.isSelectionEmpty());
        
        assertEquals("No selectionEmpty change event fired",
                1,
                selectionEmptyReport.eventCount());    

        selectionHolder.setValue(null);
        assertTrue("The selection index is empty.",
                sil.isSelectionEmpty());
        
        assertEquals("selectionEmpty changed from false to true.",
                2,
                selectionEmptyReport.eventCount());    
        assertFalse("2) selectionEmpty change event oldValue == false.",
                selectionEmptyReport.lastOldBooleanValue());    
        assertTrue("2) selectionEmpty change event newValue == true.",
                selectionEmptyReport.lastNewBooleanValue());    

    }
    

    // Selection In Synch With the Selection Index After List Operations ******
    
    public void testSelectionReflectsIndexAfterClear() {
        ValueModel selectionHolder = new ValueHolder();
        SelectionInListModel sil = new SelectionInListModel(new ValueHolder(listModel, true), selectionHolder);
        sil.setSelectionIndex(1);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION, changeReport);
        
        assertEquals("SelectionHolder holds the second list element.",
                listModel.get(1),
                selectionHolder.getValue());    

        listModel.clear();
        assertEquals("The selection index is -1.",
                -1,
                sil.getSelectionIndex());    
        assertEquals("The selection is null.",
                null,
                sil.getSelection());    
        assertEquals("The selection holder value is null.",
                null,
                sil.getSelectionHolder().getValue());
        assertEquals("A selection change event has been fired.",
                1,
                changeReport.eventCount());
        
        // If this event provides an old value, it should be the old value.
        if (changeReport.lastOldValue() != null) {
            assertEquals("The selection change's old value is 'two'.",
                    "two",
                    changeReport.lastOldValue());    
        }
        assertEquals("The selection change new value is null.",
                null,
                changeReport.lastNewValue());    
    }
    
    
    public void testSelectionReflectsIndexAfterIndexMoveBack() {
        ValueModel selectionHolder = new ValueHolder();
        SelectionInListModel sil = new SelectionInListModel(new ValueHolder(listModel, true), selectionHolder);
        sil.setSelectionIndex(1);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION, changeReport);
        
        Object initialSelection = listModel.get(1);
        assertEquals("SelectionHolder holds the second list element.",
                initialSelection,
                selectionHolder.getValue());    

        listModel.remove(0);
        Object expectedSelection = listModel.get(0);
        assertEquals("The selection index has been decreased by 1.",
                0,
                sil.getSelectionIndex());    
        assertEquals("The selection remains unchanged.",
                expectedSelection,
                sil.getSelection());    
        assertEquals("The selection holder value remains unchanged.",
                expectedSelection,
                sil.getSelectionHolder().getValue());
        assertEquals("No selection change event has been fired.",
                0,
                changeReport.eventCount());
    }
    
    
    public void testSelectionReflectsIndexAfterIndexMoveForward() {
        ValueModel selectionHolder = new ValueHolder();
        SelectionInListModel sil = new SelectionInListModel(new ValueHolder(listModel, true), selectionHolder);
        sil.setSelectionIndex(1);
        PropertyChangeReport changeReport = new PropertyChangeReport();
        sil.addPropertyChangeListener(SelectionInListModel.PROPERTYNAME_SELECTION, changeReport);
        
        Object initialSelection = listModel.get(1);
        assertEquals("SelectionHolder holds the second list element.",
                initialSelection,
                selectionHolder.getValue());    

        listModel.add(0, "zero");
        Object expectedSelection = listModel.get(2);
        assertEquals("The selection index has been increased by 1.",
                2,
                sil.getSelectionIndex());    
        assertEquals("The selection remains unchanged.",
                expectedSelection,
                sil.getSelection());    
        assertEquals("The selection holder value remains unchanged.",
                expectedSelection,
                sil.getSelectionHolder().getValue());
        assertEquals("No selection change event has been fired.",
                0,
                changeReport.eventCount());
    }
    
    
    // Properties Must be Changed Before the PropertyChangeEvent is Fired *****
    
    public void testSelectionChangeEventFiredAfterSelectionChange() {
        final SelectionInListModel sil = new SelectionInListModel(listModel);
        sil.getSelectionHolder().setValue("one");
        sil.addPropertyChangeListener(
                SelectionInListModel.PROPERTYNAME_SELECTION,
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        assertEquals("Selection equals selectionChange's new value.",
                                sil.getSelection(),
                                evt.getNewValue());
                    }
                });
        sil.getSelectionHolder().setValue("two");
    }
    

    public void testSelectionEmptyChangeEventFiredAfterSelectionEmptyChange() {
        final SelectionInListModel sil = new SelectionInListModel(listModel);
        sil.getSelectionHolder().setValue("kuckuck");
        sil.addPropertyChangeListener(
                SelectionInListModel.PROPERTYNAME_SELECTION_EMPTY,
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        assertEquals("Selection empty equals selectionEmtpyChange's new value.",
                                Boolean.valueOf(sil.isSelectionEmpty()),
                                evt.getNewValue());
                    }
                });
        sil.getSelectionHolder().setValue("two");
    }
    

    public void testSelectionIndexChangeEventFiredAfterSelectionIndexChange() {
        final SelectionInListModel sil = new SelectionInListModel(listModel);
        sil.getSelectionIndexHolder().setValue(new Integer(1));
        sil.addPropertyChangeListener(
                SelectionInListModel.PROPERTYNAME_SELECTION_INDEX,
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        assertEquals("Selection index equals selectionIndexChange's new value.",
                                new Integer(sil.getSelectionIndex()),
                                evt.getNewValue());
                    }
                });
        sil.getSelectionIndexHolder().setValue(new Integer(2));
    }
    

    // ListModel Operations Affect the Selection and Selection Index **********

    public void testContentsChangedOnSelection() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        final int initialSelection = 2;
        sil.setSelectionIndex(initialSelection);
        assertEquals("Initial selection index", 
                initialSelection, 
                sil.getSelectionIndex());
        listModel.setElementAt("another", initialSelection);
        assertEquals("Index after re-setting the element at selection index",
                initialSelection, 
                sil.getSelectionIndex());
        assertEquals("sil value must be the updated element", 
                "another", 
                sil.getSelection());
        assertEquals("selectionHolder value must equal sil value", 
                sil.getSelection(), 
                sil.getSelectionHolder().getValue());
    }


    public void testIgnoreContentsChangedOnMinusOne() {
        testIgnoreContentsChanged(2, "another");
    }


    public void testIgnoreContentsChangedOnMinusOneEmptySelection() {
        testIgnoreContentsChanged(-1, "two");
    }


    private void testIgnoreContentsChanged(int silSelectionIndex,
                                           Object comboSelection) {
        ComboBoxModel comboBoxModel = new UnforgivingComboBoxModel(AN_ARRAY);
        SelectionInListModel sil = new SelectionInListModel(comboBoxModel);
        sil.setSelectionIndex(silSelectionIndex);
        Object silSelection = sil.getSelection();
        comboBoxModel.setSelectedItem(comboSelection);
        assertEquals("Initial selection index", silSelectionIndex, sil
                .getSelectionIndex());
        assertEquals("sil value must be unchanged", silSelection, sil
                .getSelection());
    }
    

    public void testInsertBeforeSelectionIncreasesSelectionIndex() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        final int initialSelection = 2;
        sil.setSelectionIndex(initialSelection);
        assertEquals("Initial selection index", 
                initialSelection, 
                sil.getSelectionIndex());
        listModel.insertElementAt("another", 0);
        assertEquals("Index after inserting an element before the selection", 
                initialSelection + 1,
                sil.getSelectionIndex());
    }


    public void testInsertBeforeSelectionKeepsSelection() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        sil.setSelectionIndex(2);
        assertEquals("Initial selection index", 2, sil.getSelectionIndex());
        Object selection = sil.getSelection();
        listModel.insertElementAt("another", 0);
        assertEquals("Selection after inserting an element", 
                selection, 
                sil.getSelection());
    }
    
    
    public void testInsertAfterSelectionKeepsSelectionIndex() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        final int initialSelection = 1;
        sil.setSelectionIndex(initialSelection);
        assertEquals("Initial selection index", 
                initialSelection, 
                sil.getSelectionIndex());
        listModel.insertElementAt("another", initialSelection + 1);
        assertEquals("Index after inserting an element after the selection", 
                initialSelection,
                sil.getSelectionIndex());
    }


    public void testRemoveBeforeSelectionDecreasesSelectionIndex() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        final int initialSelection = 2;
        sil.setSelectionIndex(initialSelection);
        assertEquals("Initial selection index", 
                initialSelection, 
                sil.getSelectionIndex());
        listModel.remove(0);
        assertEquals("Selection index after removing an element", 
                initialSelection - 1, 
                sil.getSelectionIndex());
    }


    public void testRemoveBeforeSelectionKeepsSelection() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        sil.setSelectionIndex(2);
        assertEquals("Initial selection index", 2, sil.getSelectionIndex());
        Object selection = sil.getSelection();
        listModel.remove(0);
        assertEquals("Selection after removing an element", 
                selection, 
                sil.getSelection());
    }


    /**
     * Removes the selected first element from a non-empty list and 
     * checks whether the selection index is reset to -1.
     */
    public void testRemoveSelectedFirstElementResetsSelectionIndex() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        final int initialSelectionIndex = 0;
        sil.setSelectionIndex(initialSelectionIndex);
        assertEquals("Selection index before the remove action", 
                initialSelectionIndex,
                sil.getSelectionIndex());

        listModel.remove(initialSelectionIndex);
        assertEquals("Selection index after the removal of the selected element", 
                -1,
                sil.getSelectionIndex());
    }
    
    
    /**
     * Removes the selected last element from a non-empty list and 
     * checks whether the selection index is reset to -1.
     */
    public void testRemoveSelectedLastElementResetsSelectionIndex() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        final int initialSelectionIndex = listModel.getSize() - 1;
        sil.setSelectionIndex(initialSelectionIndex);
        assertEquals("Selection index before the remove action", 
                initialSelectionIndex,
                sil.getSelectionIndex());

        listModel.remove(initialSelectionIndex);
        assertEquals("Selection index after the removal of the selected element", 
                -1,
                sil.getSelectionIndex());
    }
    
    public void testRemoveAfterSelectionKeepsSelectionIndex() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        final int initialSelectionIndex = 0;
        sil.setSelectionIndex(initialSelectionIndex);
        assertEquals("Initial selection index", initialSelectionIndex, sil.getSelectionIndex());
        listModel.remove(1);
        assertEquals("Selection index after removing an element", 
                initialSelectionIndex, 
                sil.getSelectionIndex());
    }


    
    // Keeping the Selection on List Changes **********************************
  
    /**
     * Changes the listHolder's (list) value and checks how 
     * the SelectionInListModel keeps or resets the selection.
     * The listHolder is a <code>ValueHolder</code> that 
     * reports an old and a new value.
     */
    public void testKeepsSelectionOnListChange() {
        testKeepsSelectionOnListChange(new ValueHolder(null, true), false);
    }

    /**
     * Changes the listHolder's (list) value and checks how 
     * the SelectionInListModel keeps or resets the selection.
     * The listHolder is a <code>ValueHolder</code> that 
     * reports an old and a new value.
     */
    public void testKeepsTableSelectionOnListChange() {
        testKeepsSelectionOnListChange(new ValueHolder(null, true), true);
    }

    /**
     * Changes the listHolder's (list) value and checks how 
     * the SelectionInListModel keeps or resets the selection.
     * The listHolder is a <code>ForgetfulValueHolder</code> that 
     * uses null as old value when reporting value changes.
     */
    public void testKeepsSelectionOnListChangeNoOldList() {
        testKeepsSelectionOnListChange(new ValueHolderWithOldValueNull(null), false);
    }
    
    /**
     * Changes the listHolder's (list) value and checks how 
     * the SelectionInListModel keeps or resets the selection.
     * The listHolder is a <code>ForgetfulValueHolder</code> that 
     * uses null as old value when reporting value changes.
     */
    public void testKeepsTableSelectionOnListChangeNoOldList() {
        testKeepsSelectionOnListChange(new ValueHolderWithOldValueNull(null), true);
    }
    

    /**
     * Changes the listHolder's (list) value and checks how 
     * the SelectionInList keeps or resets the selection.
     * If specified, the SelectionInList will be bound to a JTable
     * using an AbstractTableAdapter and a SingleSelectionAdapter.
     * Since the JTable may clear the selection after some updates,
     * this tests if the selection is restored.
     * 
     * @param listHolder  the ValueModel that holds the list
     * @param bindToTable if true, the SelectionInList will be bound
     *     to a JTable
     */
    private void testKeepsSelectionOnListChange(ValueModel listHolder, boolean bindToTable) {
        List list1 = new ArrayListModel();
        list1.add("One");
        list1.add("Two");
        list1.add("Three");
        List list2 = new ArrayListModel(list1);
        List list3 = new ArrayListModel();
        list3.add("Three");
        list3.add(new String("Two"));
        list3.add("One");
        List list4 = new ArrayListModel(list1);
        list4.add("Four");
        List list5 = new ArrayListModel(list1.subList(0, 2));
        List list6 = new ArrayListModel();
        list6.add("One");
        list6.add("Three");
        list6.add(new String("Two"));
        
        listHolder.setValue(list1);        
        SelectionInListModel sil = new SelectionInListModel(listHolder);
        sil.setSelectionIndex(1);
        if (bindToTable) {
            TableModel tableModel = new AbstractTableAdapter(sil, new String[]{"Name"}){

                public Object getValueAt(int rowIndex, int columnIndex) {
                    return getRow(rowIndex);
                }};
            JTable table = new JTable(tableModel);
            table.setSelectionModel(new SingleListSelectionAdapter(sil.getSelectionIndexHolder()));
        }
        
        Object oldSelection = sil.getSelection();
        assertEquals("List1: Selection is 'Two'.", "Two", oldSelection);
        
        listHolder.setValue(list2);
        assertEquals("List2: Selection index is still 1.", 1, sil.getSelectionIndex());
        assertEquals("List2: Selection is still 'Two'.", "Two", sil.getSelection());

        listHolder.setValue(list3);
        assertEquals("List3: Selection index is still 1.", 1, sil.getSelectionIndex());
        assertEquals("List3: Selection is still 'Two'.", "Two", sil.getSelection());

        listHolder.setValue(list4);
        assertEquals("List4: Selection index is still 1.", 1, sil.getSelectionIndex());
        assertEquals("List4: Selection is still 'Two'.", "Two", sil.getSelection());

        listHolder.setValue(list5);
        assertEquals("List5: Selection index is still 1.", 1, sil.getSelectionIndex());
        assertEquals("List5: Selection is still 'Two'.", "Two", sil.getSelection());

        listHolder.setValue(list6);
        assertEquals("List6: Selection index is now 2.", 2, sil.getSelectionIndex());
        assertEquals("List6: Selection is still 'Two'.", "Two", sil.getSelection());

        listHolder.setValue(new ArrayListModel());
        assertEquals("Selection index is -1.", -1, sil.getSelectionIndex());
        assertEquals("Selection is null.", null, sil.getSelection());
        
        listHolder.setValue(list1);
        assertEquals("Selection index is still -1.", -1, sil.getSelectionIndex());
        assertEquals("Selection is still null.", null, sil.getSelection());
    }
    

    // Resetting the selection if the new list is empty or null
    
    public void testResetsSelectionIndexOnNullOrEmptyList() {
        SelectionInListModel sil = new SelectionInListModel(listModel);
        sil.setSelectionIndex(1);
        
        sil.setListModel(createListModel(new Object[0]));
        assertEquals("Selection index is -1.",   -1,   sil.getSelectionIndex());
        assertEquals("Selection is still null.", null, sil.getSelection());

        sil.setListModel(listModel);
        sil.setSelectionIndex(1);
        sil.setListModel(null);
        assertEquals("Selection index is -1.",   -1,   sil.getSelectionIndex());
        assertEquals("Selection is still null.", null, sil.getSelection());
    }
    

    // Firing ListDataEvents **************************************************
    
    /**
     * Checks that list data events from an underlying are reported
     * by the SelectionInListModel.
     */
    public void testFiresListModelListDataEvents() {
        PropertyChangeReport changeReport = new PropertyChangeReport();
        ListDataReport listDataReport1 = new ListDataReport();
        ListDataReport listDataReport2 = new ListDataReport();

        ArrayListModel arrayListModel = new ArrayListModel();
        SelectionInListModel sil = new SelectionInListModel(arrayListModel);

        arrayListModel.addListDataListener(listDataReport1);
        sil.addListDataListener(listDataReport2);

        arrayListModel.add("One");
        assertEquals("No list change.", changeReport.eventCount(), 0);
        assertEquals("An element has been added.",
                listDataReport2.eventCount(), 1);
        assertEquals("An element has been added.", listDataReport2
                .eventCountAdd(), 1);
        
        arrayListModel.addAll(Arrays.asList(new String[]{"two", "three", "four"}));
        assertEquals("No list change.", changeReport.eventCount(), 0);
        assertEquals("An element block has been added.",
                listDataReport2.eventCount(), 2);
        assertEquals("An element block has been added.", listDataReport2
                .eventCountAdd(), 2);
        
        arrayListModel.remove(0);
        assertEquals("An element has been removed.",
                listDataReport2.eventCount(), 3);
        assertEquals("No element has been added.", listDataReport2
                .eventCountAdd(), 2);
        assertEquals("An element has been removed.", listDataReport2
                .eventCountRemove(), 1);
        
        arrayListModel.set(1, "newTwo");
        assertEquals("An element has been replaced.",
                listDataReport2.eventCount(), 4);
        assertEquals("No element has been added.", listDataReport2
                .eventCountAdd(), 2);
        assertEquals("No element has been removed.", listDataReport2
                .eventCountRemove(), 1);
        assertEquals("An element has been changed.", listDataReport2
                .eventCountChange(), 1);
        
        // Compare the event counts of the list models listener
        // with the SelectionInListModel listener.
        assertEquals("Add event counts are equal.", 
                listDataReport1.eventCountAdd(),
                listDataReport2.eventCountAdd());
        assertEquals("Remove event counts are equal.", 
                listDataReport1.eventCountRemove(),
                listDataReport2.eventCountRemove());
        assertEquals("Change event counts are equal.", 
                listDataReport1.eventCountChange(),
                listDataReport2.eventCountChange());
    }
    
    
    // Registering, Deregistering and Registering of the ListDataListener *****
    
    /**
     * Checks and verifies that the SelectionInListModel registers
     * its ListDataListener with the underlying ListModel once only.
     * In other words: the SelectionInListModel doesn't register 
     * its ListDataListener multiple times.<p>
     * 
     * Uses a list holder that checks the identity and 
     * reports an old and new value.
     */
    public void testSingleListDataListener() {
        testSingleListDataListener(new ValueHolder(null, true));
    }
    
    
    /**
     * Checks and verifies that the SelectionInListModel registers
     * its ListDataListener with the underlying ListModel once only.
     * In other words: the SelectionInListModel doesn't register 
     * its ListDataListener multiple times.<p>
     * 
     * Uses a list holder uses null as old value when reporting value changes.
     */
    public void testSingleListDataListenerNoOldList() {
        testSingleListDataListener(new ValueHolderWithOldValueNull(null));
    }
    
    
    /**
     * Checks and verifies that the SelectionInListModel registers
     * its ListDataListener with the underlying ListModel once only.
     * In other words: the SelectionInListModel doesn't register 
     * its ListDataListener multiple times.
     */
    private void testSingleListDataListener(ValueModel listHolder) {
        new SelectionInListModel(listHolder);
        ArrayListModel  listModel1 = new ArrayListModel();
        LinkedListModel listModel2 = new LinkedListModel();
        listHolder.setValue(listModel1);
        assertEquals("SelectionInListModel registered its ListDataListener.",
                     1,
                     listModel1.getListDataListeners().length);
        listHolder.setValue(listModel1);
        assertEquals("SelectionInListModel reregistered its ListDataListener.",
                1,
                listModel1.getListDataListeners().length);
        listHolder.setValue(listModel2);
        assertEquals("SelectionInListModel deregistered its ListDataListener.",
                0,
                listModel1.getListDataListeners().length);
        assertEquals("SelectionInListModel registered its ListDataListener.",
                1,
                listModel2.getListDataListeners().length);
    }
    
    
    /**
     * Checks and verifies for a bunch of ListModel instances,
     * whether the ListDataListener has been reregistered properly.
     */
    public void testReregisterListDataListener() {
        ObservableList empty1 = new ArrayListModel();
        ObservableList empty2 = new ArrayListModel();
        testReregistersListDataListener(empty1, empty2);
        
        ObservableList empty3 = new LinkedListModel();
        ObservableList empty4 = new LinkedListModel();
        testReregistersListDataListener(empty3, empty4);
        
        ObservableList array1 = new ArrayListModel();
        ObservableList array2 = new ArrayListModel();
        array1.add(Boolean.TRUE);
        array2.add(Boolean.TRUE);
        testReregistersListDataListener(array1, array2);
        
        ObservableList linked1 = new LinkedListModel();
        ObservableList linked2 = new LinkedListModel();
        linked1.add(Boolean.TRUE);
        linked2.add(Boolean.TRUE);
        testReregistersListDataListener(linked1, linked2);
    }


    /**
     * Checks and verifies whether the ListDataListener has been
     * reregistered properly. This will fail if the change support
     * fails to fire a change event when the instance changes.<p>
     * 
     * Creates a SelectionInListModel on list1, then changes it to list2,
     * modifies boths lists, and finally checks whether the SelectionInListModel
     * has fired the correct events.
     */
    private void testReregistersListDataListener(
             ObservableList list1,
             ObservableList list2) {
        ListDataReport listDataReport1    = new ListDataReport();
        ListDataReport listDataReport2    = new ListDataReport();
        ListDataReport listDataReportSel  = new ListDataReport();
        
        SelectionInListModel sil = new SelectionInListModel(list1);

        // Change the list model. 
        // Changes on list1 shall not affect the SelectionInListModel.
        // Changes in list2 shall be the same as for the SelectionInListModel.
        sil.setListModel(list2);
        
        list1.addListDataListener(listDataReport1);
        list2.addListDataListener(listDataReport2);
        sil.addListDataListener(listDataReportSel);
        
        // Modify both list models.
        list1.add("one1");
        list1.add("two1");
        list1.add("three1");
        list1.add("four1");
        list1.remove(1);
        list1.remove(0);
        list1.set(0, "newOne1");
        list1.set(1, "newTwo1");

        assertEquals("Events counted for list model 1",
                8,
                listDataReport1.eventCount());
        assertEquals("No events counted for list model 2",
                0,
                listDataReport2.eventCount());
        assertEquals("No events counted for the SelectionInListModel",
                0,
                listDataReportSel.eventCount());

        list2.add("one2");
        list2.add("two2");
        list2.add("three2");
        list2.remove(1);
        list2.set(0, "newOne2");
        
        assertEquals("Events counted for list model 2",
                5,
                listDataReport2.eventCount());
        assertEquals("Events counted for the SelectionInListModel",
                5,
                listDataReportSel.eventCount());
        
        // Compare the event lists.
        assertEquals("Events for list2 and SelectionInListModel differ.", 
                listDataReport2,
                listDataReportSel);
    }
    
    
    // Helper Code ************************************************************
    
    private DefaultListModel createListModel(Object[] array) {
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < array.length; i++) {
            model.addElement(array[i]);
        }
        return model;
    }
 

    /**
     * A DefaultComboBoxModel that fires when accessing illegal
     *  index (instead of silently returning null).
     *  DefaultCombo is mad anyway - should return the selected item on -1...
     */    
    private static final class UnforgivingComboBoxModel extends DefaultComboBoxModel {
        
        UnforgivingComboBoxModel(Object[] elements) {
            super(elements);
        }
        
        public Object getElementAt(int index) {
            if ((index < 0) || (index >= getSize()))
                throw new ArrayIndexOutOfBoundsException(index);
            return super.getElementAt(index);
        }
    }


}



