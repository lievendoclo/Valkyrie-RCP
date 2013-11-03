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
