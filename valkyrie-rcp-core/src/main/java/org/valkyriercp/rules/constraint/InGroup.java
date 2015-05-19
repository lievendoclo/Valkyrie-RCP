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
