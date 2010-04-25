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

package com.jgoodies.binding.extras;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.LinkedListModel;
import com.jgoodies.binding.list.ListHolder;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

 
/**
 * Represents a selection in a List. Provides bound bean properties for 
 * the list, the selection, the selection index, and the selection empty state.
 * Selection changes fire an event only if the old and new value are not equal.
 * If you need to compare the identity you can use and observe the selection 
 * index instead of the selection or value.<p>
 * 
 * <strong>Note:</strong> This class is not yet part of the binary Binding 
 * library; it comes with the Binding distributions as an extra. 
 * <strong>The API is work in progress and may change without notice; 
 * this class may even be completely removed from future distributions.</strong> 
 * If you want to use this class, you may consider copying it into 
 * your codebase.<p>
 * 
 * The SelectionInList2 uses three ValueModels to hold the list, the selection
 * and selection index and provides bound bean properties for these models. 
 * You can access, observe and replace these ValueModels. This is useful 
 * to connect a SelectionInList2 with other ValueModels; for example you can
 * use the SelectionInList2's selection holder as bean channel for a 
 * PresentationModel. See the Binding tutorial classes for examples on how
 * to connect a SelectionInList2 with a PresentationModel.<p>
 * 
 * This class also implements the {@link ListModel} interface that allows
 * API users to observe fine grained changes in the structure and contents
 * of the list. Hence instances of this class can be used directly as model of 
 * a JList. If you want to use a SelectionInList2 with a JComboBox or JTable, 
 * you can convert the SelectionInList2 to the associated component model 
 * interfaces using the adapter classes 
 * {@link com.jgoodies.binding.adapter.ComboBoxAdapter}
 * and {@link com.jgoodies.binding.adapter.AbstractTableAdapter} respectively.
 * These classes are part of the Binding library too.<p>
 * 
 * Unlike the older SelectionInList, the SelectionInList2 supports only
 * <code>List</code>s as content of its List holder. The SelectionInListModel
 * supports <code>ListModel</code>s as content of its ListModel holder.
 * The SelectionInList2 and SelectionInListModel differ in how precise 
 * they can fire events about changes to the content and structure of 
 * the underlying List or ListModel content. The SelectionInList2
 * can only report that the List changes completely; this is done by emitting 
 * a PropertyChangeEvent for the <em>list</em> property. Also, 
 * a <code>ListDataEvent</code> is fired that reports a complete change.
 * The SelectionInListModel reports the same PropertyChangeEvent. 
 * But fine grained changes in the ListModel will be forwarded 
 * as fine grained changes in the content, added and removed elements.<p>
 * 
 * If the List or ListModel content doesn't change at all, or if it always 
 * changes completely, there's no differences between the SelectionInListModel
 * and the SelectionInList2.  
 * But if the list structure or content changes, the ListModel reports more 
 * fine grained events to registered ListDataListeners, which in turn allows
 * list views to chooser better user interface gestures: for example, a table 
 * with scroll pane may retain the current selection and scroll offset.<p>
 * 
 * An example for the benefit of fine grained ListDataEvents is the asynchronous 
 * transport of list elements from a server to a client. Let's say you transport 
 * the list elements in portions of 10 elements to improve the application's 
 * responsiveness. The user can then work with the SelectionInListModel 
 * as soon as the ListModel gets populated. If at a later time more elements 
 * are added to the ListModel, the SelectionInListModel can retain the selection 
 * index (and selection) and will just report a ListDataEvent about
 * the interval added. JList, JTable and JComboBox will then just add 
 * the new elements at the end of the list presentation.<p>
 * 
 * If you want to combine List operations and the ListModel change reports, 
 * you may consider using an implementation that combines these two interfaces, 
 * for example {@link ArrayListModel} or {@link LinkedListModel}.<p>
 * 
 * This binding library provides some help for firing PropertyChangeEvents 
 * if the old ListModel and new ListModel are equal but not the same.
 * Class {@link com.jgoodies.binding.beans.ExtendedPropertyChangeSupport} 
 * allows to permanently or individually check the identity (using 
 * <code>==</code>) instead of checking the equity (using <code>#equals</code>).
 * Class {@link com.jgoodies.binding.beans.Model} uses this extended 
 * property change support. And class {@link ValueHolder} uses it too 
 * and can be configured to always test the identity.<p> 
 * 
 * This class inherits public convenience methods for firing ListDataEvents, 
 * see the methods <code>#fireContentsChanged</code>, 
 * <code>#fireIntervalAdded</code>, and <code>#fireIntervalRemoved</code>.
 * These are automatically invoked if the list holder holds a ListModel
 * that fires these events. If on the other hand the underlying List or
 * ListModel does not fire a required ListDataEvent, you can use these
 * methods to notify presentations about a change. It is recommended
 * to avoid sending duplicate ListDataEvents; hence check if the underlying
 * ListModel fires the necessary events or not. Typically an underlying
 * ListModel will fire the add and remove events; but often it'll lack 
 * an event if the (seletcted) contents has changed. A convenient way to
 * indicate that change is <code>#fireSelectedContentsChanged</code>. See 
 * the tutorial's AlbumManagerModel for an example how to use this feature.<p> 
 * 
 * <strong>Constraints:</strong> The list holder holds instances 
 * of {@link List}, the selection holder values of type <code>Object</code> 
 * and the selection index holder of type <code>Integer</code>. The selection
 * index holder must hold non-null index values; however, when firing
 * an index value change event, both the old and new value may be null.
 * If the ListModel changes, the underyling ValueModel must fire
 * a PropertyChangeEvent.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.7 $
 * 
 * @see     SelectionInListModel
 * @see     ValueModel
 * @see     List
 * @see     ListModel
 * @see     com.jgoodies.binding.adapter.ComboBoxAdapter
 * @see     com.jgoodies.binding.adapter.AbstractTableAdapter
 * @see     com.jgoodies.binding.beans.ExtendedPropertyChangeSupport
 * @see     com.jgoodies.binding.beans.Model
 * @see     com.jgoodies.binding.value.ValueHolder
 * 
 * @since 1.1
 */
