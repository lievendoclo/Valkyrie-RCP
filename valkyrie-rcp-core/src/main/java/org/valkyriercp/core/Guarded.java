package org.valkyriercp.core;

/**
 * Interface to be implemented by objects that can be enabled or disabled. For example, these
 * objects will typically be user interface controls that the application will want
 * to disable in certain contexts.
 *
 * @author Keith Donald
 */
public interface Guarded {

    /**
     * Indicates if the object is in an enabled state.
     * @return {@true} if the object is in an enabled state, {@false} otherwise.
     */
    public boolean isEnabled();

    /**
     * Sets the enabled state of the object.
     * @param enabled {@code true} to enable the object, {@code false} to disable it.
     */
    public void setEnabled(boolean enabled);

}