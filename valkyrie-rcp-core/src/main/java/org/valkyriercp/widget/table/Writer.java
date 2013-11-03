package org.valkyriercp.widget.table;

import java.lang.reflect.InvocationTargetException;

/**
 * Writer interface: extends the {@link Accessor} to allow write-access.
 * 
 * @author Jan Hoskens
 * @since 0.5.0
 */
public interface Writer extends Accessor
{

    /**
     * Set the value on a specific target entity.
     * 
     * @param toEntity
     *            the entity on which the property has to be changed.
     * @param newValue
     *            the new value of the property.
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    void setValue(Object toEntity, Object newValue) throws IllegalAccessException, InvocationTargetException;
}