public final class SelectionInList2 extends ListHolder {
    
    
    // Constant Names for Bound Bean Properties *******************************
   
    /**
     * The name of the bound read-write <em>selection</em> property.
     */
    public static final String PROPERTYNAME_SELECTION = "selection";

    /**
     * The name of the bound read-only <em>selectionEmpty</em> property.
     */
    public static final String PROPERTYNAME_SELECTION_EMPTY = "selectionEmpty";

    /**
     * The name of the bound read-write <em>selection holder</em> property.
     */
    public static final String PROPERTYNAME_SELECTION_HOLDER = "selectionHolder";

    /**
     * The name of the bound read-write <em>selectionIndex</em> property.
     */
    public static final String PROPERTYNAME_SELECTION_INDEX = "selectionIndex";

    /**
     * The name of the bound read-write <em>selection index holder</em> property.
     */
    public static final String PROPERTYNAME_SELECTION_INDEX_HOLDER = "selectionIndexHolder";

    
    // ************************************************************************
    
    /**
     * A special index that indicates that we have no selection. 
     */
    private static final int NO_SELECTION_INDEX = -1;
    
    
    // Instance Fields ********************************************************
    
    /** 
     * Holds the selection, an instance of <code>Object</code>.          
     */
    private ValueModel selectionHolder;
    
    /** 
     * Holds the selection index, an <code>Integer</code>.           
     */
    private ValueModel selectionIndexHolder;

    /**
     * The <code>PropertyChangeListener</code> used to handle
     * changes of the selection.
     */
    private final PropertyChangeListener selectionChangeHandler;

    /**
     * The <code>PropertyChangeListener</code> used to handle
     * changes of the selection index.
     */
    private final PropertyChangeListener selectionIndexChangeHandler;
    
    /**
     * Duplicates the value of the selectionHolder.
     * Used to provide better old values in PropertyChangeEvents
     * fired after selectionIndex changes.
     */
    private Object oldSelection;
    
