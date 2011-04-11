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
