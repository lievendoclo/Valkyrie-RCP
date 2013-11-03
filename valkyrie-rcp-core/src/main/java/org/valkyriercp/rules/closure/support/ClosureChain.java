package org.valkyriercp.rules.closure.support;

import org.springframework.core.style.ToStringCreator;
import org.valkyriercp.rules.closure.Closure;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A chain of closures that evaluate their results in a ordered sequence.
 * <p>
 * For example, declaring
 * <code>new ClosureChain() { f1, f2, f3 }.call(fooArg)</code> will trigger
 * the evaluation of <code>f1</code> first, it's result will be passed to
 * <code>f2</code> for evaluation, and f2's result will be passed to
 * <code>f3</code> for evaluation. The final f3 result will be returned to the
 * caller.
 * </p>
 *
 * @author Keith Donald
 */
public class ClosureChain implements Closure {

	/** Holds the sequence of closures. */
	private Set closures = new LinkedHashSet();

	/**
	 * Constructs a function chain with no initial members. It is expected the
	 * client will call "add" to add individual predicates.
	 */
	public ClosureChain() {

	}

	/**
	 * Creates a chain composed of two functions.
	 *
	 * @param function1 the first function
	 * @param function2 the second function
	 */
	public ClosureChain(Closure function1, Closure function2) {
		closures.add(function1);
		closures.add(function2);
	}

	/**
	 * Creates a chain composed of the ordered array of functions.
	 *
	 * @param functions the aggregated functions
	 */
	public ClosureChain(Closure[] functions) {
		this.closures.addAll(Arrays.asList(functions));
	}

	/**
	 * Add the specified function to the set of functions aggregated by this
	 * function chain.
	 *
	 * @param function the function to add
	 * @return A reference to this, to support easy chaining.
	 */
	public ClosureChain add(Closure function) {
		this.closures.add(function);
		return this;
	}

	/**
	 * Return an iterator over the aggregated predicates.
	 *
	 * @return An iterator
	 */
	public Iterator iterator() {
		return closures.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object call(Object argument) {
		Object result = argument;
		Iterator it = iterator();
		while (it.hasNext()) {
			Closure f = (Closure) it.next();
			result = f.call(result);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringCreator(this).append("closureChain", closures).toString();
	}
}