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

import org.valkyriercp.rules.reporting.TypeResolvableSupport;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A constraint based on a regular expression pattern.
 *
 * @see org.valkyriercp.rules.reporting.TypeResolvable
 * @see java.util.regex.Pattern
 *
 * @author Keith Donald
 */
public class RegexpConstraint extends TypeResolvableSupport implements Constraint {

	private Pattern pattern;

	/**
	 * Creates a RegexpConstraint with the provided regular expression pattern
	 * string.
	 *
	 * @param regex The regular expression
	 */
	public RegexpConstraint(String regex) {
		this(regex, null);
	}

	/**
	 * Creates a RegexpConstraint with the provided regular expression pattern
	 * string and sets the type of the constraint to provide specific messages.
	 *
	 * @param regex the regular expression.
	 * @param type	id used to fetch the message.
	 */
	public RegexpConstraint(String regex, String type) {
		super(type);
		pattern = Pattern.compile(regex);
	}

	/**
	 * Test if the argument matches the pattern.
	 */
	public boolean test(Object argument) {
		if (argument == null) {
			argument = "";
		}
		Matcher m = pattern.matcher((CharSequence) argument);
		return m.matches();
	}

	public String toString() {
		return getDefaultMessage() + " " + pattern.pattern();
	}
}
