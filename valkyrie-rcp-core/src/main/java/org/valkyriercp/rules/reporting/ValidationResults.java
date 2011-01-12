package org.valkyriercp.rules.reporting;

import org.valkyriercp.core.Severity;
import org.valkyriercp.rules.constraint.Constraint;

/**
 * @author  Keith Donald
 */
public interface ValidationResults {

	/**
	 * @return Returns the rejectedValue.
	 */
	public Object getRejectedValue();

	/**
	 * @return Returns the violatedConstraint.
	 */
	public Constraint getViolatedConstraint();

	/**
	 * @return Returns the violatedCount.
	 */
	public int getViolatedCount();

	/**
	 * @return Returns the severity.
	 */
	public Severity getSeverity();
}
