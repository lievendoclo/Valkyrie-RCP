package org.valkyriercp.rules.closure.support;

import org.valkyriercp.rules.closure.Closure;

/**
 * Returns a trimmed copy of the string form of an object.
 *
 * @author Keith Donald
 */
public class StringTrimmer extends AbstractClosure {
	private static final StringTrimmer INSTANCE = new StringTrimmer();

	/**
	 * Returns the shared StringTrimmer instance--this is possible as the
	 * default instance is immutable and stateless.
	 *
	 * @return the shared instance
	 */
	public static Closure instance() {
		return INSTANCE;
	}

	/**
	 * Evaluate the string form of the object, returning a trimmed (no
	 * leading/trailing whitespace) copy of the string.
	 *
	 * @return The trimmed string
	 * @see Closure#call(java.lang.Object)
	 */
	public Object call(Object argument) {
		return String.valueOf(argument).trim();
	}

	public String toString() {
		return "trim(arg)";
	}

}