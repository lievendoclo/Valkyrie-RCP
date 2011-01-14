package org.valkyriercp.list;

import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.core.Guarded;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This class applies a guard to a {@link Guarded} object that enables the
 * guarded object bsaed on the contents of the selection model value. Concrete
 * subclasses must provide an implementation for {@link #shouldEnable(int[])}.
 *
 * @author Larry Streepy
 */
public abstract class AbstractListSelectionGuard implements PropertyChangeListener {

    private ValueModel selectionHolder;
    private Guarded guarded;

    /**
     * Constructor.
     *
     * @param selectionHolder ValueModel holding the list selection (value must
     *        be an array of int (<code>int[]</code).
     * @param guarded Object to guard
     */
    public AbstractListSelectionGuard( ValueModel selectionHolder, Guarded guarded ) {
        this.selectionHolder = selectionHolder;
        this.selectionHolder.addValueChangeListener(this);
        this.guarded = guarded;
        propertyChange(null);
    }

    /**
     * Handle a change in the selectionHolder value.
     */
    public void propertyChange( PropertyChangeEvent evt ) {
        int[] selected = (int[]) selectionHolder.getValue();
        guarded.setEnabled(shouldEnable(selected));
    }

    /**
     * Get the guarded object.
     *
     * @return guarded object
     */
    public Guarded getGuarded() {
        return guarded;
    }

    /**
     * Get the selection value holder. The value of this value model will be an
     * int array (<code>int[]</code).
     * @return selection value holder
     */
    public ValueModel getSelectionHolder() {
        return selectionHolder;
    }

    /**
     * Determine if the guarded object should be enabled based on the contents
     * of the current selection model value.
     *
     * @param selected The array of selected rows
     * @return boolean true if the guarded object should be enabled
     */
    protected abstract boolean shouldEnable( int[] selected );
}

