package org.valkyriercp.application.support;

import org.valkyriercp.application.ApplicationPage;

public class DefaultApplicationPageTests extends AbstractApplicationPageTests {

    @Override
    protected ApplicationPage createApplicationPage() {
        return new DefaultApplicationPage();
    }

}

