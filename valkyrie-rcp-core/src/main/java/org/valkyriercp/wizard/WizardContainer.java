package org.valkyriercp.wizard;

/**
 * Interface for containers that can host a wizard. It displays wizard pages, at
 * most one of which is considered the current page. <code>getCurrentPage</code>
 * returns the current page; <code>showPage</code> programmatically changes
 * the the current page. Note that the pages need not all belong to the same
 * wizard.
 * <p>
 * The class <code>WizardDialog</code> provides a fully functional
 * implementation of this interface which will meet the needs of most clients.
 * However, clients are also free to implement this interface if
 * <code>WizardDialog</code> does not suit their needs.
 * </p>
 * <p>
 * Implementors are responsible for disposing of their wizards.
 * </p>
 */
public interface WizardContainer {

    /**
     * Returns the current wizard page for this container.
     *
     * @return the current wizard page, or <code>null</code> if the container
     *         is not yet showing the wizard
     * @see #showPage
     */
    public WizardPage getCurrentPage();

    /**
     * Makes the given page visible.
     * <p>
     * This method should not be use for normal page sequencing (back, next)
     * which is handled by the container itself. It may, however, be used to
     * move to another page in response to some custom action such as double
     * clicking in a list.
     * </p>
     *
     * @param page
     *            the page to show
     * @see #getCurrentPage
     */
    public void showPage(WizardPage page);

}
