package org.valkyriercp.binding.form.support;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * Listener to register on a property of a bean which has a nested property shown in the JTextField. If the
 * listener is registered on property X of a bean and the nestedProperty Y of X is delivered with the
 * constructor, the given textfield will be updated when X changes.
 *
 * @author Jan Hoskens
 *
 */
public class NestedPropertyChangeListener implements PropertyChangeListener
{

    private JTextField nestedField;
    private String nestedProperty;
    private static BeanUtilsBean beanUtilsBean;

    static
    {
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(new StringConverter(), String.class);
        beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
    }

    public static final class StringConverter implements Converter
    {

        public Object convert(Class type, Object value)
        {

            if (value == null)
            {
                return ((String) null);
            }
            else if (value instanceof Date)
            {
                return DateFormatUtils.format((Date) value, "dd/MM/yyyy");
            }
            return (value.toString());
        }

    }

    public NestedPropertyChangeListener(JTextField nestedPropertyField, String nestedProperty)
    {
        this.nestedField = nestedPropertyField;
        this.nestedProperty = nestedProperty;
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        Object newValue = evt.getNewValue();
        if (newValue == null)
        {
            nestedField.setText("");
        }
        else
        {
            String nestedValue;
            try
            {
                nestedValue = beanUtilsBean.getProperty(newValue, nestedProperty);
            }
            catch (Exception ex)
            {
                nestedValue = "";
            };
            nestedField.setText(nestedValue);
            nestedField.setToolTipText(nestedValue);
        }
        nestedField.select(0, 0); // move cursor to front
    }
}

