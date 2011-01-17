package org.valkyriercp.widget.table;
import org.valkyriercp.widget.table.SimpleAccessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Basic implementation of a {@link Writer}.
 * 
 * @author Jan Hoskens
 * @since 0.5.0
 */
public class SimpleWriter extends SimpleAccessor implements Writer
{

    /** Setter method for the property. */
    private Method writeMethod;

    /**
     * Constructor. Uses the return type of the getter to find a matching setter.
     * 
     * @param beanClass
     *            the type of the bean.
     * @param propertyName
     *            name of the property.
     * @see SimpleAccessor#SimpleAccessor(Class, String)
     */
    public SimpleWriter(Class<?> beanClass, String propertyName)
    {
        super(beanClass, propertyName);
        writeMethod = ClassUtils.getWriteMethod(beanClass, propertyName, getPropertyType());
    }

    /**
     * {@inheritDoc}
     */
    public void setValue(Object toEntity, Object newValue) throws IllegalAccessException,
            InvocationTargetException
    {
        writeMethod.invoke(toEntity, new Object[]{newValue});
    }

}