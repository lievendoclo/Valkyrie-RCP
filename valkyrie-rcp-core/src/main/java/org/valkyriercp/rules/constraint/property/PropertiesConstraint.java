package org.valkyriercp.rules.constraint.property;

import org.springframework.util.Assert;
import org.valkyriercp.binding.PropertyAccessStrategy;
import org.valkyriercp.rules.constraint.BinaryConstraint;

/**
 * A constraint that returns the result of a <code>boolean</code>
 * expression that tests two variable bean property values. For example,
 * <code>pet.ageAtFirstVisit > pet.currentAge</code>
 *
 * @author Keith Donald
 */
public class PropertiesConstraint extends AbstractPropertyConstraint {

	private String otherPropertyName;

	private BinaryConstraint beanPropertyExpression;

	/**
	 * Creates a BeanPropertyExpression
	 *
	 * @param propertyName
	 *            The first property participating in the expression.
	 * @param beanPropertyExpression
	 *            The expression predicate that will test the two bean property
	 *            values.
	 * @param otherPropertyName
	 *            The second property participating in the expression.
	 */
	public PropertiesConstraint(String propertyName, BinaryConstraint beanPropertyExpression, String otherPropertyName) {
		super(propertyName);
		Assert.notNull(otherPropertyName, "otherPropertyName is required");
		Assert.notNull(beanPropertyExpression, "beanPropertyExpression is required");
		this.otherPropertyName = otherPropertyName;
		this.beanPropertyExpression = beanPropertyExpression;
	}

	public boolean isCompoundRule() {
		return true;
	}

	public boolean isDependentOn(String propertyName) {
		return getPropertyName().equals(propertyName) || getOtherPropertyName().equals(propertyName);
	}

	public String getOtherPropertyName() {
		return otherPropertyName;
	}

	public BinaryConstraint getConstraint() {
		return beanPropertyExpression;
	}

	protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
		return beanPropertyExpression.test(domainObjectAccessStrategy.getPropertyValue(getPropertyName()),
				domainObjectAccessStrategy.getPropertyValue(getOtherPropertyName()));
	}

	public String toString() {
		return super.toString() + " " + beanPropertyExpression.toString() + " " + otherPropertyName;
	}
}


