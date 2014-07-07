package org.valkyriercp.application.support

import org.valkyriercp.application.ApplicationPage

class TabbedApplicationPageSpec extends AbstractApplicationPageSpec {
    @Override
    protected ApplicationPage createApplicationPage() {
        return new TabbedApplicationPage()
    }
}