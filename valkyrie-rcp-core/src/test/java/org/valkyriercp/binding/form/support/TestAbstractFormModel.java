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
package org.valkyriercp.binding.form.support;

import org.valkyriercp.binding.MutablePropertyAccessStrategy;
import org.valkyriercp.binding.value.ValueModel;

public class TestAbstractFormModel extends AbstractFormModel {

    int preProcessCalls;

    int postProcessCalls;

    public TestAbstractFormModel(Object formObject) {
        super(formObject);
    }

    public TestAbstractFormModel(MutablePropertyAccessStrategy propertyAccessStrategy, boolean buffered) {
        super(propertyAccessStrategy, buffered);
    }

    public TestAbstractFormModel(ValueModel formObjectHolder, boolean buffering) {
        super(formObjectHolder, buffering);
    }

    protected ValueModel preProcessNewValueModel(String domainObjectProperty, ValueModel formValueModel) {
        ++preProcessCalls;
        return formValueModel;
    }

    protected void postProcessNewValueModel(String domainObjectProperty, ValueModel valueModel) {
        ++postProcessCalls;
    }

    protected ValueModel preProcessNewConvertingValueModel(String formProperty, Class targetClass,
            ValueModel formValueModel) {
        ++preProcessCalls;
        return formValueModel;
    }

    protected void postProcessNewConvertingValueModel(String formProperty, Class targetClass, ValueModel valueModel) {
        ++postProcessCalls;
    }
}