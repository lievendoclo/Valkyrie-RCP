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
