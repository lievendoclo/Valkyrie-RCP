/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
