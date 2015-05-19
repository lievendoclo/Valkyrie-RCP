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

import org.springframework.util.Assert;
import org.valkyriercp.binding.value.ValueModel;

import java.lang.reflect.Method;

/**
 * A value model that derives it's value from the result of invoking a method.
 * The parameters for the method invocation are generated from a list of
 * "source" value models. Should any of the "source" values change the method
 * will be invoked and if the return value has changed the value held by
 * this class will be updated.
 *
 * @author  Oliver Hutchison
 */
public final class MethodInvokingDerivedValueModel extends AbstractDerivedValueModel {

    private final Object target;

    private final Method method;

    private Object value;

    public MethodInvokingDerivedValueModel(Object target, String methodName, ValueModel[] paramSourceValueModels) {
        super(paramSourceValueModels);
        this.target = target;
        this.method = getPropertyMethod(target, methodName, paramSourceValueModels.length);
        Assert.notNull(method, "No method with name [" + methodName + "] and " + paramSourceValueModels.length
                + " parameters found on class.");
        sourceValuesChanged();
    }

    protected void sourceValuesChanged() {
        Object oldValue = value;
        try {
            value = method.invoke(target, getSourceValues());
            fireValueChange(oldValue, value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getValue() {
        return value;
    }

    private Method getPropertyMethod(Object target, String methodName, int numberOfParams) {
        Method propertyMethod = null;
        Method[] methods = target.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getName().equals(methodName) && method.getParameterTypes().length == numberOfParams) {
                if (propertyMethod != null) {
                    throw new UnsupportedOperationException("Found than one method with name '" + methodName + "' and "
                            + numberOfParams + " parameters on class '" + target.getClass().getName() + "'.");
                }
                propertyMethod = method;
            }
        }
        return propertyMethod;
    }
}
