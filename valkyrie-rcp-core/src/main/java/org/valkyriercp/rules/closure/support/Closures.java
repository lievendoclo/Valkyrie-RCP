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

import org.springframework.util.Assert;
import org.valkyriercp.rules.closure.Closure;
import org.valkyriercp.rules.constraint.Constraint;

/**
 * A factory for easing the construction and composition of closure (blocks of
 * executable code).
 *
 * @author Keith Donald
 */
public class Closures extends AlgorithmsAccessor {

	private static Closures INSTANCE = new Closures();

	public Closures() {
	}

	public static Closures instance() {
		return INSTANCE;
	}

	public static void load(Closures sharedInstance) {
		Assert.notNull(sharedInstance, "The global closures factory cannot be null");
		INSTANCE = sharedInstance;
	}

	public Closure chain(Closure firstFunction, Closure secondFunction) {
		return new ClosureChain(firstFunction, secondFunction);
	}

	public Closure chain(Closure[] functionsToChain) {
		return new ClosureChain(functionsToChain);
	}

	public Closure ifTrue(Constraint constraint, Closure closure) {
		return new IfBlock(constraint, closure);
	}

}