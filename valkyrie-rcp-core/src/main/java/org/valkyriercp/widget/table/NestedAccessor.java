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
package org.valkyriercp.widget.table;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This {@link Accessor} uses a chaining implementation of getter methods to allow nested properties.
 * 
 * @author Jan Hoskens
 * @since 0.5.0
 */
public class NestedAccessor implements Accessor
{

    /** Lazily created accessor to access the nested property on the top level property object. */
    private Accessor wrappedAccessor;

    /**
     * The nested property. Will be used to create an accessor together with the return type of the top level
     * property object.
     */
    final private String nestedProperty;

    /** Getter method to access the top level property object. */
    final private Method getter;

    /**
     * Convenience constructor. Creates a getter method for the given class and property and reroutes to
     * {@link NestedAccessor#NestedAccessor(Method, String)}.
     * 
     * @param clazz
     *            type with the nested property.
     * @param propertyName
     *            the first part of the nested property.
     * @param nestedPropertyName
     *            the nested property, possibly containing more nesting levels.
     * @see #NestedAccessor(Method, String)
     */
    public NestedAccessor(final Class<?> clazz, final String propertyName, final String nestedPropertyName)
    {
        this(ClassUtils.getReadMethod(clazz, propertyName), nestedPropertyName);
    }

    /**
     * Constructor. The getter will deliver the first level of the nesting. It's return value will be used to
     * construct the next accessor for the nestedProperty.
     * 
     * @param getter
     *            method delivering the first part of the nesting.
     * @param nestedProperty
     *            property that should be available on the return object of the getter. Possibly nested as
     *            well.
     * @see #NestedAccessor(Class, String, String)
     */
    public NestedAccessor(final Method getter, final String nestedProperty)
    {
        this.nestedProperty = nestedProperty;
        this.getter = getter;
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
        return propertyValue == null ? null : getWrappedAccessor(propertyValue.getClass()).getValue(
                propertyValue);
    }

    /**
     * <p>
     * Get the wrapped accessor, instantiate it lazily if needed.
     * </p>
     * 
     * <p>
     * Normally the return type of the getter method delivers the correct type on which the nested property
     * can be found. There is however a specific case in which this isn't true. It may be that a specific type
     * is only known at runtime and that you need to access a property of that specific type. The specific
     * type can be found at runtime when the wrapped accessor is constructed. But it does imply that all other
     * access will result in the same type.
     * </p>
     * 
     * <p>
     * A specific type implementation is found in PeriodicValueAdapter->BTWPercentage, here a property of
     * BTWPercentage can be accessed through the adapter, but all other adapters should contain a
     * BTWPercentage.
     * </p>
     * 
     * @param propertyType
     *            property type to use if getter doesn't yield the correct one.
     * @return an {@link Accessor} for the wrapped property.
     */
    private Accessor getWrappedAccessor(Class<?> propertyType)
    {
        if (wrappedAccessor == null)
        {
            try
            {
                wrappedAccessor = ClassUtils.getAccessorForProperty(getter.getReturnType(), nestedProperty);
            }
            catch (NoSuchMethodError nsme)
            {
                if (propertyType == null)
                    throw nsme;
                wrappedAccessor = ClassUtils.getAccessorForProperty(propertyType, nestedProperty);
            }
        }

        return wrappedAccessor;
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> getPropertyType()
    {
        return getWrappedAccessor(null).getPropertyType();
    }
}