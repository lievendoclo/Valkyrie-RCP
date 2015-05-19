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