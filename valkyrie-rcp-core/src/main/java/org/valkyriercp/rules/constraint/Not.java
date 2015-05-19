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

import org.springframework.util.Assert;

/**
 * "Nots" another unary constraint (the inverse) by using composition.
 *
 * @author Keith Donald
 */
public class Not implements Constraint {
    private Constraint constraint;

    /**
     * Creates a UnaryNot in temporary invalid state - please use only
     * if you have to, the well-formed constructor is much preferred.
     */
    public Not() {

    }

    /**
     * Creates a UnaryNot
     *
     * @param constraint
     *            The constraint to negate.
     */
    public Not(Constraint constraint) {
    	setConstraint(constraint);
    }

    public void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

    /**
     * Negates the boolean result returned by testing the wrapped constraint.
     *
     * @see Constraint#test(java.lang.Object)
     */
    public boolean test(Object value) {
    	Assert.state(constraint != null, "The constraint is not set");
        return !constraint.test(value);
    }

    public String toString() {
        return "not(" + getConstraint() + ")";
    }

    public Constraint getConstraint() {
        return constraint;
    }

}
