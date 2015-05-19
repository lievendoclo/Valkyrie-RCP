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
package org.valkyriercp.rules.constraint.property;

import org.springframework.util.Assert;
import org.valkyriercp.rules.constraint.BinaryConstraint;
import org.valkyriercp.rules.constraint.ConstraintsAccessor;

public abstract class AbstractBinaryConstraint extends ConstraintsAccessor implements BinaryConstraint {
	public boolean test(Object argument1) {
		if (argument1 == null) {
			argument1 = new Object[0];
		}
		Assert.isTrue(argument1.getClass().isArray(), "Binary argument must be an array");
		Object[] arguments = (Object[])argument1;
		Assert.isTrue(arguments.length == 2, "Binary argument must contain 2 elements");
		return test(arguments[0], arguments[1]);
	}
}
