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
