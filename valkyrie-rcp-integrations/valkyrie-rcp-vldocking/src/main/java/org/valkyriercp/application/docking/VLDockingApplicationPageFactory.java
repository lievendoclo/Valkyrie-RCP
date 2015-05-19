package org.valkyriercp.application.docking;

import com.vldocking.swing.docking.DockingContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.valkyriercp.application.*;
import org.valkyriercp.util.ValkyrieRepository;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <tt>ApplicationPageFactory</tt> that creates instances of <tt>VLDockingApplicationPage</tt>.
 * 
 * @author Rogan Dawes
 */
public class VLDockingApplicationPageFactory implements ApplicationPageFactory {

    private static final Log logger = LogFactory.getLog(VLDockingApplicationPageFactory.class);

    private boolean reusePages;
    private Map<Object, Map<Object, Object>> pageCache = new HashMap<Object, Map<Object, Object>>();
    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.richclient.application.ApplicationPageFactory#createApplicationPage(org.springframework.richclient.application.ApplicationWindow,
     *      org.springframework.richclient.application.PageDescriptor)
     */
    public ApplicationPage createApplicationPage(ApplicationWindow window, PageDescriptor descriptor) {
        if (reusePages) {
            VLDockingApplicationPage page = findPage(window, descriptor);
            if (page != null) {
                return page;
            }
        }
        VLDockingApplicationPage page = new VLDockingApplicationPage(window, descriptor);
        if (reusePages) {
            cachePage(page);
        }

        window.addPageListener(new PageListener() {
            public void pageOpened(ApplicationPage page) {
                // nothing to do here
            }

            public void pageClosed(ApplicationPage page) {
                VLDockingApplicationPage vlDockingApplicationPage = (VLDockingApplicationPage) page;
                saveDockingContext(vlDockingApplicationPage);
            }
        });

        return page;
    }

    /**
     * Saves the docking layout of a docking page fo the application
     * 
     * @param dockingPage
     *            The application window (needed to hook in for the docking context)
     */
    private void saveDockingContext(VLDockingApplicationPage dockingPage) {
        DockingContext dockingContext = dockingPage.getDockingContext();

        // Page descriptor needed for config path
        VLDockingPageDescriptor vlDockingPageDescriptor = ValkyrieRepository.getInstance().getBean(dockingPage.getId(), VLDockingPageDescriptor.class);

        // Write docking context to file
        BufferedOutputStream buffOs = null;
        try {
            File desktopLayoutFile = vlDockingPageDescriptor.getInitialLayout().getFile();
            checkForConfigPath(desktopLayoutFile);

            buffOs = new BufferedOutputStream(new FileOutputStream(desktopLayoutFile));
            dockingContext.writeXML(buffOs);
            buffOs.close();
            logger.debug("Wrote docking context to config file " + desktopLayoutFile);

        }
        catch (IOException e) {
            logger.warn("Error writing VLDocking config", e);
        }
        finally {
            try {
                if(buffOs != null)
                    buffOs.close();
            }
            catch (Exception e) {
                logger.debug("Error closing layoutfile", e);
            }
        }
    }

    /**
     * Creates the config directory, if it doesn't exist already
     * 
     * @param configFile
     *            The file for which to create the path
     */
    private void checkForConfigPath(File configFile) {
        String desktopLayoutFilePath = configFile.getAbsolutePath();
        String configDirPath = desktopLayoutFilePath.substring(0, desktopLayoutFilePath.lastIndexOf(System
                .getProperty("file.separator")));
        File configDir = new File(configDirPath);

        // create config dir if it does not exist
        if (!configDir.exists()) {
            boolean success = configDir.mkdirs();
            if(success)
                logger.debug("Newly created config directory");
            else
                logger.warn("Could not create config directory");
        }
    }

    protected VLDockingApplicationPage findPage(ApplicationWindow window, PageDescriptor descriptor) {
        Map<Object, Object> pages = pageCache.get(window);
        if (pages == null) {
            return null;
        }
        
        return (VLDockingApplicationPage) pages.get(descriptor.getId());
    }

    protected void cachePage(VLDockingApplicationPage page) {
        Map<Object, Object> pages = pageCache.get(page.getWindow());
        if (pages == null) {
            pages = new HashMap<Object, Object>();
            pageCache.put(page.getWindow(), pages);
        }
        pages.put(page.getId(), page);
    }

    /**
     * @param reusePages
     *            the reusePages to set
     */
    public void setReusePages(boolean reusePages) {
        this.reusePages = reusePages;
    }
}
