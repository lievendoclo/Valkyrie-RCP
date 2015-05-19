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

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Convenience implementation for the closure interface. Extends
 * AlgorithmsAccessorSupport for convenient execution of various data structure
 * processing algorithms taking advantage of closures and constraints.
 *
 * @author Keith Donald
 */
public abstract class AbstractClosure extends AlgorithmsAccessor implements Closure, Serializable {

	/**
	 * Execute this closure for each element in the provided collection.
	 *
	 * @param collection The collection
	 */
	public final void forEach(Collection collection) {
		forEach(collection, this);
	}

	/**
	 * Execute this closure for each element traversable via the provided
	 * iterator.
	 *
	 * @param it The iterator
	 */
	public final void forEach(Iterator it) {
		forEach(it, this);
	}
}
