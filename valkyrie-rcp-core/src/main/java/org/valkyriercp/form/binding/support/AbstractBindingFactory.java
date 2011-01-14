package org.valkyriercp.form.binding.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.BinderSelectionStrategy;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.BindingFactory;
import org.valkyriercp.form.builder.FormComponentInterceptor;
import org.valkyriercp.form.builder.FormComponentInterceptorFactory;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base implementation of <code>BindingFactory</code>.
 *
 * @author Oliver Hutchison
 */
@Configurable
public abstract class AbstractBindingFactory implements BindingFactory {

    @Autowired
    private BinderSelectionStrategy binderSelectionStrategy;

    private FormModel formModel;

    private FormComponentInterceptor interceptor;

    @Autowired
    private ApplicationConfig applicationConfig;

    protected AbstractBindingFactory(FormModel formModel) {
        Assert.notNull(formModel, "formModel can not be null.");
        this.formModel = formModel;
    }

    @PostConstruct
    private void postConstruct() {
        FormComponentInterceptorFactory factory = applicationConfig.formComponentInterceptorFactory();
        interceptor = factory.getInterceptor(formModel);
    }

    public Binding createBinding(String formPropertyPath) {
        return createBinding(formPropertyPath, Collections.EMPTY_MAP);
    }

    public Binding createBinding(Class controlType, String formPropertyPath) {
        return createBinding(controlType, formPropertyPath, Collections.EMPTY_MAP);
    }

    public Binding bindControl(JComponent control, String formPropertyPath) {
        return bindControl(control, formPropertyPath, Collections.EMPTY_MAP);
    }

    public Binding createBinding(String formPropertyPath, Map context) {
        Assert.notNull(context, "Context must not be null");
        Binder binder = getBinderSelectionStrategy().selectBinder(formModel, formPropertyPath);
        Binding binding = binder.bind(formModel, formPropertyPath, context);
        interceptBinding(binding);
        return binding;
    }

    public Binding createBinding(Class controlType, String formPropertyPath, Map context) {
        Assert.notNull(context, "Context must not be null");
        Binder binder = getBinderSelectionStrategy().selectBinder(controlType, formModel, formPropertyPath);
        Binding binding =  binder.bind(formModel, formPropertyPath, context);
        interceptBinding(binding);
        return binding;
    }

    public Binding bindControl(JComponent control, String formPropertyPath, Map context) {
        Assert.notNull(context, "Context must not be null");
        Binder binder = getBinderSelectionStrategy().selectBinder(control.getClass(), formModel, formPropertyPath);
        Binding binding =  binder.bind(control, formModel, formPropertyPath, context);
        interceptBinding(binding);
        return binding;
    }

    public void interceptBinding(Binding binding) {
        if (interceptor != null) {
            interceptor.processComponent(binding.getProperty(), binding.getControl());
        }
    }

    public FormModel getFormModel() {
        return formModel;
    }

    protected BinderSelectionStrategy getBinderSelectionStrategy() {
        return binderSelectionStrategy;
    }

    public void setBinderSelectionStrategy(BinderSelectionStrategy binderSelectionStrategy) {
        this.binderSelectionStrategy = binderSelectionStrategy;
    }

    protected Map createContext(String key, Object value) {
        Map context = new HashMap(4);
        context.put(key, value);
        return context;
    }
}