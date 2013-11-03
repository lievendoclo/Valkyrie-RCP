package org.valkyriercp.application.support;

import org.valkyriercp.application.ApplicationPage;

/**
 * Testcase for {@link TabbedApplicationPage}
 *
 * @author Peter De Bruycker
 */
public class TabbedApplicationPageTests extends AbstractApplicationPageTests {

    @Override
    protected ApplicationPage createApplicationPage() {
        return new TabbedApplicationPage();
    }

}