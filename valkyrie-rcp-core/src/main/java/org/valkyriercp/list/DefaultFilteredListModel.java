package org.valkyriercp.list;

import org.springframework.util.Assert;
import org.valkyriercp.rules.constraint.Constraint;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Decorates an existing {@link javax.swing.ListModel} by applying a constraint. The constraint can implement {@link java.util.Observable} to
 * notify a change of the filter condition.
 *
 * @author Keith Donald
 * @author Mathias Broekelmann
 */
public class DefaultFilteredListModel extends AbstractFilteredListModel implements Observer {

    private Constraint constraint;

    private int[] indexes;

    private int filteredSize;

    /**
     * Constructs a new instance
     *
     * @param listModel
     *            the list model to filter.
     * @param constraint
     *            the constraint which is applied to the list model elements
     *
     * @throws IllegalArgumentException
     *             if list model or constraint parameters where null
     */
    public DefaultFilteredListModel(ListModel listModel, Constraint constraint) {
        super(listModel);
        setConstraint(constraint);
    }

    protected void fireContentsChanged(Object source, int index0, int index1) {
        reallocateIndexes();
        super.fireContentsChanged(source, index0, index1);
    }

    /**
     * Defines the constraint which is applied to the list model elements
     *
     * @param constraint
     *            the constraint to set
     *
     * @throws IllegalArgumentException
     *             if constraint is null
     */
    public final void setConstraint(Constraint constraint) {
        Assert.notNull(constraint);
        if (!constraint.equals(this.constraint)) {
            if (this.constraint instanceof Observable) {
                ((Observable) constraint).deleteObserver(this);
            }
            this.constraint = constraint;
            if (constraint instanceof Observable) {
                ((Observable) constraint).addObserver(this);
            }
            fireContentsChanged(this, -1, -1);
        }
    }

    /**
     * @return the constraint
     */
    public Constraint getConstraint() {
        return constraint;
    }

    /**
     * Internally called to reallocate the indexes. This method should be called when the filtered model changes its
     * element size
     */
    protected void reallocateIndexes() {
        if (this.indexes == null || this.indexes.length != getFilteredModel().getSize()) {
            this.indexes = new int[getFilteredModel().getSize()];
        }
        applyConstraint();
    }

    /**
     * If the constraint implements {@link Observable} this method is called and will apply the constraint to the list
     * model elements
     */
    public void update(Observable changed, Object arg) {
        fireContentsChanged(this, -1, -1);
    }

    private void applyConstraint() {
        filteredSize = 0;
        ListModel filteredListModel = getFilteredModel();
        for (int i = 0, size = filteredListModel.getSize(); i < size; i++) {
            Object element = filteredListModel.getElementAt(i);
            if (constraint.test(element)) {
                indexes[filteredSize++] = i;
                onMatchingElement(element);
            }
        }
        postConstraintApplied();
    }

    /**
     * Called to notify that an element has matched the filter constraint. This implementation does nothing.
     *
     * @param element
     *            the element which was accepted by the filter
     */
    protected void onMatchingElement(Object element) {

    }

    /**
     * Called to notify that the constraint was applied to all elements. This implementation does nothing.
     */
    protected void postConstraintApplied() {

    }

    /**
     * Returns the size of the elements which passes the filter constraint.
     */
    public int getSize() {
        return filteredSize;
    }

    /**
     * Returns the element index for a filtered index
     *
     * @param filteredIndex
     *            the filtered index
     * @return the unfiltered index of the filtered model
     */
    public int getElementIndex(int filteredIndex) {
        return indexes[filteredIndex];
    }

    public void contentsChanged(ListDataEvent e) {
        reallocateIndexes();
        super.contentsChanged(e);
    }

    public void intervalAdded(ListDataEvent e) {
        reallocateIndexes();
        super.intervalAdded(e);
    }

    public void intervalRemoved(ListDataEvent e) {
        reallocateIndexes();
        super.intervalRemoved(e);
    }

}