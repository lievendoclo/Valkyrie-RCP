package org.valkyriercp.rules.constraint;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.valkyriercp.rules.closure.Closure;
import org.valkyriercp.rules.closure.support.AlgorithmsAccessor;
import org.valkyriercp.rules.constraint.property.*;

import java.util.Comparator;
import java.util.Set;

/**
 * A factory for easing the construction and composition of constraints.
 *
 * @author Keith Donald
 */
public class Constraints extends AlgorithmsAccessor {

    private static Constraints INSTANCE = new Constraints();

    public Constraints() {

    }

    public static Constraints instance() {
        return INSTANCE;
    }

    public static void load(Constraints sharedInstance) {
        Assert.notNull(sharedInstance, "The global constraints factory cannot be null");
        INSTANCE = sharedInstance;
    }

    /**
     * Bind the specified parameter to the second argument of the
     * <code>BinaryConstraint</code>. The result is a <code>Constraint</code>
     * which will test a single variable argument against the constant
     * parameter.
     *
     * @param constraint the binary constraint to bind to
     * @param parameter the parameter value (constant)
     * @return The constraint
     */
    public Constraint bind(BinaryConstraint constraint, Object parameter) {
        return new ParameterizedBinaryConstraint(constraint, parameter);
    }

    /**
     * Bind the specified <code>int</code> parameter to the second argument of
     * the <code>BinaryConstraint</code>. The result is a
     * <code>Constraint</code> which will test a single variable argument
     * against the constant <code>int</code> parameter.
     *
     * @param constraint the binary constraint to bind to
     * @param parameter the <code>int</code> parameter value (constant)
     * @return The constraint
     */
    public Constraint bind(BinaryConstraint constraint, int parameter) {
        return new ParameterizedBinaryConstraint(constraint, parameter);
    }

    /**
     * Bind the specified <code>float</code> parameter to the second argument
     * of the <code>BinaryConstraint</code>. The result is a
     * <code>Constraint</code> which will test a single variable argument
     * against the constant <code>float</code> parameter.
     *
     * @param constraint the binary constraint to bind to
     * @param parameter the <code>float</code> parameter value (constant)
     * @return The constraint
     */
    public Constraint bind(BinaryConstraint constraint, float parameter) {
        return new ParameterizedBinaryConstraint(constraint, parameter);
    }

    /**
     * Bind the specified <code>double</code> parameter to the second argument
     * of the <code>BinaryConstraint</code>. The result is a
     * <code>Constraint</code> which will test a single variable argument
     * against the constant <code>double</code> parameter.
     *
     * @param constraint the binary constraint to bind to
     * @param parameter the <code>double</code> parameter value (constant)
     * @return The constraint
     */
    public Constraint bind(BinaryConstraint constraint, double parameter) {
        return new ParameterizedBinaryConstraint(constraint, parameter);
    }

    /**
     * Bind the specified <code>boolean</code> parameter to the second
     * argument of the <code>BinaryConstraint</code>. The result is a
     * <code>Constraint</code> which will test a single variable argument
     * against the constant <code>boolean</code> parameter.
     *
     * @param constraint the binary constraint to bind to
     * @param parameter the <code>boolean</code> parameter value (constant)
     * @return The constraint
     */
    public Constraint bind(BinaryConstraint constraint, boolean parameter) {
        return new ParameterizedBinaryConstraint(constraint, parameter);
    }

    /**
     * Attaches a constraint that tests the result returned by evaluating the
     * specified closure. This effectively attaches a constraint on the closure
     * return value.
     *
     * @param closure the closure
     * @param constraint the constraint to test the closure result
     * @return The testing constraint, which on the call to test(o) first
     *         evaluates 'o' using the closure and then tests the result.
     */
    public Constraint testResultOf(Closure closure, Constraint constraint) {
        return new ClosureResultConstraint(closure, constraint);
    }

