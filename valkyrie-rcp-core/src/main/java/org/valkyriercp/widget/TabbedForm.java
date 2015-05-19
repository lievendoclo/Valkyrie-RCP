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

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.form.NewFormObjectAware;
import org.valkyriercp.binding.form.VetoableCommitListener;
import org.valkyriercp.component.MessagableTabbedPane;
import org.valkyriercp.component.SkipComponentsFocusTraversalPolicy;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class TabbedForm extends AbstractFocussableWidgetForm implements ChangeListener, NewFormObjectAware
{

    private List<VetoableCommitListener> vetoableCommitListeners;

    private JTabbedPane tabbedPane = null;

    protected TabbedForm() {
    }

    protected TabbedForm(String id) {
        super(id);
    }

    protected JTabbedPane getTabbedPane()
    {
        return tabbedPane;
    }

    protected final JComponent createFormControl()
    {
        tabbedPane = new MessagableTabbedPane(SwingConstants.TOP);
        tabbedPane.setFocusTraversalPolicyProvider(true);
        tabbedPane
                .setFocusTraversalPolicy(SkipComponentsFocusTraversalPolicy.skipJTextComponentTraversalPolicy);
        for (TabbedForm.Tab tab : getTabs())
        {
            tab.setParent(tabbedPane);
        }
        tabbedPane.addChangeListener(this);
        return tabbedPane;
    }

    public JComponent getRevertComponent()
    {
        return getRevertCommand().createButton();
    }

    public void setFormObject(Object formObject)
    {
        if (formObject == null)
            selectTab(0);
        super.setFormObject(formObject);
    }

    public void setNewFormObject(Object formObject)
    {
        if (formObject != null)
        {
            super.setFormObject(formObject);
        }
        else
        {
            getNewFormObjectCommand().execute();
        }
        selectTab(0);
    }

    public void selectTab(int tabIndex)
    {
        if ((tabbedPane != null) && (tabbedPane.getTabCount() > tabIndex))
            tabbedPane.setSelectedIndex(tabIndex);
    }

    public void selectTab(Tab tab)
    {
        if (tab.getTabIndex() > 0)
        {
            tabbedPane.setSelectedIndex(tab.getTabIndex());
        }
    }

    protected abstract Tab[] getTabs();

    protected class Tab
    {

        private final String tabId;

        private final String title;

        private final JComponent panel;

        private FocusTraversalPolicy focusTraversalPolicy;

        private JTabbedPane parentPane;

        private int tabIndex = -1;

        private boolean enabled = true;

        private boolean visible = true;

        public Tab(String tabId, JComponent panel)
        {
            Assert.notNull(panel);
            this.tabId = tabId;
            this.title = getApplicationConfig().messageResolver().getMessage(getId(), this.tabId, "title");
            this.panel = panel;
            this.panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        /**
         * Set parent for overlays and enabling
         *
         * @param parentPane
         */
        protected void setParent(JTabbedPane parentPane)
        {
            this.parentPane = parentPane;
            if (this.parentPane != null)
                setVisible(visible);
        }

        public void setVisible(boolean visible)
        {
            if (parentPane != null)
            {
                if (visible)
                {
                    parentPane.addTab(title, panel);
                    tabIndex = parentPane.indexOfComponent(panel);
                    parentPane.setEnabledAt(tabIndex, isEnabled());
                }
                else
                {
                    parentPane.remove(panel);
                    tabIndex = -1;
                }
            }
            this.visible = visible;
        }

        public void setEnabled(boolean enabled)
        {
            if ((parentPane != null) && (tabIndex > -1))
                parentPane.setEnabledAt(tabIndex, enabled);

            this.enabled = enabled;
        }

        /**
         * Gets the index of the tab on the tabbedpane
         *
         * @return index of the tab, -1 if not visible
         */
        public int getTabIndex()
        {
            return tabIndex;
        }

        public boolean isEnabled()
        {
            return this.enabled;
        }

        public void setMarked(boolean enable)
        {
            Icon icon = getApplicationConfig().iconSource().getIcon(tabId + ".icon");
            if ((parentPane != null) && (tabIndex > -1))
                parentPane.setIconAt(getTabIndex(), enable ? icon : null);

        }

        public void setFocusTraversalPolicy(FocusTraversalPolicy focusTraversalPolicy)
        {
            this.focusTraversalPolicy = focusTraversalPolicy;
            panel.setFocusTraversalPolicy(this.focusTraversalPolicy);
            panel.setFocusTraversalPolicyProvider(this.focusTraversalPolicy == null ? false : true);
        }
    }

    @Override
    public void commit()
    {
        FormModel formModel = getFormModel();
        if (vetoableCommitListeners != null)
        {
            for (VetoableCommitListener v : vetoableCommitListeners)
            {
                if (!v.proceedWithCommit(formModel))
                    return;
            }
        }
        super.commit();
    }

    public void stateChanged(ChangeEvent e)
    {
    }

    /**
     * Adding a vetoableCommitListener might prevent a formModel.commit() but this is not the correct location
     * to add back-end logic to check for a consistent formObject. Besides this the vetoableCommitListener
     * doesn't add any other real advantage for our case. Therefor deprecating to prevent wrong usage.
     */
    @Deprecated
    public void addVetoableCommitListener(VetoableCommitListener vetoableCommitListener)
    {
        if (vetoableCommitListeners == null)
            vetoableCommitListeners = new ArrayList<VetoableCommitListener>(5);
        vetoableCommitListeners.add(vetoableCommitListener);
    }

    @Deprecated
    public void removeVetoableCommitListener(VetoableCommitListener vetoableCommitListener)
    {
        if (vetoableCommitListeners != null)
            vetoableCommitListeners.remove(vetoableCommitListener);
    }
}
