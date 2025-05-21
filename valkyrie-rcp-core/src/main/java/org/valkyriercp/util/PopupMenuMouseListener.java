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
package org.valkyriercp.util;

import org.springframework.util.Assert;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Convenient listener that will show a popup menu when it receives a popup
 * trigger.
 *
 * @author Keith Donald
 * @author Oliver Hutchison
 * @see java.awt.event.MouseEvent#isPopupTrigger
 */
public class PopupMenuMouseListener extends MouseAdapter {

    private JPopupMenu popupMenu;

    /**
     * Creates a new PopupMenuMouseListener.
     * <p>
     * NOTE: When using this constructor subclasses must overide one of the
     * getPopupMenu methods.
     */
    protected PopupMenuMouseListener() {
    }

    /**
     * Creates a new PopupMenuMouseListener that will show the provided popup.
     */
    public PopupMenuMouseListener(JPopupMenu popupMenu) {
        Assert.notNull(popupMenu, "popupMenu should not be null");
        this.popupMenu = popupMenu;
    }

    /**
     * Called before the popup menu becomes visible. May veto the display of the
     * popup by returning false.
     */
    protected boolean onAboutToShow(MouseEvent e) {
        return true;
    }

    /**
     * Called to display the popup menu.
     */
    protected void showPopupMenu(MouseEvent e) {
        if (onAboutToShow(e)) {
            JPopupMenu popupToShow = getPopupMenu(e);
            if (popupToShow == null) {
                return;
            }
            popupToShow.show(e.getComponent(), e.getX(), e.getY());
            popupToShow.setVisible(true);
        }
    }

    /**
     * Returns the popup menu to be displayed. Default implementation
     * delegates to {@link  #getPopupMenu()}.
     */
    protected JPopupMenu getPopupMenu(MouseEvent e) {
        return getPopupMenu();
    }

    /**
     * Returns the popup menu to be displayed.
     */
    protected JPopupMenu getPopupMenu() {
        if (popupMenu == null) {
            throw new UnsupportedOperationException(
                    "One of the getPopupMenu methods must be overridden when default constructor is used.");
        }
        return popupMenu;
    }

    public void mousePressed(MouseEvent e) {
        checkEvent(e);
    }

    public void mouseReleased(MouseEvent e) {
        checkEvent(e);
    }

    private void checkEvent(MouseEvent e) {
        if (e.isPopupTrigger()) {
            showPopupMenu(e);
        }
    }
}