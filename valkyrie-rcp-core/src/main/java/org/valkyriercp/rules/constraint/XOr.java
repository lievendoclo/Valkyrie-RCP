package org.valkyriercp.rules.constraint;

import java.util.Iterator;

/**
 * A "xor" compound constraint (aka exclusive disjunction).
 *
 * @author Mathias Broekelmann
 *
 */
public class XOr extends CompoundConstraint {

  /**
   * Creates a empty UnaryOr disjunction.
   */
  public XOr() {
    super();
  }

  /**
   * "Ors" two constraints.
   *
   * @param constraint1
   *          The first constraint.
   * @param constraint2
   *          The second constraint.
   */
  public XOr(Constraint constraint1, Constraint constraint2) {
    super(constraint1, constraint2);
  }

  /**
   * "Ors" the specified constraints.
   *
   * @param constraints
   *          The constraints
   */
  public XOr(Constraint[] constraints) {
    super(constraints);
  }

  /**
   * Tests if any of the constraints aggregated by this compound constraint test
   * <code>true</code>.
   *
   * @see Constraint#test(java.lang.Object)
   */
  public boolean test(Object value) {
    boolean found = false;
    for (Iterator i = iterator(); i.hasNext();) {
      if (((Constraint) i.next()).test(value)) {
        if (found) {
          return false;
        }
        found = true;
      }
    }
    return found;
  }

}

