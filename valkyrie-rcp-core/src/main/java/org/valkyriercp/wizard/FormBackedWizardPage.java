/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