    public Constraint eq(Object value) {
        return EqualTo.value(value);
    }

    public Constraint eq(int value) {
        return eq(new Integer(value));
    }

    public Constraint eq(Object value, Comparator comparator) {
        return EqualTo.value(value, comparator);
    }

    public Constraint gt(Comparable value) {
        return GreaterThan.value(value);
    }

    public Constraint gt(Object value, Comparator comparator) {
        return GreaterThan.value(value, comparator);
    }

    public Constraint gt(int value) {
        return gt(new Integer(value));
    }

    public Constraint gt(long value) {
        return gt(new Long(value));
    }

    public Constraint gt(float value) {
        return gt(new Float(value));
    }

    public Constraint gt(double value) {
        return gt(new Double(value));
    }

    public Constraint gte(Comparable value) {
        return GreaterThanEqualTo.value(value);
    }

    public Constraint gte(Object value, Comparator comparator) {
        return GreaterThanEqualTo.value(value, comparator);
    }

    public Constraint gte(int value) {
        return gte(new Integer(value));
    }

    public Constraint gte(long value) {
        return gte(new Long(value));
    }

    public Constraint gte(float value) {
        return gte(new Float(value));
    }

    public Constraint gte(double value) {
        return gte(new Double(value));
    }

    public Constraint lt(Comparable value) {
        return LessThan.value(value);
    }

    public Constraint lt(Comparable value, Comparator comparator) {
        return LessThan.value(value, comparator);
    }

    public Constraint lt(int value) {
        return lt(new Integer(value));
    }

    public Constraint lt(long value) {
        return lt(new Long(value));
    }

    public Constraint lt(float value) {
        return lt(new Float(value));
    }

    public Constraint lt(double value) {
        return lt(new Double(value));
    }

    public Constraint lte(Comparable value) {
        return LessThanEqualTo.value(value);
    }

    public Constraint lte(Object value, Comparator comparator) {
        return LessThanEqualTo.value(value, comparator);
    }

    public Constraint lte(int value) {
        return lte(new Integer(value));
    }

    public Constraint lte(long value) {
        return lte(new Long(value));
    }

    public Constraint lte(float value) {
        return lte(new Float(value));
    }

    public Constraint lte(double value) {
        return lte(new Double(value));
    }

    public Constraint range(Comparable min, Comparable max) {
        return new Range(min, max);
    }

    public Constraint range(Comparable min, Comparable max, boolean inclusive) {
        return new Range(min, max, inclusive);
    }

    public Constraint range(Object min, Object max, Comparator comparator) {
        return new Range(min, max, comparator);
    }

    public Constraint range(Object min, Object max, Comparator comparator, boolean inclusive) {
        return new Range(min, max, comparator, inclusive);
    }

    public Constraint range(int min, int max) {
        return new Range(min, max);
    }

    public Constraint range(long min, long max) {
        return new Range(min, max);
    }

    public Constraint range(float min, float max) {
        return new Range(min, max);
    }

    public Constraint range(double min, double max) {
        return new Range(min, max);
    }

    public Constraint present() {
        return Required.present();
    }

	/**
     * Returns a required constraint.
     *
     * @return The required constraint instance.
     */
    public Constraint required() {
        return Required.instance();
    }

    public Constraint ifTrue(Constraint constraint, Constraint mustAlsoBeTrue) {
        return new IfTrue(constraint, mustAlsoBeTrue);
    }

    public Constraint ifTrue(Constraint constraint, Constraint mustAlsoBeTrue, Constraint elseMustAlsoBeTrue) {
        return new IfTrue(constraint, mustAlsoBeTrue, elseMustAlsoBeTrue);
    }

    public Constraint ifTrue(Constraint constraint, Constraint mustAlsoBeTrue, String type) {
        return new IfTrue(constraint, mustAlsoBeTrue, type);
    }

