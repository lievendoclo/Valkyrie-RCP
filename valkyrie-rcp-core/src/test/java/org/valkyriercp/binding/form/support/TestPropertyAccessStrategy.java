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

import org.springframework.beans.BeansException;
import org.valkyriercp.binding.support.BeanPropertyAccessStrategy;
import org.valkyriercp.binding.value.ValueModel;

public class TestPropertyAccessStrategy extends BeanPropertyAccessStrategy {

    private int numValueModelRequests;
    private String lastRequestedValueModel;

    public TestPropertyAccessStrategy(Object bean) {
        super(bean);
    }

    public ValueModel getPropertyValueModel(String propertyPath) throws BeansException {
        numValueModelRequests++;
        lastRequestedValueModel = propertyPath;
        return super.getPropertyValueModel(propertyPath);
    }

    public int numValueModelRequests() {
        return numValueModelRequests;
    }

    public String lastRequestedValueModel() {
        return lastRequestedValueModel;
    }
}
