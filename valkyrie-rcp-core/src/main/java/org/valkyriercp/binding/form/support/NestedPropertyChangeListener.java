/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valkyriercp.binding.form.support;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtilsBean;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
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
                return null;
            }
            else if (value instanceof Date)
            {
                return new SimpleDateFormat("dd/MM/yyyy").format((Date) value);
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

