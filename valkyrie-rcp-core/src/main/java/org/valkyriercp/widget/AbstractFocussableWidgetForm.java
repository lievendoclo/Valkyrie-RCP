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
package org.valkyriercp.widget;

import org.valkyriercp.component.Focussable;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import java.awt.*;

/**
 * Form implementation for the Focussable interface.
 *
 * @author Jan Hoskens
 *
 */
public abstract class AbstractFocussableWidgetForm extends AbstractWidgetForm implements Focussable//, SecurityControllable
{

    public static final String UNSAVEDCHANGES_WARNING_ID = "unsavedchanges.warning";

    public static final String UNSAVEDCHANGES_HASERRORS_WARNING_ID = "unsavedchanges.haserrors.warning";

    private JComponent focusControl;

    private final Runnable focusRequestRunnable = new Runnable()
    {

        public void run()
        {
            if (focusControl != null)
                focusControl.requestFocusInWindow();
        }
    };

    protected AbstractFocussableWidgetForm() {
    }

    protected AbstractFocussableWidgetForm(String id) {
        super(id);
    }

    /**
     * Override to do nothing. Superclass registers a default command, but we are using a different system to
     * define default commands.
     */
    @Override
    protected void handleEnabledChange(boolean enabled)
    {
    }

    /**
     * Registers the component that receives the focus when the form receives focus.
     *
     * @see #grabFocus
     */
    public void setFocusControl(JComponent field)
    {
        this.focusControl = field;
    }

    public void grabFocus()
    {
        if (this.focusControl != null)
            EventQueue.invokeLater(focusRequestRunnable);
    }

    public boolean canClose()
    {
        boolean userBreak = false;
        int answer = JOptionPane.NO_OPTION; // by default no save is required.

        // unless of course there are unsaved changes and we can commit (isAuthorized)
        if (this.getFormModel().isEnabled() && this.getFormModel().isDirty()
                && this.getCommitCommand().isAuthorized())
        { // then we ask the user to save the mess first: yes/no/cancel
            answer = ValkyrieRepository.getInstance().getApplicationConfig().dialogFactory().showWarningDialog(this.getControl(), UNSAVEDCHANGES_WARNING_ID,
                    JOptionPane.YES_NO_CANCEL_OPTION);

            switch (answer)
            {
                case JOptionPane.CANCEL_OPTION :
                    // backup the selection change so table and detail keep in sync
                    // gives problems (asks unsavedchanges twice)
                    userBreak = true;
                    break;
                case JOptionPane.YES_OPTION :
                    if (this.getFormModel().getHasErrors() == true)
                    {
                        ValkyrieRepository.getInstance().getApplicationConfig().dialogFactory().showWarningDialog(this.getControl(), UNSAVEDCHANGES_HASERRORS_WARNING_ID);
                        userBreak = true;
                        break;
                    }
                    this.getCommitCommand().execute();
                    break;
                case JOptionPane.NO_OPTION :
                {
                    this.revert(); // revert so no strange things happen (hopefully)
                    break;
                }
            }
        }

        return !userBreak;
    }
}


