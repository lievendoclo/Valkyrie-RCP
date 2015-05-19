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
package org.valkyriercp.form.builder;

import com.google.common.collect.Lists;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author oliverh
 */
public class ChainedInterceptorFactory implements FormComponentInterceptorFactory {

    private List<FormComponentInterceptorFactory> interceptorFactories = Lists.newArrayList();

    public ChainedInterceptorFactory() {
    }

    public void setInterceptorFactories(List<FormComponentInterceptorFactory> interceptorFactories) {
        Assert.notNull(interceptorFactories);
        this.interceptorFactories = interceptorFactories;
    }

    public List<FormComponentInterceptorFactory> getInterceptorFactories() {
        return interceptorFactories;
    }

    public FormComponentInterceptor getInterceptor(FormModel formModel) {
        List interceptors = getInterceptors(formModel);
        if (interceptors.size() == 0) {
            return null;
        }
        return new ChainedInterceptor(interceptors);
    }

    private List getInterceptors(FormModel formModel) {
        List interceptors = new ArrayList();
        for (FormComponentInterceptorFactory factory : interceptorFactories) {
            FormComponentInterceptor interceptor = factory
                    .getInterceptor(formModel);
            if (interceptor != null) {
                interceptors.add(interceptor);
            }
        }
        return interceptors;
    }

    private static class ChainedInterceptor implements FormComponentInterceptor {
        private List<FormComponentInterceptor> interceptors;

        public ChainedInterceptor(List<FormComponentInterceptor> interceptors) {
            this.interceptors = interceptors;
        }

        public void processLabel(String propertyName, JComponent label) {
            for (FormComponentInterceptor interceptor : interceptors) {
                interceptor.processLabel(propertyName, label);
            }
        }

        public void processComponent(String propertyName, JComponent component) {
            for (FormComponentInterceptor interceptor : interceptors) {
                interceptor.processComponent(propertyName, component);
            }
        }
    }
}
