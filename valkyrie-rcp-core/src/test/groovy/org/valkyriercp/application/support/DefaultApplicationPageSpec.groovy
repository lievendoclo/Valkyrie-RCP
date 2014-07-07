package org.valkyriercp.application.support

import org.valkyriercp.application.ApplicationPage

class DefaultApplicationPageSpec extends AbstractApplicationPageSpec {
    @Override
    protected ApplicationPage createApplicationPage() {
        return new DefaultApplicationPage()
    }
}