package org.valkyriercp.rules.constraint.property;

import org.springframework.util.Assert;
import org.valkyriercp.rules.constraint.BinaryConstraint;
import org.valkyriercp.rules.constraint.ConstraintsAccessor;

public abstract class AbstractBinaryConstraint extends ConstraintsAccessor implements BinaryConstraint {
	public boolean test(Object argument1) {
		if (argument1 == null) {
			argument1 = new Object[0];
		}
		Assert.isTrue(argument1.getClass().isArray(), "Binary argument must be an array");
		Object[] arguments = (Object[])argument1;
		Assert.isTrue(arguments.length == 2, "Binary argument must contain 2 elements");
		return test(arguments[0], arguments[1]);
	}
}
