package org.valkyriercp.component;

import org.valkyriercp.core.Messagable;
import org.valkyriercp.core.Message;
import org.valkyriercp.factory.ControlFactory;

public interface MessagePane extends ControlFactory, Messagable {

    public Message getMessage();

    /**
     * Is this pane currently showing a message?
     *
     * @return true or false
     */
    public boolean isMessageShowing();

}