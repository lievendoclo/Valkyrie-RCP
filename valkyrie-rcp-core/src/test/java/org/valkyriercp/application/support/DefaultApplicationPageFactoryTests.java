package org.valkyriercp.application.support;

import org.junit.Test;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.application.ApplicationWindow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test for <code>DefaultApplicationPageFactory</code>
 *
 * @author Peter De Bruycker
 */
public class DefaultApplicationPageFactoryTests extends AbstractValkyrieTest {

    @Test
	public void testCreate() {
		DefaultApplicationPageFactory factory = new DefaultApplicationPageFactory();

		ApplicationWindow window = mock(ApplicationWindow.class);
		SingleViewPageDescriptor descriptor= new SingleViewPageDescriptor(new DefaultViewDescriptor());

		DefaultApplicationPage page = (DefaultApplicationPage) factory.createApplicationPage(window, descriptor);
		assertNotNull("page cannot be null", page);
		assertEquals(window, page.getWindow());
		assertEquals(descriptor, page.getPageDescriptor());
	}

}
