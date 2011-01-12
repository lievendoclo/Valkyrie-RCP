package org.valkyriercp.rules.closure.support;

import org.valkyriercp.rules.closure.Closure;

/**
 * Returns the Integer length of an object's string form, or zero if the object
 * is null.
 *
 * @author Keith Donald
 */
public class StringLength extends AbstractClosure {
    private static final StringLength INSTANCE = new StringLength();

    /**
     * Returns the shared StringLength instance--this is possible as the default
     * instance is immutable and stateless.
     *
     * @return the shared instance
     */
    public static Closure instance() {
        return INSTANCE;
    }

    /**
     * Evaluate the string form of the object returning an Integer representing
     * the string length.
     *
     * @return The string length Integer.
     * @see Closure#call(java.lang.Object)
     */
    public Object call(Object value) {
        if (value == null) { return new Integer(0); }
        return new Integer(String.valueOf(value).length());
    }

    public String toString() {
        return "strLength(arg)";
    }

}