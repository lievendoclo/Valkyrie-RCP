package org.valkyriercp.form.binding.jide;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.BindingFactory;
import org.valkyriercp.form.binding.BindingFactoryProvider;

public class JideBindingFactoryProvider implements BindingFactoryProvider {
    @Override
    public BindingFactory getBindingFactory(FormModel formModel) {
        return new JideBindingFactory(formModel);
    }
}
