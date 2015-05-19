/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valkyriercp.rules.constraint;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.valkyriercp.rules.closure.support.Algorithms;
import org.valkyriercp.rules.closure.support.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract base class for unary constraints which compose other constraints.
 *
 * @author Keith Donald
 */
public abstract class CompoundConstraint extends AbstractConstraint {

	private List constraints = new ArrayList();

	/**
	 * Constructs a compound constraint with no initial members. It is expected
	 * the client will call "add" to add individual constraints.
	 */
	public CompoundConstraint() {

	}

	/**
	 * Creates a CompoundUnaryPredicate composed of two constraints.
	 *
	 * @param constraint1
	 *            the first constraint
	 * @param constraint2
	 *            the second constraint
	 */
	public CompoundConstraint(Constraint constraint1, Constraint constraint2) {
		Assert.isTrue(constraint1 != null && constraint2 != null, "Both constraints are required");
		constraints.add(constraint1);
		constraints.add(constraint2);
	}

	/**
	 * Creates a CompoundUnaryPredicate composed of the specified constraints.
	 *
	 * @param constraints
	 *            the aggregated constraints
	 */
	public CompoundConstraint(Constraint[] constraints) {
		this.constraints.addAll(Arrays.asList(constraints));
	}

	/**
	 * Add the specified constraint to the set of constraints aggregated by this
	 * compound constraint.
	 *
	 * @param constraint
	 *            the constraint to add
	 * @return A reference to this, to support chaining.
	 */
	public CompoundConstraint add(Constraint constraint) {
		this.constraints.add(constraint);
		return this;
	}

	/**
	 * Add the list of constraints to the set of constraints aggregated by this
	 * compound constraint.
	 *
	 * @param constraints
	 *            the list of constraints to add
	 * @return A reference to this, to support chaining.
	 */
	public CompoundConstraint addAll(List constraints) {
		Algorithms.instance().forEach(constraints, new Block() {
			protected void handle(Object o) {
				add((Constraint)o);
			}
		});
		return this;
	}

	public void remove(Constraint constraint) {
		constraints.remove(constraint);
	}

	public int indexOf(Constraint child) {
		return constraints.indexOf(child);
	}

	public Constraint get(int index) {
		return (Constraint)constraints.get(index);
	}

	public void copyInto(CompoundConstraint p) {
		p.constraints.clear();
		p.constraints.addAll(constraints);
	}

	public void set(int index, Constraint constraint) {
		constraints.set(index, constraint);
	}

	/**
	 * Return an iterator over the aggregated constraints.
	 *
	 * @return An iterator
	 */
	public Iterator iterator() {
		return constraints.iterator();
	}

	/**
	 * Returns the number of constraints aggregated by this compound constraint.
	 *
	 * @return The size.
	 */
	public int size() {
		return constraints.size();
	}

	public abstract boolean test(Object argument);

	public void validateTypeSafety(final Class constraintType) {
		Assert.notNull(constraintType, "Constraint type is required");
		Assert.isTrue(Constraint.class.isAssignableFrom(constraintType),
				"Argument must be a specialization of the Constraint interface");
		boolean result = new AbstractConstraint() {
			public boolean test(Object o) {
				return constraintType.isAssignableFrom(o.getClass());
			}
		}.allTrue(iterator());
		Assert.isTrue(result, "One or more of the aggregated constraints is not assignable to " + constraintType);
	}

	public String toString() {
		return new ToStringCreator(this).append("constraints", constraints).toString();
	}

}