package org.valkyriercp.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.valkyriercp.core.Message;
import org.valkyriercp.core.Severity;
import org.valkyriercp.image.IconSource;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Jan Hoskens
 * @author Geoffrey De Smet
 */
@Configurable
public class MessagableTabbedPane extends JTabbedPane implements MessagableTab {

    private List<TabMetaData> tabMetaDatas = new ArrayList<TabMetaData>();

    @Autowired
    private IconSource iconSource;

    public MessagableTabbedPane() {
        super();
    }

    public MessagableTabbedPane(int tabPlacement) {
        super(tabPlacement);
    }

    public MessagableTabbedPane(int tabPlacement, int tabPolicy) {
        super(tabPlacement, tabPolicy);
    }

    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip, index);
        tabMetaDatas.add(index, new TabMetaData());
    }

    @Override
    public void setIconAt(int index, Icon icon) {
        // Hack to allow the error icon to overwrite the real icon
        TabMetaData tabMetaData = tabMetaDatas.get(index);
        tabMetaData.setIcon(icon);
        if (!tabMetaData.hasErrors()) {
            super.setIconAt(index, icon);
        }
    }

    @Override
    public void setToolTipTextAt(int index, String toolTipText) {
        // Hack to allow the error toolTipText to overwrite the real toolTipText
        TabMetaData tabMetaData = tabMetaDatas.get(index);
        tabMetaData.setToolTipText(toolTipText);
        if (!tabMetaData.hasErrors()) {
            super.setToolTipTextAt(index, toolTipText);
        }
    }

    public void setMessage(Object source, Message message, int index) {
        TabMetaData tabMetaData = tabMetaDatas.get(index);
        tabMetaData.put(source, message);
        if (tabMetaData.hasErrors()) {
            // Calling super to avoid the error icon/toolTipText overwrite hack
            super.setIconAt(index, loadIcon(Severity.ERROR.getLabel()));
            super.setToolTipTextAt(index, tabMetaData.getFirstErrorMessage());
        } else {
            // Calling super to avoid the error icon/toolTipText overwrite hack
            super.setIconAt(index, tabMetaData.getIcon());
            super.setToolTipTextAt(index, tabMetaData.getToolTipText());
        }
    }

    private Icon loadIcon(String severityLabel) {
        return iconSource.getIcon("severity." + severityLabel + ".overlay");
    }

    private static class TabMetaData {

        private Map<Object, Message> messageMap = new HashMap<Object, Message>();
        private Stack<Message> errorMessageStack = new Stack<Message>();

        private Icon icon = null;
        private String toolTipText = null;

        public void put(Object key, Message message) {
            Message oldMessage = messageMap.get(key);
            if (oldMessage != message) {
                // Update errorMessageStack
                if ((oldMessage != null) && (oldMessage.getSeverity() == Severity.ERROR)) {
                    errorMessageStack.remove(oldMessage);
                }
                if ((message != null) && (message.getSeverity() == Severity.ERROR)) {
                    errorMessageStack.add(message);
                }
                // Update messageMap
                if (message != null) {
                    messageMap.put(key, message);
                } else {
                    messageMap.remove(key);
                }
            }
        }

        public Message get(Object key) {
            return messageMap.get(key);
        }

        public String getFirstErrorMessage() {
            if (!hasErrors()) {
                return null;
            }
            return errorMessageStack.firstElement().getMessage();
        }

        public boolean hasErrors() {
            return errorMessageStack.size() > 0;
        }

        public Icon getIcon() {
            return icon;
        }

        public void setIcon(Icon icon) {
            this.icon = icon;
        }

        public String getToolTipText() {
            return toolTipText;
        }

        public void setToolTipText(String toolTipText) {
            this.toolTipText = toolTipText;
        }
    }

}

