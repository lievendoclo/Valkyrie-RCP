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
package org.valkyriercp.command.config;

import org.springframework.util.Assert;
import org.valkyriercp.command.support.AbstractCommand;

import javax.swing.*;

public class DefaultCommandButtonConfigurer implements CommandButtonConfigurer {

    /**
     * {@inheritDoc}
     */
    public void configure(AbstractButton button, AbstractCommand command, CommandFaceDescriptor faceDescriptor) {
        Assert.notNull(button, "The button to configure cannot be null.");
        Assert.notNull(faceDescriptor, "The command face descriptor cannot be null.");
        faceDescriptor.configureLabel(button);
        faceDescriptor.configureIcon(button);
        faceDescriptor.configureColor(button);
        button.setToolTipText(faceDescriptor.getCaption());
    }

}