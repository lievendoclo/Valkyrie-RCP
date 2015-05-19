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
package org.valkyriercp.text;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * A simple FocusListener that selects all the contents of a JTextComponent upon entering the component.
 *
 * @author Jan Hoskens
 */
public class SelectAllFocusListener implements FocusListener {

    private final JTextComponent textComponent;

    /**
     * Create a SelectAllFocusListener to select all text upon entering the given JTextComponent.
     *
     * @param textComponent the JTextComponent that needs the select all upon entering.
     */
    public SelectAllFocusListener(JTextComponent textComponent) {
        this.textComponent = textComponent;
    }

    /**
     * Select all text upon gaining focus.
     */
    public void focusGained(FocusEvent e) {
        textComponent.selectAll();
    }

    /**
     * Remove selection when focus is lost.
     */
    public void focusLost(FocusEvent e) {
        textComponent.select(0,0);
    }
}

