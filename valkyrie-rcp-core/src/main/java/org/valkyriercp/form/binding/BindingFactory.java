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

import javax.swing.*;
import java.util.Map;

/**
 * A BindingFactory creates bindings for a specific FormModel.
 *
 * @author Oliver Hutchison
 */
public interface BindingFactory {

    /**
     * Returns the form model that contains the properties being bound.
     */
    FormModel getFormModel();

    /**
     * Returns a binding for the provided formPropertyPath.
     */
    Binding createBinding(String formPropertyPath);

    /**
     * Returns a binding for the provided formPropertyPath.
     */
    Binding createBinding(String formPropertyPath, Map context);

    /**
     * Returns a binding to a control of type controlType for the provided formPropertyPath
     */
    Binding createBinding(Class controlType, String formPropertyPath);

    /**
     * Returns a binding to a control of type controlType for the provided formPropertyPath
     */
    Binding createBinding(Class controlType, String formPropertyPath, Map context);

    /**
     * Returns a binding between the provided control and the provided formPropertyPath
     */
    Binding bindControl(JComponent control, String formPropertyPath);

    /**
     * Returns a binding between the provided control and the provided formPropertyPath
     */
    Binding bindControl(JComponent control, String formPropertyPath, Map context);
}
