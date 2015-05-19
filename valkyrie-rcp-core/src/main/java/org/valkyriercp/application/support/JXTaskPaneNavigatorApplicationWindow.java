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

import net.miginfocom.swing.MigLayout;
import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.ApplicationWindowConfigurer;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.command.support.CommandGroup;

import javax.swing.*;
import java.awt.*;


public class JXTaskPaneNavigatorApplicationWindow extends DefaultApplicationWindow
{
//    private JSplitPane framedPage;
    private SplitPage framedPage;

    public JXTaskPaneNavigatorApplicationWindow(ApplicationConfig config) {
        super(config);
    }

    @Override
    protected JComponent createWindowContentPane()
    {
        CommandGroup navigationCommandGroup = getAdvisor()
                .getNavigationCommandGroup();
        JXTaskPaneNavigatorView taskPaneNavigatorView = new JXTaskPaneNavigatorView(navigationCommandGroup);

        JScrollPane leftComponent = new JScrollPane(taskPaneNavigatorView.getControl(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftComponent.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        framedPage = new SplitPage(leftComponent, null);

        return new JScrollPane(framedPage);
    }

    @Override
    protected void setActivePage(ApplicationPage page)
    {
        framedPage.setRightComponent(page.getControl());
        framedPage.revalidate();
    }

    protected void applyStandardLayout( JFrame windowControl, ApplicationWindowConfigurer configurer ) {
        windowControl.setTitle( configurer.getTitle() );
        windowControl.setIconImage( configurer.getImage() );
        if(getAdvisor().getMenuBarCommandGroup().getMemberCount() > 0)
            windowControl.setJMenuBar( createMenuBarControl() );
        windowControl.getContentPane().setLayout( new BorderLayout() );
        if(getAdvisor().getToolBarCommandGroup().getMemberCount() > 0)
            windowControl.getContentPane().add( createToolBarControl(), BorderLayout.NORTH );
        windowControl.getContentPane().add( createWindowContentPane() );
        windowControl.getContentPane().add( createStatusBarControl(), BorderLayout.SOUTH );
    }

    private class SplitPage extends JPanel
    {
        private JComponent leftComponent = new JPanel();
        private JComponent rightComponent = new JPanel();

        public SplitPage(JComponent leftComponent, JComponent rightComponent) {
            if(leftComponent != null)
                this.leftComponent = leftComponent;
            if(rightComponent != null)
                this.rightComponent = rightComponent;
            MigLayout migLayout = new MigLayout("fill, insets 0","[][fill]","[grow]");
            setLayout(migLayout);
            add(this.leftComponent, "growy,pushy");
            add(this.rightComponent, "push, grow");
        }

        public void setLeftComponent(JComponent leftComponent) {
            remove(this.leftComponent);
            this.leftComponent = leftComponent;
            add(this.leftComponent, "growy,pushy");
        }

        public void setRightComponent(JComponent rightComponent) {
            remove(this.rightComponent);
            this.rightComponent = rightComponent;
            add(this.rightComponent, "push, grow");
        }
    }
}

