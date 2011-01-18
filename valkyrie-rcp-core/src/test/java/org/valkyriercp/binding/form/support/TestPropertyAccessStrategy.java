package org.valkyriercp.binding.form.support;

import org.springframework.beans.BeansException;
import org.valkyriercp.binding.support.BeanPropertyAccessStrategy;
import org.valkyriercp.binding.value.ValueModel;

public class TestPropertyAccessStrategy extends BeanPropertyAccessStrategy {

    private int numValueModelRequests;
    private String lastRequestedValueModel;

    public TestPropertyAccessStrategy(Object bean) {
        super(bean);
    }

    public ValueModel getPropertyValueModel(String propertyPath) throws BeansException {
        numValueModelRequests++;
        lastRequestedValueModel = propertyPath;
        return super.getPropertyValueModel(propertyPath);
    }

    public int numValueModelRequests() {
        return numValueModelRequests;
    }

    public String lastRequestedValueModel() {
        return lastRequestedValueModel;
    }
}
