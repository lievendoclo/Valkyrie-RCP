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

import org.valkyriercp.application.PageComponentContext;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.form.AbstractForm;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * Provides an easy way to create widgets based on an AbstractForm.
 */
public abstract class AbstractWidgetForm extends AbstractForm implements Widget
{
    /** Id for the undo command. */
    public static final String UNDO_CMD_ID = "undo";

    /** Id for the save command. */
    public static final String SAVE_CMD_ID = "save";

    protected boolean showing = false;

    protected AbstractWidgetForm() {
    }

    protected AbstractWidgetForm(String id) {
        super(id);
    }

    public boolean canClose()
    {
        return true;
    }

    public List<? extends AbstractCommand> getCommands()
    {
        return Collections.emptyList();
    }

    public JComponent getComponent()
    {
        return getControl();
    }

    public void onAboutToHide()
    {
        showing = false;
    }

    public void onAboutToShow()
    {
        showing = true;
    }

    public boolean isShowing()
    {
        return showing;
    }

    @Override
    protected String getCommitCommandFaceDescriptorId()
    {
        return SAVE_CMD_ID;
    }

    @Override
    protected String getRevertCommandFaceDescriptorId()
    {
        return UNDO_CMD_ID;
    }

    @Override
    public void registerLocalCommandExecutors(PageComponentContext context) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

