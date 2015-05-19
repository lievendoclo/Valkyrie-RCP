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
package org.valkyriercp.application.docking;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.util.ObjectUtils;

import java.util.Map;

/**
 * A bean post processor that automatically recognizes view descriptors and make them VLDocking aware dependending on
 * view characteristics.
 *
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Arg??ello (JAF)</a>
 */
public class VLDockingBeanPostProcessor implements BeanPostProcessor {
    /**
     * The mapping between page components types and view descriptor templates.
     * <p/>
     * The key is the page component type and the value the name of the prototype bean with the VLDocking view
     * descriptor template.
     * <p/>
     * Key is intencionally defined as <code>String</code> in order to make it easy defining new view types.
     */
    private Map<String, String> viewDescriptorsTemplates;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

        // throws BeansException {

        return bean;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Replaces those view descriptors not implementing {@link VLDockingViewDescriptor}.
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        // throws BeansException {

        if (bean instanceof VLDockingViewDescriptor) {
            return bean;
        } else if (bean instanceof ViewDescriptor) {
            final ViewDescriptor sourceViewDescriptor = (ViewDescriptor) bean;
            final ViewDescriptor targetViewDescriptor = this.getTemplate(sourceViewDescriptor);

            // Copy source state
            ObjectUtils.shallowCopy(sourceViewDescriptor, targetViewDescriptor);

            return targetViewDescriptor;
        }

        return bean;
    }

    /**
     * Gets the configured template for the given view descriptor.
     *
     * @param viewDescriptor the view descriptor.
     * @return the more suitable template.
     */
    private VLDockingViewDescriptor getTemplate(ViewDescriptor viewDescriptor) {

        Assert.notNull(viewDescriptor, "viewDescriptor");

        final VLDockingViewDescriptor vlDockingViewDescriptor;

        vlDockingViewDescriptor = new VLDockingViewDescriptor();

        return vlDockingViewDescriptor;
    }
}

