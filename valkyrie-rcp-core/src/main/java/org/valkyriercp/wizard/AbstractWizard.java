package org.valkyriercp.wizard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.valkyriercp.core.TitleConfigurable;
import org.valkyriercp.form.Form;
import org.valkyriercp.util.EventListenerListHelper;
import org.valkyriercp.util.ValkyrieRepository;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A convenience implementation of the {@link Wizard} interface. This abstract class provides the
 * following basic wizard functionaliy:
 *
 * <ul>
 * <li>Adding and removing pages from the wizard.</li>
 * <li>Stepping forward and back through the wizard pages.</li>
 * <li>Adding and removing wizard listeners.</li>
 * <li>Notifying listeners of events such as {@code cancel} and {@code finish}.</li>
 * </ul>
 *
 *
 * @author Keith Donald
 */
public abstract class AbstractWizard implements Wizard, TitleConfigurable {

    /** The key that will be used to retrieve the default page image icon for the wizard. */
    public static final String DEFAULT_IMAGE_KEY = "wizard.pageIcon";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String wizardId;

    private String title;

    private boolean forcePreviousAndNextButtons;

    private List pages = new ArrayList(6);

    private WizardContainer container;

    private EventListenerListHelper listeners = new EventListenerListHelper(WizardListener.class);

    private boolean autoConfigureChildPages = true;
    /**
     * Creates a new uninitialized {@code AbstractWizard}.
     */
    public AbstractWizard() {
        this(null);
    }

    /**
     * Creates a new {@code AbstractWizard} with the given identifier.
     *
     * @param wizardId The id used to identify this wizard.
     */
    public AbstractWizard(String wizardId) {
        this.wizardId = wizardId;
    }

    /**
     * Returns this wizard's identifier.
     *
     * @return the identifier of this wizard, may be null.
     */
    public String getId() {
        return wizardId;
    }

    /**
     * Sets the flag that determines whether or not wizard pages will be configured as they are
     * added to this wizard.
     *
     * @param autoConfigure
     */
    public void setAutoConfigureChildPages(boolean autoConfigure) {
        this.autoConfigureChildPages = autoConfigure;
    }

    /**
     * Controls whether the wizard needs Previous and Next buttons even if it
     * currently contains only one page.
     * <p>
     * This flag should be set on wizards where the first wizard page adds
     * follow-on wizard pages based on user input.
     * </p>
     *
     * @param b
     *            <code>true</code> to always show Next and Previous buttons,
     *            and <code>false</code> to suppress Next and Previous buttons
     *            for single page wizards
     */
    public void setForcePreviousAndNextButtons(boolean b) {
        this.forcePreviousAndNextButtons = b;
    }

    /**
     * Returns the window title for the container that host this wizard.
     *
     * @return the wizard title, may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the window title for the container that hosts this page to the given
     * string.
     *
     * @param newTitle
     *            the window title for the container
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     * Returns the component that contains this wizard.
     *
     * @return the wizard container.
     */
    public WizardContainer getContainer() {
        return container;
    }

    /**
     * Sets the component that contains this wizard.
     *
     * @param container the container to set
     */
    public void setContainer(WizardContainer container) {
        this.container = container;
    }

    /**
     * Adds a new page to this wizard. The page is inserted at the end of the
     * page list.
     *
     * @param page
     *            the new page
     */
    public void addPage(WizardPage page) {
        addPage(getId(), page);
    }

    /**
     * Adds a new page to this wizard. The page is inserted at the end of the
     * page list.
     *
     * @param wizardConfigurationKey
     *            the parent configuration key of the page, used for
     *            configuration, by default this wizard's id *
     * @param page
     *            the new page
     */
    protected void addPage(String wizardConfigurationKey, WizardPage page) {
        pages.add(page);
        page.setWizard(this);
        if (autoConfigureChildPages) {
            String key = ((wizardConfigurationKey != null) ? wizardConfigurationKey + "." : "") + page.getId();
            ValkyrieRepository.getInstance().getApplicationConfig().applicationObjectConfigurer().configure(page, key);
        }
    }

    /**
     * Adds a new page to this wizard. The page is created by wrapping the form in a
     * {@link FormBackedWizardPage} and appending it to the end of the page list.
     *
     * @param formPage The form page to be added to the wizard.
     * @return the newly created wizard page that wraps the given form.
     *
     * @throws IllegalArgumentException if {@code formPage} is null.
     */
    public WizardPage addForm(Form formPage) {
        Assert.notNull(formPage, "The form page cannot be null");
        WizardPage page = new FormBackedWizardPage(formPage, !autoConfigureChildPages);
        addPage(page);
        return page;
    }

