package org.valkyriercp.rules.reporting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;
import org.valkyriercp.core.Severity;
import org.valkyriercp.rules.constraint.Constraint;

import java.util.Locale;

/**
 * @author Keith Donald
 */
@Configurable
public class PropertyResults implements ValidationResults {

    private String propertyName;
    private Object rejectedValue;
    private Constraint violatedConstraint;
    private Severity severity = Severity.ERROR;
    @Autowired
    private MessageTranslatorFactory messageTranslatorFactory;

    public PropertyResults(String propertyName, Object rejectedValue,
            Constraint violatedConstraint) {
        this.propertyName = propertyName;
        this.rejectedValue = rejectedValue;
        this.violatedConstraint = violatedConstraint;
    }

    /**
     * @deprecated MessageSource is configured by MessageTranslator. use <code>buildMessage(Locale)</code>
     * @see #buildMessage(java.util.Locale)
     */
    public String buildMessage(MessageSource messages, Locale locale) {
		return buildMessage(locale);
    }

    public String buildMessage(Locale locale) {
    	MessageTranslator messageTranslator = messageTranslatorFactory.createTranslator(null, locale);
        return messageTranslator.getMessage(this);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public Constraint getViolatedConstraint() {
        return violatedConstraint;
    }

    public int getViolatedCount() {
        return new SummingVisitor(getViolatedConstraint()).sum();
    }

    public Severity getSeverity() {
        return severity;
    }

}
