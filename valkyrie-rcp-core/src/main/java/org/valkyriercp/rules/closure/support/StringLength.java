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
package org.valkyriercp.rules.closure.support;

import org.valkyriercp.rules.closure.Closure;

/**
 * Returns the Integer length of an object's string form, or zero if the object
 * is null.
 *
 * @author Keith Donald
 */
public class StringLength extends AbstractClosure {
    private static final StringLength INSTANCE = new StringLength();

    /**
     * Returns the shared StringLength instance--this is possible as the default
     * instance is immutable and stateless.
     *
     * @return the shared instance
     */
    public static Closure instance() {
        return INSTANCE;
    }

    /**
     * Evaluate the string form of the object returning an Integer representing
     * the string length.
     *
     * @return The string length Integer.
     * @see Closure#call(java.lang.Object)
     */
    public Object call(Object value) {
        if (value == null) { return new Integer(0); }
        return new Integer(String.valueOf(value).length());
    }

    public String toString() {
        return "strLength(arg)";
    }

}