    /**
     * Duplicates the value of the selectionIndexHolder.
     * Used to provide better old values in PropertyChangeEvents
     * fired after selectionIndex changes and selection changes.
     */
    private int oldSelectionIndex;
    
    
    // Instance creation ****************************************************

    /**
     * Constructs a SelectionInList2 with an empty initial 
     * ArrayList using defaults for the selection holder 
     * and selection index holder.
     */
    public SelectionInList2() {     
        this(new ArrayList());
    }

    
    /**
     * Constructs a SelectionInList2 on the given item array 
     * using defaults for the selection holder and selection index holder.
     * The specified array will be converted to a List.<p>
     * 
     * Changes to the list "write through" to the array, and changes 
     * to the array contents will be reflected in the list.
     *  
     * @param listItems        the array of initial items
     * 
     * @throws NullPointerException if <code>listItems</code> is <code>null</code>
     */
    public SelectionInList2(Object[] listItems) {     
        this(Arrays.asList(listItems));
    }


    /**
     * Constructs a SelectionInList2 on the given item array and
     * selection holder using a default selection index holder.
     * The specified array will be converted to a List.<p>
     * 
     * Changes to the list "write through" to the array, and changes 
     * to the array contents will be reflected in the list.
     *  
     * @param listItems        the array of initial items
     * @param selectionHolder  holds the selection
     * 
     * @throws NullPointerException if <code>listItems</code> or
     *     <code>selectionHolder</code> is <code>null</code>
     */
    public SelectionInList2(Object[] listItems, ValueModel selectionHolder) {     
        this(Arrays.asList(listItems), selectionHolder);
    }

    
    /**
     * Constructs a SelectionInList2 on the given item array and
     * selection holder using a default selection index holder.
     * The specified array will be converted to a List.<p>
     * 
     * Changes to the list "write through" to the array, and changes 
     * to the array contents will be reflected in the list.
     *  
     * @param listItems        the array of initial items
     * @param selectionHolder  holds the selection
     * @param selectionIndexHolder  holds the selection index
     * 
     * @throws NullPointerException if <code>listItems</code>, 
     *     <code>selectionHolder</code>, or <code>selectionIndexHolder</code> 
     *     is <code>null</code>
     */
    public SelectionInList2(
            Object[] listItems, 
            ValueModel selectionHolder,
            ValueModel selectionIndexHolder) {     
        this(Arrays.asList(listItems), selectionHolder, selectionIndexHolder);
    }

    
    /**
     * Constructs a SelectionInList2 on the given List 
     * using defaults for the selection holder and selection index holder.
     *  
     * @param list        the initial list
     */
    public SelectionInList2(List list) {     
        this(new ValueHolder(list));
    }


    /**
     * Constructs a SelectionInList2 on the given List and 
     * selection holder using a default selection index holder.
     *  
     * @param list             the initial list
     * @param selectionHolder  holds the selection
     * 
     * @throws NullPointerException 
     *     if <code>selectionHolder</code> is <code>null</code>
     */
    public SelectionInList2(List list, ValueModel selectionHolder) {     
        this(new ValueHolder(list), selectionHolder);
    }


    /**
     * Constructs a SelectionInList2 on the given List, 
     * selection holder, and selection index holder.
     *  
     * @param list                  the initial list
     * @param selectionHolder       holds the selection
     * @param selectionIndexHolder  holds the selection index
     * 
     * @throws NullPointerException if <code>selectionHolder</code>, 
     *     or <code>selectionIndexHolder</code> is <code>null</code>
     */
    public SelectionInList2(
            List list, 
            ValueModel selectionHolder,
            ValueModel selectionIndexHolder) {     
        this(new ValueHolder(list), 
             selectionHolder,
             selectionIndexHolder);
    }


    /**
     * Constructs a SelectionInList2 on the given list holder
     * using defaults for the selection holder and selection index holder.
     * 
     * @param listHolder          holds the list
     * 
     * @throws NullPointerException 
     *     if <code>listHolder</code> is <code>null</code>
     */
    public SelectionInList2(ValueModel listHolder) {     
        this(listHolder, new ValueHolder(null, true));
    }