    public Constraint ifTrue(Constraint constraint, Constraint mustAlsoBeTrue, Constraint elseMustAlsoBeTrue, String type) {
        return new IfTrue(constraint, mustAlsoBeTrue, elseMustAlsoBeTrue, type);
    }

    /**
	 * Returns a ConditionalPropertyConstraint: one property will trigger the
	 * validation of another.
	 *
	 * @see ConditionalPropertyConstraint
	 */
	public PropertyConstraint ifTrue(PropertyConstraint ifConstraint, PropertyConstraint thenConstraint) {
		return new ConditionalPropertyConstraint(ifConstraint, thenConstraint);
	}

	/**
	 * Returns a ConditionalPropertyConstraint: one property will trigger the
	 * validation of another.
	 *
	 * @see ConditionalPropertyConstraint
	 */
	public PropertyConstraint ifTrue(PropertyConstraint ifConstraint, PropertyConstraint thenConstraint, String type) {
		return new ConditionalPropertyConstraint(ifConstraint, thenConstraint, type);
	}

    /**
	 * Returns a ConditionalPropertyConstraint: one property will trigger the
	 * validation of another.
	 *
	 * @see ConditionalPropertyConstraint
	 */
	public PropertyConstraint ifTrue(PropertyConstraint ifConstraint, PropertyConstraint thenConstraint,
			PropertyConstraint elseConstraint) {
		return new ConditionalPropertyConstraint(ifConstraint, thenConstraint, elseConstraint);
	}

	/**
	 * Returns a ConditionalPropertyConstraint: one property will trigger the
	 * validation of another.
	 *
	 * @see ConditionalPropertyConstraint
	 */
	public PropertyConstraint ifTrue(PropertyConstraint ifConstraint, PropertyConstraint thenConstraint,
			PropertyConstraint elseConstraint, String type) {
		return new ConditionalPropertyConstraint(ifConstraint, thenConstraint, elseConstraint, type);
	}

	/**
	 * Returns a ConditionalPropertyConstraint: one property will trigger the
	 * validation of another.
	 *
	 * @see ConditionalPropertyConstraint
	 */
	public PropertyConstraint ifTrue(PropertyConstraint ifConstraint, PropertyConstraint[] thenConstraints) {
		return new ConditionalPropertyConstraint(ifConstraint, new CompoundPropertyConstraint(new And(thenConstraints)));
	}

	/**
	 * Returns a ConditionalPropertyConstraint: one property will trigger the
	 * validation of another.
	 *
	 * @see ConditionalPropertyConstraint
	 */
	public PropertyConstraint ifTrue(PropertyConstraint ifConstraint, PropertyConstraint[] thenConstraints, String type) {
		return new ConditionalPropertyConstraint(ifConstraint, new CompoundPropertyConstraint(new And(thenConstraints)), type);
	}

    /**
	 * Returns a ConditionalPropertyConstraint: one property will trigger the
	 * validation of another.
	 *
	 * @see ConditionalPropertyConstraint
	 */
	public PropertyConstraint ifTrue(PropertyConstraint ifConstraint, PropertyConstraint[] thenConstraints,
			PropertyConstraint[] elseConstraints) {
		return new ConditionalPropertyConstraint(ifConstraint,
				new CompoundPropertyConstraint(new And(thenConstraints)), new CompoundPropertyConstraint(new And(
						elseConstraints)));
	}

	/**
	 * Returns a ConditionalPropertyConstraint: one property will trigger the
	 * validation of another.
	 *
	 * @see ConditionalPropertyConstraint
	 */
	public PropertyConstraint ifTrue(PropertyConstraint ifConstraint, PropertyConstraint[] thenConstraints,
			PropertyConstraint[] elseConstraints, String type) {
		return new ConditionalPropertyConstraint(ifConstraint,
				new CompoundPropertyConstraint(new And(thenConstraints)), new CompoundPropertyConstraint(new And(
						elseConstraints)), type);
	}

