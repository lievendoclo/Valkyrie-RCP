package org.valkyriercp.rules.constraint;

import java.util.Comparator;

/**
 * Predicate that tests if one comparable object is greater than another.
 *
 * @author Keith Donald
 */
public class GreaterThan extends ComparisonBinaryPredicate implements BinaryConstraint {

	public static GreaterThan INSTANCE = new GreaterThan();

	public static synchronized BinaryConstraint instance() {
		return INSTANCE;
	}

	public static void load(GreaterThan instance) {
		INSTANCE = instance;
	}

	public static BinaryConstraint instance(Comparator c) {
		return new GreaterThan(c);
	}

	public static Constraint value(Comparable value) {
		return INSTANCE.bind(instance(), value);
	}

	public static Constraint value(Object value, Comparator comparator) {
		return INSTANCE.bind(instance(comparator), value);
	}

	public GreaterThan() {
		super();
	}

	public GreaterThan(Comparator comparator) {
		super(comparator);
	}

	protected boolean testCompareResult(int result) {
		return result > 0;
	}

	public String toString() {
		return RelationalOperator.GREATER_THAN.toString();
	}

}
