package org.valkyriercp.wizard;

import org.valkyriercp.core.Guarded;
import org.valkyriercp.dialog.DialogPage;

public interface WizardPage extends DialogPage, Guarded {

    /**
     * Returns the wizard page that would to be shown if the user was to press
     * the Next button.
     *
     * @return the next wizard page, or <code>null</code> if none
     */
    public WizardPage getNextPage();

    /**
     * Returns the wizard page that would to be shown if the user was to press
     * the Back button.
     *
     * @return the previous wizard page, or <code>null</code> if none
     */
    public WizardPage getPreviousPage();

    /**
     * Sets the wizard page that would typically be shown if the user was to
     * press the Back button.
     * <p>
     * This method is called by the container.
     * </p>
     *
     * @param page
     *            the previous wizard page
     */
    public void setPreviousPage(WizardPage page);

    /**
     * Returns the wizard that hosts this wizard page.
     *
     * @return the wizard, or <code>null</code> if this page has not been
     *         added to any wizard
     * @see #setWizard
     */
    public Wizard getWizard();

    /**
     * Returns whether the next page could be displayed.
     *
     * @return <code>true</code> if the next page could be displayed, and
     *         <code>false</code> otherwise
     */
    public boolean canFlipToNextPage();

    /**
     * Sets the wizard that hosts this wizard page. Once established, a page's
     * wizard cannot be changed to a different wizard.
     *
     * @param newWizard
     *            the wizard
     * @see #getWizard
     */
    public void setWizard(Wizard newWizard);

    public void onAboutToShow();
}