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