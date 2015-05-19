package org.valkyriercp.application.docking;

import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.ViewDescriptor;

import java.awt.*;

import bibliothek.gui.dock.DefaultDockable;

/**
 * @author Rogan Dawes
 */
public class ViewDescriptorDockable extends DefaultDockable {

    private ViewDescriptor viewDescriptor;

    private PageComponent pageComponent;

    public ViewDescriptorDockable(ViewDescriptor viewDescriptor) {
        this(viewDescriptor, null);
    }

    public ViewDescriptorDockable(ViewDescriptor viewDescriptor,
                                  PageComponent pageComponent) {
        this.viewDescriptor = viewDescriptor;
        this.pageComponent = pageComponent;
        setTitleText(viewDescriptor.getCaption());
        setTitleIcon(viewDescriptor.getIcon());

    }

    public PageComponent getPageComponent() {
        if (pageComponent == null)
            pageComponent = viewDescriptor.createPageComponent();
        return pageComponent;
    }

    public void setPageComponent(PageComponent pageComponent) {
        this.pageComponent = pageComponent;
    }

    /*
    * (non-Javadoc)
    *
    * @see com.vlsolutions.swing.docking.Dockable#getComponent()
    */
    public Component getComponent() {
        return getPageComponent().getControl();
    }
}
