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

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.extras.SelectionInList2;
import com.jgoodies.binding.test.event.PropertyChangeReport;
import com.jgoodies.binding.test.value.ValueHolderWithOldValueNull;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import junit.framework.TestCase;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A test case for class {@link SelectionInList2}.
 *
 * @author Karsten Lentzsch
 * @author Jeanette Winzenburg
 * @version $Revision: 1.9 $
 */
public final class SelectionInList2Test extends TestCase {

    private static final Object[] AN_ARRAY = {"one", "two", "three"};

    private List list;


    // Initialization *********************************************************

    protected void setUp() throws Exception {
        super.setUp();
        list = new ArrayList(Arrays.asList(AN_ARRAY));
    }


    // Testing Setup **********************************************************

    public void testRejectNullSelectionIndex() {
        ValueHolder indexHolder = new ValueHolder(0);
        SelectionInList2 sil = new SelectionInList2(list);
        sil.setSelectionIndexHolder(indexHolder);
        try {
            indexHolder.setValue(null);
            fail("The SelectionInList2 must reject null selection index values.");
        } catch (Exception e) {
            // The expected behavior.
        }
    }

    public void testRejectNewNullSelectionHolder() {
        SelectionInList2 sil = new SelectionInList2(list);
        try {
            sil.setSelectionHolder(null);
            fail("The SelectionInList2 must reject a null selection holder.");
        } catch (Exception e) {
            // The expected behavior.
        }
    }

    public void testRejectNewNullSelectionIndexHolder() {
        SelectionInList2 sil = new SelectionInList2(list);
        try {
            sil.setSelectionIndexHolder(null);
            fail("The SelectionInList2 must reject a null selection index holder.");
        } catch (Exception e) {
            // The expected behavior.
        }
    }

    public void testRejectNewSelectionIndexHolderWithNullValue() {
        SelectionInList2 sil = new SelectionInList2(list);
        try {
            sil.setSelectionIndexHolder(new ValueHolder(null));
            fail("The SelectionInList2 must reject selection index holder with null values.");
        } catch (Exception e) {
            // The expected behavior.
        }
    }

    public void testSelectionIndexAfterInitialization() {
        Object value = "two";
        ValueModel selectionHolder = new ValueHolder(value);
        SelectionInList2 sil = new SelectionInList2(list, selectionHolder);
        int initialSelectionIndex = sil.getSelectionIndex();
        assertEquals(
                "The initial selection index reflects the index of the selection holder's initial value.",
                list.indexOf(value),
                initialSelectionIndex);
        selectionHolder.setValue(null);
        assertEquals(
                "The selection index has been updated to indicate no selection.",
                -1,
                sil.getSelectionIndex());
    }


    public void testDefaultSelectionHolderChecksIdentity() {
        SelectionInList2 sil = new SelectionInList2(list);
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
        SelectionInList2 sil = new SelectionInList2(list);
        sil.setSelectionIndexHolder(indexHolder);
        indexHolder.fireValueChange(null, new Integer(selectionIndex));
        assertEquals("Selection index",
                selectionIndex,
                sil.getSelectionIndex());
        Object selection = list.get(selectionIndex);
        assertEquals("Selection",
                selection,
                sil.getSelection());
    }

    public void testAcceptsNullNewValueInSelectionIndexPropertyChangeEvent() {
        int selectionIndex = 0;
        ValueHolder indexHolder = new ValueHolder(selectionIndex);
        SelectionInList2 sil = new SelectionInList2(list);
        sil.setSelectionIndexHolder(indexHolder);
        indexHolder.fireValueChange(new Integer(selectionIndex), null);
        assertEquals("Selection index",
                selectionIndex,
                sil.getSelectionIndex());
        Object selection = list.get(selectionIndex);
        assertEquals("Selection",
                selection,
                sil.getSelection());
    }

    public void testAcceptsNullOldAndNewValueInSelectionIndexPropertyChangeEvent() {
        int selectionIndex = 0;
        ValueHolder indexHolder = new ValueHolder(selectionIndex);
        SelectionInList2 sil = new SelectionInList2(list);
        sil.setSelectionIndexHolder(indexHolder);
        indexHolder.fireValueChange(null, null);
        assertEquals("Selection index",
                selectionIndex,
                sil.getSelectionIndex());
        Object selection = list.get(selectionIndex);
        assertEquals("Selection",
                selection,
                sil.getSelection());
    }


    // ************************************************************************

