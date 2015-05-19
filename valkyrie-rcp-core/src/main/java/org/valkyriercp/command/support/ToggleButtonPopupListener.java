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
package org.valkyriercp.command.support;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Attaches a {@link javax.swing.JPopupMenu} to a button.
 *
 * @author Keith Donald
 */
public class ToggleButtonPopupListener implements PopupMenuListener, ItemListener, MouseListener {
    private AbstractButton button;

    private JPopupMenu menu;

    private boolean buttonWasPressed;

    /**
     * Attach the menu to the button with a {@link ToggleButtonPopupListener}.
     *
     * @param button Button the menu should be attached to.
     * @param menu The menu to attach to the button.
     */
    public static void bind(AbstractButton button, JPopupMenu menu) {
        new ToggleButtonPopupListener(button, menu);
    }

    private ToggleButtonPopupListener(AbstractButton button, JPopupMenu menu) {
        this.button = button;
        this.menu = menu;
        button.addItemListener(this);
        menu.addPopupMenuListener(this);
        button.addMouseListener(this);
    }

    public void itemStateChanged(ItemEvent e) {
        if (button.isSelected()) {
            menu.show(button, 0, button.getHeight());
        }
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        if (!buttonWasPressed) {
            button.setSelected(false);
        }
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

    }

    public void popupMenuCanceled(PopupMenuEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        buttonWasPressed = true;
    }

    public void mouseReleased(MouseEvent e) {
        buttonWasPressed = false;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
