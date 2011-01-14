package org.valkyriercp.form.binding;

import org.valkyriercp.binding.form.FormModel;

/**
 * Implementations provide {@link BindingFactory} instances on demand.
 * @author Larry Streepy
 */
public interface BindingFactoryProvider {

    /**
     * Produce a BindingFactory using the provided form model.
     * @param formModel Form model on which to construct the BindingFactory
     * @return BindingFactory
     */
    BindingFactory getBindingFactory(FormModel formModel);
}

