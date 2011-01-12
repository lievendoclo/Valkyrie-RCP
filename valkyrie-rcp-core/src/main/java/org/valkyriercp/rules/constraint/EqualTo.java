package org.valkyriercp.rules.constraint;


import org.springframework.util.ObjectUtils;

import java.util.Comparator;

/**
 * Constraint that tests object equality (not identity.)
 *
 * @author Keith Donald
 */
public class EqualTo extends ComparisonBinaryPredicate {

	public static EqualTo INSTANCE = new EqualTo();

	public static BinaryConstraint instance() {
		return INSTANCE;
	}

	public static void load(EqualTo instance) {
		INSTANCE = instance;
	}

	public static BinaryConstraint instance(Comparator c) {
		return new EqualTo(c);
	}

	public static Constraint value(Object value) {
		return INSTANCE.bind(instance(), value);
	}

	public static Constraint value(Object value, Comparator comparator) {
		return INSTANCE.bind(instance(comparator), value);
	}

	public EqualTo() {
		super();
	}

	public EqualTo(Comparator comparator) {
		super(comparator);
	}

	/**
	 * Test if the two arguments are equal.
	 *
	 * @param argument1
	 *            the first argument
	 * @param argument2
	 *            the second argument
	 * @return true if they are equal, false otherwise
	 */
	public boolean test(Object argument1, Object argument2) {
		if (getComparator() == null)
			return ObjectUtils.nullSafeEquals(argument1, argument2);

        return super.test(argument1, argument2);
	}

	protected boolean testCompareResult(int result) {
		return result == 0;
	}

	public String toString() {
		return RelationalOperator.EQUAL_TO.toString();
	}
}