    /**
	 * Returns a maxlength constraint.
	 *
	 * @param maxLength The maximum length in characters.
	 * @return The configured maxlength constraint.
	 */
    public Constraint maxLength(int maxLength) {
        return new StringLengthConstraint(maxLength);
    }

    /**
     * Returns a minlength constraint.
     *
     * @param minLength The minimum length in characters.
     * @return The configured minlength constraint.
     */
    public Constraint minLength(int minLength) {
        return new StringLengthConstraint(RelationalOperator.GREATER_THAN_EQUAL_TO, minLength);
    }

    /**
     * Returns a 'like' constraint.
     *
     * @param encodedLikeString the likeString
     * @return The Like constraint.
     */
    public Constraint like(String encodedLikeString) {
        return new Like(encodedLikeString);
    }

    /**
     * Creates a constraint backed by a regular expression.
     *
     * @param regexp The regular expression string.
     * @return The constraint.
     */
    public Constraint regexp(String regexp) {
        return new RegexpConstraint(regexp);
    }

    /**
     * Creates a constraint backed by a regular expression, with a type for
     * reporting.
     *
     * @param regexp The regular expression string.
     * @return The constraint.
     */
    public Constraint regexp(String regexp, String type) {
        RegexpConstraint c = new RegexpConstraint(regexp);
        c.setType(type);
        return c;
    }

    /**
     * Returns a constraint whose test is determined by a boolean method on a
     * target object.
     *
     * @param target The targetObject
     * @param methodName The method name
     * @return The constraint.
     */
    public Constraint method(Object target, String methodName, String constraintType) {
        return new MethodInvokingConstraint(target, methodName, constraintType);
    }

    /**
     * Returns a 'in' group (or set) constraint.
     *
     * @param group the group items
     * @return The InGroup constraint
     */
    public Constraint inGroup(Set group) {
        return new InGroup(group);
    }

    /**
     * Returns a 'in' group (or set) constraint.
     *
     * @param group the group items
     * @return The InGroup constraint.
     */
    public Constraint inGroup(Object[] group) {
        return new InGroup(group);
    }

    /**
     * Returns a 'in' group (or set) constraint.
     *
     * @param group the group items
     * @return The InGroup constraint.
     */
    public Constraint inGroup(int[] group) {
        return inGroup(ObjectUtils.toObjectArray(group));
    }

    /**
     * AND two constraints.
     *
     * @param constraint1 the first constraint
     * @param constraint2 the second constraint
     * @return The compound AND constraint
     */
    public And and(Constraint constraint1, Constraint constraint2) {
        return new And(constraint1, constraint2);
    }

    /**
     * Return the conjunction (all constraint) for all constraints.
     *
     * @param constraints the constraints
     * @return The compound AND constraint
     */
    public And all(Constraint[] constraints) {
        return new And(constraints);
    }

    /**
     * Returns a new, empty conjunction prototype, capable of composing
     * individual constraints where 'ALL' must test true.
     *
     * @return the UnaryAnd
     */
    public And conjunction() {
        return new And();
    }

    /**
     * OR two constraints.
     *
     * @param constraint1 the first constraint
     * @param constraint2 the second constraint
     * @return The compound OR constraint
     */
    public Or or(Constraint constraint1, Constraint constraint2) {
        return new Or(constraint1, constraint2);
    }

    /**
     * Return the disjunction (any constraint) for all constraints.
     *
     * @param constraints the constraints
     * @return The compound AND constraint
     */
    public Or any(Constraint[] constraints) {
        return new Or(constraints);
    }

    /**
     * Returns a new, empty disjunction prototype, capable of composing
     * individual constraints where 'ANY' must test true.
     *
     * @return the UnaryOr
     */
    public Or disjunction() {
        return new Or();
    }

