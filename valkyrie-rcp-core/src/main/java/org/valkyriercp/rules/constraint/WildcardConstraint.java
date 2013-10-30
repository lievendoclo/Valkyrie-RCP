package org.valkyriercp.rules.constraint;

/**
 * Always returns true; a wildcard match
 *
 * @author Keith Donald
 */
public class WildcardConstraint extends AbstractConstraint {
	public boolean test(Object argument) {
		return true;
	}
}