    /**
     * Constructs a SelectionInList2 on the given list holder,
     * selection holder and selection index holder.
     * 
     * @param listHolder             holds the list
     * @param selectionHolder        holds the selection
     * @throws NullPointerException  if <code>listHolder</code>
     *     or <code>selectionHolder</code> is <code>null</code>
     */
    public SelectionInList2(ValueModel listHolder, ValueModel selectionHolder) {
        this(
            listHolder,
            selectionHolder,
            new ValueHolder(new Integer(NO_SELECTION_INDEX)));
    }
    

    /**
     * Constructs a SelectionInList2 on the given list holder,
     * selection holder and selection index holder.
     * 
     * @param listHolder               holds the list
     * @param selectionHolder          holds the selection
     * @param selectionIndexHolder     holds the selection index
     * @throws NullPointerException    if <code>listHolder</code>,
     *     <code>selectionHolder</code>, or <code>selectionIndexHolder</code> 
     *     is <code>null</code>
     * @throws IllegalArgumentException if the listHolder contents 
     *     is neither a List nor a ListModel
     */
    public SelectionInList2(
        ValueModel listHolder, 
        ValueModel selectionHolder,
        ValueModel selectionIndexHolder) {
        super(listHolder);
        if (selectionHolder == null)
            throw new NullPointerException("The selection holder must not be null.");
        if (selectionIndexHolder == null)
            throw new NullPointerException("The selection index holder must not be null.");

        selectionChangeHandler      = new SelectionChangeHandler();
        selectionIndexChangeHandler = new SelectionIndexChangeHandler();

        this.selectionHolder = selectionHolder;
        this.selectionIndexHolder = selectionIndexHolder;
        initializeSelectionIndex();

        this.selectionHolder.addValueChangeListener(selectionChangeHandler);
        this.selectionIndexHolder.addValueChangeListener(selectionIndexChangeHandler);
    }

    
    // Accessing the List, Selection and Index ********************************

    /**
     * Looks up and returns the current selection using 
     * the current selection index. Returns <code>null</code> if 
     * no object is selected or if the list has no elements.
     * 
     * @return the current selection, <code>null</code> if none is selected
     */
    public Object getSelection() {
        return getSafeElementAt(getSelectionIndex());
    }
    
    /**
     * Sets the first list element that equals the given new selection
     * as new selection. Does nothing if the list is empty.
     * 
     * @param newSelection   the object to be set as new selection
     */
    public void setSelection(Object newSelection) {
        if (!isEmpty())
            setSelectionIndex(indexOf(newSelection));
    }
    
    /**
     * Returns the selection holder.
     * 
     * @return the selection holder
     */
    public ValueModel getSelectionHolder() {
        return selectionHolder;
    }
    
    /**
     * Sets a new selection holder. 
     * Does nothing if the new is the same as before.
     * The selection remains unchanged and is still driven
     * by the selection index holder. It's just that future
     * index changes will update the new selection holder
     * and that future selection holder changes affect the
     * selection index.
     * 
     * @param newSelectionHolder   the selection holder to set
     * 
     * @throws NullPointerException if the new selection holder is null
     */
    public void setSelectionHolder(ValueModel newSelectionHolder) {
        if (newSelectionHolder == null) 
            throw new NullPointerException("The new selection holder must not be null.");
        
        ValueModel oldSelectionHolder = getSelectionHolder();
        if (equals(oldSelectionHolder, newSelectionHolder))
            return;
            
        oldSelectionHolder.removeValueChangeListener(selectionChangeHandler);
        selectionHolder = newSelectionHolder;
        oldSelection = newSelectionHolder.getValue();
        newSelectionHolder.addValueChangeListener(selectionChangeHandler);
        firePropertyChange(PROPERTYNAME_SELECTION_HOLDER, 
                           oldSelectionHolder,
                           newSelectionHolder);
    }
    
