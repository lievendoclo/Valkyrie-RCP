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
package org.valkyriercp.rules.reporting;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.style.ToStringCreator;
import org.valkyriercp.rules.constraint.*;

import java.util.Stack;

/**
 * @author Keith Donald
 */
public abstract class ValidationResultsBuilder {
    protected static final Log logger = LogFactory
            .getLog(ValidationResultsBuilder.class);
    private Constraint top;
    private Stack levels = new Stack();

    public void pushAnd() {
        And and = new And();
        add(and);
    }

    public void pushOr() {
        Or or = new Or();
        add(or);
    }

    public void pushNot() {
        Not not = new Not();
        add(not);
    }

    private void add(Constraint predicate) {
        if (top != null) {
            if (top instanceof Not) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Negating predicate [" + predicate + "]");
                }
                ((Not)this.top).setConstraint(predicate);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Aggregating nested predicate [" + predicate
                            + "]");
                }
                ((CompoundConstraint)this.top).add(predicate);
            }
        }
        levels.push(predicate);
        this.top = predicate;
        if (logger.isDebugEnabled()) {
            logger.debug("Predicate [" + predicate + "] is at the top.");
        }
    }

    public void push(Constraint constraint) {
        if (this.top instanceof CompoundConstraint) {
            if (logger.isDebugEnabled()) {
                logger.debug("Adding constraint [" + constraint + "]");
            }
            ((CompoundConstraint)this.top).add(constraint);
        } else if (this.top instanceof Not) {
            if (logger.isDebugEnabled()) {
                logger.debug("Negating constraint [" + constraint + "]");
            }
            ((Not)this.top).setConstraint(constraint);
        } else if (this.top == null) {
            constraintViolated(constraint);
        } else {
            throw new IllegalArgumentException(constraint.toString());
        }
    }

    public void pop(boolean result) {
        Constraint p = (Constraint)levels.pop();
        if (logger.isDebugEnabled()) {
            logger.debug("Top [" + p + "] popped; result was " + result
                    + "; stack now has " + levels.size() + " elements");
        }
        if (levels.isEmpty()) {
            if (!result) {
                constraintViolated(top);
            } else {
                constraintSatisfied();
            }
            top = null;
        } else {
            this.top = (Constraint)levels.peek();
            if (result) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Removing compound predicate [" + p
                            + "]; tested true.");
                }
                ((CompoundConstraint)this.top).remove(p);
            }
        }
    }

    protected void clear() {
        levels.clear();
        top = null;
    }

    public boolean negated() {
        if (levels.size() == 0) {
            return false;
        }
        return peek() instanceof Not;
    }

    private Constraint peek() {
        return (Constraint)levels.peek();
    }

    protected abstract void constraintViolated(Constraint constraint);
    protected abstract void constraintSatisfied();

    public String toString() {
        return new ToStringCreator(this).append("topOfStack", top).append(
                "levelsStack", levels).toString();
    }

}
