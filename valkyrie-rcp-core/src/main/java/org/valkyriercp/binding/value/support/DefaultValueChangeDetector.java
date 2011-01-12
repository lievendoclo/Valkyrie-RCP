package org.valkyriercp.binding.value.support;

import org.valkyriercp.binding.value.ValueChangeDetector;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Implementation of ValueChangeDetector that maintains a set of classes for which it is
 * "safe" to use <code>equals()</code> for detecting a change in value (those classes
 * that have immutable values). For all objects of a type in this set, the value
 * comparison will be done using equals. For all other types, object equivalence (the ==
 * operator) will be used.
 * <p>
 * This is the default value change detector handed out by the Application Services.
 *
 * @author Larry Streepy
 *
 */
public class DefaultValueChangeDetector implements ValueChangeDetector {

    /*
     * All the classes that are known to have a safe implementation of equals.
     */
    protected final Set classesWithSafeEquals = new HashSet( Arrays.asList(new Class[]{Boolean.class, Byte.class,
            Short.class, Integer.class, Long.class, Float.class, Double.class, String.class, Character.class,
            BigDecimal.class, BigInteger.class, Date.class, Calendar.class}) );

    /**
     * Determines if there has been a change in value between the provided arguments. As
     * many objects do not implement #equals in a manner that is strict enough for the
     * requirements of this class, difference is determined using <code>!=</code>,
     * however, to improve accuracy #equals will be used when this is definitely safe e.g.
     * for Strings, Booleans, Numbers, Dates.
     *
     * @param oldValue Original object value
     * @param newValue New object value
     * @return true if the objects are different enough to indicate a change in the value
     *         model
     */
    public boolean hasValueChanged(Object oldValue, Object newValue) {
        if( oldValue != null && classesWithSafeEquals.contains( oldValue.getClass() ) )
            return !oldValue.equals( newValue );

        return oldValue != newValue;
    }

    /**
     * Specify the set of classes that have an equals method that is safe to use for
     * determining a value change.
     *
     * @param classes with safe equals methods
     */
    public void setClassesWithSafeEquals(Collection classes) {
        classesWithSafeEquals.clear();
        classesWithSafeEquals.addAll( classes );
    }

    /**
     * Get the set of classes that have an equals method that is safe to use for
     * determining a value change.
     * @return Collection of classes with safe equals methods
     */
    public Collection getClassesWithSafeEquals() {
        return classesWithSafeEquals;
    }
}

