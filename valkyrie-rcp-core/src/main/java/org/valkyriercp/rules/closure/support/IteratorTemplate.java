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
