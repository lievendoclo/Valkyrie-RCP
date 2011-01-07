package org.valkyriercp.factory;

import javax.swing.*;

/**
 * @author Keith Donald
 */
public class DefaultMenuFactory implements MenuFactory {

    public JMenu createMenu() {
        return new JMenu();
    }

    public JMenuItem createMenuItem() {
        return new JMenuItem();
    }

    public JCheckBoxMenuItem createCheckBoxMenuItem() {
        return new JCheckBoxMenuItem();
    }

    public JRadioButtonMenuItem createRadioButtonMenuItem() {
        return new JRadioButtonMenuItem();
    }

    public JPopupMenu createPopupMenu() {
        return new JPopupMenu();
    }

    public JMenuBar createMenuBar() {
        return new JMenuBar();
    }

}