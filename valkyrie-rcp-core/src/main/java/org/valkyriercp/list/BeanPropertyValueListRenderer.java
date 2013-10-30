package org.valkyriercp.list;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

/**
 * Renders objects in a list, using a propertyName for rendering.
 *
 * @author Keith Donald
 */
public class BeanPropertyValueListRenderer extends TextValueListRenderer {

    private BeanWrapperImpl beanWrapper;

    private String propertyName;

    /**
     * Creates a new <code>BeanPropertyValueListRenderer</code>
     *
     * @param propertyName
     *            the property name to use for rendering
     */
    public BeanPropertyValueListRenderer(String propertyName) {
        Assert.notNull(propertyName, "propertyName can not be null.");
        this.propertyName = propertyName;
    }

    protected String getTextValue(Object value) {
        if (value == null) {
            return "";
        }
        if (beanWrapper == null) {
            beanWrapper = new BeanWrapperImpl(value);
        } else {
            beanWrapper.setWrappedInstance(value);
        }
        return String.valueOf(beanWrapper.getPropertyValue(propertyName));
    }

    /**
     * @return the property name
     */
    public String getPropertyName() {
        return propertyName;
    }
}