    /**
     * Returns the selection index.
     * 
     * @return the selection index
     * 
     * @throws NullPointerException if the selection index holder 
     *     has a null Object set
     */
    public int getSelectionIndex() {
        return ((Integer) getSelectionIndexHolder().getValue()).intValue();
    }
    
    /**
     * Sets a new selection index. Does nothing if it is the same as before.
     * 
     * @param newSelectionIndex   the selection index to be set
     * @throws IndexOutOfBoundsException if the new selection index
     *    is outside the bounds of the list
     */
    public void setSelectionIndex(int newSelectionIndex) {
        if (newSelectionIndex < NO_SELECTION_INDEX || newSelectionIndex > getSize())
            throw new IndexOutOfBoundsException(
                    "The selection index must be between -1 and " + getSize());
        
        oldSelectionIndex = getSelectionIndex();
        if (oldSelectionIndex == newSelectionIndex)
            return;
        
        getSelectionIndexHolder().setValue(new Integer(newSelectionIndex));
    }
    

    // Accessing the Holders for: List, Selection and Index *******************

    /**
     * Returns the selection index holder.
     * 
     * @return the selection index holder
     */
    public ValueModel getSelectionIndexHolder() {
        return selectionIndexHolder;
    }
    
    /**
     * Sets a new selection index holder. 
     * Does nothing if the new is the same as before.
     * 
     * @param newSelectionIndexHolder   the selection index holder to set
     * 
     * @throws NullPointerException if the new selection index holder is null
     * @throws IllegalArgumentException if the value of the new selection index
     *     holder is null
     */
    public void setSelectionIndexHolder(ValueModel newSelectionIndexHolder) {
        if (newSelectionIndexHolder == null) 
            throw new NullPointerException("The new selection index holder must not be null.");
        
        if (newSelectionIndexHolder.getValue() == null)
            throw new IllegalArgumentException("The value of the new selection index holder must not be null.");
        
        ValueModel oldSelectionIndexHolder = getSelectionIndexHolder();
        if (equals(oldSelectionIndexHolder, newSelectionIndexHolder))
            return;
            
        oldSelectionIndexHolder.removeValueChangeListener(selectionIndexChangeHandler);
        selectionIndexHolder = newSelectionIndexHolder;
        newSelectionIndexHolder.addValueChangeListener(selectionIndexChangeHandler);
        oldSelectionIndex = getSelectionIndex();
        oldSelection = getSafeElementAt(oldSelectionIndex);
        firePropertyChange(PROPERTYNAME_SELECTION_INDEX_HOLDER, 
                           oldSelectionIndexHolder,
                           newSelectionIndexHolder);
    }
    
    
    // Convenience Code *******************************************************
    
    
    /**
     * Checks and answers if an element is selected.
     * 
     * @return true if an element is selected, false otherwise
     */
    public boolean hasSelection() {
        return getSelectionIndex() != NO_SELECTION_INDEX;
    }
    
    
    /**
     * Checks and answers whether the selection is empty or not.
     * Unlike #hasSelection, the underlying property #selectionEmpty
     * for this method is bound. I.e. you can observe this property
     * using a PropertyChangeListener to update UI state.
     *
     * @return true if nothing is selected, false if there's a selection
     * @see #clearSelection
     * @see #hasSelection
     */
    public boolean isSelectionEmpty() {
        return !hasSelection();
    }
    
    
    /**
     * Clears the selection of this SelectionInList2 - if any.
     */
    public void clearSelection() {
        setSelectionIndex(NO_SELECTION_INDEX);
    }

    
    // ListModel Helper Code **************************************************
    
