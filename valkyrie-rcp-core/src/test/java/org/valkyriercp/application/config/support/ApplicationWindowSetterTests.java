package org.valkyriercp.application.config.support;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.valkyriercp.application.ApplicationWindow;

/**
 * Provides unit tests for the {@link ApplicationWindowSetter} class.
 *
 * @author Kevin Stembridge
 * @since 0.3
 *
 */
public class ApplicationWindowSetterTests extends TestCase {

    /**
     * Creates a new uninitialized {@code ApplicationWindowSetterTests}.
     */
    public ApplicationWindowSetterTests() {
        super();
    }

    /**
     * Confirms that the constructor throws an IllegalArgumentException when passed a null window.
     */
    public void testConstructor() {

        try {
            new ApplicationWindowSetter(null);
            Assert.fail("Should have thrown an IllegalArgumentException for null ApplicationWindow");
        }
        catch(IllegalArgumentException e) {
            //test passes
        }

    }

    /**
     * Confirms that the postProcessBeforeInitialization method correctly sets the window on the
     * windowAware object.
     */
    public void testPostProcessBeforeInit() {

        //create required mocks
        ApplicationWindow window = (ApplicationWindow) EasyMock.createMock(ApplicationWindow.class);
        ApplicationWindowAware windowAware
                = (ApplicationWindowAware) EasyMock.createMock(ApplicationWindowAware.class);

        //confirm null bean is ok
        ApplicationWindowSetter windowSetter = new ApplicationWindowSetter(window);
        EasyMock.replay(windowAware);
        EasyMock.replay(window);
        windowSetter.postProcessBeforeInitialization(null, "bogusBeanName");
        EasyMock.verify(windowAware);
        EasyMock.verify(window);

        //confirm that the windowAware has its window set
        EasyMock.reset(window);
        EasyMock.reset(windowAware);

        windowAware.setApplicationWindow(window);

        EasyMock.replay(window);
        EasyMock.replay(windowAware);

        windowSetter.postProcessBeforeInitialization(windowAware, "bogusBeanName");

        EasyMock.verify(window);
        EasyMock.verify(windowAware);

    }

}

