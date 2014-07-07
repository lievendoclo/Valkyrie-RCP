package org.valkyriercp.application.config.support

import org.valkyriercp.application.ApplicationWindow
import spock.lang.FailsWith
import spock.lang.Specification

class ApplicationWindowSetterSpec extends Specification {
    @FailsWith(IllegalArgumentException)
    def "initializing with null should throw exception"() {
        expect:
        new ApplicationWindowSetter(null)
    }

    def "application window setter should set window aware property"() {
        given:
        def window = Mock(ApplicationWindow)
        def windowAware = Mock(ApplicationWindowAware)
        ApplicationWindowSetter windowSetter = new ApplicationWindowSetter(window);

        when:
        windowSetter.postProcessBeforeInitialization(null, "bogusBeanName");

        then:
        0 * windowAware.setApplicationWindow(window)

        when:
        windowSetter.postProcessBeforeInitialization(windowAware, "bogusBeanName");

        then:
        1 * windowAware.setApplicationWindow(window)
    }
}