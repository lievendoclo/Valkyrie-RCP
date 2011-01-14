package org.valkyriercp.component;


/**
 * This interface is needed to link a MessagableTabbedPane to an overlay.
 */
public interface MayHaveMessagableTab
{
    void setMessagableTab(MessagableTab component, int tabIndex);
}