    /**
     * Notifies all registered ListDataListeners that the contents 
     * of the selected list item - if any - has changed.
     * Useful to update a presentation after editing the selection.
     * See the tutorial's AlbumManagerModel for an example how to use
     * this feature.<p>
     * 
     * If the list holder holds a ListModel, this SelectionInList2 listens
     * to ListDataEvents fired by that ListModel, and forwards these events
     * by invoking the associated <code>#fireXXX</code> method, which in turn
     * notifies all registered ListDataListeners. Therefore if you fire 
     * ListDataEvents in an underlying ListModel, you don't need this method 
     * and should not use it to avoid sending duplicate ListDataEvents.
     *
     * @see ListModel
     * @see ListDataListener
     * @see ListDataEvent
     */
    public void fireSelectedContentsChanged() {
        if (hasSelection()) {
            int selectionIndex = getSelectionIndex();
            fireContentsChanged(selectionIndex, selectionIndex);
        }
    }
    
    
    // Misc ******************************************************************
    
    /**
     * Removes the internal listeners from the list holder, selection holder,
     * selection index holder. This SelectionInList2
     * must not be used after calling <code>#release</code>.<p>
     * 
     * To avoid memory leaks it is recommended to invoke this method, 
     * if the list holder, selection holder, or selection index holder 
     * live much longer than this SelectionInList2.
     * Instead of releasing the SelectionInList2, you typically make 
     * the list holder, selection holder, and selection index holder
     * obsolete by releasing the PresentationModel or BeanAdapter that has
     * created them before.<p>
     * 
     * As an alternative you may use ValueModels that in turn use 
     * event listener lists implemented using <code>WeakReference</code>.<p>
     * 
     * Basically this release method performs the reverse operation
     * performed during the SelectionInList2 construction.
     * 
     * @see PresentationModel#release()
     * @see BeanAdapter#release()
     * @see java.lang.ref.WeakReference
     *
     * @since 1.2
     */
    public void release() {
        super.release();
        selectionHolder.removeValueChangeListener(selectionChangeHandler);
        selectionIndexHolder.removeValueChangeListener(selectionIndexChangeHandler);
    }


    // Overriding Superclass Behavior *****************************************
    
    /**
     * Updates the selection index and fires a property change for
     * the list and a contents change event.
     * 
     * @param oldList   the old List content
     * @param newList   the new List content
     */
    protected void updateList(List oldList, List newList) {
        boolean hadSelection = hasSelection();
        Object oldSelectionHolderValue = hadSelection
            ? getSelectionHolder().getValue()
            : null;
        list = newList;
        firePropertyChange(PROPERTYNAME_LIST, oldList, newList);
        fireListChanged(getSize(oldList) - 1, getSize(newList) - 1);
        if (hadSelection) {
            setSelectionIndex(indexOf(newList, oldSelectionHolderValue));
        }
    }
    
    
    // Helper Code ***********************************************************
    
    /**
     * Returns the index in the list of the first occurrence of the specified
     * element, or -1 if the list does not contain this element.
     *
     * @param element  the element to search for
     * @return the index in the list of the first occurrence of the 
     *     given element, or -1 if the list does not contain this element.
     */    
    private int indexOf(Object element) {
        return indexOf(getList(), element);
    }
    
    
    private int indexOf(List aList, Object element) {
        if ( aList == null || element == null)
            return NO_SELECTION_INDEX;
        
        return aList.indexOf(element);
    }
    
    
    private Object getSafeElementAt(int index) {
        return (index < 0 || index >= getSize())
            ? null
            : getElementAt(index);
    }

    
    /**
     * Sets the index according to the selection and initializes
     * the copied selection and selection index.
     * This method is invoked by the constructors to synchronize
     * the selection and index. No listeners are installed yet.
     */
    private void initializeSelectionIndex() {
        Object selectionValue = selectionHolder.getValue();
        if (selectionValue != null) {
            setSelectionIndex(indexOf(selectionValue));
        }
        oldSelection      = selectionValue;
        oldSelectionIndex = getSelectionIndex();
    }
    
    
    // Event Handlers *********************************************************
        
    /**
     * Listens to changes of the selection.
     */
    private final class SelectionChangeHandler implements PropertyChangeListener {

