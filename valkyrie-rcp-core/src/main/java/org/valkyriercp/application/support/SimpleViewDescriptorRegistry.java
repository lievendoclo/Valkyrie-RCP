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
package org.valkyriercp.application.support;

import org.springframework.util.Assert;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.ViewDescriptorRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Very simple {@link ViewDescriptorRegistry} implementation, mostly for testing purposes.
 *
 * @author Peter De Bruycker
 */
public class SimpleViewDescriptorRegistry implements ViewDescriptorRegistry {

    private Map<String, ViewDescriptor> descriptors = new HashMap<String, ViewDescriptor>();

    public SimpleViewDescriptorRegistry() {

    }

    public SimpleViewDescriptorRegistry(List<ViewDescriptor> descriptors) {
        Assert.notNull(descriptors, "descriptors cannot be null");

        for (ViewDescriptor descriptor : descriptors) {
            addViewDescriptor(descriptor);
        }
    }

    public void addViewDescriptor(ViewDescriptor descriptor) {
        Assert.notNull(descriptor, "descriptor cannot be null");

        descriptors.put(descriptor.getId(), descriptor);
    }

    public void removeViewDescriptor(ViewDescriptor descriptor) {
        Assert.notNull(descriptor, "descriptor cannot be null");

        descriptors.remove(descriptor.getId());
    }

    public ViewDescriptor getViewDescriptor(String viewDescriptorId) {
        Assert.hasText(viewDescriptorId, "viewDescriptorId cannot be empty");

        return descriptors.get(viewDescriptorId);
    }

    public ViewDescriptor[] getViewDescriptors() {
        return descriptors.values().toArray(new ViewDescriptor[descriptors.size()]);
    }

}

