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

package com.jgoodies.binding.value;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A ValueModel that wraps another ValueModel, the subject, 
 * and delays changes of the subject's value. Returns the subject's value 
 * until a value has been set. The buffered value is not written to the 
 * subject until the trigger channel changes to <code>Boolean.TRUE</code>.
 * The buffered value can be flushed by changing the trigger channel value
 * to <code>Boolean.FALSE</code>. Note that the commit and flush events
 * are performed only if the trigger channel fires a change event. Since a
 * plain ValueHolder fires no property change event if a value is set that has
 * been set before, it is recommended to use a {@link Trigger} instead
 * and invoke its <code>#triggerCommit</code> and <code>triggerFlush</code>
 * methods.<p>
 * 
 * The BufferedValueModel has been designed to behave much like its subject
 * when accessing the value. Therefore it throws all exceptions that would
 * arise when accessing the subject directly. Hence, attempts to read or
 * write a value while the subject is <code>null</code> are always rejected 
 * with a <code>NullPointerException</code>.<p>
 * 
 * This class provides the bound read-write properties <em>subject</em> and 
 * <em>triggerChannel</em> for the subject and trigger channel and a bound
 * read-only property <em>buffering</em> for the buffering state.<p>
 * 
 * The BufferedValueModel registers listeners with the subject and 
 * trigger channel. It is recommended to remove these listeners by invoking 
 * <code>#release</code> if the subject and trigger channel live much longer 
 * than this buffer. After <code>#release</code> has been called 
 * you must not use the BufferedValueModel instance any longer.
 * As an alternative you may use event listener lists in subjects and
 * trigger channels that are based on <code>WeakReference</code>s.<p>
 * 
 * If the subject value changes while this model is in buffering state
 * this change won't show through as this model's new value. If you want
 * to update the value whenever the subject value changes, register a
 * listener with the subject value and flush this model's trigger.<p>
 * 
 * <strong>Constraints:</strong> The subject is of type <code>Object</code>, 
 * the trigger channel value of type <code>Boolean</code>.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.6 $
 * 
 * @see     ValueModel
 * @see     ValueModel#getValue()
 * @see     ValueModel#setValue(Object)
 */

public final class BufferedValueModel extends AbstractValueModel {
    
    // Names of the bound bean properties *************************************

    /**
     * The name of the bound read-only bean property that indicates
     * whether this models is buffering or in write-through state.
     * 
     * @see #isBuffering()
     */
    public static final String PROPERTYNAME_BUFFERING = "buffering";
    
    /**
     * The name of the bound read-write bean property for the subject.
     * 
     * @see #getSubject()
     * @see #setSubject(ValueModel)
     */
    public static final String PROPERTYNAME_SUBJECT = "subject";
    
    /**
     * The name of the bound read-write bean property for the trigger channel.
     * 
     * @see #getTriggerChannel()
     * @see #setTriggerChannel(ValueModel)
     */
    public static final String PROPERTYNAME_TRIGGER_CHANNEL = "triggerChannel";
    
    
    // ************************************************************************
    
    /**
     * Holds the subject that provides the underlying value 
     * of type <code>Object</code>.
     */
    private ValueModel subject;         
    
    /**
     * Holds the three-state trigger of type <code>Boolean</code>.
     */
    private ValueModel triggerChannel; 

    /**
     * Holds the buffered value. This value is ignored if we are not buffering.
     */ 
    private Object bufferedValue;
    
    /**
     * Indicates whether a value has been assigned since the last trigger change.
     */
    private boolean valueAssigned;
    
    /**
     * Holds a PropertyChangeListener that observes subject value changes.
     */
    private final ValueChangeHandler valueChangeHandler;
    
    /**
     * Holds a PropertyChangeListener that observes trigger changes.
     */
    private final TriggerChangeHandler triggerChangeHandler;
    

    // Instance Creation ****************************************************

    /**
     * Constructs a <code>BufferedValueHolder</code> on the given subject
     * using the given trigger channel. 
     * 
     * @param subject          the value model to be buffered
     * @param triggerChannel   the value model that triggers the commit or flush event
     * @throws NullPointerException  if the triggerChannel is <code>null</code> 
     */
    public BufferedValueModel(
        ValueModel subject,
        ValueModel triggerChannel) {
        valueChangeHandler   = new ValueChangeHandler();
        triggerChangeHandler = new TriggerChangeHandler();
        setSubject(subject);
        setTriggerChannel(triggerChannel);
        setBuffering(false);
    }
    
    
    // Accessing the Subject and Trigger Channel ******************************
    
    /**
     * Returns the subject, i.e. the underlying ValueModel that provides 
     * the unbuffered value.
     * 
     * @return the ValueModel that provides the unbuffered value  
     */
    public ValueModel getSubject() {
        return subject;
    }
    