    public void testFiresSelectionChangeOnlyForSelectionChanges() {
        int selectionIndex = 0;
        ValueHolder indexHolder = new ValueHolder(selectionIndex);
        SelectionInList2 sil = new SelectionInList2(list);
        sil.setSelectionIndexHolder(indexHolder);

        // Create change reports.
        PropertyChangeReport selectionReport = new PropertyChangeReport();
        PropertyChangeReport selectionEmptyReport = new PropertyChangeReport();
        PropertyChangeReport selectionIndexReport = new PropertyChangeReport();

        // Register change reports for selection, selection index,
        // and selectionEmpty
        sil.addPropertyChangeListener(SelectionInList2.PROPERTYNAME_SELECTION, selectionReport);
        sil.addPropertyChangeListener(SelectionInList2.PROPERTYNAME_SELECTION_EMPTY, selectionEmptyReport);
        sil.addPropertyChangeListener(SelectionInList2.PROPERTYNAME_SELECTION_INDEX, selectionIndexReport);

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
        SelectionInList2 sil = new SelectionInList2(list);
        sil.setSelectionIndexHolder(indexHolder);

        // Create change reports.
        PropertyChangeReport selectionReport = new PropertyChangeReport();
        PropertyChangeReport selectionIndexReport = new PropertyChangeReport();

        // Register change reports for selection, selection index,
        // and selectionEmpty
        sil.addPropertyChangeListener(SelectionInList2.PROPERTYNAME_SELECTION, selectionReport);
        sil.addPropertyChangeListener(SelectionInList2.PROPERTYNAME_SELECTION_INDEX, selectionIndexReport);

        // We change the selection index holder's value to the new index.
        // The ValueModel used for the selectionIndexHolder fires no old value.
        indexHolder.setValue(newSelectionIndex);
        Object oldSelection = selectionReport.lastEvent().getOldValue();
        Object oldSelectionIndex = selectionIndexReport.lastEvent().getOldValue();
        assertTrue("Non-null old value in selection change event",
                oldSelection != null);
        assertTrue("Non-null old value in selectionIndex change event",
                oldSelectionIndex != null);
    }


