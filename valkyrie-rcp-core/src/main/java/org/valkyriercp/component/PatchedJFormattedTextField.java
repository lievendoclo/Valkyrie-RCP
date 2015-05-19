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
package org.valkyriercp.component;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.Format;

public class PatchedJFormattedTextField extends JFormattedTextField {

    private static final String TOGGLE_OVERWRITE_MODE_ACTION = "toggleOverwriteModeAction";

    public PatchedJFormattedTextField() {
        super();
        customInit();
    }

    public PatchedJFormattedTextField(Object value) {
        super(value);
        customInit();
    }

    public PatchedJFormattedTextField(Format format) {
        super(format);
        customInit();
    }

    public PatchedJFormattedTextField(AbstractFormatter formatter) {
        super(formatter);
        customInit();
    }

    public PatchedJFormattedTextField(AbstractFormatterFactory factory) {
        super(factory);
        customInit();
    }

    public PatchedJFormattedTextField(AbstractFormatterFactory factory, Object currentValue) {
        super(factory, currentValue);
        customInit();
    }

    private void customInit() {
        setFocusLostBehavior(COMMIT);
        if (getFormatter() instanceof DefaultFormatter) {
            final DefaultFormatter d = (DefaultFormatter)getFormatter();
            AbstractAction toggleOverwrite = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    d.setOverwriteMode(!(d.getOverwriteMode()));
                }
            };
            getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0), TOGGLE_OVERWRITE_MODE_ACTION);
            getActionMap().put(TOGGLE_OVERWRITE_MODE_ACTION, toggleOverwrite);
        }
    }

    /**
     * Overiding this method prevents the TextField from intercepting the Enter
     * Key when focus is not on it. This allows default buttons to function.
     * This should be removed when Swing fixes their bug.
     *
     * @see javax.swing.JComponent#processKeyBinding(javax.swing.KeyStroke,
     *      java.awt.event.KeyEvent, int, boolean)
     */
    public boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        if (ks == KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0) || ks == KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)) {
            return false;
        }
        return super.processKeyBinding(ks, e, condition, pressed);
    }
}

