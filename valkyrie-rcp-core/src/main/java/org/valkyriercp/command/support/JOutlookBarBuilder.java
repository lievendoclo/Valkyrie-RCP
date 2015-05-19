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

import com.l2fprod.common.swing.JOutlookBar;
import com.l2fprod.common.swing.PercentLayout;
import org.valkyriercp.command.config.CommandButtonConfigurer;

import javax.swing.*;

public class JOutlookBarBuilder extends CommandGroupJComponentBuilder
{
    private CommandButtonConfigurer commandButtonConfigurer = new JOutlookBarCommandButtonConfigurer();

    protected JComponent buildRootComponent(AbstractCommand command)
    {
        return new JOutlookBar();
    }

    protected JComponent buildChildComponent(JComponent parentComponent, AbstractCommand command, int level)
    {
        // filter out seperators
        if (command != null)
        {
            JButton button = new JButton();
            command.attach(button, commandButtonConfigurer);

            parentComponent.add(button);
            return button;
        }
        return null;
    }

    protected JComponent buildGroupComponent(JComponent parentComponent, CommandGroup command, int level)
    {
        // only add panels for the first level (level 0 = root, level > 1 should
        // never be reached. see #continueDeeper()
        if (level != 1)
            return parentComponent;

        JPanel panel = new JPanel();
        panel.setLayout(new PercentLayout(PercentLayout.VERTICAL, 0));
        panel.setOpaque(false);

        JOutlookBar outlookBar = (JOutlookBar) parentComponent;
        outlookBar.addTab(command.getText(), command.getIcon(), outlookBar.makeScrollPane(panel));

        return panel;
    }

    protected boolean continueDeeper(CommandGroup commandGroup, int level)
    {
        return (level < 2);
    }

}
