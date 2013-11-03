package org.valkyriercp.binding.value.support;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.valkyriercp.binding.value.ObservableList;
import org.valkyriercp.binding.value.ValueModel;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * A <code>BufferedValueModel</code> that uses an ObservableList as a buffer to hold
 * chandes to a <code>Collection</code> or <code>array</code>. Internally this is
 * called the "buffered list model."
 * <p>
 * On commit the following steps occur:
 * <ol>
 * <li>a new instance of the backing collection type is created</li>
 * <li>the contents of the list model is inserted into this new collection</li>
 * <li>the new collection is saved into the underlying collection's value model</li>
 * <li>the structure of the list model is compared to the structure of the new underlying
 * collection and if they differ the list model is updated to reflect the new structure.</li>
 * </ol>
 * <p>
 * NOTE: Between calls to commit the list model adheres to the contract defined in
 * <code>java.util.List</code> NOT the contract of the underlying collection's type.
 * This can result in the list model representing a state that is not possible for the
 * underlying collection.
 *
 *
 * @author oliverh
 */
public class BufferedCollectionValueModel extends BufferedValueModel {

    private final ListChangeHandler listChangeHandler = new ListChangeHandler();

    private final Class wrappedType;

    private final Class wrappedConcreteType;

    private ObservableList bufferedListModel;

    /**
     * Constructs a new BufferedCollectionValueModel.
     *
     * @param wrappedModel the value model to wrap
     * @param wrappedType the class of the value contained by wrappedModel; this must be
     *            assignable to <code>java.util.Collection</code> or
     *            <code>Object[]</code>.
     */
    public BufferedCollectionValueModel(ValueModel wrappedModel, Class wrappedType) {
        super(wrappedModel);
        Assert.notNull(wrappedType);
        this.wrappedType = wrappedType;
        this.wrappedConcreteType = getConcreteCollectionType(wrappedType);
        updateBufferedListModel(getWrappedValue());
        if (getValue() != bufferedListModel) {
            super.setValue(bufferedListModel);
        }
    }

    public void setValue(Object value) {
        if (value != bufferedListModel) {
            if (!hasSameStructure()) {
                updateBufferedListModel(value);
                fireValueChange(bufferedListModel, bufferedListModel);
            }
        }
    }

    protected Object getValueToCommit() {
        Object wrappedValue = getWrappedValue();
        // If the wrappedValue is null and the buffer is empty
        // just return null rather than an empty collection
        if (wrappedValue == null && bufferedListModel.size() == 0)
            return null;

        return createCollection(wrappedValue);
    }

    //    protected void doBufferedValueCommit(Object bufferedValue) {
    //        if (hasSameStructure()) {
    //            return;
    //        }
    //        getWrappedValueModel().setValue(createCollection());
    //        if (hasSameStructure()) {
    //            return;
    //        }
    //        updateListModel(getWrappedValue());
    //    }

    public static Class getConcreteCollectionType(Class wrappedType) {
        Class class2Create;
        if (wrappedType.isArray()) {
            if (ClassUtils.isPrimitiveArray(wrappedType)) {
                throw new IllegalArgumentException("wrappedType can not be an array of primitive types");
            }
            class2Create = wrappedType;
        }
        else if (wrappedType == Collection.class) {
            class2Create = ArrayList.class;
        }
        else if (wrappedType == List.class) {
            class2Create = ArrayList.class;
        }
        else if (wrappedType == Set.class) {
            class2Create = HashSet.class;
        }
        else if (wrappedType == SortedSet.class) {
            class2Create = TreeSet.class;
        }
        else if (Collection.class.isAssignableFrom(wrappedType)) {
            if (wrappedType.isInterface()) {
                throw new IllegalArgumentException("unable to handle Collection of type [" + wrappedType
                        + "]. Do not know how to create a concrete implementation");
            }
            class2Create = wrappedType;
        }
        else {
            throw new IllegalArgumentException("wrappedType [" + wrappedType + "] must be an array or a Collection");
        }
        return class2Create;
    }

    /**
     * Checks if the structure of the buffered list model is the same as the wrapped
     * collection. "same structure" is defined as having the same elements in the
     * same order with the one exception that NULL == empty list.
     */
    private boolean hasSameStructure() {
        Object wrappedCollection = getWrappedValue();
        if (wrappedCollection == null) {
            return bufferedListModel.size() == 0;
        }
        else if (wrappedCollection instanceof Object[]) {
            Object[] wrappedArray = (Object[])wrappedCollection;
            if (wrappedArray.length != bufferedListModel.size()) {
                return false;
            }
            for (int i = 0; i < bufferedListModel.size(); i++) {
                if(super.hasValueChanged(wrappedArray[i], bufferedListModel.get(i))) {
                    return false;
                }
            }
        }
        else {
            if (((Collection)wrappedCollection).size() != bufferedListModel.size()) {
                return false;
            }
            for (Iterator i = ((Collection)wrappedCollection).iterator(), j = bufferedListModel.iterator(); i.hasNext();) {
                if (super.hasValueChanged(i.next(), j.next())) {
                    return false;
                }
            }
        }
        return true;
    }

