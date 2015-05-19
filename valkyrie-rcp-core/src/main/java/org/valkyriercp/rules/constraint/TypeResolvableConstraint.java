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

/**
 * Convenient abstract super class for predicates whose type's are resolvable,
 * useful for mapping the type to a i18n message in a message source.
 *
 * @author Keith Donald
 */
public abstract class TypeResolvableConstraint extends
        TypeResolvableSupport implements Constraint {

	public TypeResolvableConstraint() {
		super();
	}

	public TypeResolvableConstraint(String type) {
		super(type);
	}
}
