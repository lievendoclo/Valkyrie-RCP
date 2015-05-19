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

import java.util.Collection;
import java.util.Iterator;

/**
 * Simple process template that iterates over elements.
 *
 * @author Keith Donald
 */
public class IteratorTemplate extends AbstractElementGeneratorWorkflow {

	/** Collection of objects to iterate over. */
	private Collection collection;

	/** Iterator on the collection. */
	private Iterator it;

	/**
	 * Constructor.
	 *
	 * @param collection the elements to iterate over.
	 */
	public IteratorTemplate(Collection collection) {
		this.collection = collection;
	}

	/**
	 * Constructor. When passing an Iterator, the Template will be a run-once
	 * instance.
	 *
	 * @param it Iterator over the elements.
	 */
	public IteratorTemplate(Iterator it) {
		super(true);
		this.it = it;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doSetup() {
		if (this.collection != null) {
			this.it = this.collection.iterator();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean hasMoreWork() {
		return it.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object doWork() {
		return it.next();
	}
}
