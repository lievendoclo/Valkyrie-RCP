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
package org.valkyriercp.application.support

import org.valkyriercp.application.PageLayoutBuilder
import spock.lang.Specification

class MultiViewPageDescriptorSpec extends Specification {
    def testBuildInitialLayout() {
        given:
        MultiViewPageDescriptor pageDescriptor = new MultiViewPageDescriptor();
        List descriptors = new ArrayList();
        descriptors.add("view0");
        descriptors.add("view1");
        descriptors.add("view2");
        descriptors.add("view3");
        pageDescriptor.setViewDescriptors(descriptors);
        PageLayoutBuilder mockBuilder = Mock(PageLayoutBuilder);

        when:
        pageDescriptor.buildInitialLayout(mockBuilder);
        then:
        1 * mockBuilder.addView("view0")
        1 * mockBuilder.addView("view1")
        1 * mockBuilder.addView("view2")
        1 * mockBuilder.addView("view3")
    }

    def testBeanAware() {
        given:
        MultiViewPageDescriptor pageDescriptor = new MultiViewPageDescriptor();
        when:
        pageDescriptor.setBeanName("bean name");
        then:
        pageDescriptor.getId() == "bean name"
    }
}