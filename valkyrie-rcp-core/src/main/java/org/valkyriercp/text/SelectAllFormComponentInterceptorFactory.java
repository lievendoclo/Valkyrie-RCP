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
