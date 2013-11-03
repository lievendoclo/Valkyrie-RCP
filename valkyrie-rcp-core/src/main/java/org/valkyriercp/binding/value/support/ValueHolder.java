package org.valkyriercp.binding.value.support;

import org.springframework.core.style.ToStringCreator;

/**
 * A simple value model that contains a single value. Notifies listeners when
 * the contained value changes.
 *
 * @author Keith Donald
 * @author Karsten Lentzsch
 */
public class ValueHolder extends AbstractValueModel {

	private Object value;

	/**
	 * Constructs a <code>ValueHolder</code> with <code>null</code> as
	 * initial value.
	 */
	public ValueHolder() {
		this(null);
	}

	/**
	 * Constructs a <code>ValueHolder</code> with the given initial value.
	 *
	 * @param value the initial value
	 */
	public ValueHolder(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		if (hasValueChanged(this.value, value)) {
			Object oldValue = this.value;
			if (logger.isDebugEnabled()) {
				logger.debug("Setting held value from '" + oldValue + "' to '" + value + "'");
			}
			this.value = value;
			fireValueChange(oldValue, this.value);
		}
	}

	public String toString() {
		return new ToStringCreator(this).append("value", getValue()).toString();
	}
}
