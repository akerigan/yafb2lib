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

import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.LinkedListModel;
import com.jgoodies.binding.list.ListModelHolder;
import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.test.event.ListDataReport;
import com.jgoodies.binding.test.event.ListSizeConstraintChecker;
import com.jgoodies.binding.test.value.ValueHolderWithOldValueNull;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import junit.framework.TestCase;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import java.util.Arrays;

/**
 * A test case for class {@link ListModelHolder}.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public final class ListModelHolderTest extends TestCase {

    private static final Object[] AN_ARRAY = {"one", "two", "three"};

    private DefaultListModel listModel;


    // Initialization *********************************************************

    protected void setUp() throws Exception {
        super.setUp();
        listModel = createListModel(AN_ARRAY);
    }


    // Testing Constructors ***************************************************

    public void testConstructorRejectsNullListModelHolder() {
        try {
            new ListModelHolder((ValueModel) null);
            fail("The ListModelHolder must reject a null ListModel holder.");
        } catch (NullPointerException e) {
            // The expected behavior.
        }
    }


    public void testConstructorRejectsNonIdentityCheckingValueHolder() {
        try {
            new ListModelHolder(new ValueHolder(listModel));
            fail("The ListModelHolder must reject ListModel holders that have the idenity check is disabled.");
        } catch (IllegalArgumentException e) {
            // The expected behavior.
        }
    }


    public void testConstructorRejectsInvalidListModelHolderContent() {
        try {
            new ListModelHolder(new ValueHolder("Hello", true));
            fail("The ListModelHolder must reject ListModel holder content other than ListModel.");
        } catch (ClassCastException e) {
            // The expected behavior.
        }
    }


    // ************************************************************************

    /**
     * Checks that list data events from an underlying are reported
     * by the SelectionInList.
     */
    public void testFiresListModelListDataEvents() {
        ListDataReport listDataReport1 = new ListDataReport();
        ListDataReport listDataReport2 = new ListDataReport();

        ArrayListModel arrayListModel = new ArrayListModel();
        ListModelHolder lmh = new ListModelHolder(arrayListModel);

        arrayListModel.addListDataListener(listDataReport1);
        lmh.addListDataListener(listDataReport2);

        arrayListModel.add("One");
        assertEquals("An element has been added.",
                listDataReport2.eventCount(), 1);
        assertEquals("An element has been added.", listDataReport2
                .eventCountAdd(), 1);

        arrayListModel.addAll(Arrays.asList(new String[]{"two", "three", "four"}));
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
        // with the SelectionInList listener.
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
     * Checks and verifies that the SelectionInList registers
     * its ListDataListener with the underlying ListModel once only.
     * In other words: the SelectionInList doesn't register
     * its ListDataListener multiple times.<p>
     * <p/>
     * Uses a list holder that checks the identity and
     * reports an old and new value.
     */
    public void testSingleListDataListener() {
        testSingleListDataListener(new ValueHolder(null, true));
    }


    /**
     * Checks and verifies that the SelectionInList registers
     * its ListDataListener with the underlying ListModel once only.
     * In other words: the SelectionInList doesn't register
     * its ListDataListener multiple times.<p>
     * <p/>
     * Uses a list holder uses null as old value when reporting value changes.
     */
    public void testSingleListDataListenerNoOldList() {
        testSingleListDataListener(new ValueHolderWithOldValueNull(null));
    }


    /**
     * Checks and verifies that the SelectionInList registers
     * its ListDataListener with the underlying ListModel once only.
     * In other words: the SelectionInList doesn't register
     * its ListDataListener multiple times.
     */
    private void testSingleListDataListener(ValueModel listHolder) {
        new ListModelHolder(listHolder);
        ArrayListModel listModel1 = new ArrayListModel();
        LinkedListModel listModel2 = new LinkedListModel();
        listHolder.setValue(listModel1);
        assertEquals("SelectionInList registered its ListDataListener.",
                1,
                listModel1.getListDataListeners().length);
        listHolder.setValue(listModel1);
        assertEquals("SelectionInList reregistered its ListDataListener.",
                1,
                listModel1.getListDataListeners().length);
        listHolder.setValue(listModel2);
        assertEquals("SelectionInList deregistered its ListDataListener.",
                0,
                listModel1.getListDataListeners().length);
        assertEquals("SelectionInList registered its ListDataListener.",
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
     * <p/>
     * Creates a SelectionInList on list1, then changes it to list2,
     * modifies boths lists, and finally checks whether the SelectionInList
     * has fired the correct events.
     */
    private void testReregistersListDataListener(
            ObservableList list1,
            ObservableList list2) {
        ListDataReport listDataReport1 = new ListDataReport();
        ListDataReport listDataReport2 = new ListDataReport();
        ListDataReport listDataReportSel = new ListDataReport();

        ListModelHolder lmh = new ListModelHolder(list1);

        // Change the list model. 
        // Changes on list1 shall not affect the SelectionInList.
        // Changes in list2 shall be the same as for the SelectionInList.
        lmh.setListModel(list2);

        list1.addListDataListener(listDataReport1);
        list2.addListDataListener(listDataReport2);
        lmh.addListDataListener(listDataReportSel);

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
        assertEquals("No events counted for the SelectionInList",
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
        assertEquals("Events counted for the SelectionInList",
                5,
                listDataReportSel.eventCount());

        // Compare the event lists.
        assertEquals("Events for list2 and SelectionInList differ.",
                listDataReport2,
                listDataReportSel);
    }


    // List Change Events *****************************************************

    /**
     * Tests the ListDataEvents fired during ListModel changes.
     * The transistions are {} -> {} -> {a, b} -> {b, c} -> {a, b, c} -> {b, c} -> {}.
     */
    public void testListModelChangeEvents() {
        ListModel list1 = createListModel(new Object[]{});
        ListModel list2 = createListModel(new Object[]{});
        ListModel list3 = createListModel(new String[]{"a", "b"});
        ListModel list4 = createListModel(new String[]{"b", "c"});
        ListModel list5 = createListModel(new String[]{"a", "b", "c"});
        ListModel list6 = createListModel(new Object[]{});

        ListModelHolder listModelHolder = new ListModelHolder(list1);
        ListDataReport report = new ListDataReport();
        listModelHolder.addListDataListener(report);

        listModelHolder.setListModel(list2);
        assertEquals("The transistion {} -> {} fires no ListDataEvent.",
                0,
                report.eventCount());

        report.clearEventList();
        listModelHolder.setListModel(list3);
        assertEquals("The transistion {} -> {a, b} fires 1 add event.",
                1,
                report.eventCount());
        assertEvent("The transistion {} -> {a, b} fires an add event with interval[0, 1].",
                ListDataEvent.INTERVAL_ADDED, 0, 1,
                report.lastEvent());

        report.clearEventList();
        listModelHolder.setListModel(list4);
        assertEquals("The transistion {a, b} -> {b, c} fires 1 add event.",
                1,
                report.eventCount());
        assertEvent("The transistion {a, b} -> {b, c} fires an add event with interval[0, 1].",
                ListDataEvent.CONTENTS_CHANGED, 0, 1,
                report.lastEvent());

        report.clearEventList();
        listModelHolder.setListModel(list5);
        assertEquals("The transistion {b, c} -> {a, b, c} fires two events.",
                2,
                report.eventCount());
        assertEvent("The transistion {b, c} -> {a, b, c} fires an add event with interval[2, 2].",
                ListDataEvent.INTERVAL_ADDED, 2, 2,
                report.previousEvent());
        assertEvent("The transistion {b, c} -> {a, b, c} fires a contents changed with interval[0, 1].",
                ListDataEvent.CONTENTS_CHANGED, 0, 1,
                report.lastEvent());

        report.clearEventList();
        listModelHolder.setListModel(list4);
        assertEquals("The transistion {a, b, c} -> {b, c} fires two events.",
                2,
                report.eventCount());
        assertEvent("The transistion {a, b, c} -> {b, c} fires a remove event with interval[2, 2].",
                ListDataEvent.INTERVAL_REMOVED, 2, 2,
                report.previousEvent());
        assertEvent("The transistion {a, b, c} -> {b, c} fires a contents changed with interval[0, 1].",
                ListDataEvent.CONTENTS_CHANGED, 0, 1,
                report.lastEvent());

        report.clearEventList();
        listModelHolder.setListModel(list6);
        assertEquals("The transistion {b, c} -> {} fires one event.",
                1,
                report.eventCount());
        assertEvent("The transistion {b, c} -> {} fires a remove event with interval[0, 1].",
                ListDataEvent.INTERVAL_REMOVED, 0, 1,
                report.lastEvent());
    }


    /**
     * Tests that ListDataEvents fired during list changes
     * provide size information that are consitent with the
     * size of the list model.
     * The transistions are {} -> {} -> {a, b} -> {b, c} -> {a, b, c} -> {b, c} -> {}.
     */
    public void testListChangeEventsRetainSizeConstraints() {
        ListModel list1 = createListModel(new Object[]{});
        ListModel list2 = createListModel(new Object[]{});
        ListModel list3 = createListModel(new String[]{"a", "b"});
        ListModel list4 = createListModel(new String[]{"b", "c"});
        ListModel list5 = createListModel(new String[]{"a", "b", "c"});
        ListModel list6 = createListModel(new Object[]{});

        ListModelHolder listModelHolder = new ListModelHolder(list1);
        listModelHolder.addListDataListener(
                new ListSizeConstraintChecker(listModelHolder.getSize()));

        listModelHolder.setListModel(list2);
        listModelHolder.setListModel(list3);
        listModelHolder.setListModel(list4);
        listModelHolder.setListModel(list5);
        listModelHolder.setListModel(list4);
        listModelHolder.setListModel(list6);
    }


    private void assertEvent(String description, int eventType, int index0, int index1, ListDataEvent event) {
        assertEquals("Type: " + description,
                eventType,
                event.getType());
        assertEquals("Index0: " + description,
                index0,
                event.getIndex0());
        assertEquals("Index1: " + description,
                index1,
                event.getIndex1());
    }


    // Helper Code ************************************************************

    private DefaultListModel createListModel(Object[] array) {
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < array.length; i++) {
            model.addElement(array[i]);
        }
        return model;
    }


}



