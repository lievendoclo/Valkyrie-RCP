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
