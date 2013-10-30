package org.valkyriercp.dialog.control;

import javax.swing.*;

/**
 * Custom <code>SingleSelectionModel</code> implementation that allows for
 * vetoing the selection of tabs.
 * <p>
 * The {@link #selectionAllowed(int)} method must return <code>true</code> if
 * the tab at the index can be selected, or <code>false</code> if not.
 *
 * @author Peter De Bruycker
 */
public abstract class VetoableSingleSelectionModel extends DefaultSingleSelectionModel {

	public final void setSelectedIndex(int index) {
		if (selectionAllowed(index)) {
			super.setSelectedIndex(index);
		}
	}

	protected abstract boolean selectionAllowed(int index);
}