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
package org.valkyriercp.list;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Provides auto-completion for an editable combobox. Based on public domain postings.
 * Original author unknown.  Also copied some code from {@link ComboBoxAutoCompletion}
 * to deal with focus loss.
 *
 * @author Larry Streepy
 *
 */
public class EditableComboBoxAutoCompletion extends KeyAdapter {

    private final FocusHandler focusHandler = new FocusHandler();

    private final JComboBox comboBox;

    private final JTextField editor;

    /**
     * Adds autocompletion support to the given <code>combobox</code>.
     *
     * @param comboBox the combobox to augment
     */
    public EditableComboBoxAutoCompletion(JComboBox comboBox) {
        this.comboBox = comboBox;
        editor = (JTextField)comboBox.getEditor().getEditorComponent();
        editor.addKeyListener(this);
        editor.addFocusListener(focusHandler);
    }

    /**
     * Handle a key release event. See if what they've type so far matches anything in the
     * selectable items list. If so, then show the popup and select the item. If not, then
     * hide the popup.
     *
     * @param e key event
     */
    public void keyReleased(KeyEvent e) {
        char ch = e.getKeyChar();
        if (ch == KeyEvent.CHAR_UNDEFINED || Character.isISOControl(ch))
            return;
        int pos = editor.getCaretPosition();
        String str = editor.getText();
        if (str.length() == 0)
            return;

        boolean matchFound = false;
        for (int k = 0; k < comboBox.getItemCount(); k++) {
            String item = comboBox.getItemAt(k).toString();
            if (startsWithIgnoreCase(item, str)) {
                comboBox.setSelectedIndex(k);
                editor.setText(item);
                editor.setCaretPosition(item.length());
                editor.moveCaretPosition(pos);

                // show popup when the user types
                if (comboBox.isDisplayable())
                    comboBox.setPopupVisible(true);

                matchFound = true;
                break;
            }
        }
        if (!matchFound) {
            // hide popup when there is no match
            comboBox.setPopupVisible(false);
        }
    }

    /**
     * See if one string begins with another, ignoring case.
     *
     * @param str1 The string to test
     * @param str2 The prefix to test for
     * @return true if str1 starts with str2, ingnoring case
     */
    private boolean startsWithIgnoreCase(String str1, String str2) {
        return str1 != null && str2 != null && str1.toUpperCase().startsWith(str2.toUpperCase());
    }

    /**
     * Highlight the text from the given start location to the end of the text.
     *
     * @param start Starting location to highlight
     */
    private void highlightText(int start) {
        editor.setCaretPosition(editor.getText().length());
        editor.moveCaretPosition(start);
    }

    /**
     * This class handles focus events to provide a work-around for a java 1.5 bug.
     */
    private final class FocusHandler implements FocusListener {

        // Bug 5100422 on Java 1.5: Editable JComboBox won't hide popup when
        // tabbing out
        private boolean hidePopupOnFocusLoss = System.getProperty("java.version").startsWith("1.5");

        public void focusGained(FocusEvent e) {
            // Highlight whole text when gaining focus
            highlightText(0);
        }

        public void focusLost(FocusEvent e) {
            // Workaround for Bug 5100422 - Hide Popup on focus loss
            if (hidePopupOnFocusLoss)
                comboBox.setPopupVisible(false);
        }
    }
}

