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
