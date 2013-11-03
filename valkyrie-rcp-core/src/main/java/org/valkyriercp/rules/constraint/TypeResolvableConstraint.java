package org.valkyriercp.rules.constraint;

import org.valkyriercp.rules.reporting.TypeResolvableSupport;

/**
 * Convenient abstract super class for predicates whose type's are resolvable,
 * useful for mapping the type to a i18n message in a message source.
 *
 * @author Keith Donald
 */
public abstract class TypeResolvableConstraint extends
        TypeResolvableSupport implements Constraint {

	public TypeResolvableConstraint() {
		super();
	}

	public TypeResolvableConstraint(String type) {
		super(type);
	}
}
