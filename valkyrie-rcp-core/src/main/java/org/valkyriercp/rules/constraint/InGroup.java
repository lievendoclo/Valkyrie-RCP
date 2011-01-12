package org.valkyriercp.rules.constraint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A constraint that tests if an argument is one of a group. Similiar to a database's 'in' operator, and more convenient
 * than using a bunch of ORs.
 *
 * @author Keith Donald
 */
public class InGroup implements Constraint {
    private Set group;

    public InGroup(Set group) {
        this.group = new HashSet(group);
    }

    public InGroup(Object[] group) {
        this.group = new HashSet(Arrays.asList(group));
    }

    /**
     * returns an iterator of the group elements
     *
     * @return iterator containing the elements of the group.
     */
    public Iterator iterator() {
        return group.iterator();
    }

    /**
     * Tests the variable argument value is in this group.
     *
     * @see Constraint#test(java.lang.Object)
     */
    public boolean test(Object value) {
        return group.contains(value);
    }

    public String toString() {
        return "inGroup [" + group + "]";
    }
}
