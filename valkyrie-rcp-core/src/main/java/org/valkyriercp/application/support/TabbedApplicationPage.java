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

import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageLayoutBuilder;
import org.valkyriercp.util.PopupMenuMouseListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <code>ApplicationPage</code> implementation that puts the <code>PageComponent</code>s in a <code>JTabbedPane</code>.
 *
 * @author Peter De Bruycker
 */
public class TabbedApplicationPage extends AbstractApplicationPage implements PageLayoutBuilder {

    private JTabbedPane tabbedPane;
    private int tabPlacement = -1;
    private int tabLayoutPolicy = -1;

    private boolean addingComponent;

    protected JComponent createControl() {
        tabbedPane = new JTabbedPane();
        if (tabPlacement != -1) {
            tabbedPane.setTabPlacement(tabPlacement);
        }
        if (tabLayoutPolicy != -1) {
            tabbedPane.setTabLayoutPolicy(tabLayoutPolicy);
        }

        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // if we're adding a component, ignore change of active component
                if (!addingComponent && tabbedPane.getSelectedIndex() >= 0) {
                    setActiveComponent(getComponent(tabbedPane.getSelectedIndex()));
                }
            }
        });
        JPopupMenu popup = new JPopupMenu();
        JMenuItem close = new JMenuItem("Close");
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close(getComponent(tabbedPane.getSelectedIndex()));
            }
        });
        popup.add(close);
        tabbedPane.addMouseListener(new PopupMenuMouseListener(popup));

        this.getPageDescriptor().buildInitialLayout(this);

        return tabbedPane;
    }

    protected void updatePageComponentProperties(PageComponent pageComponent) {
        int index = indexOf(pageComponent);

        tabbedPane.setIconAt(index, pageComponent.getIcon());
        tabbedPane.setTitleAt(index, pageComponent.getDisplayName());
        tabbedPane.setToolTipTextAt(index, pageComponent.getCaption());
    }

    public void addView(String viewDescriptorId) {
        showView(viewDescriptorId);
    }

    protected void doAddPageComponent(PageComponent pageComponent) {
        try {
            addingComponent = true;
            tabbedPane.addTab(pageComponent.getDisplayName(), pageComponent.getIcon(), pageComponent.getContext()
                    .getPane().getPageComponent().getControl(), pageComponent.getCaption());
        } finally {
            addingComponent = false;
        }
    }

    protected void doRemovePageComponent(PageComponent pageComponent) {
        tabbedPane.removeTabAt(indexOf(pageComponent));
    }

    protected boolean giveFocusTo(PageComponent pageComponent) {
        int componentIndex = indexOf(pageComponent);
        if (componentIndex < 0) {
            return false;
        }

        tabbedPane.setSelectedIndex(componentIndex);
        return true;
    }

    private int indexOf(PageComponent component) {
        return getPageComponents().indexOf(component);
    }

    private PageComponent getComponent(int index) {
        return (PageComponent) getPageComponents().get(index);
    }

    public void setTabPlacement(int tabPlacement) {
        this.tabPlacement = tabPlacement;
    }

    public void setTabLayoutPolicy(int tabLayoutPolicy) {
        this.tabLayoutPolicy = tabLayoutPolicy;
    }
}

