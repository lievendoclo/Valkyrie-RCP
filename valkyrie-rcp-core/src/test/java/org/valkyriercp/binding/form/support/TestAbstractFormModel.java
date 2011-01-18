package org.valkyriercp.binding.form.support;

import org.valkyriercp.binding.MutablePropertyAccessStrategy;
import org.valkyriercp.binding.value.ValueModel;

public class TestAbstractFormModel extends AbstractFormModel {

    int preProcessCalls;

    int postProcessCalls;

    public TestAbstractFormModel(Object formObject) {
        super(formObject);
    }

    public TestAbstractFormModel(MutablePropertyAccessStrategy propertyAccessStrategy, boolean buffered) {
        super(propertyAccessStrategy, buffered);
    }

    public TestAbstractFormModel(ValueModel formObjectHolder, boolean buffering) {
        super(formObjectHolder, buffering);
    }

    protected ValueModel preProcessNewValueModel(String domainObjectProperty, ValueModel formValueModel) {
        ++preProcessCalls;
        return formValueModel;
    }

    protected void postProcessNewValueModel(String domainObjectProperty, ValueModel valueModel) {
        ++postProcessCalls;
    }

    protected ValueModel preProcessNewConvertingValueModel(String formProperty, Class targetClass,
            ValueModel formValueModel) {
        ++preProcessCalls;
        return formValueModel;
    }

    protected void postProcessNewConvertingValueModel(String formProperty, Class targetClass, ValueModel valueModel) {
        ++postProcessCalls;
    }
}