    /**
     * Removes the given page from this wizard.
     *
     * @param page The page to be removed.
     */
    public void removePage(WizardPage page) {
        if (pages.remove(page)) {
            page.setWizard(null);
        }
    }

    /**
     * This implementation of {@link Wizard#addPages()} does nothing. Subclasses should override
     * this method if extra pages need to be added before the wizard is displayed. New pages should
     * be added by calling {@link #addPage(WizardPage)}.
     */
    public void addPages() {
        //do nothing
    }

    /**
     * Returns true if all the pages of this wizard have been completed.
     *
     */
    public boolean canFinish() {
        // Default implementation is to check if all pages are complete.
        for (int i = 0; i < pages.size(); i++) {
            if (!((WizardPage)pages.get(i)).isPageComplete())
                return false;
        }
        return true;
    }

    /**
     * Returns the image stored under the key {@value #DEFAULT_IMAGE_KEY}.
     */
    public Image getDefaultPageImage() {
        return ValkyrieRepository.getInstance().getApplicationConfig().imageSource().getImage(DEFAULT_IMAGE_KEY);
    }

    /**
     * {@inheritDoc}
     */
    public WizardPage getNextPage(WizardPage page) {
        int index = pages.indexOf(page);
        if (index == pages.size() - 1 || index == -1) {
            // last page or page not found
            return null;
        }
        return (WizardPage)pages.get(index + 1);
    }

    /**
     * {@inheritDoc}
     */
    public WizardPage getPage(String pageId) {
        Iterator it = pages.iterator();
        while (it.hasNext()) {
            WizardPage page = (WizardPage)it.next();
            if (page.getId().equals(pageId)) {
                return page;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int getPageCount() {
        return pages.size();
    }

    /**
     * {@inheritDoc}
     */
    public WizardPage[] getPages() {
        return (WizardPage[])pages.toArray(new WizardPage[pages.size()]);
    }

    /**
     * {@inheritDoc}
     */
    public WizardPage getPreviousPage(WizardPage page) {
        int index = pages.indexOf(page);
        if (index == 0 || index == -1) {
            // first page or page not found
            return null;
        }

        logger.debug("Returning previous page...");
        return (WizardPage)pages.get(index - 1);
    }

    /**
     * {@inheritDoc}
     */
    public WizardPage getStartingPage() {
        if (pages.size() == 0) {
            return null;
        }
        return (WizardPage)pages.get(0);
    }

    /**
     * {@inheritDoc}
     */
    public boolean needsPreviousAndNextButtons() {
        return forcePreviousAndNextButtons || pages.size() > 1;
    }

    /**
     * {@inheritDoc}
     */
    public void addWizardListener(WizardListener wizardListener) {
        listeners.add(wizardListener);
    }

    /**
     * {@inheritDoc}
     */
    public void removeWizardListener(WizardListener wizardListener) {
        listeners.remove(wizardListener);
    }

    /**
     * Fires an {@code onPerformFinish} event to all listeners.
     */
    protected void fireFinishedPerformed(boolean result) {
        listeners.fire("onPerformFinish", this, Boolean.valueOf(result));
    }

    /**
     * Fires an {@code onPerformCancel} event to all listeners.
     */
    protected void fireCancelPerformed(boolean result) {
        listeners.fire("onPerformCancel", this, Boolean.valueOf(result));
    }

    /**
     * Performs any required processing when the wizard receives a finish request, and then fires
     * an appropriate event to any wizard listeners listening to this wizard.
     *
     * @return {@code true} to indicate that the finish request was accepted, {@code false} to
     * indicate that it was refused.
     */
    public boolean performFinish() {
        boolean result = onFinish();
        fireFinishedPerformed(result);
        return result;
    }

    /**
     * Performs any required processing when the wizard is cancelled, and then fires an appropriate
     * event to any wizard listeners listening to this wizard.
     *
     * @return {@code true} to indicate that the cancel request was accepted, {@code false} to
     * indicate that it was refused.
     */
    public boolean performCancel() {
        boolean result = onCancel();
        fireCancelPerformed(result);
        return result;
    }

    /**
     * Subclasses must implement this method to perform any processing when the wizard receives a
     * finish request.
     *
     * @return {@code true} to indicate that the finish request was accepted, {@code false} to
     * indicate that it was refused.
     */
    protected abstract boolean onFinish();

    /**
     * Subclasses can override this method to perform processing when the wizard receives a cancel
     * request. This default implementation always returns true.
     *
     * @returrn {@code true} to indicate that the cancel request was accepted, {@code false} to
     * indicate that it was refused.
     */
    protected boolean onCancel() {
        return true;
    }

}
