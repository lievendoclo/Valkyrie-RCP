package org.valkyriercp.rules.constraint;

import org.springframework.util.Assert;
import org.valkyriercp.rules.reporting.TypeResolvable;

public class IfTrue extends AbstractConstraint implements TypeResolvable {

	private Constraint constraint;

	private Constraint mustBeTrueConstraint;

	private Constraint elseTrueConstraint;

	private String type;

	public IfTrue(Constraint constraint, Constraint mustAlsoBeTrue) {
		this(constraint, mustAlsoBeTrue, (Constraint)null);
	}

	public IfTrue(Constraint constraint, Constraint mustAlsoBeTrue, String type) {
		this(constraint, mustAlsoBeTrue, null, type);
	}

	public IfTrue(Constraint constraint, Constraint mustAlsoBeTrue, Constraint elseMustAlsoBeTrue) {
		this(constraint, mustAlsoBeTrue, elseMustAlsoBeTrue, "ifTrue");
	}

	public IfTrue(Constraint constraint, Constraint mustAlsoBeTrue, Constraint elseMustAlsoBeTrue, String type) {
		Assert.notNull(constraint, "The constraint that may be true is required");
		Assert.notNull(mustAlsoBeTrue, "The constraint that must be true IF the first constraint is true is required");
		this.constraint = constraint;
		this.mustBeTrueConstraint = mustAlsoBeTrue;
		this.elseTrueConstraint = elseMustAlsoBeTrue;
		this.type = type;
	}

	public boolean test(Object argument) {
		if (constraint.test(argument))
			return mustBeTrueConstraint.test(argument);

		if (elseTrueConstraint != null)
			return elseTrueConstraint.test(argument);

        return true;
	}

	public String getType() {
		return type;
	}
}
