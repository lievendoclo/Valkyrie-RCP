package org.valkyriercp.widget.table;

import java.lang.reflect.InvocationTargetException;

/**
 * Accessor interface: defines a way to access a value and it's type.
 * 
 * @author Jan Hoskens
 * @since 0.5.0
 */
public interface Accessor
{

    /**
     * Get the value from the given target entity.
     * 
     * @param fromEntity
     *            the entity from which the value should be obtained.
     * @return the value.
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    Object getValue(Object fromEntity) throws IllegalAccessException, InvocationTargetException;

    /**
     * Returns the property type of the value that can be obtained through this accessor.
     */
    Class<?> getPropertyType();
}