    public void testSelectionForDirectSelectionIndexChanges() {
        SelectionInList2 sil = new SelectionInList2(list);

        // Create change reports.
        PropertyChangeReport selectionReport = new PropertyChangeReport();
        PropertyChangeReport selectionEmptyReport = new PropertyChangeReport();
        PropertyChangeReport selectionIndexReport = new PropertyChangeReport();

        // Register change reports for selection, selection index,
        // and selectionEmpty
        sil.addPropertyChangeListener(SelectionInList2.PROPERTYNAME_SELECTION, selectionReport);
        sil.addPropertyChangeListener(SelectionInList2.PROPERTYNAME_SELECTION_EMPTY, selectionEmptyReport);
        sil.addPropertyChangeListener(SelectionInList2.PROPERTYNAME_SELECTION_INDEX, selectionIndexReport);

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
                list.get(0),
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
        SelectionInList2 sil = new SelectionInList2(list);
        ValueHolder selectionIndexHolder = new ValueHolder(-1);
        sil.setSelectionIndexHolder(selectionIndexHolder);
        PropertyChangeReport selectionEmptyReport = new PropertyChangeReport();
        sil.addPropertyChangeListener(SelectionInList2.PROPERTYNAME_SELECTION_EMPTY, selectionEmptyReport);

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
        SelectionInList2 sil = new SelectionInList2(list);
        PropertyChangeReport selectionEmptyReport = new PropertyChangeReport();
        sil.addPropertyChangeListener(SelectionInList2.PROPERTYNAME_SELECTION_EMPTY, selectionEmptyReport);

        sil.setSelection(list.get(0));
        assertFalse("The selection index is 0 and not empty.",
                sil.isSelectionEmpty());

        assertEquals("selectionEmpty changed from true to false.",
                1,
                selectionEmptyReport.eventCount());
        assertTrue("1) selectionEmpty change event oldValue == true.",
                selectionEmptyReport.lastOldBooleanValue());
        assertFalse("1) selectionEmpty change event newValue == false.",
                selectionEmptyReport.lastNewBooleanValue());

        sil.setSelection(list.get(1));
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
        SelectionInList2 sil = new SelectionInList2(new ValueHolder(list), selectionHolder);
        PropertyChangeReport selectionEmptyReport = new PropertyChangeReport();
        sil.addPropertyChangeListener(SelectionInList2.PROPERTYNAME_SELECTION_EMPTY, selectionEmptyReport);

        assertTrue("The selection index is -1 and empty.",
                sil.isSelectionEmpty());
        selectionHolder.setValue(list.get(0));
        assertFalse("The selection index is 0 and not empty.",
                sil.isSelectionEmpty());

        assertEquals("selectionEmpty changed from true to false.",
                1,
                selectionEmptyReport.eventCount());
        assertTrue("1) selectionEmpty change event oldValue == true.",
                selectionEmptyReport.lastOldBooleanValue());
        assertFalse("1) selectionEmpty change event newValue == false.",
                selectionEmptyReport.lastNewBooleanValue());

        selectionHolder.setValue(list.get(1));
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


    // Properties Must be Changed Before the PropertyChangeEvent is Fired *****

    public void testSelectionChangeEventFiredAfterSelectionChange() {
        final SelectionInList2 sil = new SelectionInList2(list);
        sil.getSelectionHolder().setValue("one");
        sil.addPropertyChangeListener(
                SelectionInList2.PROPERTYNAME_SELECTION,
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
        final SelectionInList2 sil = new SelectionInList2(list);
        sil.getSelectionHolder().setValue("kuckuck");
        sil.addPropertyChangeListener(
                SelectionInList2.PROPERTYNAME_SELECTION_EMPTY,
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
        final SelectionInList2 sil = new SelectionInList2(list);
        sil.getSelectionIndexHolder().setValue(new Integer(1));
        sil.addPropertyChangeListener(
                SelectionInList2.PROPERTYNAME_SELECTION_INDEX,
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
        SelectionInList2 sil = new SelectionInList2(list);
        final int initialSelectionIndex = 2;
        sil.setSelectionIndex(initialSelectionIndex);
        assertEquals("Initial selection index",
                initialSelectionIndex,
                sil.getSelectionIndex());
        list.set(initialSelectionIndex, "another");
        assertEquals("Index after re-setting the element at selection index",
                initialSelectionIndex,
                sil.getSelectionIndex());
        assertEquals("sil value must be the updated element",
                "another",
                sil.getSelection());
    }


    // Keeping the Selection on List Changes **********************************

    /**
     * Changes the listHolder's (list) value and checks how
     * the SelectionInList2 keeps or resets the selection.
     * The listHolder is a <code>ValueHolder</code> that
     * reports an old and a new value.
     */
    public void testKeepsSelectionOnListChange() {
        testKeepsSelectionOnListChange(new ValueHolder(null), false);
    }

    /**
     * Changes the listHolder's (list) value and checks how
     * the SelectionInList2 keeps or resets the selection.
     * The listHolder is a <code>ValueHolder</code> that
     * reports an old and a new value.
     */
    public void testKeepsTableSelectionOnListChange() {
        testKeepsSelectionOnListChange(new ValueHolder(null), true);
    }

    /**
     * Changes the listHolder's (list) value and checks how
     * the SelectionInList2 keeps or resets the selection.
     * The listHolder is a <code>ForgetfulValueHolder</code> that
     * uses null as old value when reporting value changes.
     */
    public void testKeepsSelectionOnListChangeNoOldList() {
        testKeepsSelectionOnListChange(new ValueHolderWithOldValueNull(null), false);
    }

    /**
     * Changes the listHolder's (list) value and checks how
     * the SelectionInList2 keeps or resets the selection.
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
     *                    to a JTable
     */
    private void testKeepsSelectionOnListChange(ValueModel listHolder, boolean bindToTable) {
        List list1 = new ArrayList();
        list1.add("One");
        list1.add("Two");
        list1.add("Three");
        List list2 = new ArrayList(list1);
        List list3 = new ArrayList();
        list3.add("Three");
        list3.add(new String("Two"));
        list3.add("One");
        List list4 = new ArrayList(list1);
        list4.add("Four");
        List list5 = list1.subList(0, 2);
        List list6 = new ArrayList();
        list6.add("One");
        list6.add("Three");
        list6.add(new String("Two"));

        listHolder.setValue(list1);
        SelectionInList2 sil = new SelectionInList2(listHolder);
        sil.setSelectionIndex(1);
        if (bindToTable) {
            TableModel tableModel = new AbstractTableAdapter(sil, new String[]{"Name"}) {

                public Object getValueAt(int rowIndex, int columnIndex) {
                    return getRow(rowIndex);
                }
            };
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

        listHolder.setValue(new ArrayList());
        assertEquals("Selection index is -1.", -1, sil.getSelectionIndex());
        assertEquals("Selection is null.", null, sil.getSelection());

        listHolder.setValue(list1);
        assertEquals("Selection index is still -1.", -1, sil.getSelectionIndex());
        assertEquals("Selection is still null.", null, sil.getSelection());
    }


    // Resetting the selection if the new list is empty or null

    public void testResetsSelectionIndexOnNullOrEmptyList() {
        SelectionInList2 sil = new SelectionInList2(list);
        sil.setSelectionIndex(1);

        sil.setList(Collections.EMPTY_LIST);
        assertEquals("Selection index is -1.", -1, sil.getSelectionIndex());
        assertEquals("Selection is still null.", null, sil.getSelection());

        sil.setList(list);
        sil.setSelectionIndex(1);
        sil.setList(null);
        assertEquals("Selection index is -1.", -1, sil.getSelectionIndex());
        assertEquals("Selection is still null.", null, sil.getSelection());
    }


}
