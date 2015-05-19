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
package org.valkyriercp.form.binding;

import org.valkyriercp.binding.form.FormModel;

/**
 * Strategy interface that encapsulates the selection of a <code>Binder</code>
 * for the provided form model, property name and optionally control type.
 *
 * @author Oliver Hutchison
 */
public interface BinderSelectionStrategy {

    /**
     * Returns a binder for the provided form model and property name.
     * @param formModel the form model which contains the property to be bound
     * @param propertyName the name of the property to be bound
     * @return the <code>Binder</code> (never null). This binder must be capable of
     * generating it's own control to bind to.
     */
    Binder selectBinder(FormModel formModel, String propertyName);

    /**
     * Returns a binder that is capable of binding the provided control type to
     * the provided form model and property name.
     * @param controlType the type of the control to be bound
     * @param formModel the form model which contains the property to be bound
     * @param propertyName the name of the property to be bound
     * @return the <code>Binder</code> (never null). This binder must be capable of
     * binding to a pre-created control that is of the specified <code>controlType</code>.
     */
    Binder selectBinder(Class controlType, FormModel formModel, String propertyName);

    void registerBinderForPropertyName(Class parentObjectType, String propertyName, Binder binder);

    void registerBinderForPropertyType(Class propertyType, Binder binder);

    void registerBinderForControlType(Class controlType, Binder binder);
}
