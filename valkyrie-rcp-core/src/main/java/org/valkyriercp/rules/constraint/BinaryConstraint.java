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
package org.valkyriercp.rules.constraint;

/**
 * A function object that tests two arguments and returns a single
 * <code>boolean</code> result.
 * <p><p>
 * A binary constraint is a function object that tests two arguments against
 * some conditional expression. For example, a "GreaterThan" constraint will
 * return true if the first argument is greater than the second.
 *
 * @author Keith Donald
 */
public interface BinaryConstraint extends Constraint {

    /**
     * Test the provided arguments against this predicates conditional
     * expression.
     *
     * @param argument1
     *            the first argument
     * @param argument2
     *            the second argument
     * @return true if the condition was satisfied, false otherwise
     */
    public boolean test(Object argument1, Object argument2);
}