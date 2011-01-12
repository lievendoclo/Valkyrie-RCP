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
