package org.valkyriercp.form.binding.swing;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.BindingFactory;
import org.valkyriercp.form.binding.BindingFactoryProvider;

public class SwingBindingFactoryProvider implements BindingFactoryProvider {
    /**
     * Produce a BindingFactory using the provided form model.
     * @param formModel Form model on which to construct the BindingFactory
     * @return BindingFactory
     */
    public BindingFactory getBindingFactory(FormModel formModel) {
        return new SwingBindingFactory(formModel);
    }

}