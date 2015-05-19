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

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.FormComponentInterceptor;
import org.valkyriercp.form.builder.FormComponentInterceptorFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class TextCaretFormComponentInterceptorFactory implements FormComponentInterceptorFactory {

    /**
     * Create a new <code>TextCaretFixerComponentInterceptor</code> instance
     *
     * @return the interceptor
     */
    public FormComponentInterceptor getInterceptor( FormModel formModel ) {
        return new TextCaretComponentInterceptor();
    }

    /**
     * The <code>FormComponentInterceptor</code> implementation.
     */
    public class TextCaretComponentInterceptor extends TextComponentInterceptor {
        protected void processComponent( String propertyName, final JTextComponent textComponent ) {
            textComponent.getDocument().addDocumentListener( new DocumentHandler( textComponent ) );
        }
    }

    private static final class DocumentHandler implements DocumentListener {
        private JTextComponent component;

        private DocumentHandler( JTextComponent component ) {
            this.component = component;
        }

        public void removeUpdate( DocumentEvent e ) {
            fixCaret();
        }

        public void insertUpdate( DocumentEvent e ) {
            fixCaret();
        }

        public void changedUpdate( DocumentEvent e ) {
            fixCaret();
        }

        private void fixCaret() {
            if( !component.hasFocus() ) {
                // need to invoke later, as the text change also changes the caret
                // position
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        component.setCaretPosition(0);
                    }
                });
            }
        }
    }
}
