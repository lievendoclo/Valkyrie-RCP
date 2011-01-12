package org.valkyriercp.rules.reporting;

import org.valkyriercp.rules.constraint.Constraint;

public interface MessageTranslator {
    String getMessage(Constraint constraint);

	String getMessage(String objectName, Constraint constraint);

	String getMessage(String objectName, Object rejectedValue,
			Constraint constraint);

	String getMessage(String objectName, ValidationResults results);

	String getMessage(PropertyResults results);
}
