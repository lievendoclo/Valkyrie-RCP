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
