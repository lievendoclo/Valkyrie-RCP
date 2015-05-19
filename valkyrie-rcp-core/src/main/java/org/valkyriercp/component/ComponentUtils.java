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
package org.valkyriercp.component;

import org.valkyriercp.command.config.CommandButtonConfigurer;
import org.valkyriercp.command.config.CommandFaceDescriptor;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.factory.ButtonFactory;
import org.valkyriercp.factory.DefaultButtonFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ComponentUtils {
    public static JPanel createIconButtonPanel(List<? extends AbstractCommand> commands)
    {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ButtonFactory factory = new DefaultButtonFactory();
        CommandButtonConfigurer configurer = new CommandButtonConfigurer()
        {

            public void configure(AbstractButton button, AbstractCommand command,
                    CommandFaceDescriptor faceDescriptor)
            {
                faceDescriptor.configureIcon(button);
                button.setToolTipText(faceDescriptor.getCaption());
            }
        };
        for (AbstractCommand command : commands)
        {
                buttons.add(command.createButton(factory, configurer));
        }
        return buttons;
    }

    public static JComponent createDummyPanel(String vakske)
    {
        JPanel dummy = new JPanel();
        dummy.add(new JLabel(vakske));
        return dummy;
    }
}
