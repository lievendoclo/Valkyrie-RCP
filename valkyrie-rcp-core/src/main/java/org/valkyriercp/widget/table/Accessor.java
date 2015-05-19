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