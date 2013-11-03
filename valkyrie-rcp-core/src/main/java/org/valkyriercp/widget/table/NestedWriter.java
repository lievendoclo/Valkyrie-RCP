package org.valkyriercp.widget.table;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This {@link Writer} uses a chaining implementation of getter methods to allow nested properties.
 * 
 * @author Jan Hoskens
 * @since 0.5.0
 */
public class NestedWriter implements Writer
{

    /** The nested writer to access and write the property. */
    private final Writer nestedWriter;

    /** The getter to access the first level object. */
    private final Method getter;

    /**
     * Convenience constructor. Creates a getter method for the given class and property and reroutes to
     * {@link NestedWriter#NestedWriter(Method, String)}.
     * 
     * @param clazz
     *            type with the nested property.
     * @param propertyName
     *            the first part of the nested property.
     * @param nestedPropertyName
     *            the nested property, possibly containing more nesting levels. Only the last one in the line
     *            needs the setter method.
     * @see #NestedWriter(Method, String)
     */
    public NestedWriter(Class<?> clazz, String propertyName, String nestedPropertyName)
    {
        this(ClassUtils.getReadMethod(clazz, propertyName), nestedPropertyName);
    }

    /**
     * Constructor. The given getter should yield the return value on which the nested property exists.
     * 
     * @param getter
     *            the method providing the entity.
     * @param nestedPropertyName
     *            the nested property on the entity.
     */
    public NestedWriter(Method getter, String nestedPropertyName)
    {
        this.getter = getter;
        this.nestedWriter = ClassUtils.getWriterForProperty(getter.getReturnType(), nestedPropertyName);
    }

    /**
     * Set the value on the source entity. If at any point the chaining results in a null value. The chaining
     * should end.
     * 
     * @param toEntity
     *            the entity on which the getter should operate.
     * @param newValue
     *            the value to set.
     */
    public void setValue(Object toEntity, Object newValue) throws IllegalAccessException,
            InvocationTargetException
    {
        Object propertyValue = getter.invoke(toEntity);
        if (propertyValue != null)
            nestedWriter.setValue(propertyValue, newValue);
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> getPropertyType()
    {
        return nestedWriter.getPropertyType();
    }

    /**
     * Get the value from the source entity. If at any point the chaining results in a null value. The
     * chaining should end and return <code>null</code>.
     * 
     * @param fromEntity
     *            the entity on which the getter should operate.
     * @return <code>null</code> if at any point in the chaining a property returned <code>null</code> or
     *         the value of the nested property.
     */
    public Object getValue(Object fromEntity) throws IllegalAccessException, InvocationTargetException
    {
        Object propertyValue = getter.invoke(fromEntity);
        return propertyValue == null ? null : nestedWriter.getValue(propertyValue);
    }
}