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