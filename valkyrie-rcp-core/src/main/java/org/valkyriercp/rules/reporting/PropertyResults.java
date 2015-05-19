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

import org.springframework.context.MessageSource;
import org.valkyriercp.core.Severity;
import org.valkyriercp.rules.constraint.Constraint;
import org.valkyriercp.util.ValkyrieRepository;

import java.util.Locale;

/**
 * @author Keith Donald
 */
public class PropertyResults implements ValidationResults {

    private String propertyName;
    private Object rejectedValue;
    private Constraint violatedConstraint;
    private Severity severity = Severity.ERROR;

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
    	MessageTranslator messageTranslator = ValkyrieRepository.getInstance().getApplicationConfig().messageTranslatorFactory().createTranslator(null, locale);
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
