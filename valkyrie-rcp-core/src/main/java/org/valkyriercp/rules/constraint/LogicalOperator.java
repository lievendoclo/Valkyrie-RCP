package org.valkyriercp.rules.constraint;

/**
 * Type-safe enums for various conditional or logical operators.
 *
 * @author Keith Donald
 */
public abstract class LogicalOperator extends Operator {

	/**
	 * The <code>AND</code> operator
	 */
	public static final LogicalOperator AND = new LogicalOperator("and", "&&") {
		public CompoundConstraint createConstraint() {
			return new And();
		}
	};

	/**
	 * The <code>OR</code> operator
	 */
	public static final LogicalOperator OR = new LogicalOperator("or", "||") {
		public CompoundConstraint createConstraint() {
			return new Or();
		}
	};

  /**
   * The <code>XOR</code> operator
   */
  public static final LogicalOperator XOR = new LogicalOperator("xor", "^") {
    public CompoundConstraint createConstraint() {
      return new XOr();
    }
  };

  private LogicalOperator(String code, String symbol) {
		super(code, symbol);
	}

	public CompoundConstraint createConstraint() {
		throw new UnsupportedOperationException();
	}
}

