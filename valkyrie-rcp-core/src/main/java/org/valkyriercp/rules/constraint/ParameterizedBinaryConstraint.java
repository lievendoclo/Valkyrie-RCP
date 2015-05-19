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
 * A unary constraint adapting a binary constraint that uses a parameterized
 * constant value as the second argument when testing.
 *
 * @author Keith Donald
 */
public class ParameterizedBinaryConstraint implements Constraint {
	private BinaryConstraint constraint;

	private Object parameter;

	/**
	 * Creates a ParameterizedBinaryPredicate that binds the provided parameter
	 * constant as the second argument to the constraint during tests.
	 *
	 * @param constraint
	 *            The binary constraint to adapt as a unary constraint
	 * @param parameter
	 *            The constant parameter value
	 */
	public ParameterizedBinaryConstraint(BinaryConstraint constraint, Object parameter) {
		this.constraint = constraint;
		this.parameter = parameter;
	}

	/**
	 * Convenience constructor for <code>short</code> parameters.
	 */
	public ParameterizedBinaryConstraint(BinaryConstraint constraint, short number) {
		this(constraint, new Short(number));
	}

	/**
	 * Convenience constructor for <code>byte</code> parameters.
	 */
	public ParameterizedBinaryConstraint(BinaryConstraint constraint, byte b) {
		this(constraint, new Byte(b));
	}

	/**
	 * Convenience constructor for <code>integer</code> parameters.
	 */
	public ParameterizedBinaryConstraint(BinaryConstraint constraint, int number) {
		this(constraint, new Integer(number));
	}

	/**
	 * Convenience constructor for <code>float</code> parameters.
	 */
	public ParameterizedBinaryConstraint(BinaryConstraint constraint, float number) {
		this(constraint, new Float(number));
	}

	/**
	 * Convenience constructor for <code>double</code> parameters.
	 */
	public ParameterizedBinaryConstraint(BinaryConstraint constraint, double number) {
		this(constraint, new Double(number));
	}

	/**
	 * Convenience constructor for <code>boolean</code> parameters.
	 */
	public ParameterizedBinaryConstraint(BinaryConstraint constraint, boolean bool) {
		this(constraint, (bool ? Boolean.TRUE : Boolean.FALSE));
	}

	public Object getParameter() {
		return parameter;
	}

	public BinaryConstraint getConstraint() {
		return constraint;
	}

	/**
	 * Tests the wrapped binary constraint with the variable argument value,
	 * passing in the parameter constant as the second argument.
	 *
	 * @see Constraint#test(java.lang.Object)
	 */
	public boolean test(Object value) {
		return constraint.test(value, this.parameter);
	}

	public String toString() {
		return constraint.toString() + " " + getParameter();
	}

}