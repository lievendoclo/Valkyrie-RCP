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

import com.google.common.collect.Lists;
import org.springframework.util.Assert;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.dialog.DialogPage;
import org.valkyriercp.dialog.TitledApplicationDialog;
import org.valkyriercp.util.GuiStandardUtils;
import org.valkyriercp.util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Dialog for wizards.
 *
 * @author Keith Donald
 */
public class WizardDialog extends TitledApplicationDialog implements WizardContainer, PropertyChangeListener {
    protected Wizard wizard;

    protected ActionCommand nextCommand;

    protected ActionCommand backCommand;

    protected WizardPage currentPage;

    protected int largestPageWidth;

    protected int largestPageHeight;

    public WizardDialog() {
        this(null);
    }

    public WizardDialog(Wizard wizard) {
        super();
        setWizard(wizard);
        setResizable(true);
    }

    public void setWizard(Wizard wizard) {
        if (this.wizard != wizard) {
            if (this.wizard != null) {
                this.wizard.setContainer(null);
            }
            this.wizard = wizard;
            if (this.wizard != null) {
                this.wizard.setContainer(this);
                this.setTitle(wizard.getTitle());
                this.wizard.addPages();
            }
        }
    }

    protected String getFinishCommandId() {
        return "finishCommand";
    }

    protected JComponent createTitledDialogContentPane() {
        createPageControls();
        WizardPage startPage = wizard.getStartingPage();
        Assert.notNull(startPage, "No starting page returned; unable to show wizard.");
        JComponent control = startPage.getControl();
        if (getPreferredSize() == null) {
            control.setPreferredSize(getLargestPageSize());
        }
        return control;
    }

    private Dimension getLargestPageSize() {
        return new Dimension(largestPageWidth + UIConstants.ONE_SPACE, largestPageHeight + UIConstants.ONE_SPACE);
    }

    protected java.util.List<? extends AbstractCommand> getCommandGroupMembers() {
        if (!wizard.needsPreviousAndNextButtons()) {
            return super.getCommandGroupMembers();
        }
        nextCommand = new ActionCommand("nextCommand") {
            public void doExecuteCommand() {
                onNext();
            }
        };
        backCommand = new ActionCommand("backCommand") {
            public void doExecuteCommand() {
                onBack();
            }
        };
        backCommand.setEnabled(false);
        return Lists.newArrayList( backCommand, nextCommand, getFinishCommand(), getCancelCommand());
    }

    protected void onAboutToShow() {
        showPage(wizard.getStartingPage());
        super.onAboutToShow();
    }

    /**
     * Allow the wizard's pages to pre-create their page controls. This allows
     * the wizard dialog to open to the correct size.
     */
    private void createPageControls() {
        WizardPage[] pages = wizard.getPages();
        for (int i = 0; i < pages.length; i++) {
            JComponent c = pages[i].getControl();
            GuiStandardUtils.attachDialogBorder(c);
            Dimension size = c.getPreferredSize();
            if (size.width > largestPageWidth) {
                largestPageWidth = size.width;
            }
            if (size.height > largestPageHeight) {
                largestPageHeight = size.height;
            }
        }
    }

    public void showPage(WizardPage page) {
        if (this.currentPage != page) {
            if (this.currentPage != null) {
                this.currentPage.removePropertyChangeListener(this);
            }
            this.currentPage = page;
            this.currentPage.addPropertyChangeListener(this);
            updateDialog();
            setContentPane(page.getControl());
        }
        this.currentPage.onAboutToShow();
        this.currentPage.setVisible(true);
    }

    public WizardPage getCurrentPage() {
        return currentPage;
    }

    protected void onBack() {
        WizardPage newPage = currentPage.getPreviousPage();
        if (newPage == null || newPage == currentPage) {
            throw new IllegalStateException("No such page.");
        }
        showPage(newPage);
    }

    protected void onNext() {
        WizardPage newPage = currentPage.getNextPage();
        if (newPage == null || newPage == currentPage) {
            throw new IllegalStateException("No such page.");
        }
        showPage(newPage);
    }

    protected boolean onFinish() {
        return wizard.performFinish();
    }

    protected void onCancel() {
        if (wizard.performCancel()) {
            super.onCancel();
        }
    }

    /**
     * Updates this dialog's controls to reflect the current page.
     */
    protected void updateDialog() {
        if (!isControlCreated()) {
            throw new IllegalStateException("Container controls not initialized - update not allowed.");
        }

        // Update the title pane
        updateTitlePane();

        // Update the message line
        updateMessagePane();

        // Update the buttons
        updateButtons();
    }

    /**
     * Updates the title bar (title, description, and image) to reflect the
     * state of the currently active page in this container.
     */
    protected void updateTitlePane() {
        setTitlePaneTitle(currentPage.getTitle());
        setTitlePaneImage(currentPage.getImage());
        setDescription(currentPage.getDescription());
    }

    /**
     * Updates the message (or error message) shown in the message line to
     * reflect the state of the currently active page in this container.
     */
    protected void updateMessagePane() {
        setMessage(currentPage.getMessage());
    }

    private void updateButtons() {
        if (wizard.needsPreviousAndNextButtons()) {
            backCommand.setEnabled(currentPage.getPreviousPage() != null);
            nextCommand.setEnabled(canFlipToNextPage());
        }
        setFinishEnabled(wizard.canFinish());
        if (canFlipToNextPage() && !wizard.canFinish()) {
            registerDefaultCommand(nextCommand);
        }
        else {
            registerDefaultCommand();
        }
    }

    private boolean canFlipToNextPage() {
        return currentPage.canFlipToNextPage();
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (Messagable.MESSAGE_PROPERTY.equals(e.getPropertyName())) {
            updateMessagePane();
        }
        else if (DialogPage.PAGE_COMPLETE_PROPERTY.equals(e.getPropertyName())) {
            updateButtons();
        }
        else if (DialogPage.DESCRIPTION_PROPERTY.equals(e.getPropertyName())) {
            updateTitlePane();
        }
    }
}