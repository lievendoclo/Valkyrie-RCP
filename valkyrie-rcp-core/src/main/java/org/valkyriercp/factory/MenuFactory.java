package org.valkyriercp.factory;

import javax.swing.*;

public interface MenuFactory {
    /**
     * Create a menu.
     */
    public JMenu createMenu();

    /**
     * Create a menu item.
     */
    public JMenuItem createMenuItem();

    /**
     * Create a menu item with a checkbox LaF.
     */
    public JCheckBoxMenuItem createCheckBoxMenuItem();

    /**
     * Create a menu item with a radio button LaF.
     */
    public JRadioButtonMenuItem createRadioButtonMenuItem();

    /**
     * Create a popup menu most commonly used when with the mouse.
     */
    public JPopupMenu createPopupMenu();

    /**
     * Create a menu bar.
     */
    public JMenuBar createMenuBar();
}
