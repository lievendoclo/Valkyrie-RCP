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
package org.valkyriercp.rules.support;

import java.util.Comparator;

/**
 * Comparator for comparing Number instances.
 * <p>
 * Uses the double value of the number to do the actual comparison
 *
 * @author Peter De Bruycker
 */
public class NumberComparator implements Comparator {

	public static final NumberComparator INSTANCE = new NumberComparator();

	private NumberComparator() {
		// singleton
	}

	public int compare(Object o1, Object o2) {
		Number n1 = (Number) o1;
		Number n2 = (Number) o2;

		return Double.compare(n1.doubleValue(), n2.doubleValue());
	}
}
