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

import org.springframework.util.comparator.ComparableComparator;
import org.springframework.util.comparator.NullSafeComparator;
import org.valkyriercp.rules.constraint.property.AbstractBinaryConstraint;

import java.util.Comparator;

/**
 * Abstract helper superclass for binary predicates involved in comparison
 * operations.
 *
 * @author Keith Donald
 */
public abstract class ComparisonBinaryPredicate extends AbstractBinaryConstraint {

    private static final NullSafeComparator COMPARATOR = new NullSafeComparator(new ComparableComparator(), true);

	private Comparator comparator;

	/**
	 * Creates a comparing binary predicate which operates on
	 * <code>Comparable</code> objects.
	 */
	protected ComparisonBinaryPredicate() {

	}

	/**
	 * Creates a comparing binary predicate which compares using the provided
	 * <code>Comparator</code>.
	 *
	 * @param comparator
	 *            the comparator, may be null
	 */
	protected ComparisonBinaryPredicate(Comparator comparator) {
		if (!(comparator instanceof NullSafeComparator)) {
			this.comparator = new NullSafeComparator(comparator, true);
		}
		else {
			this.comparator = comparator;
		}
	}

    /**
     * Returns the comparator which is used to compare the arguments
     *
     * @return null if no custom comparator is defined
     */
	public Comparator getComparator() {
		return comparator;
	}

	/**
	 * Tests two arguments against a comparsion expression. This method
	 * delegates to the {@link #testCompareResult(int)}template method to
	 * evaluate the compareTo result.
	 *
	 * @param argument1
	 *            the first argument
	 * @param argument2
	 *            the second argument
	 * @return true if the comparsion result passes, false otherwise
	 */
	public boolean test(Object argument1, Object argument2) {
		if (getComparator() != null)
			return testCompareResult(getComparator().compare(argument1, argument2));

        return testCompareResult(COMPARATOR.compare(argument1, argument2));
	}

	/**
	 * Template method for evaluating the compare result.
	 *
	 * @param result
	 *            The compare result
	 * @return true or false
	 */
	protected abstract boolean testCompareResult(int result);

}
