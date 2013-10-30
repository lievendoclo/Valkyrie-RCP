package org.valkyriercp.rules.reporting;

import org.valkyriercp.core.Severity;
import org.valkyriercp.rules.constraint.Constraint;

/**
 * @author Keith Donald
 */
public class ValueValidationResults implements ValidationResults {

	private Object argument;

	private Constraint violatedConstraint;

	public ValueValidationResults(Object argument, Constraint violatedConstraint) {
		this.argument = argument;
		this.violatedConstraint = violatedConstraint;
	}

	public ValueValidationResults(Object argument) {
		this.argument = argument;
	}

	public Object getRejectedValue() {
		return argument;
	}

	public Constraint getViolatedConstraint() {
		return violatedConstraint;
	}

	public int getViolatedCount() {
		if (violatedConstraint != null)
			return new SummingVisitor(violatedConstraint).sum();

        return 0;
	}

	public Severity getSeverity() {
		return Severity.ERROR;
	}

}
