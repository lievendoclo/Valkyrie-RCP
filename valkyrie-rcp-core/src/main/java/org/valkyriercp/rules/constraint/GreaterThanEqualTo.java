package org.valkyriercp.rules.constraint;

import java.util.Comparator;

/**
 * Predicate that tests if one comparable object is greater than or equal to
 * another.
 *
 * @author Keith Donald
 */
public class GreaterThanEqualTo extends ComparisonBinaryPredicate implements BinaryConstraint {

	public static GreaterThanEqualTo INSTANCE = new GreaterThanEqualTo();

	public static synchronized BinaryConstraint instance() {
		return INSTANCE;
	}

	public static void load(GreaterThanEqualTo instance) {
		INSTANCE = instance;
	}

	public static BinaryConstraint instance(Comparator c) {
		return new GreaterThanEqualTo(c);
	}

	public static Constraint value(Comparable value) {
		return INSTANCE.bind(instance(), value);
	}

	public static Constraint value(Object value, Comparator comparator) {
		return INSTANCE.bind(instance(comparator), value);
	}

	public GreaterThanEqualTo() {
		super();
	}

	public GreaterThanEqualTo(Comparator comparator) {
		super(comparator);
	}

	protected boolean testCompareResult(int result) {
		return result >= 0;
	}

	public String toString() {
		return RelationalOperator.GREATER_THAN_EQUAL_TO.toString();
	}
}
