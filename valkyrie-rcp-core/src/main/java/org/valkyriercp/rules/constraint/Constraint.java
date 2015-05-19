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
 * A function object that tests one argument and returns a single
 * <code>boolean</code> result.
 * <p>
 * A constraint tests a single argument against some conditional expression. For
 * example, a "required" constraint will return true if the provided argument is
 * non-null or empty, false otherwise.
 * </p>
 *
 * @author Keith Donald
 */
public interface Constraint {

	/**
	 * Test the provided argument against this predicate's condition.
	 *
	 * @param argument the argument value
	 * @return <code>true</code> if the condition was satisfied,
	 * <code>false</code> otherwise
	 */
	boolean test(Object argument);
}
