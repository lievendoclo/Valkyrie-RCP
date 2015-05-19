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
package org.valkyriercp.rules.constraint.property;

import org.valkyriercp.rules.constraint.BinaryConstraint;
import org.valkyriercp.rules.constraint.Constraint;
import org.valkyriercp.rules.constraint.ParameterizedBinaryConstraint;

/**
 * A constraint that returns the result of a <code>boolean</code>
 * expression that tests a variable bean property value against a constant
 * parameter value. For example: <code>pet.age > 5</code>
 *
 * @author Keith Donald
 */
public class ParameterizedPropertyConstraint implements PropertyConstraint {
	private PropertyValueConstraint parameterizedExpression;

	/**
	 * Creates a BeanPropertyExpressionTester.
	 *
	 * @param propertyName
	 *            The property participating in the expression.
	 * @param expression
	 *            The expression predicate (tester).
	 * @param parameter
	 *            The constant parameter value participating in the expression.
	 */
	public ParameterizedPropertyConstraint(String propertyName, BinaryConstraint expression, Object parameter) {
		this(propertyName, new ParameterizedBinaryConstraint(expression, parameter));
	}

	public ParameterizedPropertyConstraint(String propertyName, Constraint parameterizedExpression) {
		this.parameterizedExpression = new PropertyValueConstraint(propertyName, parameterizedExpression);
	}

	public String getPropertyName() {
		return parameterizedExpression.getPropertyName();
	}

	public boolean isDependentOn(String propertyName) {
		return parameterizedExpression.isDependentOn(propertyName);
	}

	public boolean isCompoundRule() {
		return parameterizedExpression.isCompoundRule();
	}

	public BinaryConstraint getConstraint() {
		return getParameterizedBinaryConstraint().getConstraint();
	}

	public Object getParameter() {
		return getParameterizedBinaryConstraint().getParameter();
	}

	private ParameterizedBinaryConstraint getParameterizedBinaryConstraint() {
		return (ParameterizedBinaryConstraint)this.parameterizedExpression.getConstraint();
	}

	/**
	 * Tests the value of the configured propertyName for this bean against the
	 * configured parameter value using the configured binary predicate.
	 *
	 */
	public boolean test(Object bean) {
		return parameterizedExpression.test(bean);
	}

	public String toString() {
		return getPropertyName() + " " + parameterizedExpression.toString();
	}
}
