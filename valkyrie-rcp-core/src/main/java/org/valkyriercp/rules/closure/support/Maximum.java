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
package org.valkyriercp.rules.closure.support;

import org.springframework.util.comparator.ComparableComparator;
import org.springframework.util.comparator.NullSafeComparator;
import org.valkyriercp.rules.closure.BinaryClosure;

/**
 * Returns the maximum of two <code>Comparable</code> objects; with nulls regarded a
 * being less than non null.
 *
 * @author Keith Donald
 */
public class Maximum extends AbstractBinaryClosure {

    private static final Maximum INSTANCE = new Maximum();

    private static final NullSafeComparator COMPARATOR = new NullSafeComparator(new ComparableComparator(), true);

	/**
	 * Returns the shared instance--this is possible as the default functor for
	 * this class is immutable and stateless.
	 *
	 * @return the shared instance
	 */
	public static final BinaryClosure instance() {
		return INSTANCE;
	}

	/**
	 * Return the maximum of the two Comparable objects.
	 *
	 * @param comparable1
	 *            the first comparable
	 * @param comparable2
	 *            the second comparable
	 * @return the maximum
	 */
	public Object call(Object comparable1, Object comparable2) {
		int result = COMPARATOR.compare(comparable1,
				comparable2);
		if (result > 0) {
			return comparable1;
		}
		else if (result < 0) {
			return comparable2;
		}
		return comparable1;
	}

	public String toString() {
		return "max(arg1, arg2)";
	}
}