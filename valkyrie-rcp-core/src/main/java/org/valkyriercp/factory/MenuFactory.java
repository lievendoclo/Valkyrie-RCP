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
