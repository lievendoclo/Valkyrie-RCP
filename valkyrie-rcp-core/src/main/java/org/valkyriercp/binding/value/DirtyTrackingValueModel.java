package org.valkyriercp.binding.value;

import org.valkyriercp.core.PropertyChangePublisher;

/**
 * Adds the <code>dirty</code> aspect to a valueModel, tracking changes when needed.
 */
public interface DirtyTrackingValueModel extends ValueModel, PropertyChangePublisher
{

    /** The name of the bound property <em>dirty</em>. */
    String DIRTY_PROPERTY = "dirty";

    /**
     * Returns <code>true</code> if value held by this model has changed since the last call to reset or the
     * last time a value came up from the inner model.
     */
    boolean isDirty();

    /**
     * Resets the dirty state of this model to <code>false</code>.
     */
    void clearDirty();

    /**
     * Reverts the value held by this model to the original value at the last call to reset or the last time a
     * value came up from the inner model.
     */
    void revertToOriginal();
}