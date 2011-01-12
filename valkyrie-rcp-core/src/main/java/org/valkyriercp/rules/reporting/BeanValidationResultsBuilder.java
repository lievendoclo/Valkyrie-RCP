package org.valkyriercp.rules.reporting;

import org.valkyriercp.binding.PropertyAccessStrategy;
import org.valkyriercp.binding.support.BeanPropertyAccessStrategy;
import org.valkyriercp.rules.constraint.Constraint;
import org.valkyriercp.rules.constraint.property.PropertyConstraint;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Keith Donald
 */
public class BeanValidationResultsBuilder extends ValidationResultsBuilder
		implements BeanValidationResults {

	private String currentProperty;

	private Object currentPropertyValue;

	private Map beanResults = new HashMap();

	private PropertyAccessStrategy beanPropertyAccessStrategy;

	public BeanValidationResultsBuilder(Object bean) {
		super();
		if (bean instanceof PropertyAccessStrategy) {
			this.beanPropertyAccessStrategy = (PropertyAccessStrategy) bean;
		}
		else {
			this.beanPropertyAccessStrategy = new BeanPropertyAccessStrategy(
					bean);
		}
	}

	public Map getResults() {
		return Collections.unmodifiableMap(beanResults);
	}

	public PropertyResults getResults(String propertyName) {
		return (PropertyResults) beanResults.get(propertyName);
	}

	public int getViolatedCount() {
		int count = 0;
		Iterator it = beanResults.values().iterator();
		while (it.hasNext()) {
			count += ((PropertyResults) it.next()).getViolatedCount();
		}
		return count;
	}

	protected void constraintViolated(Constraint constraint) {
		if (logger.isDebugEnabled()) {
			logger.debug("[Done] collecting results for property '"
					+ getCurrentPropertyName() + "'.  Constraints violated: ["
					+ constraint + "]");
		}
		PropertyResults results = new PropertyResults(getCurrentPropertyName(),
				getCurrentPropertyValue(), constraint);
		beanResults.put(getCurrentPropertyName(), results);
	}

	protected void constraintSatisfied() {
		if (logger.isDebugEnabled()) {
			logger.debug("[Done] collecting results for property '"
					+ getCurrentPropertyName() + "'.  All constraints met.");
		}
	}

	public String getCurrentPropertyName() {
		return currentProperty;
	}

	public Object getCurrentPropertyValue() {
		return currentPropertyValue;
	}

	public void setCurrentBeanPropertyExpression(
			PropertyConstraint expression) {
		this.currentProperty = expression.getPropertyName();
		this.currentPropertyValue = getPropertyValue(this.currentProperty);
		super.clear();
	}

	private Object getPropertyValue(String propertyName) {
		return beanPropertyAccessStrategy.getPropertyValue(propertyName);
	}

}
