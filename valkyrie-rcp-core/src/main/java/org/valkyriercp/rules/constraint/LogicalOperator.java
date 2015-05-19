/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

