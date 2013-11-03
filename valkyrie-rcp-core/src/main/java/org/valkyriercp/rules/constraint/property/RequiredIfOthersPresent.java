package org.valkyriercp.rules.constraint.property;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.valkyriercp.rules.constraint.CompoundConstraint;
import org.valkyriercp.rules.constraint.LogicalOperator;

/**
 * @author Keith Donald
 */
public class RequiredIfOthersPresent extends RequiredIfTrue {

    /**
     * Tests that the property is required if all "other properties" are present. Present
     * means they are "non null."
     *
     * @param otherPropertyNames to test
     */
    public RequiredIfOthersPresent( String propertyName, String[] otherPropertyNames ) {
        this(propertyName, otherPropertyNames, LogicalOperator.AND);
    }

    /**
     * Tests that the property is required if "other properties" are present. Present
     * means they are "non null." The operator parameter determines how the set of other
     * property names is handled. If AND, then all must be present before the primary
     * proeprty will be required. If OR, then if any of the other properties are present,
     * then the primary property will be required. the logical operator, either AND or OR.
     *
     * @param otherPropertyNames to test
     * @param operator Either AND or OR.
     */
    public RequiredIfOthersPresent( String propertyName, String[] otherPropertyNames, LogicalOperator operator ) {
        super(propertyName);
        Assert.notNull(otherPropertyNames, "otherPropertyNames is required");
        Assert.notNull(operator, "operator is required");
        Assert.notEmpty(otherPropertyNames, "otherPropertyNames must consist of at least one name");

        CompoundConstraint compoundConstraint = operator.createConstraint();
        CompoundPropertyConstraint propertyConstraint = new CompoundPropertyConstraint(compoundConstraint);
        for( int i = 0; i < otherPropertyNames.length; i++ ) {
            propertyConstraint.add(new PropertyPresent(otherPropertyNames[i]));
        }
        setConstraint(propertyConstraint);
    }

    /**
     * Tests that the property is required if all "other properties" are present. Present
     * means they are "non null."
     *
     * @param otherPropertyNames one or more other properties, delimited by commas.
     */
    public RequiredIfOthersPresent( String propertyName, String otherPropertyNames ) {
        this(propertyName, otherPropertyNames, LogicalOperator.AND);
    }

    /**
     * Tests that the property is required if all or any of the "other properties" are
     * present.
     *
     * @param otherPropertyNames one or more other properties, delimited by commas.
     * @param operator the logical operator, either AND or OR.
     */
    public RequiredIfOthersPresent( String propertyName, String otherPropertyNames, LogicalOperator operator ) {
        this(propertyName, StringUtils.commaDelimitedListToStringArray(otherPropertyNames), operator);
    }

    public boolean isDependentOn( String propertyName ) {
        return getPropertyName().equals(propertyName)
                || ((CompoundPropertyConstraint) getConstraint()).isDependentOn(propertyName);
    }

    public boolean isCompoundRule() {
        return true;
    }
}

