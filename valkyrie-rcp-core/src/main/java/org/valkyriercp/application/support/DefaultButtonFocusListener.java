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
package org.valkyriercp.application.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.valkyriercp.command.support.ActionCommand;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DefaultButtonFocusListener implements PropertyChangeListener
{
    public static final String DEFAULT_COMMAND = "defaultCommand";
    private static final transient Log log = LogFactory.getLog(DefaultButtonFocusListener.class);

    /**
     * Installeert een Listener op KeyboardFocusManager.getCurrentKeyboardFocusManager().
     * Wanneer de focusOwner wijzigt, wordt in zijn hierarchy gekeken of er de clientProperty "defaultCommand" aanwezig is.
     * Als deze er is, wordt hierop de defaultbutton gezet.
     */
    public DefaultButtonFocusListener()
    {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", this);
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        Component comp = (Component) evt.getNewValue();

        if (comp != null && comp instanceof JComponent)
        {

            JComponent jcomp = (JComponent) comp;

            while (jcomp != null && jcomp.getClientProperty(DEFAULT_COMMAND) == null)
            {
                if (jcomp.getParent() instanceof JComponent)
                {
                    JComponent newParent = (JComponent) jcomp.getParent();
                    if (newParent == jcomp)
                        jcomp = null;
                    else
                        jcomp = newParent ;
                }
                else
                    jcomp = null;
            }

            if (jcomp != null)
            {
                ActionCommand command = (ActionCommand) jcomp.getClientProperty(DEFAULT_COMMAND);
                command.setDefaultButton();
            }
        }

    }

    public static void setDefaultButton(JComponent comp, ActionCommand command)
    {
        comp.putClientProperty(DefaultButtonFocusListener.DEFAULT_COMMAND, command);
    }
}

