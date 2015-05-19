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
package org.valkyriercp.binding.value.support;

import org.valkyriercp.binding.MutablePropertyAccessStrategy;
import org.valkyriercp.binding.support.BeanPropertyAccessStrategy;
import org.valkyriercp.binding.value.ValueModel;

/**
 * Adapts a value model to JavaBean's property.
 *
 * @author Oliver Hutchison
 */
public class PropertyAdapter extends AbstractValueModelAdapter {

    private final ValueModel propertValueModel;

    public PropertyAdapter(ValueModel valueModel, Object bean, String property) {
        this(valueModel, new BeanPropertyAccessStrategy(bean), property);
    }

    public PropertyAdapter(ValueModel valueModel, MutablePropertyAccessStrategy propertyAccessStrategy, String property) {
        super(valueModel);
        this.propertValueModel = propertyAccessStrategy.getPropertyValueModel(property);
        initalizeAdaptedValue();
    }

    protected void valueModelValueChanged(Object newValue) {
        propertValueModel.setValue(newValue);
    }
}
