package org.valkyriercp.binding.value.support;

import org.valkyriercp.binding.MutablePropertyAccessStrategy;
import org.valkyriercp.binding.support.BeanPropertyAccessStrategy;
import org.valkyriercp.binding.value.ValueModel;

/**
 * Adapts a value model to JavaBean's property.
 *
 * @author Oliver Hutchison
 */
public class PropertyAdapter extends AbstractValueModelAdapter {

    private final ValueModel propertValueModel;

    public PropertyAdapter(ValueModel valueModel, Object bean, String property) {
        this(valueModel, new BeanPropertyAccessStrategy(bean), property);
    }

    public PropertyAdapter(ValueModel valueModel, MutablePropertyAccessStrategy propertyAccessStrategy, String property) {
        super(valueModel);
        this.propertValueModel = propertyAccessStrategy.getPropertyValueModel(property);
        initalizeAdaptedValue();
    }

    protected void valueModelValueChanged(Object newValue) {
        propertValueModel.setValue(newValue);
    }
}
