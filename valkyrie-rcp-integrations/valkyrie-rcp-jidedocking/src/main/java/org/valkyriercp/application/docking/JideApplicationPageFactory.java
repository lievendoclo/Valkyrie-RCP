package org.valkyriercp.application.docking;

import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageDescriptor;

/**
 * Simple implementation of the ApplicationPageFactory service that
 * simply constructs instances of JideApplicationPage objects.
 * 
 * @author Jonny Wray
 *
 */
public class JideApplicationPageFactory implements ApplicationPageFactory {

	public ApplicationPage createApplicationPage(ApplicationWindow window, PageDescriptor pageDescriptor) {
        return new JideApplicationPage(window,  pageDescriptor);
	}

}
