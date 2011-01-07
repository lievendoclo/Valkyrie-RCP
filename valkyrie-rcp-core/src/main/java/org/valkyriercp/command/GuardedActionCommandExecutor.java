package org.valkyriercp.command;

import org.valkyriercp.core.Guarded;

import java.beans.PropertyChangeListener;

/**
 * An {@link ActionCommandExecutor} that can be enabled or disabled, with optional listeners
 * for these state changes.
 *
 * @author Keith Donald
 */
public interface GuardedActionCommandExecutor extends Guarded, ActionCommandExecutor {

	/**
	 * Adds the given listener to the collection of listeners that will be notified
	 * when the command executor's enabled state changes.
	 *
	 * @param listener The listener to be added.
	 */
    public void addEnabledListener(PropertyChangeListener listener);

    /**
     * Removes the given listener from the collection of listeners that will be
     * notified when the command executor's enabled state changes.
     *
     * @param listener The listener to be removed.
     */
    public void removeEnabledListener(PropertyChangeListener listener);

}
