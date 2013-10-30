package org.valkyriercp.widget.table;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Basic implementation of an {@link Accessor}.
 * 
 * @author Jan Hoskens
 * @since 0.5.0
 */
public class SimpleAccessor implements Accessor
{

    /** The getter method. */
    private final Method accessor;

    /**
     * Constructor. Retrieves the getter of the given property.
     * 
     * @param clazz
     *            the type of the entity.
     * @param propertyName
     *            name of the property.
     */
    public SimpleAccessor(Class<?> clazz, String propertyName)
    {
        this.accessor = ClassUtils.getReadMethod(clazz, propertyName);
        if (accessor == null)
            throw new IllegalArgumentException("propertyName " + propertyName
                    + " does not represent a readable property.");
    }

    /**
     * {@inheritDoc}
     */
    public Object getValue(Object fromEntity) throws IllegalAccessException, InvocationTargetException
    {
        return accessor.invoke(fromEntity);
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> getPropertyType()
    {
        return ClassUtils.getTypeForProperty(accessor);
    }

}