        /**
         * The selection has been changed. Updates the selection index holder's
         * value and notifies registered listeners about the changes - if any -
         * in the selection index, selection empty, selection, and value.<p>
         * 
         * Adjusts the selection holder's value and the old selection index
         * before any event is fired. This ensures that the event old and
         * new values are consistent with the SelectionInList2's state.
         * 
         * @param evt   the property change event to be handled
         */
        public void propertyChange(PropertyChangeEvent evt) {
            Object oldValue     = evt.getOldValue();
            Object newSelection = evt.getNewValue();
            int newSelectionIndex = indexOf(newSelection);
            if (newSelectionIndex != oldSelectionIndex) {
                selectionIndexHolder.removeValueChangeListener(selectionIndexChangeHandler);
                selectionIndexHolder.setValue(new Integer(newSelectionIndex));
                selectionIndexHolder.addValueChangeListener(selectionIndexChangeHandler);
            }
            int theOldSelectionIndex = oldSelectionIndex;
            oldSelectionIndex = newSelectionIndex;
            oldSelection      = newSelection;
            firePropertyChange(PROPERTYNAME_SELECTION_INDEX, 
                    theOldSelectionIndex, 
                    newSelectionIndex);
            firePropertyChange(PROPERTYNAME_SELECTION_EMPTY, 
                    theOldSelectionIndex == NO_SELECTION_INDEX,
                    newSelectionIndex    == NO_SELECTION_INDEX);
            /*
             * Implementation Note: The following two lines fire the 
             * PropertyChangeEvents for the 'selection' and 'value' properties. 
             * If the old and new value are equal, no event is fired.
             * 
             * TODO: Consider using ==, not equals to check for changes.
             * That would enable API users to use the selection holder with
             * beans that must be checked with ==, not equals.
             * However, the SelectionInList2's List would still use equals
             * to find the index of an element.
             */
            firePropertyChange(PROPERTYNAME_SELECTION, oldValue, newSelection);
        }
    }

    /**
     * Listens to changes of the selection index.
     */
    private final class SelectionIndexChangeHandler implements PropertyChangeListener {

        /**
         * The selection index has been changed. Updates the selection holder
         * value and notifies registered listeners about changes - if any -
         * in the selection index, selection empty, selection, and value.<p>
         * 
         * Handles null old values in the index PropertyChangeEvent.
         * Ignores null new values in this events, because the selection
         * index value must always be a non-null value.<p>
         * 
         * Adjusts the selection holder's value and the old selection index
         * before any event is fired. This ensures that the event old and
         * new values are consistent with the SelectionInList2's state.
         * 
         * @param evt   the property change event to be handled
         */
        public void propertyChange(PropertyChangeEvent evt) {
            int newSelectionIndex = getSelectionIndex();
            Object theOldSelection = oldSelection;
            //Object oldSelection = getSafeElementAt(oldSelectionIndex);
            Object newSelection = getSafeElementAt(newSelectionIndex);
            /*
             * Implementation Note: The following conditional suppresses 
             * value change events if the old and new selection are equal.
             * 
             * TODO: Consider using ==, not equals to check for changes.
             * That would enable API users to use the selection holder with
             * beans that must be checked with ==, not equals.
             * However, the SelectionInList2's List would still use equals
             * to find the index of an element.
             */
            if (!SelectionInList2.this.equals(theOldSelection, newSelection)) {
                selectionHolder.removeValueChangeListener(selectionChangeHandler);
                selectionHolder.setValue(newSelection);
                selectionHolder.addValueChangeListener(selectionChangeHandler);
            }
            int theOldSelectionIndex = oldSelectionIndex;
            oldSelectionIndex = newSelectionIndex;
            oldSelection      = newSelection;
            firePropertyChange(PROPERTYNAME_SELECTION_INDEX, 
                    theOldSelectionIndex, 
                    newSelectionIndex);
            firePropertyChange(PROPERTYNAME_SELECTION_EMPTY, 
                    theOldSelectionIndex == NO_SELECTION_INDEX,
                    newSelectionIndex    == NO_SELECTION_INDEX);
            firePropertyChange(PROPERTYNAME_SELECTION, theOldSelection, newSelection);
        }
    }


}
