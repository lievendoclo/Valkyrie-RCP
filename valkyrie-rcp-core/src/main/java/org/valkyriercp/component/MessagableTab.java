package org.valkyriercp.component;


import org.valkyriercp.core.Message;

/**
 * Set a message on a tab, coming from some source.
 */
public interface MessagableTab
{

    void setMessage(Object source, Message message, int tabIndex);
}
