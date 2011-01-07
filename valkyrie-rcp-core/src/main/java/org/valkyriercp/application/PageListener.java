package org.valkyriercp.application;

/**
 * Client interface for listening to page lifecycle events.
 * <p>
 */
public interface PageListener {

    /**
     * Notifies this listener that the given page has been opened.
     *
     * @param page
     *            the page that was opened
     */
    public void pageOpened(ApplicationPage page);

    /**
     * Notifies this listener that the given page has been closed.
     *
     * @param page
     *            the page that was closed
     */
    public void pageClosed(ApplicationPage page);

}