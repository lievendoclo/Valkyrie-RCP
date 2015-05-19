package org.valkyriercp.application.docking;

import org.springframework.core.io.Resource;
import org.valkyriercp.application.support.MultiViewPageDescriptor;

/**
 * @author Rogan Dawes
 */
public class VLDockingPageDescriptor extends MultiViewPageDescriptor {

    private VLDockingLayoutManager layoutManager;

    private Resource initialLayout;

    /**
     * @return the layoutManager
     */
    public VLDockingLayoutManager getLayoutManager() {
        return this.layoutManager;
    }

    /**
     * @param layoutManager the layoutManager to set
     */
    public void setLayoutManager(VLDockingLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    /**
     * @return the initialLayout
     */
    public Resource getInitialLayout() {
        return this.initialLayout;
    }

    /**
     * @param initialLayout the initialLayout to set
     */
    public void setInitialLayout(Resource initialLayout) {
        this.initialLayout = initialLayout;
    }

}
