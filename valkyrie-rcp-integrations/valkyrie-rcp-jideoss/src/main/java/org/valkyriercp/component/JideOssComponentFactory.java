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

import com.jidesoft.swing.JideTabbedPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.valkyriercp.factory.ComponentFactoryDecorator;
import org.valkyriercp.factory.DefaultComponentFactory;
import org.valkyriercp.image.ImageSource;
import org.valkyriercp.util.SwingUtils;

import javax.swing.*;
import java.awt.*;

public class JideOssComponentFactory extends ComponentFactoryDecorator {

    /**
     * The key of the jide tabbed pane tab trailing image in the image source.
     */
    private static final String JIDE_TABBED_PANE_TAB_TRAILING_IMAGE = "jideTabbedPane.tabTrailingImage";

    @Autowired
    private ImageSource imageSource;

    /**
     * Creates de Jide OSS component factory.
     */
    public JideOssComponentFactory() {

        super(new DefaultComponentFactory());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JTabbedPane createTabbedPane() {

        final JideTabbedPane tabbedPane = new JideTabbedPane();

        tabbedPane.setShowTabButtons(Boolean.TRUE);
        tabbedPane.setHideOneTab(Boolean.FALSE);
        tabbedPane.setShowTabArea(Boolean.TRUE);
        tabbedPane.setShowTabContent(Boolean.TRUE);
        tabbedPane.setUseDefaultShowIconsOnTab(Boolean.FALSE);
        tabbedPane.setShowIconsOnTab(Boolean.TRUE);
        tabbedPane.setBoldActiveTab(Boolean.TRUE);
        tabbedPane.setScrollSelectedTabOnWheel(Boolean.TRUE);
        tabbedPane.setShowCloseButton(Boolean.FALSE);
        tabbedPane.setUseDefaultShowCloseButtonOnTab(Boolean.FALSE);
        tabbedPane.setShowCloseButtonOnTab(Boolean.TRUE);
        tabbedPane.setTabEditingAllowed(Boolean.FALSE);

        // Install tab trailing component
        final Image image = imageSource.getImage(JideOssComponentFactory.JIDE_TABBED_PANE_TAB_TRAILING_IMAGE);
        tabbedPane.setTabTrailingComponent(SwingUtils.generateComponent(image));

        return tabbedPane;
    }
}