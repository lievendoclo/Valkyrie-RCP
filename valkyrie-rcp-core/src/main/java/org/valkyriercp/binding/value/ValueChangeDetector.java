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
package org.valkyriercp.binding.value;

/**
 * Defines the operations for determining if two values (an old and a new value) are
 * different enough to indicate a change in the value model. An implementation of this
 * interface can be registered in the application context configuration. The configured
 * instance is used in several classes to determine if two object values are sufficiently
 * different to trigger further logic, like firing a value changed event, updating
 * conversion values, etc.
 *
 * @author Larry Streepy
 * @see org.springframework.binding.value.support.DefaultValueChangeDetector
 * @see org.springframework.binding.value.support.EquivalenceValueChangeDetector
 */
public interface ValueChangeDetector {

    /**
     * Determine if there has been a change between two values (an old and a new value).
     * The definition of <em>different enough</em>, is dependent upon the needs of a
     * ValueModel implementation.
     *
     * @param oldValue Original object value
     * @param newValue New object value
     * @return true if the objects are different enough to indicate a change in the value
     *         model
     */
    public boolean hasValueChanged(Object oldValue, Object newValue);
}
