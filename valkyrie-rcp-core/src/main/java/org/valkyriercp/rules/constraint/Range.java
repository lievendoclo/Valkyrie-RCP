package org.valkyriercp.rules.constraint;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;

import java.util.Comparator;

/**
 * A range whose edges are defined by a minimum Comparable and a maximum
 * Comparable.
 *
 * @author Keith Donald
 */
public final class Range extends AbstractConstraint {
	private Object min;

	private Object max;

	private boolean inclusive = true;

	private Constraint rangeConstraint;

	/**
	 * Creates a range with the specified <code>Comparable</code> min and max
	 * edges.
	 *
	 * @param min
	 *            the low edge of the range
	 * @param max
	 *            the high edge of the range
	 */
	public Range(Comparable min, Comparable max) {
		this(min, max, true);
	}

	/**
	 * Creates a range with the specified <code>Comparable</code> min and max
	 * edges.
	 *
	 * @param min
	 *            the low edge of the range
	 * @param max
	 *            the high edge of the range
	 * @param inclusive
	 *            the range is inclusive?
	 */
	public Range(Comparable min, Comparable max, boolean inclusive) {
		commonAssert(min, max);
		Constraint minimum;
		Constraint maximum;
		this.inclusive = inclusive;
		if (this.inclusive) {
			Assert.isTrue(LessThanEqualTo.instance().test(min, max), "Minimum " + min
                    + " must be less than or equal to maximum " + max);
			minimum = gte(min);
			maximum = lte(max);
		}
		else {
			Assert.isTrue(LessThan.instance().test(min, max), "Minimum " + min + " must be less than maximum " + max);
			minimum = gt(min);
			maximum = lt(max);
		}
		this.rangeConstraint = and(minimum, maximum);
		this.min = min;
		this.max = max;
	}

	/**
	 * Creates a range with the specified min and max edges.
	 *
	 * @param min
	 *            the low edge of the range
	 * @param max
	 *            the high edge of the range
	 * @param comparator
	 *            the comparator to use to perform value comparisons
	 */
	public Range(Object min, Object max, Comparator comparator) {
		this(min, max, comparator, true);
	}

	/**
	 * Creates a range with the specified min and max edges.
	 *
	 * @param min
	 *            the low edge of the range
	 * @param max
	 *            the high edge of the range
	 * @param comparator
	 *            the comparator to use to perform value comparisons
	 */
	public Range(Object min, Object max, Comparator comparator, boolean inclusive) {
		commonAssert(min, max);
		Constraint minimum;
		Constraint maximum;
		this.inclusive = inclusive;
		if (this.inclusive) {
			Assert.isTrue(LessThanEqualTo.instance(comparator).test(min, max), "Minimum " + min
					+ " must be less than or equal to maximum " + max);
			minimum = bind(GreaterThanEqualTo.instance(comparator), min);
			maximum = bind(LessThanEqualTo.instance(comparator), max);
		}
		else {
			Assert.isTrue(LessThan.instance(comparator).test(min, max), "Minimum " + min
					+ " must be less than maximum " + max);
			minimum = bind(GreaterThan.instance(comparator), min);
			maximum = bind(LessThan.instance(comparator), max);
		}
		this.rangeConstraint = and(minimum, maximum);
		this.min = min;
		this.max = max;
	}

	private void commonAssert(Object min, Object max) {
		Assert.isTrue(min != null && max != null, "Min and max are required");
		Assert.isTrue(min.getClass().equals(max.getClass()), "Min and max must be of the same class");
	}

	/**
	 * Convenience constructor for <code>int</code> ranges.
	 */
	public Range(int min, int max) {
		this(new Integer(min), new Integer(max));
	}

	/**
	 * Convenience constructor for <code>int</code> ranges.
	 */
	public Range(int min, int max, boolean inclusive) {
		this(new Integer(min), new Integer(max), inclusive);
	}

	/**
	 * Convenience constructor for <code>float</code> ranges.
	 */
	public Range(float min, float max) {
		this(new Float(min), new Float(max));
	}

	/**
	 * Convenience constructor for <code>float</code> ranges.
	 */
	public Range(float min, float max, boolean inclusive) {
		this(new Float(min), new Float(max), inclusive);
	}

	/**
	 * Convenience constructor for <code>double</code> ranges.
	 */
	public Range(double min, double max) {
		this(new Double(min), new Double(max));
	}

	/**
	 * Convenience constructor for <code>double</code> ranges.
	 */
	public Range(double min, double max, boolean inclusive) {
		this(new Double(min), new Double(max), inclusive);
	}

	public Object getMin() {
		return min;
	}

	public Object getMax() {
		return max;
	}

	public boolean isInclusive() {
		return inclusive;
	}

	/**
	 * Test if the specified argument falls within the established range.
	 *
	 * @see Constraint#test(java.lang.Object)
	 */
	public boolean test(Object argument) {
		return this.rangeConstraint.test(argument);
	}

	public String toString() {
		return new ToStringCreator(this).append("rangeConstraint", rangeConstraint).toString();
	}

}
