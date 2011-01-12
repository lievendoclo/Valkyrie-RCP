package org.valkyriercp.rules.constraint;

/**
 * Type-safe enum class for supported binary operators.
 *
 * @author Keith Donald
 */
public abstract class RelationalOperator extends Operator {

	/**
	 * The <code>EQUAL_TO (==)</code> operator
	 */
	public static final RelationalOperator EQUAL_TO = new RelationalOperator(
			"eq", "=") {
		public BinaryConstraint getConstraint() {
			return EqualTo.instance();
		}
	};

	/**
	 * The <code>LESS_THAN (<)</code> operator
	 */
	public static final RelationalOperator LESS_THAN = new RelationalOperator(
			"lt", "<") {
		public Operator negation() {
			return GREATER_THAN;
		}

		public BinaryConstraint getConstraint() {
			return LessThan.instance();
		}
	};

	/**
	 * The <code>LESS_THAN_EQUAL_TO (<=)</code> operator
	 */
	public static final RelationalOperator LESS_THAN_EQUAL_TO = new RelationalOperator(
			"lte", "<=") {
		public Operator negation() {
			return GREATER_THAN_EQUAL_TO;
		}

		public BinaryConstraint getConstraint() {
			return LessThanEqualTo.instance();
		}
	};

	/**
	 * The <code>GREATER_THAN (>)</code> operator
	 */
	public static final RelationalOperator GREATER_THAN = new RelationalOperator(
			"gt", ">") {
		public Operator negation() {
			return LESS_THAN;
		}

		public BinaryConstraint getConstraint() {
			return GreaterThan.instance();
		}
	};

	/**
	 * The <code>GREATER_THAN_EQUAL_TO (>=)</code> operator
	 */
	public static final RelationalOperator GREATER_THAN_EQUAL_TO = new RelationalOperator(
			"gte", ">=") {
		public Operator negation() {
			return LESS_THAN_EQUAL_TO;
		}

		public BinaryConstraint getConstraint() {
			return GreaterThanEqualTo.instance();
		}
	};

	private RelationalOperator(String code, String symbol) {
		super(code, symbol);
	}

	/**
	 * Returns the predicate instance associated with this binary operator.
	 *
	 * @return the associated binary predicate
	 */
	public abstract BinaryConstraint getConstraint();

}
