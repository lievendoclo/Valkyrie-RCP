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