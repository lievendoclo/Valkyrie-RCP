package org.valkyriercp.rules.closure.support;

import org.valkyriercp.rules.closure.Closure;
import org.valkyriercp.rules.constraint.Constraint;

import java.util.Collection;
import java.util.Iterator;

/**
 * Algorithms accessor support class, for convenient extending by subclasses.
 *
 * @author Keith Donald
 */
public abstract class AlgorithmsAccessor {
	/**
	 * @return Algorithms instance.
	 */
	protected Algorithms getAlgorithms() {
		return Algorithms.instance();
	}

	/**
	 * @see Algorithms#findFirst(java.util.Collection, Constraint)
	 */
	public Object findFirst(Collection collection, Constraint constraint) {
		return getAlgorithms().findFirst(collection, constraint);
	}

	/**
	 * @see Algorithms#findFirst(java.util.Iterator, Constraint)
	 */
	public Object findFirst(Iterator it, Constraint constraint) {
		return getAlgorithms().findFirst(it, constraint);
	}

	/**
	 * @see Algorithms#findAll(Collection, org.springframework.rules.constraint.Constraint)
	 */
	public Collection findAll(Collection collection, Constraint constraint) {
		return getAlgorithms().findAll(collection, constraint);
	}

	/**
	 * @see Algorithms#findAll(Iterator, org.springframework.rules.constraint.Constraint)
	 */
	public Collection findAll(Iterator it, Constraint constraint) {
		return getAlgorithms().findAll(it, constraint);
	}

	/**
	 * @see Algorithms#allTrue(Collection, Constraint)
	 */
	public boolean allTrue(Collection collection, Constraint constraint) {
		return getAlgorithms().allTrue(collection, constraint);
	}

	/**
	 * @see Algorithms#allTrue(Iterator, Constraint)
	 */
	public boolean allTrue(Iterator it, Constraint constraint) {
		return getAlgorithms().allTrue(it, constraint);
	}

	/**
	 * @see Algorithms#anyTrue(Collection, org.springframework.rules.constraint.Constraint)
	 */
	public boolean anyTrue(Collection collection, Constraint constraint) {
		return getAlgorithms().anyTrue(collection, constraint);
	}

	/**
	 * @see Algorithms#anyTrue(Iterator, Constraint)
	 */
	public boolean anyTrue(Iterator it, Constraint constraint) {
		return getAlgorithms().anyTrue(it, constraint);
	}

	/**
	 * @see Algorithms#forEach(Collection, org.springframework.rules.closure.Closure)
	 */
	public void forEach(Collection collection, Closure closure) {
		getAlgorithms().forEach(collection, closure);
	}

	/**
	 * @see Algorithms#forEach(Iterator, org.springframework.rules.closure.Closure)
	 */
	public void forEach(Iterator it, Closure closure) {
		getAlgorithms().forEach(it, closure);
	}

}