    /**
     * Returns a new, empty exclusive disjunction prototype, capable of composing
     * individual constraints where only one must test true.
     *
     * @return the UnaryXOr
     */
    public XOr exclusiveDisjunction() {
        return new XOr();
    }

    /**
     * Negate the specified constraint.
     *
     * @param constraint The constraint to negate
     * @return The negated constraint.
     */
    public Constraint not(Constraint constraint) {
        if (!(constraint instanceof Not))
            return new Not(constraint);

        return ((Not)constraint).getConstraint();
    }

    /**
     * Attach a value constraint for the provided bean property.
     *
     * @param propertyName the bean property name
     * @param valueConstraint the value constraint
     * @return The bean property expression that tests the constraint
     */
    public PropertyConstraint value(String propertyName, Constraint valueConstraint) {
        return new PropertyValueConstraint(propertyName, valueConstraint);
    }

    /**
     * Returns a present bean property expression.
     *
     * @return The present constraint instance.
     */
	public PropertyConstraint present(String propertyName) {
		return value(propertyName, present());
	}

    /**
     * Returns a required bean property expression.
     *
     * @return The required constraint instance.
     */
    public PropertyConstraint required(String property) {
        return value(property, required());
    }

    /**
     * Return a 'like' constraint applied as a value constraint to the provided
     * property.
     *
     * @param property The property to constrain
     * @param likeType The like type
     * @param value The like string value to match
     * @return The Like constraint
     */
    public PropertyConstraint like(String property, Like.LikeType likeType, String value) {
        return value(property, new Like(likeType, value));
    }

    /**
     * Returns a 'in' group (or set) constraint appled to the provided property.
     *
     * @param propertyName the property
     * @param group the group items
     * @return The InGroup constraint.
     */
    public PropertyConstraint inGroup(String propertyName, Object[] group) {
        return value(propertyName, new InGroup(group));
    }

    /**
     * Apply an "all" value constraint to the provided bean property.
     *
     * @param propertyName The bean property name
     * @param constraints The constraints that form a all conjunction
     * @return
     */
    public PropertyConstraint all(String propertyName, Constraint[] constraints) {
        return value(propertyName, all(constraints));
    }

    /**
     * Apply an "any" value constraint to the provided bean property.
     *
     * @param propertyName The bean property name
     * @param constraints The constraints that form a all disjunction
     * @return
     */
    public PropertyConstraint any(String propertyName, Constraint[] constraints) {
        return value(propertyName, any(constraints));
    }

    /**
     * Negate a bean property expression.
     *
     * @param e the expression to negate
     * @return The negated expression
     */
    public PropertyConstraint not(PropertyConstraint e) {
        return new NegatedPropertyConstraint(e);
    }

    public PropertyConstraint valueProperty(String propertyName, BinaryConstraint constraint, Object value) {
        return new ParameterizedPropertyConstraint(propertyName, constraint, value);
    }

    /**
     * Apply a "equal to" constraint to a bean property.
     *
     * @param propertyName The first property
     * @param propertyValue The constraint value
     * @return The constraint
     */
    public PropertyConstraint eq(String propertyName, Object propertyValue) {
        return new ParameterizedPropertyConstraint(propertyName, eq(propertyValue));
    }

    /**
     * Apply a "equal to" constraint to a bean property.
     *
     * @param propertyName The first property
     * @param propertyValue The constraint value
     * @param comparator the comparator to use while comparing the values
     * @return The constraint
     *
     * @since 0.3.0
     */
    public PropertyConstraint eq(String propertyName, Object propertyValue, Comparator comparator) {
        return new ParameterizedPropertyConstraint(propertyName, eq(propertyValue, comparator));
    }

    /**
     * Apply a "greater than" constraint to a bean property.
     *
     * @param propertyName The first property
     * @param propertyValue The constraint value
     * @return The constraint
     */
    public PropertyConstraint gt(String propertyName, Comparable propertyValue) {
        return new ParameterizedPropertyConstraint(propertyName, gt(propertyValue));
    }

