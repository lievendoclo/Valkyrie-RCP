package org.valkyriercp.component;

import javax.swing.*;

/**
 * Separate listener because when format is changed during focus lost/gained,
 * ordinary DocumentListener fires events. This listener can be used to listen
 * to userInputEvents only.
 */
public interface UserInputListener {
    public void update(JComponent source);
}