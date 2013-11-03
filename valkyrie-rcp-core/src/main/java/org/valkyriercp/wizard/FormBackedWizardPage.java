package org.valkyriercp.wizard;

import org.valkyriercp.form.Form;

import javax.swing.*;

/**
 * An implementation of WizardPage that delegates to a FormPage for its control,
 * pageComplete status and messages.
 *
 * @author Oliver Hutchison
 */
public class FormBackedWizardPage extends AbstractWizardPage {
    private Form backingForm;

    /**
     * Createa a new FormBackedWizardPage
     *
     * @param backingForm
     *            the named form page which will provide the control for this
     *            wizard page.
     */
    public FormBackedWizardPage(Form backingForm) {
        this(backingForm, true);
    }

    public FormBackedWizardPage(Form backingForm, boolean autoConfigure) {
        super(backingForm.getId(), autoConfigure);
        this.backingForm = backingForm;
    }

    /**
     * Creates a new FormBackedWizardPage.
     *
     * @param parentPageId
     *            the id of a containing parent page. This will be used to
     *            configure page titles/description
     * @param backingForm
     *            the names form page which will provide the control for this
     *            wizard page.
     */
    public FormBackedWizardPage(String parentPageId, Form backingForm) {
        super(parentPageId + (backingForm.getId() != null ? "." + backingForm.getId() : ""));
        this.backingForm = backingForm;
    }

    protected Form getBackingForm() {
        return backingForm;
    }

    public void onAboutToShow() {
        setEnabled(!backingForm.hasErrors());
    }

    protected JComponent createControl() {
        JComponent formControl = backingForm.getControl();
        initPageValidationReporter();
        return formControl;
    }

    protected void initPageValidationReporter() {
        backingForm.newSingleLineResultsReporter(this);
        backingForm.addGuarded(this);
    }

    public void setEnabled(boolean enabled) {
        setPageComplete(enabled);
    }
}
