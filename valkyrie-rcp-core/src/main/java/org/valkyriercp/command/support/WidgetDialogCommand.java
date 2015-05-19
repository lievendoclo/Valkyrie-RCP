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
package org.valkyriercp.command.support;

import org.valkyriercp.dialog.ApplicationDialog;
import org.valkyriercp.util.ValkyrieRepository;
import org.valkyriercp.widget.TitledWidgetApplicationDialog;

import java.awt.*;

public class WidgetDialogCommand extends AbstractWidgetCommand
{
    private ApplicationDialog dialog;

    /** parent for centering the dialog. */
    private Component parent;

    public WidgetDialogCommand(String id)
    {
        super(id);
    }

    protected void doExecuteCommand()
    {
        dialog = (dialog == null) ? createDialog() : dialog;
        if (getParent() != null)
        {
            dialog.setParentComponent(getParent());
        }
        dialog.showDialog();
    }

    protected ApplicationDialog createDialog()
    {
        ApplicationDialog newlyCreatedDialog = new TitledWidgetApplicationDialog(getWidget());
        ValkyrieRepository.getInstance().getApplicationConfig().applicationObjectConfigurer().configure(newlyCreatedDialog, getId());
        return newlyCreatedDialog;
    }

    public Component getParent()
    {
        return parent;
    }

    /**
     * @param dialogParent
     *            The parent of the dialog for preservation of hierarchy and correct modality.
     */
    public void setParent(Component dialogParent)
    {
        this.parent = dialogParent;
    }
}


