package org.valkyriercp.binding.value.support;

import org.valkyriercp.binding.value.ValueChangeDetector;

/**
 * An implementation of ValueChangeDetector that provides the same semantics as
 * {@link org.springframework.util.ObjectUtils#nullSafeEquals(java.lang.Object, java.lang.Object)}.
 * If the objects are not the same object, they are compared using the equals method of
 * the first object. Nulls are handled safely.
 *
 * @author Larry Streepy
 *
 */
public class EqualsValueChangeDetector implements ValueChangeDetector {

    /**
     * Determines if there has been a change in value between the provided arguments. The
     * objects are compared using the <code>equals</code> method.
     *
     * @param oldValue Original object value
     * @param newValue New object value
     * @return true if the objects are different enough to indicate a change in the value
     *         model
     */
    public boolean hasValueChanged(Object oldValue, Object newValue) {
        return !(oldValue == newValue || (oldValue != null && oldValue.equals( newValue )));
    }
}
