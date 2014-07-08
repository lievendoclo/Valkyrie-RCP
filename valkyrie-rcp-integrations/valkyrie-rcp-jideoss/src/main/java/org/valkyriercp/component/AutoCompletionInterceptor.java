package org.valkyriercp.component;

import com.jidesoft.swing.AutoCompletion;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.AbstractFormComponentInterceptor;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.List;

public class AutoCompletionInterceptor extends AbstractFormComponentInterceptor {
    private AutoCompletionProvider autoCompletionProvider;

    public AutoCompletionInterceptor(FormModel formModel, AutoCompletionProvider autoCompletionProvider) {
        super(formModel);
        this.autoCompletionProvider = autoCompletionProvider;
    }

    @Override
    public void processComponent(String propertyName, JComponent component) {
        if(component instanceof JTextComponent) {
            JTextComponent comp = (JTextComponent) component;
            new AutoCompletion(comp, getAutoCompletionList(propertyName));
        }

        super.processComponent(propertyName, component);
    }

    private List getAutoCompletionList(String propertyName) {
        return autoCompletionProvider.getAutoCompletionOptions(getFormModel().getId(), propertyName);
    }
}
