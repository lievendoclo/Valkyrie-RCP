/*
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

import org.valkyriercp.application.PageLayoutBuilder;

import java.util.ArrayList;
import java.util.List;

public class MultiViewPageDescriptor  extends AbstractPageDescriptor {

    private List<String> viewDescriptorIds = new ArrayList<>();

    public void buildInitialLayout(PageLayoutBuilder pageLayout) {
        getViewDescriptorIds().forEach(pageLayout::addView);
    }

    public List<String> getViewDescriptorIds() {
        return viewDescriptorIds;
    }

    public void setViewDescriptorIds(List<String> viewDescriptorIds) {
        this.viewDescriptorIds = viewDescriptorIds;
    }

    public void setBeanName(String name) {
        setId(name);
    }

}