    private Object createCollection(Object wrappedCollection) {
        return populateFromListModel(createNewCollection(wrappedCollection));
    }

    private Object createNewCollection(Object wrappedCollection) {
        if (wrappedConcreteType.isArray())
            return Array.newInstance(wrappedConcreteType.getComponentType(), bufferedListModel.size());

        Object newCollection;
        if (SortedSet.class.isAssignableFrom(wrappedConcreteType) && wrappedCollection instanceof SortedSet
                && ((SortedSet)wrappedCollection).comparator() != null) {
            try {
                Constructor con = wrappedConcreteType.getConstructor(new Class[] {Comparator.class});
                newCollection = BeanUtils.instantiateClass(con,
                        new Object[]{((SortedSet) wrappedCollection).comparator()});
            }
            catch (NoSuchMethodException e) {
                throw new FatalBeanException("Could not instantiate SortedSet class ["
                        + wrappedConcreteType.getName() + "]: no constructor taking Comparator found", e);
            }
        }
        else {
            newCollection = BeanUtils.instantiateClass(wrappedConcreteType);
        }
        return newCollection;
    }

    private Object populateFromListModel(Object collection) {
        if (collection instanceof Object[]) {
            Object[] wrappedArray = (Object[])collection;
            for (int i = 0; i < bufferedListModel.size(); i++) {
                wrappedArray[i] = bufferedListModel.get(i);
            }
        }
        else {
            Collection wrappedCollection = ((Collection)collection);
            wrappedCollection.clear();
            wrappedCollection.addAll(bufferedListModel);
        }
        return collection;
    }

    /**
     * Create an empty buffered list model. May be overridden to provide specialized
     * implementations.
     * @return ObservableList to use for buffered value. This default uses an instance of
     *         ListListModel.
     */
    protected ObservableList createBufferedListModel() {
        return new ListListModel();
    }

    /**
     * Gets the list value associated with this value model, creating a list
     * model buffer containing its contents, suitable for manipulation.
     *
     * @return The list model buffer
     */
    private Object updateBufferedListModel(final Object wrappedCollection) {
        if (bufferedListModel == null) {
            bufferedListModel = createBufferedListModel();
            bufferedListModel.addListDataListener(listChangeHandler);
            setValue(bufferedListModel);
        }
        if (wrappedCollection == null) {
            bufferedListModel.clear();
        }
        else {
            if (wrappedType.isAssignableFrom(wrappedCollection.getClass())) {
                Collection buffer = null;
                if (wrappedCollection instanceof Object[]) {
                    Object[] wrappedArray = (Object[])wrappedCollection;
                    buffer = Arrays.asList(wrappedArray);
                }
                else {
                    buffer = (Collection)wrappedCollection;
                }
                bufferedListModel.clear();
                bufferedListModel.addAll(prepareBackingCollection(buffer));
            }
            else {
                throw new IllegalArgumentException("wrappedCollection must be assignable from " + wrappedType.getName());
            }

        }
        return bufferedListModel;
    }

    /**
     * Prepare the backing collection for installation into the buffered list model.  The default
     * implementation of this method simply returns it.  Subclasses can do whatever is needed
     * to the elements of the colleciton (or the collection itself).  For example, the
     * elements might be cloned or wrapped in a an adapter.
     * @param col The collection of objects to process
     * @return processed collection
     */
    protected Collection prepareBackingCollection(Collection col) {
        return col;
    }

    private Object getWrappedValue() {
        return getWrappedValueModel().getValue();
    }

    protected void fireListModelChanged() {
        if (isBuffering()) {
            super.fireValueChange(bufferedListModel, bufferedListModel);
        }
        else {
            super.setValue(bufferedListModel);
        }
    }

    protected boolean hasValueChanged(Object oldValue, Object newValue) {
        return (oldValue == bufferedListModel && newValue == bufferedListModel) || super.hasValueChanged(oldValue, newValue);
    }

    private class ListChangeHandler implements ListDataListener {
        public void contentsChanged(ListDataEvent e) {
            fireListModelChanged();
        }

        public void intervalAdded(ListDataEvent e) {
            fireListModelChanged();
        }

        public void intervalRemoved(ListDataEvent e) {
            fireListModelChanged();
        }
    }
}
