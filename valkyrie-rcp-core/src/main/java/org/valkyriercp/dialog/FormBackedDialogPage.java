package org.valkyriercp.dialog;

import org.valkyriercp.form.Form;

import javax.swing.*;

/**
 * An implementation of DialogPage that delegates to a FormPage for its control,
 * pageComplete status and messages.
 *
 * @author Oliver Hutchison
 */
public class FormBackedDialogPage extends AbstractDialogPage {
    private Form backingFormPage;

    /**
     * Creates a new FormBackedDialogPage
     *
     * @param backingFormPage
     *            a named form page that will provide the control for this
     *            dialog page
     */
    public FormBackedDialogPage(Form backingFormPage) {
        this(backingFormPage, true);
    }

    public FormBackedDialogPage(Form backingFormPage, boolean autoConfigure) {
        super(backingFormPage.getId(), autoConfigure);
        this.backingFormPage = backingFormPage;
    }

    /**
     * Creates a new FormPageBackedDialogPage.
     *
     * @param parentPageId
     *            the id of a containing parent page. This will be used to
     *            configure page titles/description
     * @param backingFormPage
     *            the FormPage which will provide the control for this page.
     */
    public FormBackedDialogPage(String parentPageId, Form backingFormPage) {
        super(parentPageId + (backingFormPage.getId() != null ? "." + backingFormPage.getId() : ""));
        this.backingFormPage = backingFormPage;
    }

    /**
     * Get the Form backing this dialog page.
     * @return form
     */
    public Form getBackingFormPage() {
        return backingFormPage;
    }

    public void onAboutToShow() {
        setEnabled(!backingFormPage.hasErrors());
    }

    protected JComponent createControl() {
        JComponent formControl = backingFormPage.getControl();
        initPageValidationReporter();
        return formControl;
    }

    protected void initPageValidationReporter() {
        backingFormPage.newSingleLineResultsReporter(this);
        backingFormPage.addGuarded(this);
    }

    public void setEnabled(boolean enabled) {
        setPageComplete(enabled);
    }
}