    /**
     * Sets a new subject ValueModel, i.e. the model that provides 
     * the unbuffered value. Notifies all listeners that the <i>subject</i> 
     * property has changed.
     * 
     * @param newSubject  the subject ValueModel to be set
     */
    public void setSubject(ValueModel newSubject) {
        ValueModel oldSubject = getSubject();
        ReadAccessResult oldReadValue = readBufferedOrSubjectValue();
        Object oldValue = oldReadValue.value;
        if (oldSubject != null) {
            oldSubject.removeValueChangeListener(valueChangeHandler);
        }
        subject = newSubject;
        if (newSubject != null) {
            newSubject.addValueChangeListener(valueChangeHandler);
        }
        firePropertyChange(PROPERTYNAME_SUBJECT, oldSubject, newSubject);
        if (isBuffering()) 
            return;
        
        ReadAccessResult newReadValue = readBufferedOrSubjectValue();
        Object newValue = newReadValue.value;
        // TODO: Check if the following conditional is valid.
        // Note that the old and/or new value may be null 
        // just because the property is read-only.
        if (oldValue != null || newValue != null) {
            fireValueChange(oldValue, newValue, true);
        }
    }
    
    /**
     * Returns the ValueModel that is used to trigger commit and flush events.
     * 
     * @return the ValueModel that is used to trigger commit and flush events
     */
    public ValueModel getTriggerChannel() {
        return triggerChannel;
    }
    
    /**
     * Sets the ValueModel that triggers the commit and flush events.
     *  
     * @param newTriggerChannel  the ValueModel to be set as trigger channel
     * @throws NullPointerException  if the newTriggerChannel is <code>null</code> 
     */
    public void setTriggerChannel(ValueModel newTriggerChannel) {
        if (newTriggerChannel == null) 
            throw new NullPointerException("The trigger channel must not be null.");
        
        ValueModel oldTriggerChannel = getTriggerChannel();
        if (oldTriggerChannel != null) {
            oldTriggerChannel.removeValueChangeListener(triggerChangeHandler);
        }
        triggerChannel = newTriggerChannel;
        //if (newTriggerChannel != null) {
            newTriggerChannel.addValueChangeListener(triggerChangeHandler);
        //}
        firePropertyChange(PROPERTYNAME_TRIGGER_CHANNEL, oldTriggerChannel, newTriggerChannel);
    }
    

    // Implementing the ValueModel Interface ********************************

    /**
     * Returns the subject's value if no value has been set since the last
     * commit or flush, and returns the buffered value otherwise.
     * Attempts to read a value when no subject is set are rejected
     * with a NullPointerException.
     * 
     * @return the buffered value
     * @throws NullPointerException  if no subject is set
     */
    public Object getValue() {
        if (subject == null)
            throw new NullPointerException("The subject must not be null "
                + "when reading a value from a BufferedValueModel.");
        
        return isBuffering() 
            ? bufferedValue 
            : subject.getValue(); 
    }

    /**
     * Sets a new buffered value and turns this BufferedValueModel into
     * the buffering state. The buffered value is not provided to the
     * underlying model until the trigger channel indicates a commit.
     * Attempts to set a value when no subject is set are rejected
     * with a NullPointerException.<p>
     * 
     * The above semantics is easy to understand, however it is tempting
     * to check the new value against the current subject value to avoid 
     * that the buffer unnecessary turns into the buffering state. But here's 
     * a problem. Let's say the subject value is "first" at buffer 
     * creation time, and let's say the subject value has changed in the
     * meantime to "second". Now someone sets the value "second" to this buffer.
     * The subject value and the value to be set are equal. Shall we buffer? 
     * Also, this decision would depend on the ability to read the subject.
     * The semantics would depend on the subject' state and capabilities.<p>
     * 
     * It is often sufficient to observe the buffering state when enabling
     * or disabling a commit command button like "OK" or "Apply". 
     * And later check the <em>changed</em> state in a PresentationModel.
     * You may want to do better and may want to observe a property like
     * "defersTrueChange" that indicates whether flushing a buffer will 
     * actually change the subect. But note that such a state may change
     * with subject value changes, which may be hard to understand for a user.<p>
     * 
     * TODO: Consider adding an optimized execution path for the case
     * that this model is already in buffering state. In this case 
     * the old buffered value can be used instead of invoking 
     * <code>#readBufferedOrSubjectValue()</code>.
     * 
     * @param newBufferedValue   the value to be buffered
     * @throws NullPointerException  if no subject is set
     */
    public void setValue(Object newBufferedValue) {
        if (subject == null)
            throw new NullPointerException("The subject must not be null "
                + "when setting a value to a BufferedValueModel.");
        
        ReadAccessResult oldReadValue = readBufferedOrSubjectValue();
        Object oldValue = oldReadValue.value;
        bufferedValue = newBufferedValue;
        setBuffering(true);
        if (oldReadValue.readable && oldValue == newBufferedValue)
            return;
        fireValueChange(oldValue, newBufferedValue, true); 
    }

