package org.valkyriercp.rules.constraint;

import java.util.Comparator;

/**
 * Predicate that tests if one comparable object is less than or equal to
 * another.
 *
 * @author Keith Donald
 */
public class LessThanEqualTo extends ComparisonBinaryPredicate implements BinaryConstraint {

	public static LessThanEqualTo INSTANCE = new LessThanEqualTo();

	public static synchronized BinaryConstraint instance() {
		return INSTANCE;
	}

	public static void load(LessThanEqualTo instance) {
		INSTANCE = instance;
	}

	public static BinaryConstraint instance(Comparator c) {
		return new LessThanEqualTo(c);
	}

	public static Constraint value(Comparable value) {
		return INSTANCE.bind(instance(), value);
	}

	public static Constraint value(Object value, Comparator comparator) {
		return INSTANCE.bind(instance(comparator), value);
	}

	public LessThanEqualTo() {
		super();
	}

	public LessThanEqualTo(Comparator comparator) {
		super(comparator);
	}

	protected boolean testCompareResult(int result) {
		return result <= 0;
	}

	public String toString() {
		return RelationalOperator.LESS_THAN_EQUAL_TO.toString();
	}
}