    /**
     * Apply a "greater than equal to" constraint to a bean property.
     *
     * @param propertyName The first property
     * @param propertyValue The constraint value
     * @return The constraint
     */
    public PropertyConstraint gte(String propertyName, Comparable propertyValue) {
        return new ParameterizedPropertyConstraint(propertyName, gte(propertyValue));
    }

    /**
     * Apply a "less than" constraint to a bean property.
     *
     * @param propertyName The first property
     * @param propertyValue The constraint value
     * @return The constraint
     */
    public PropertyConstraint lt(String propertyName, Comparable propertyValue) {
        return new ParameterizedPropertyConstraint(propertyName, lt(propertyValue));
    }

    /**
     * Apply a "less than equal to" constraint to a bean property.
     *
     * @param propertyName The first property
     * @param propertyValue The constraint value
     * @return The constraint
     */
    public PropertyConstraint lte(String propertyName, Comparable propertyValue) {
        return new ParameterizedPropertyConstraint(propertyName, lte(propertyValue));
    }

    public PropertyConstraint valueProperties(String propertyName, BinaryConstraint constraint, String otherPropertyName) {
        return new PropertiesConstraint(propertyName, constraint, otherPropertyName);
    }

    /**
     * Apply a "equal to" constraint to two bean properties.
     *
     * @param propertyName The first property
     * @param otherPropertyName The other property
     * @param comparator the comparator to use while comparing the values
     * @return The constraint
     *
     * @since 0.3.0
     */
    public PropertyConstraint eqProperty(String propertyName, String otherPropertyName, Comparator comparator) {
        return valueProperties(propertyName, EqualTo.instance(comparator), otherPropertyName);
    }

    /**
     * Apply a "greater than" constraint to two properties
     *
     * @param propertyName The first property
     * @param otherPropertyName The other property
     * @param comparator the comparator to use while comparing the values
     * @return The constraint
     *
     * @since 0.3.0
     */
    public PropertyConstraint gtProperty(String propertyName, String otherPropertyName, Comparator comparator) {
        return valueProperties(propertyName, GreaterThan.instance(comparator), otherPropertyName);
    }

    /**
     * Apply a "greater than or equal to" constraint to two properties.
     *
     * @param propertyName The first property
     * @param otherPropertyName The other property
     * @param comparator the comparator to use while comparing the values
     * @return The constraint
     *
     * @since 0.3.0
     */
    public PropertyConstraint gteProperty(String propertyName, String otherPropertyName, Comparator comparator) {
        return valueProperties(propertyName, GreaterThanEqualTo.instance(comparator), otherPropertyName);
    }

    /**
     * Apply a "less than" constraint to two properties.
     *
     * @param propertyName The first property
     * @param otherPropertyName The other property
     * @param comparator the comparator to use while comparing the values
     * @return The constraint
     *
     * @since 0.3.0
     */
    public PropertyConstraint ltProperty(String propertyName, String otherPropertyName, Comparator comparator) {
        return valueProperties(propertyName, LessThan.instance(comparator), otherPropertyName);
    }

    /**
     * Apply a "less than or equal to" constraint to two properties.
     *
     * @param propertyName The first property
     * @param otherPropertyName The other property
     * @param comparator the comparator to use while comparing the values
     * @return The constraint
     *
     * @since 0.3.0
     */
    public PropertyConstraint lteProperty(String propertyName, String otherPropertyName, Comparator comparator) {
        return valueProperties(propertyName, LessThanEqualTo.instance(comparator), otherPropertyName);
    }

    /**
     * Apply a inclusive "range" constraint to a bean property.
     *
     * @param propertyName the property with the range constraint.
     * @param min the low edge of the range
     * @param max the high edge of the range
     * @param comparator the comparator to use while comparing the values
     * @return The range constraint constraint
     *
     * @since 0.3.0
     */
    public PropertyConstraint inRange(String propertyName, Object min, Object max, Comparator comparator) {
        return value(propertyName, range(min, max, comparator));
    }