    /**
     * Tries to lookup the current buffered or subject value 
     * and returns this value plus a marker that indicates 
     * whether the read-access succeeded or failed.
     * The latter situation arises in an attempt to read a value from
     * a write-only subject if this BufferedValueModel is not buffering
     * and if this model changes its subject.
     * 
     * @return the current value plus a boolean that indicates the success or failure
     */
    private ReadAccessResult readBufferedOrSubjectValue() {
        try {
            Object value = getValue(); // May fail with write-only models
            return new ReadAccessResult(value, true); 
        } catch (Exception e) {
            return new ReadAccessResult(null, false);
        }
    }
    

    // Releasing PropertyChangeListeners **************************************
    
    /**
     * Removes the PropertyChangeListeners from the subject and 
     * trigger channel.<p>
     * 
     * To avoid memory leaks it is recommended to invoke this method 
     * if the subject and trigger channel live much longer than this buffer.
     * Once #release has been invoked the BufferedValueModel instance
     * must not be used any longer.<p>
     * 
     * As an alternative you may use event listener lists in subjects and
     * trigger channels that are based on <code>WeakReference</code>s.
     * 
     * @see java.lang.ref.WeakReference
     */
    public void release() {
        ValueModel aSubject = getSubject();
        if (aSubject != null) {
            aSubject.removeValueChangeListener(valueChangeHandler);
        }
        ValueModel aTriggerChannel = getTriggerChannel();
        if (aTriggerChannel != null) {
            aTriggerChannel.removeValueChangeListener(triggerChangeHandler);
        }
    }
    
    
    // Misc *****************************************************************

    /**
     * Returns whether this model buffers a value or not, that is, whether
     * a value has been assigned since the last commit or flush. 
     * 
     * @return true if a value has been assigned since the last commit or flush
     */
    public boolean isBuffering() {
        return valueAssigned;
    }
    
    private void setBuffering(boolean newValue) {
        boolean oldValue = isBuffering();
        valueAssigned = newValue;
        firePropertyChange(PROPERTYNAME_BUFFERING, oldValue, newValue);
    }
    
    /**
     * Sets the buffered value as new subject value - if any value has been set. 
     * After this commit this BufferedValueHolder behaves as if no value 
     * has been set before. This method is invoked if the trigger has changed 
     * to <code>Boolean.TRUE</code>.<p>
     * 
     * Since the subject's value is assigned <em>after</em> the buffer marker
     * is reset, subject change notifications will be handled. In this case
     * the subject's old value is not this BufferedValueModel's old value;
     * instead the old value reported to listeners of this model
     * is the formerly buffered value.
     * 
     * @throws NullPointerException   if no subject is set
     */
    private void commit() {
        if (isBuffering()) {
            setBuffering(false);
            valueChangeHandler.oldValue = bufferedValue;
            subject.setValue(bufferedValue);
            valueChangeHandler.oldValue = null;
        } else if (subject == null)
            throw new NullPointerException("The subject must not be null "
                + "while commiting a value in a BufferedValueModel.");
    }
    
    /**
     * Flushes the buffered value. This method is invoked if the trigger 
     * has changed to <code>Boolean.FALSE</code>. After this flush 
     * this BufferedValueHolder behaves as if no value has been set before.<p>
     * 
     * TODO: Check whether we need to use #getValueSafe instead of #getValue.
     * 
     * @throws NullPointerException   if no subject is set
     */
    private void flush() {
        Object oldValue = getValue();
        setBuffering(false);
        Object newValue = getValue();
        fireValueChange(oldValue, newValue, true);
    }
    
    
    // Helper Class ***********************************************************
    
    /**
     * Describes the result of a subject value read-access plus a marker
     * that indicates if the value could be read or not. The latter is
     * used in <code>#setValue</code> to suppress some unnecessary
     * change notifications in case the value could be read successfully.
     * 
     * @see BufferedValueModel#setValue(Object)
     */
    private static final class ReadAccessResult {
        
        final Object  value;
        final boolean readable;
        
        private ReadAccessResult(Object value, boolean readable) {
            this.value = value;
            this.readable = readable;
        }
        
    }
    
    
    // Event Handling *********************************************************

    /**
     * Listens to changes of the subject.
     */
    private final class ValueChangeHandler implements PropertyChangeListener {

        Object oldValue;
        
        /**
         * The subject's value has changed. Notifies this BufferedValueModel's
         * listeners iff we are not buffering, does nothing otherwise.<p>
         * 
         * @param evt   the property change event to be handled
         */
        public void propertyChange(PropertyChangeEvent evt) {
            if (!isBuffering()) {
                fireValueChange(
                        oldValue != null ? oldValue : evt.getOldValue(), 
                        evt.getNewValue(), 
                        true);
            }
        }
    }

    
    /**
     * Listens to changes of the trigger channel.
     */
    private final class TriggerChangeHandler implements PropertyChangeListener {

        /**
         * The trigger has been changed. Commits or flushes the buffered value.
         * 
         * @param evt   the property change event to be handled
         */
        public void propertyChange(PropertyChangeEvent evt) {
            if (Boolean.TRUE.equals(evt.getNewValue()))
                commit();
            else if (Boolean.FALSE.equals(evt.getNewValue()))
                flush();
        }
    }
    
    
}
