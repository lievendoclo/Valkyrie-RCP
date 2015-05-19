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

import com.google.common.collect.Lists;
import org.valkyriercp.application.PageLayoutBuilder;
import org.valkyriercp.application.ViewDescriptor;

import java.util.List;

public class MultiViewPageDescriptor  extends AbstractPageDescriptor {

    private List viewDescriptors = Lists.newArrayList();

    public void buildInitialLayout(PageLayoutBuilder pageLayout) {
        for (Object viewDescriptor : getViewDescriptors()) {
            String descriptor = null;
            if (viewDescriptor instanceof String) {
               descriptor = (String) viewDescriptor;
            } else if(viewDescriptor instanceof ViewDescriptor) {
                descriptor = ((ViewDescriptor) viewDescriptor).getId();
            } else {
                throw new IllegalStateException("ViewDescriptors should either be String or ViewDescriptor elements");
            }
            pageLayout.addView(descriptor);
        }
    }

    public List getViewDescriptors() {
        return viewDescriptors;
    }

    public void setViewDescriptors(List viewDescriptors) {
        this.viewDescriptors = viewDescriptors;
    }

    public void setBeanName(String name) {
        setId(name);
    }

}

