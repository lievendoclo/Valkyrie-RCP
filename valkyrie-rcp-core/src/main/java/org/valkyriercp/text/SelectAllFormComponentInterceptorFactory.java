package org.valkyriercp.text;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.FormComponentInterceptor;
import org.valkyriercp.form.builder.FormComponentInterceptorFactory;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Implements "select all" behaviour for form components. If the form component is a text
 * field, or a spinner, the contents of the component are selected if it receives focus.
 *
 * @author Peter De Bruycker
 */
public class SelectAllFormComponentInterceptorFactory implements FormComponentInterceptorFactory {

    public FormComponentInterceptor getInterceptor( FormModel formModel ) {
        return new SelectAllFormComponentInterceptor();
    }

    public class SelectAllFormComponentInterceptor extends TextComponentInterceptor {
        private FocusListener selector = new FocusAdapter() {

            public void focusGained( FocusEvent e ) {
                if( !e.isTemporary() ) {
                    final JTextComponent textComponent = (JTextComponent) e.getComponent();
                    // using invokeLater as fix for bug 4740public class ChainedInterceptorFactory {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            textComponent.selectAll();
                        }
                    });
                }
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                if( !e.isTemporary() ) {
                    final JTextComponent textComponent = (JTextComponent) e.getComponent();
                    SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            textComponent.select(0,0);
                        }
                    } );
                }
            }
        };

        protected void processComponent( String propertyName, JTextComponent textComponent ) {
            textComponent.addFocusListener( selector );
        }
    }

}
