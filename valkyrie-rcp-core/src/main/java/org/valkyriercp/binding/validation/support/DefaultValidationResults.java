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
package org.valkyriercp.binding.validation.support;

import org.springframework.binding.collection.AbstractCachingMapDecorator;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.ObjectUtils;
import org.valkyriercp.binding.validation.ValidationMessage;
import org.valkyriercp.binding.validation.ValidationResults;
import org.valkyriercp.core.Severity;

import java.util.*;

public class DefaultValidationResults implements ValidationResults {

    private final Set messages = new HashSet();

    private AbstractCachingMapDecorator messagesSubSets = new AbstractCachingMapDecorator() {

        protected Object create(Object key) {
            Set messagesSubSet = new HashSet();
            for (Iterator i = messages.iterator(); i.hasNext();) {
                ValidationMessage message = (ValidationMessage)i.next();
                if (key instanceof Severity && message.getSeverity().equals(key)) {
                    messagesSubSet.add(message);
                }
                else if (ObjectUtils.nullSafeEquals(message.getProperty(), key)) {
                    messagesSubSet.add(message);
                }
            }
            return Collections.unmodifiableSet(messagesSubSet);
        }

    };

    public DefaultValidationResults() {
    }

    public DefaultValidationResults(ValidationResults validationResults) {
        addAllMessages(validationResults);
    }

    public DefaultValidationResults(Collection validationMessages) {
        addAllMessages(validationMessages);
    }

    public void addAllMessages(ValidationResults validationResults) {
        addAllMessages(validationResults.getMessages());
    }

    public void addAllMessages(Collection validationMessages) {
        if (messages.addAll(validationMessages)) {
            messagesSubSets.clear();
        }
    }

    public void addMessage(ValidationMessage validationMessage) {
        if (messages.add(validationMessage)) {
            messagesSubSets.clear();
        }
    }

    public void addMessage(String field, Severity severity, String message) {
        addMessage(new DefaultValidationMessage(field, severity, message));
    }

    public void removeMessage(ValidationMessage message) {
        messages.remove(message);
        messagesSubSets.clear();
    }

    public boolean getHasErrors() {
        return getMessageCount(Severity.ERROR) > 0;
    }

    public boolean getHasWarnings() {
        return getMessageCount(Severity.WARNING) > 0;
    }

    public boolean getHasInfo() {
        return getMessageCount(Severity.INFO) > 0;
    }

    public int getMessageCount() {
        return messages.size();
    }

    public int getMessageCount(Severity severity) {
        return getMessages(severity).size();
    }

    public int getMessageCount(String fieldName) {
        return getMessages(fieldName).size();
    }

    public Set getMessages() {
        return Collections.unmodifiableSet(messages);
    }

    public Set getMessages(Severity severity) {
        return (Set)messagesSubSets.get(severity);
    }

    public Set getMessages(String fieldName) {
        return (Set)messagesSubSets.get(fieldName);
    }

    public String toString() {
        return new ToStringCreator(this).append("messages", getMessages()).toString();
    }

    /**
     * Clear all messages.
     */
    public void clearMessages()
    {
        messages.clear();
        messagesSubSets.clear();
    }

    /**
     * Clear all messages of the given fieldName.
     */
    public void clearMessages(String fieldName) {
    	Set messagesForFieldName = getMessages(fieldName);
    	for (Iterator mi = messagesForFieldName.iterator(); mi.hasNext();) {
			messages.remove(mi.next());
		}
    	messagesSubSets.clear();
    }
}