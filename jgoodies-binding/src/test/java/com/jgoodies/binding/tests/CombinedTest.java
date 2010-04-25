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

import javax.swing.JTextField;

import junit.framework.TestCase;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;

/**
 * A test case for changing a SelectionInList's list by changing an
 * underlying bean that holds a list.
 * 
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public final class CombinedTest extends TestCase {
    
    public void testPersonChangeWithPresentationModel() {
        testPersonChange(true);
    }
    
    public void testPersonChangeWithPropertyAdapter() {
        testPersonChange(false);
    }
    
    private void testPersonChange(boolean usePresentationModel) {
        String phoneNumber = "32168";
        Person person1 = new Person();
        person1.addPhone(new Phone(phoneNumber));
        Person person2 = new Person();
        
        PresentationModel masterModel = new PresentationModel(person1);
        SelectionInList sil = new SelectionInList(
                masterModel.getModel(Person.PROPERTYNAME_PHONES));
        
        ValueModel phoneNumberModel = usePresentationModel
            ? new PresentationModel(sil).getModel(Phone.PROPERTYNAME_NUMBER)
            : new PropertyAdapter(sil, Phone.PROPERTYNAME_NUMBER, true);
            
        JTextField phoneNumberField = BasicComponentFactory.createTextField(phoneNumberModel);
        
        assertEquals(
                "Person1, no number selected (model): ",
                null,
                phoneNumberModel.getValue());
        assertEquals("Person1, no number selected (text): ",
                "",
                phoneNumberField.getText());

        sil.setSelectionIndex(0);
        assertEquals("Person1, first number selected (model): ",
                phoneNumber,
                phoneNumberModel.getValue());
        assertEquals(
                "Person1, first number selected (text): ",
                phoneNumber,
                phoneNumberField.getText());
        
        masterModel.setBean(person2);
        assertEquals(
                "Person2, no number selected (model): ",
                null,
                phoneNumberModel.getValue());
        assertEquals(
                "Person2, no number selected (text): ",
                "",
                phoneNumberField.getText());
    }
    
        
    // Helper Classes *********************************************************
    
    public static class Phone extends Model {
        
        public static final String PROPERTYNAME_NUMBER = "number";
        
        private String number;
        
        Phone(String number) {
            this.number = number;
        }
        
        public String getNumber() {
            return number;
        }
        
        public void setNumber(String newNumber) {
            String oldNumber = getNumber();
            number = newNumber;
            firePropertyChange(PROPERTYNAME_NUMBER, oldNumber, newNumber);
        }
        
    }
    
    public static final class Person extends Model {
        
        public static final String PROPERTYNAME_PHONES = "phones";
        
        private final ObservableList phones;
        
        private Person() {
            phones = new ArrayListModel();
        }
        
        public ObservableList getPhones() {
            return phones;
        }
        
        public void addPhone(Phone phone) {
            phones.add(phone);
        }
        
    }

}
