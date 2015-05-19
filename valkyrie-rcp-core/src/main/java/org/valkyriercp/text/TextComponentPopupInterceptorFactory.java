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

import org.valkyriercp.binding.form.CommitListener;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.value.CommitTrigger;
import org.valkyriercp.form.builder.AbstractFormComponentInterceptor;
import org.valkyriercp.form.builder.FormComponentInterceptor;
import org.valkyriercp.form.builder.FormComponentInterceptorFactory;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author oliverh
 */
public class TextComponentPopupInterceptorFactory implements FormComponentInterceptorFactory {

    public TextComponentPopupInterceptorFactory() {
    }

    public FormComponentInterceptor getInterceptor(FormModel formModel) {
        return new TextComponentPopupInterceptor(formModel);
    }

    private class TextComponentPopupInterceptor extends AbstractFormComponentInterceptor implements
            PropertyChangeListener, CommitListener {

        private CommitTrigger resetTrigger;

        protected TextComponentPopupInterceptor(FormModel formModel) {
            super(formModel);
        }

        public void processComponent(String propertyName, JComponent component) {
            JComponent innerComp = getInnerComponent(component);
            if (innerComp instanceof JTextComponent) {
                TextComponentPopup.attachPopup((JTextComponent)innerComp, getResetTrigger());
            }
            else if (innerComp instanceof TextComponentContainer) {
                TextComponentPopup.attachPopup(((TextComponentContainer)innerComp).getComponent(), getResetTrigger());
            }
        }

        private CommitTrigger getResetTrigger() {
            if (resetTrigger == null) {
                resetTrigger = new CommitTrigger();
                registerListeners();
            }
            return resetTrigger;
        }

        private void registerListeners() {
            FormModel formModel = getFormModel();
            formModel.addCommitListener(this);
            formModel.getFormObjectHolder().addValueChangeListener(this);
        }

        public void preCommit(FormModel formModel) {
            // do nothing
        }

        public void postCommit(FormModel formModel) {
            resetTrigger.commit();
        }

        public void propertyChange(PropertyChangeEvent evt) {
            resetTrigger.commit();
        }

    }
}
