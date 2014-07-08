package org.valkyriercp.component;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.builder.FormComponentInterceptor;
import org.valkyriercp.form.builder.FormComponentInterceptorFactory;

public class AutoCompletionInterceptorFactory implements FormComponentInterceptorFactory {
    private AutoCompletionProvider autoCompletionProvider;

    public AutoCompletionInterceptorFactory(AutoCompletionProvider autoCompletionProvider) {
        this.autoCompletionProvider = autoCompletionProvider;
    }

    @Override
    public FormComponentInterceptor getInterceptor(FormModel formModel) {
        return new AutoCompletionInterceptor(formModel, autoCompletionProvider);
    }
}
