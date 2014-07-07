package org.valkyriercp.application.support

import org.valkyriercp.application.ApplicationWindow
import spock.lang.Specification

class DefaultApplicationPageFactorySpec extends Specification {
    def "test create"() {
        when:
        def factory = new DefaultApplicationPageFactory();
        def window = Mock(ApplicationWindow);
        def descriptor= new SingleViewPageDescriptor(new DefaultViewDescriptor());
        def page = factory.createApplicationPage(window, descriptor) as DefaultApplicationPage;

        then:
        page != null
        page.window == window
        page.pageDescriptor == descriptor
    }
}