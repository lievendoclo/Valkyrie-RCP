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
package org.valkyriercp.rules.constraint;

import org.springframework.util.StringUtils;
import org.valkyriercp.rules.reporting.TypeResolvable;

import java.util.Collection;
import java.util.Map;

/**
 * Validates a required property. Required is defined as non-null and, if the
 * object is a string, not empty and not blank. If the object is an instance of
 * {@link java.util.Collection}, {@link java.util.Map} or an array it will test if it is empty.
 *
 * @author Keith Donald
 * @author Mathias Broekelmann
 */
public class Required implements Constraint, TypeResolvable {
	private static final Required required = new Required();

	private static final Required present = new Required("present");

	private final String type;

	/**
	 * Creates a required constraint by using a custom type. This is primarly
	 * used to separate required and present constraint. These constraints where
	 * equaly implemented but where used differently
	 *
	 * @param type the type of the constraint. see {@link #getType()}
	 */
	public Required(String type) {
		this.type = type;
	}

	/**
	 * Default constructor which creates a required constraint by using the type
	 * <code>required</code>
	 */
	public Required() {
		this("required");
	}

	/**
	 * Tests if this argument is present (non-null, not-empty, not blank)
	 *
	 * @see Constraint#test(java.lang.Object)
	 */
	public boolean test(Object argument) {
		if (argument != null) {
			if (argument instanceof String) {
				if (StringUtils.hasText((String) argument)) {
					return true;
				}
			}
			else if (argument instanceof Collection) {
				return !((Collection) argument).isEmpty();
			}
			else if (argument instanceof Map) {
				return !((Map) argument).isEmpty();
			}
			else if (argument.getClass().isArray()) {
				return ((Object[]) argument).length > 0;
			}
			else {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns the required instance
	 *
	 * @return the required instance (never null)
	 */
	public static Required instance() {
		return required;
	}

	/**
	 * returns the present instance
	 *
	 * @return the present instance (never null)
	 */
	public static Required present() {
		return present;
	}

	public String toString() {
		return getType();
	}

	public String getType() {
		return type;
	}
}
