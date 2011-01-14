package org.valkyriercp.list;

import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.core.Guarded;

/**
 * This class applies a guard to a {@link Guarded} object that only enables the
 * guarded object if the provided list selection model value holder has one or
 * more item selected. This can also be configured to monitor for an exact
 * number of selected items.
 *
 * @author Larry Streepy
 */
public class ListMultipleSelectionGuard extends AbstractListSelectionGuard {

    private int requiredCount = -1;

    /**
     * Constructor.
     *
     * @param selectionHolder ValueModel holding the list selection (value must
     *        be an array of int (<code>int[]</code).
     * @param guarded Object to guard
     */
    public ListMultipleSelectionGuard( ValueModel selectionHolder, Guarded guarded ) {
        super(selectionHolder, guarded);
    }

    /**
     * Constructor.
     *
     * @param selectionHolder ValueModel holding the list selection (value must
     *        be an array of int (<code>int[]</code).
     * @param guarded Object to guard
     * @param requiredCount Required number of selected items to enable
     */
    public ListMultipleSelectionGuard( ValueModel selectionHolder, Guarded guarded, int requiredCount ) {
        super(selectionHolder, guarded);
        this.requiredCount = requiredCount;
    }

    /**
     * Determine if the guarded object should be enabled based on the contents
     * of the current selection model value.
     *
     * @param selected The array of selected rows
     * @return boolean true if the guarded object should be enabled
     */
    protected boolean shouldEnable( int[] selected ) {
        return selected != null && selected.length >= 1 && (requiredCount == -1 || requiredCount == selected.length);
    }
}
