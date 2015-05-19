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