package org.valkyriercp.application.support

import org.valkyriercp.AbstractValkyrieSpec
import org.valkyriercp.application.PageListener

class DefaultApplicationWindowSpec extends AbstractValkyrieSpec {
    def "test regression failure to remove page listener" () {
        given:
        def pageListener = Mock(PageListener);
        def daw = new DefaultApplicationWindow(applicationConfig);
        daw.addPageListener(pageListener);
        expect:
        daw.removePageListener(pageListener);
    }
}
