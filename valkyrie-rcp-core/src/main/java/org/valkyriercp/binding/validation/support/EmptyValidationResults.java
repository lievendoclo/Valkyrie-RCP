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

import org.springframework.core.style.ToStringCreator;
import org.valkyriercp.binding.validation.ValidationResults;
import org.valkyriercp.core.Severity;

import java.util.Collections;
import java.util.Set;

/**
 * An implementation of ValidationResults that contains no results.
 *
 * @author Oliver Hutchison
 */
public class EmptyValidationResults implements ValidationResults {

    /**
     * The singleton instance of this class.
     */
    public static final ValidationResults INSTANCE = new EmptyValidationResults();

    protected EmptyValidationResults() {
    }

    /**
     * Always returns <code>false</code>
     */
    public boolean getHasErrors() {
        return false;
    }

    /**
     * Always returns <code>false</code>
     */
    public boolean getHasWarnings() {
        return false;
    }

    /**
     * Always returns <code>false</code>
     */
    public boolean getHasInfo() {
        return false;
    }

    /**
     * Always returns 0
     */
    public int getMessageCount() {
        return 0;
    }

    /**
     * Always returns 0
     */
    public int getMessageCount(Severity severity) {
        return 0;
    }

    /**
     * Always returns 0
     */
    public int getMessageCount(String propertyName) {
        return 0;
    }

    /**
     * Always returns an empty list.
     */
    public Set getMessages() {
        return Collections.EMPTY_SET;
    }

    /**
     * Always returns an empty list.
     */
    public Set getMessages(Severity severity) {
        return Collections.EMPTY_SET;
    }

    /**
     * Always returns an empty list.
     */
    public Set getMessages(String propertyName) {
        return Collections.EMPTY_SET;
    }

    public String toString() {
        return new ToStringCreator(this).toString();
    }
}

