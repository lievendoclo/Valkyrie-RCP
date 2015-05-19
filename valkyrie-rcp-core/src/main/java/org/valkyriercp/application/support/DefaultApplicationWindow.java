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

import org.jdesktop.swingx.JXPanel;
import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.config.ApplicationConfig;

import javax.swing.*;
import java.awt.*;

public class DefaultApplicationWindow extends AbstractApplicationWindow {
    private JXPanel panel;

    public DefaultApplicationWindow(ApplicationConfig config) {
        super(config);
    }

    public DefaultApplicationWindow( int number,ApplicationConfig config ) {
        super(number, config);
    }

    protected JComponent createWindowContentPane(){
        panel = new JXPanel(new BorderLayout());
        return panel;
    }

    protected void setActivePage( ApplicationPage page ) {
        panel.removeAll();
        panel.add(page.getControl());
        panel.validate();
    }


}
