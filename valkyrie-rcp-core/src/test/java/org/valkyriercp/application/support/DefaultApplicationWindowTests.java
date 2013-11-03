package org.valkyriercp.application.support;

import org.junit.Test;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ApplicationWindowConfigurer;
import org.valkyriercp.application.PageListener;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.support.DefaultApplicationLifecycleAdvisor;
import org.valkyriercp.command.support.CommandGroup;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Test cases for {@link DefaultApplicationWindow}.
 *
 * @author Andy DePue
 */
public class DefaultApplicationWindowTests extends AbstractValkyrieTest {

    @Test
    public void testRegressionFailureToRemovePageListener() {
        PageListener pageListener = mock(PageListener.class);
        DefaultApplicationWindow daw = new DefaultApplicationWindow(applicationConfig);
        daw.addPageListener(pageListener);

        try {
            daw.removePageListener(pageListener);
        } catch(IllegalArgumentException iae) {
            iae.printStackTrace();
            fail("DefaultApplicationWindow.removePageListener threw IllegalArgumentException when removing a valid pageListener: " + iae);
        }
    }

    /**
     * Mocks out various methods on the returned ApplicationLifecycleAdvisor
     * as they are not needed for the current unit test(s) and will throw
     * exceptions without further setup for the test.  If more unit tests
     * are added to this class in the future, then the returned
     * ApplicationLifecycleAdvisor should be revisited to ensure it still
     * meets the needs of this test case.
     */
    protected ApplicationLifecycleAdvisor createApplicationLifecycleAdvisor() {
        return new DefaultApplicationLifecycleAdvisor() {
            public void onPreWindowOpen(ApplicationWindowConfigurer configurer) {
            }
            public void onCommandsCreated(ApplicationWindow window) {
            }
            public ApplicationWindowCommandManager createWindowCommandManager() {
                return null;
            }
            public CommandGroup getMenuBarCommandGroup() {
                return null;
            }
            public CommandGroup getToolBarCommandGroup() {
                return null;
            }
        };
    }
}

