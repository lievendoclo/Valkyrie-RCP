package org.valkyriercp.application.support;

import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageComponentListener;

public class PageComponentListenerAdapter implements PageComponentListener {
    private PageComponent activeComponent;

    public PageComponent getActiveComponent() {
        return activeComponent;
    }

    public void componentOpened(PageComponent component) {

    }

    public void componentFocusGained(PageComponent component) {
        this.activeComponent = component;
    }

    public void componentFocusLost(PageComponent component) {
        this.activeComponent = null;
    }

    public void componentClosed(PageComponent component) {

    }

}