package org.valkyriercp.rules.constraint.property;

import org.springframework.util.Assert;
import org.valkyriercp.binding.PropertyAccessStrategy;
import org.valkyriercp.rules.reporting.TypeResolvable;

/**
 * <p>
 * Provides a way to trigger rules for propertyB when propertyA satisfies a
 * certain condition:
 * </p>
 *
 * <pre>
 * 	if (propertyA satisfies the conditional constraint)
 * 	{
 * 		check the rules for propertyB
 * 	}
 * </pre>
 *
 * with an optional part:
 *
 * <pre>
 *  else
 *  {
 *      check the rules for propertyC
 *  }
 * </pre>
 *
 * <p>
 * More complex situations are possible by using compound constraints which
 * leverages the previous to:
 * </p>
 *
 * <pre>
 * if (constraint(propertyA, propertyB,...) == true)
 * {
 *     checkConstraint(property1, property2,...);
 * }
 * \\ optional part
 * else
 * {
 *     checkConstraint(propertyX, propertyY,...);
 * }
 * </pre>
 *
 * <p>
 * This class can be compared to the {@link org.valkyriercp.rules.constraint.IfTrue} class: it applies the same
 * pattern but on different <b>properties</b> instead of on a <b>property value</b>.
 * </p>
 *
 * @author jh
 *
 */
public class ConditionalPropertyConstraint extends AbstractPropertyConstraint implements TypeResolvable {

	/** The condition which triggers further rules to be checked. */
	private final PropertyConstraint ifConstraint;

	/** The constraint to be checked when the condition is satisfied. */
	private final PropertyConstraint thenConstraint;

	/** The constraint to be checked when the condition is <b>NOT</b> satisfied. */
	private final PropertyConstraint elseConstraint;

	/** Type used to fetch message. */
	private String type;

	/**
	 * @see #ConditionalPropertyConstraint(PropertyConstraint, PropertyConstraint, String)
	 */
	public ConditionalPropertyConstraint(PropertyConstraint ifConstraint, PropertyConstraint thenConstraint) {
		this(ifConstraint, thenConstraint, null, null);
	}

	/**
	 * Create a constraint which simulates the if...then pattern applied
	 * on separate properties.
	 *
	 * @param ifConstraint the PropertyConstraint that triggers the test
	 * (satisfying a certain condition).
	 * @param thenConstraint the PropertyConstraint to test in the specified
	 * condition.
	 */
	public ConditionalPropertyConstraint(PropertyConstraint ifConstraint, PropertyConstraint thenConstraint, String type) {
		this(ifConstraint, thenConstraint, null, type);
	}

	/**
	 * @see #ConditionalPropertyConstraint(PropertyConstraint, PropertyConstraint, PropertyConstraint, String)
	 */
	public ConditionalPropertyConstraint(PropertyConstraint ifConstraint, PropertyConstraint thenConstraint,
			PropertyConstraint elseConstraint) {
		this(ifConstraint, thenConstraint, elseConstraint, null);
	}
	/**
	 * Create a constraint which simulates the if...then...else pattern applied
	 * on separate properties.
	 *
	 * @param ifConstraint the PropertyConstraint that triggers the test
	 * (satisfying a certain condition).
	 * @param thenConstraint the PropertyConstraint to test in the specified
	 * condition.
	 * @param elseConstraint the PropertyConstraint to test if the condition is
	 * <b>NOT</b> satisfied. May be <code>null</code>.
	 * @param type the messageCode used to fetch the message.
	 */
	public ConditionalPropertyConstraint(PropertyConstraint ifConstraint, PropertyConstraint thenConstraint,
			PropertyConstraint elseConstraint, String type) {
		super(ifConstraint.getPropertyName());
		Assert.notNull(ifConstraint);
		Assert.notNull(thenConstraint);
		this.ifConstraint = ifConstraint;
		this.thenConstraint = thenConstraint;
		this.elseConstraint = elseConstraint;
		this.type = type;
	}

	public boolean isCompoundRule() {
		return true;
	}

	public boolean isDependentOn(String propertyName) {
		if (elseConstraint == null)
			return ifConstraint.isDependentOn(propertyName) || thenConstraint.isDependentOn(propertyName);

		return ifConstraint.isDependentOn(propertyName) || thenConstraint.isDependentOn(propertyName)
				|| elseConstraint.isDependentOn(propertyName);
	}

	protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
		if (ifConstraint.test(domainObjectAccessStrategy))
			return thenConstraint.test(domainObjectAccessStrategy);
		if (elseConstraint != null)
			return elseConstraint.test(domainObjectAccessStrategy);

		return true;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