    /**
     * Apply a inclusive "range" constraint between two other properties to a
     * bean property.
     *
     * @param propertyName the property with the range constraint.
     * @param minPropertyName the low edge of the range
     * @param maxPropertyName the high edge of the range
     * @param comparator the comparator to use while comparing the values
     * @return The range constraint constraint
     *
     * @since 0.3.0
     */
    public PropertyConstraint inRangeProperties(String propertyName, String minPropertyName, String maxPropertyName, Comparator comparator) {
        Constraint min = gteProperty(propertyName, minPropertyName, comparator);
        Constraint max = lteProperty(propertyName, maxPropertyName, comparator);
        return new CompoundPropertyConstraint(new And(min, max));
    }

    /**
     * Apply a "equal to" constraint to two bean properties.
     *
     * @param propertyName The first property
     * @param otherPropertyName The other property
     * @return The constraint
     */
    public PropertyConstraint eqProperty(String propertyName, String otherPropertyName) {
        return valueProperties(propertyName, EqualTo.instance(), otherPropertyName);
    }

    /**
     * Apply a "greater than" constraint to two properties
     *
     * @param propertyName The first property
     * @param otherPropertyName The other property
     * @return The constraint
     */
    public PropertyConstraint gtProperty(String propertyName, String otherPropertyName) {
        return valueProperties(propertyName, GreaterThan.instance(), otherPropertyName);
    }

    /**
     * Apply a "greater than or equal to" constraint to two properties.
     *
     * @param propertyName The first property
     * @param otherPropertyName The other property
     * @return The constraint
     */
    public PropertyConstraint gteProperty(String propertyName, String otherPropertyName) {
        return valueProperties(propertyName, GreaterThanEqualTo.instance(), otherPropertyName);
    }

    /**
     * Apply a "less than" constraint to two properties.
     *
     * @param propertyName The first property
     * @param otherPropertyName The other property
     * @return The constraint
     */
    public PropertyConstraint ltProperty(String propertyName, String otherPropertyName) {
        return valueProperties(propertyName, LessThan.instance(), otherPropertyName);
    }

    /**
     * Apply a "less than or equal to" constraint to two properties.
     *
     * @param propertyName The first property
     * @param otherPropertyName The other property
     * @return The constraint
     */
    public PropertyConstraint lteProperty(String propertyName, String otherPropertyName) {
        return valueProperties(propertyName, LessThanEqualTo.instance(), otherPropertyName);
    }

    /**
     * Apply a inclusive "range" constraint to a bean property.
     *
     * @param propertyName the property with the range constraint.
     * @param min the low edge of the range
     * @param max the high edge of the range
     * @return The range constraint constraint
     */
    public PropertyConstraint inRange(String propertyName, Comparable min, Comparable max) {
        return value(propertyName, range(min, max));
    }

    /**
     * Apply a inclusive "range" constraint between two other properties to a
     * bean property.
     *
     * @param propertyName the property with the range constraint.
     * @param minPropertyName the low edge of the range
     * @param maxPropertyName the high edge of the range
     * @return The range constraint constraint
     */
    public PropertyConstraint inRangeProperties(String propertyName, String minPropertyName, String maxPropertyName) {
        Constraint min = gteProperty(propertyName, minPropertyName);
        Constraint max = lteProperty(propertyName, maxPropertyName);
        return new CompoundPropertyConstraint(new And(min, max));
    }

    /**
     * Create a unique property value constraint that will test a collection of
     * domain objects, returning true if all objects have unique values for the
     * provided propertyName.
     * @param propertyName The property name
     * @return The constraint
     */
    public PropertyConstraint unique(String propertyName) {
        return new UniquePropertyValueConstraint(propertyName);
    }
}
