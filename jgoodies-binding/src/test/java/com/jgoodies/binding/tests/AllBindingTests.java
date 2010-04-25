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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A test suite for all tests related to the JGoodies Binding framework.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.36 $
 */
public final class AllBindingTests extends TestCase {

    
    private AllBindingTests() { 
        // Suppresses default constructor, ensuring non-instantiability.
    }
    
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllBindingTests.class);
    }

    public static Test suite() {
        return new TestSuite(new Class[]{
                AbstractConverterTest.class,
                AbstractTableAdapterTest.class,
                BasicComponentFactoryTest.class,
                BeanAdapterTest.class,
                BeanUtilsTest.class,
                BindingsTest.class,
                BoundedRangeAdapterTest.class,
                BufferedValueModelTest.class,
                CombinedTest.class,
                ComboBoxAdapterTest.class,
                ConverterFactoryTest.class,
                ExtendedPropertyChangeSupportTest.class,
                IndirectPropertyChangeSupportTest.class,
                ListHolderTest.class,
                ListModelHolderTest.class,
                ObservableListTest.class,
                PreferencesAdapterTest.class,
                PresentationModelTest.class,
                PropertyAdapterTest.class,
                PropertyConnectorTest.class,
                PropertyExceptionTest.class,
                RadioButtonAdapterTest.class,
                ReflectionTest.class,
                SelectionInListTest.class,
                SelectionInList2Test.class,
                SelectionInListModelTest.class,
                SingleListSelectionAdapterTest.class,
                SpinnerAdapterFactoryTest.class,
                TextComponentConnectorTest.class,
                ToggleButtonAdapterTest.class,
                TriggerTest.class,
                ValueChangeTest.class,
                ValueHolderTest.class},
                "All Binding tests");
    }
}
