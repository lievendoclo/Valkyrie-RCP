package org.valkyriercp.rules.reporting;

import org.springframework.util.Assert;
import org.valkyriercp.rules.Rules;
import org.valkyriercp.rules.closure.support.Block;
import org.valkyriercp.rules.constraint.Constraint;
import org.valkyriercp.rules.constraint.property.CompoundPropertyConstraint;
import org.valkyriercp.rules.constraint.property.PropertyConstraint;
import org.valkyriercp.rules.constraint.property.PropertyValueConstraint;

/**
 * @author Keith Donald
 */
public class BeanValidationResultsCollector extends ValidationResultsCollector {

    private Object bean;

    public BeanValidationResultsCollector(Object bean) {
        super();
        setBean(bean);
    }

    public void setBean(Object bean) {
        Assert.notNull(bean, "bean is required");
        this.bean = bean;
    }

    protected BeanValidationResultsBuilder getBeanResultsBuilder() {
        return (BeanValidationResultsBuilder)getResultsBuilder();
    }

    public void setResultsBuilder(ValidationResultsBuilder builder) {
        Assert.isTrue(builder instanceof BeanValidationResultsBuilder,
                "Builder must be a bean validation results builder");
        super.setResultsBuilder(builder);
    }

    public BeanValidationResults collectResults(Rules rules) {
        Assert.notNull(rules, "rules is required");
        setResultsBuilder(new BeanValidationResultsBuilder(bean));
        new Block() {
            protected void handle(Object beanPropertyConstraint) {
                collectPropertyResultsInternal((PropertyConstraint)beanPropertyConstraint);
            }
        }.forEach(rules.iterator());
        return getBeanResultsBuilder();
    }

    public PropertyResults collectPropertyResults(PropertyConstraint propertyRootExpression) {
        Assert.notNull(propertyRootExpression, "propertyRootExpression is required");
        setResultsBuilder(new BeanValidationResultsBuilder(this.bean));
        return collectPropertyResultsInternal(propertyRootExpression);
    }

    private PropertyResults collectPropertyResultsInternal(PropertyConstraint rootExpression) {
        getBeanResultsBuilder().setCurrentBeanPropertyExpression(rootExpression);
        setArgument(getBeanResultsBuilder().getCurrentPropertyValue());
        boolean result = ((Boolean)visitorSupport.invokeVisit(this, rootExpression)).booleanValue();
        if (logger.isDebugEnabled()) {
            logger.debug("Final validation result: " + result);
        }
        if (!result)
            return getBeanResultsBuilder().getResults(rootExpression.getPropertyName());

        return null;
    }

    Boolean visit(CompoundPropertyConstraint rule) {
        if (logger.isDebugEnabled()) {
            logger.debug("Validating compound bean property expression [" + rule + "]...");
        }
        return (Boolean)visitorSupport.invokeVisit(this, rule.getPredicate());
    }

    boolean visit(PropertyConstraint constraint) {
        if (logger.isDebugEnabled()) {
            logger.debug("Validating bean properties expression [" + constraint + "]...");
        }
        return testBeanPropertyExpression(constraint);
    }

    private boolean testBeanPropertyExpression(PropertyConstraint constraint) {
        boolean result = constraint.test(bean);
        result = applyAnyNegation(result);
        if (!result) {
            getBeanResultsBuilder().push(constraint);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Constraint [" + constraint + "] " + (result ? "passed" : "failed"));
        }
        return result;
    }

    Boolean visit(PropertyValueConstraint valueConstraint) {
        if (logger.isDebugEnabled()) {
            logger.debug("Validating property value constraint [" + valueConstraint + "]...");
        }
        return (Boolean)visitorSupport.invokeVisit(this, valueConstraint.getConstraint());
    }

    boolean visit(Constraint constraint) {
        return super.visit(constraint);
    }
}