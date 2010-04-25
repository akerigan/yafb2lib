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

package com.jgoodies.binding.tests.beans;


/**
 * Consists of a bunch of Java Bean classes and Bean-like classes
 * that are used by the PropertyAdapter unit tests.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.6 $
 */
public final class BeanClasses {
    
    private static final Class[] OBSERVABLE_CLASSES =
        {   ObservableBean.class, 
            ObservableBeanWithNamedPCLSupport.class };
            
    private static final Class[] UNOBSERVABLE_CLASSES =
        {   UnobservableBean.class,
            BeanWithPCLAdder.class,
            BeanWithPCLRemover.class,
            UnobservableBeanWithNamedPCLSupport.class };

            
    private BeanClasses() {
        // Override default constructor; prevents instantiation.
    }
    
    
    // Accessing Bean Classes ************************************************

    /**
     * Returns an array of bean classes that are observable,
     * i.e. that provide support for bound properties.
     * 
     * @return an array of observable bean classes
     */
    public static Class[] getObservableClasses() {
        return OBSERVABLE_CLASSES;
    }
    
    /**
     * Returns an array of bean classes that are not observable,
     * i.e. that do not provide support for bound properties.
     * 
     * @return an array of unobservable bean classes
     */
    public static Class[] getUnobservableClasses() {
        return UNOBSERVABLE_CLASSES;
    }


    // Accessing Bean Instance ***********************************************
    
    /**
     * Returns a bean that provides support for bound properties.
     * 
     * @return an observable bean
     */
    public static Object getObservable() {
        return new ObservableBean();
    }

    /**
     * Returns an array of bean instances that provide support
     * for bound properties.
     * 
     * @return an array of observable beans instances
     */
    public static Object[] getObservableBeans() {
        Object[] beans = new Object[OBSERVABLE_CLASSES.length];
        for (int i = 0; i < OBSERVABLE_CLASSES.length; i++) {
            Class beanClass = OBSERVABLE_CLASSES[i];
            try {
                beans[i] = beanClass.newInstance();
            } catch (Exception e) {
                // Should not happen in this context
            }
        }
        return beans;
    }
    
    /**
     * Returns a bean that provides no support for bound properties.
     * 
     * @return an unobservable bean
     */
    public static Object getUnobservable() {
        return new UnobservableBean();
    }

    /**
     * Returns an array of bean instances that provide no support
     * for bound properties.
     * 
     * @return an array of unobservable beans instances
     */
    public static Object[] getUnobservableBeans() {
        Object[] beans = new Object[UNOBSERVABLE_CLASSES.length];
        for (int i = 0; i < UNOBSERVABLE_CLASSES.length; i++) {
            Class beanClass = UNOBSERVABLE_CLASSES[i];
            try {
                beans[i] = beanClass.newInstance();
            } catch (Exception e) {
                // Should not happen in this context
            }
        }
        return beans;
    }

    /**
     * Returns an array of bean instances where some provide 
     * support for bound properties, and others do not.
     * 
     * @return an array of beans instances
     */
    public static Object[] getBeans() {
        Object[] beans = new Object[OBSERVABLE_CLASSES.length +
                                    UNOBSERVABLE_CLASSES.length];
        for (int i = 0; i < OBSERVABLE_CLASSES.length; i++) {
            Class beanClass = OBSERVABLE_CLASSES[i];
            try {
                beans[i] = beanClass.newInstance();
            } catch (Exception e) {
                // Should not happen in this context
            }
        }
        int offset = OBSERVABLE_CLASSES.length;
        for (int i = 0; i < UNOBSERVABLE_CLASSES.length; i++) {
            Class beanClass = UNOBSERVABLE_CLASSES[i];
            try {
                beans[i+offset] = beanClass.newInstance();
            } catch (Exception e) {
                // Should not happen in this context
            }
        }
        return beans;
    }



}
