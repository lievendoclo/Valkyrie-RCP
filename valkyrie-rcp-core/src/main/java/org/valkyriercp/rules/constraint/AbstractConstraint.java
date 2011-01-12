package org.valkyriercp.rules.constraint;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractConstraint extends ConstraintsAccessor implements Constraint, Serializable {

	public boolean allTrue(Collection collection) {
		return allTrue(collection, this);
	}

	public boolean allTrue(Iterator it) {
		return allTrue(it, this);
	}

	public boolean anyTrue(Collection collection) {
		return anyTrue(collection, this);
	}

	public boolean anyTrue(Iterator it) {
		return anyTrue(it, this);
	}

	public Collection findAll(Collection collection) {
		return findAll(collection, this);
	}

	public Object findAll(Iterator it) {
		return findAll(it, this);
	}

	public Object findFirst(Collection collection) {
		return findFirst(collection, this);
	}

	public Object findFirst(Iterator it) {
		return findFirst(it, this);
	}
}
