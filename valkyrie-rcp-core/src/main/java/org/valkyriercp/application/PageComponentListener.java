package org.valkyriercp.application;

import org.valkyriercp.application.PageComponent;

public interface PageComponentListener {

    /**
     * Notifies this listener that the given component has been created.
     *
     * @param component
     *            the component that was created
     */
    public void componentOpened(PageComponent component);

    /**
     * Notifies this listener that the given component has been given focus
     *
     * @param component
     *            the component that was given focus
     */
    public void componentFocusGained(PageComponent component);

    /**
     * Notifies this listener that the given component has lost focus.
     *
     * @param component
     *            the component that lost focus
     */
    public void componentFocusLost(PageComponent component);

    /**
     * Notifies this listener that the given component has been closed.
     *
     * @param component
     *            the component that was closed
     */
    public void componentClosed(PageComponent component);

}

