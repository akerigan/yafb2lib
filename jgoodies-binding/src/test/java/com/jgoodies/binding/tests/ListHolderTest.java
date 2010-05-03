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

import com.jgoodies.binding.list.ListHolder;
import com.jgoodies.binding.test.event.ListDataReport;
import com.jgoodies.binding.test.event.ListSizeConstraintChecker;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import junit.framework.TestCase;

import javax.swing.event.ListDataEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A test case for class {@link ListHolder}.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public final class ListHolderTest extends TestCase {

    // private static final Object[] AN_ARRAY = { "one", "two", "three" };

    // private List list;


    // Initialization *********************************************************

    protected void setUp() throws Exception {
        super.setUp();
        // list = new ArrayList(Arrays.asList(AN_ARRAY));
    }


    // Testing Constructors ***************************************************

    public void testConstructorRejectsNullListHolder() {
        try {
            new ListHolder((ValueModel) null);
            fail("The ListHolder must reject a null List holder.");
        } catch (NullPointerException e) {
            // The expected behavior.
        }
    }


    public void testConstructorRejectsInvalidListHolderContent() {
        try {
            new ListHolder(new ValueHolder("Hello", true));
            fail("The ListHolder must reject List holder content other than List.");
        } catch (ClassCastException e) {
            // The expected behavior.
        }
    }


    // List Change Events *****************************************************

    /**
     * Tests the ListDataEvents fired during list changes.
     * The transistions are {} -> {} -> {a, b} -> {b, c} -> {a, b, c} -> {b, c} -> {}.
     */
    public void testListChangeEvents() {
        List list1 = Collections.EMPTY_LIST;
        List list2 = Collections.EMPTY_LIST;
        List list3 = Arrays.asList(new String[]{"a", "b"});
        List list4 = Arrays.asList(new String[]{"b", "c"});
        List list5 = Arrays.asList(new String[]{"a", "b", "c"});
        List list6 = Collections.EMPTY_LIST;

        ListHolder listHolder = new ListHolder(list1);
        ListDataReport report = new ListDataReport();
        listHolder.addListDataListener(report);

        listHolder.setList(list2);
        assertEquals("The transistion {} -> {} fires no ListDataEvent.",
                0,
                report.eventCount());

        report.clearEventList();
        listHolder.setList(list3);
        assertEquals("The transistion {} -> {a, b} fires 1 event.",
                1,
                report.eventCount());
        assertEvent("The transistion {} -> {a, b} fires an add event with interval[0, 1].",
                ListDataEvent.INTERVAL_ADDED, 0, 1,
                report.lastEvent());

        report.clearEventList();
        listHolder.setList(list4);
        assertEquals("The transistion {a, b} -> {b, c} fires 1 event.",
                1,
                report.eventCount());
        assertEvent("The transistion {a, b} -> {b, c} fires a contents changed event with interval[0, 1].",
                ListDataEvent.CONTENTS_CHANGED, 0, 1,
                report.lastEvent());

        report.clearEventList();
        listHolder.setList(list5);
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
        listHolder.setList(list4);
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
        listHolder.setList(list6);
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
        List list1 = Collections.EMPTY_LIST;
        List list2 = Collections.EMPTY_LIST;
        List list3 = Arrays.asList(new String[]{"a", "b"});
        List list4 = Arrays.asList(new String[]{"b", "c"});
        List list5 = Arrays.asList(new String[]{"a", "b", "c"});
        List list6 = Collections.EMPTY_LIST;

        ListHolder listHolder = new ListHolder(list1);
        listHolder.addListDataListener(
                new ListSizeConstraintChecker(listHolder.getSize()));

        listHolder.setList(list2);
        listHolder.setList(list3);
        listHolder.setList(list4);
        listHolder.setList(list5);
        listHolder.setList(list4);
        listHolder.setList(list6);
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


}



