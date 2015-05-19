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
package org.valkyriercp.binding.value.support;

import org.valkyriercp.binding.value.ValueChangeDetector;

/**
 * An implementation of ValueChangeDetector that provides the same semantics as
 * {@link org.springframework.util.ObjectUtils#nullSafeEquals(java.lang.Object, java.lang.Object)}.
 * If the objects are not the same object, they are compared using the equals method of
 * the first object. Nulls are handled safely.
 *
 * @author Larry Streepy
 *
 */
public class EqualsValueChangeDetector implements ValueChangeDetector {

    /**
     * Determines if there has been a change in value between the provided arguments. The
     * objects are compared using the <code>equals</code> method.
     *
     * @param oldValue Original object value
     * @param newValue New object value
     * @return true if the objects are different enough to indicate a change in the value
     *         model
     */
    public boolean hasValueChanged(Object oldValue, Object newValue) {
        return !(oldValue == newValue || (oldValue != null && oldValue.equals( newValue )));
    }
}
