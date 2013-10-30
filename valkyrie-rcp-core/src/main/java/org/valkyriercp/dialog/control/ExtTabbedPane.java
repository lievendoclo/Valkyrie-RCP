package org.valkyriercp.dialog.control;

import org.valkyriercp.factory.ControlFactory;
import org.valkyriercp.util.EventListenerListHelper;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper around <code>JTabbedPane</code>. When a <code>Tab</code> is made invisible, the tab is hidden from the
 * ui, and vice versa.
 * <p>
 * TODO: move this to another package?
 *
 * @author Peter De Bruycker
 */
public class ExtTabbedPane implements ControlFactory {

    private List tabs = new ArrayList();

    private JTabbedPane tabbedPane;

    private PropertyChangeListener propertyChangeHandler = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            Tab tab = (Tab) evt.getSource();
            if (tab.isVisible()) {
                int index = getUIIndex(tab);
                if (index >= 0) {
                    if (evt.getPropertyName().equals(Tab.TITLE_PROPERTY)) {
                        tabbedPane.setTitleAt(index, tab.getTitle());
                    }
                    if (evt.getPropertyName().equals(Tab.TOOLTIP_PROPERTY)) {
                        tabbedPane.setToolTipTextAt(index, tab.getTooltip());
                    }
                    if (evt.getPropertyName().equals(Tab.ICON_PROPERTY)) {
                        tabbedPane.setIconAt(index, tab.getIcon());
                    }
                    if (evt.getPropertyName().equals(Tab.COMPONENT_PROPERTY)) {
                        tabbedPane.setComponentAt(index, tab.getComponent());
                    }
                    if (evt.getPropertyName().equals(Tab.MNEMONIC_PROPERTY)) {
                        tabbedPane.setMnemonicAt(index, tab.getMnemonic());
                    }
                    if (evt.getPropertyName().equals(Tab.ENABLED_PROPERTY)) {
                        tabbedPane.setMnemonicAt(index, tab.getMnemonic());
                    }
                }
            }
            if (evt.getPropertyName().equals("visible")) {
                updateTabVisibility(tab);
            }
        }
    };

    private EventListenerListHelper changeListeners = new EventListenerListHelper(ChangeListener.class);

    public ExtTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // delegate change events to registered listeners
                changeListeners.fire("stateChanged", e);
            }
        });
    }

    public ExtTabbedPane() {
        this(new JTabbedPane());
    }

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }

    public JComponent getControl() {
        return tabbedPane;
    }

    public void addTab(Tab tab) {
        tabs.add(tab);
        if (tab.isVisible()) {
            tabbedPane.addTab(tab.getTitle(), tab.getIcon(), tab.getComponent(), tab.getTooltip());
        }
        tab.addPropertyChangeListener(propertyChangeHandler);
    }

    public Tab getTab(int index) {
        return (Tab) tabs.get(index);
    }

    public void removeTab(Tab tab) {
        removeTab(tabs.indexOf(tab));
    }

    public void addTab(int index, Tab tab) {
        tabs.add(index, tab);
        if (tab.isVisible()) {
            tabbedPane.insertTab(tab.getTitle(), tab.getIcon(), tab.getComponent(), tab.getTooltip(), index);
        }
        tab.addPropertyChangeListener(propertyChangeHandler);
    }

    public void removeTab(int index) {
        tabbedPane.removeTabAt(index);
        Tab tab = (Tab) tabs.remove(index);
        tab.removePropertyChangeListener(propertyChangeHandler);
    }

    public void selectTab(Tab tab) {
        if (tab.isVisible()) {
            int targetIndex = getUIIndex(tab);
            if (targetIndex >= 0) {
                tabbedPane.setSelectedIndex(targetIndex);
            }
        }
    }

    public int convertUIIndexToModelIndex(int index) {
        int modelIndex = index;
        for (int i = 0; i <= modelIndex; i++) {
            if (!getTab(i).isVisible()) {
                modelIndex++;
            }
        }

        return modelIndex;
    }

    public int getUIIndex(Tab tab) {
        return convertModelIndexToUIIndex(tabs.indexOf(tab));
    }

    public int convertModelIndexToUIIndex(int index) {
        int uiIndex = 0;

        for (int i = 0; i < index; i++) {
            if (getTab(i).isVisible()) {
                uiIndex++;
            }
        }

        return uiIndex;
    }

    private void updateTabVisibility(Tab tab) {
        if (tab.isVisible()) {
            tabbedPane.insertTab(tab.getTitle(), tab.getIcon(), tab.getComponent(), tab.getTooltip(), getUIIndex(tab));
        }
        else {
            tabbedPane.removeTabAt(getUIIndex(tab));

        }

        // because insert and remove don't change the selected tab index, we
        // trigger one manually
        ChangeEvent event = new ChangeEvent(tabbedPane);
        changeListeners.fire("stateChanged", event);
    }
}
