package org.valkyriercp.application.docking;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageDescriptor;

import java.util.HashMap;
import java.util.Map;

/**
 * <tt>ApplicationPageFactory</tt> that creates instances of <tt>DockingFramesApplicationPage</tt>.
 * 
 * @author Rogan Dawes
 */
public class DockingFramesApplicationPageFactory implements ApplicationPageFactory {

    private static final Log logger = LogFactory.getLog(DockingFramesApplicationPageFactory.class);

    private boolean reusePages;
    private Map<Object, Map<Object, Object>> pageCache = new HashMap<Object, Map<Object, Object>>();
    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.richclient.application.ApplicationPageFactory#createApplicationPage(org.springframework.richclient.application.ApplicationWindow,
     *      org.springframework.richclient.application.PageDescriptor)
     */
    public ApplicationPage createApplicationPage(ApplicationWindow window, PageDescriptor descriptor) {
        return new DockingFramesApplicationPage(window, descriptor);
    }
}
