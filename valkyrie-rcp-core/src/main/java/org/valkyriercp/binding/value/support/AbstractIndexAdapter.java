package org.valkyriercp.binding.value.support;

import org.valkyriercp.binding.value.IndexAdapter;

/**
 * Base implementation of an {@link IndexAdapter} which provides basic index
 * storing.
 */
public abstract class AbstractIndexAdapter extends AbstractValueModel implements IndexAdapter {

	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		if (hasChanged(this.index, index)) {
			int oldValue = this.index;
			this.index = index;
			firePropertyChange("index", oldValue, this.index);
		}
	}

}
