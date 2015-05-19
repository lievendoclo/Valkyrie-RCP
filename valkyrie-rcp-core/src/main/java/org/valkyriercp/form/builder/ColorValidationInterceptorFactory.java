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
package org.valkyriercp.form.builder;

import org.jdesktop.swingx.JXDatePicker;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.core.Message;
import org.valkyriercp.core.Severity;
import org.valkyriercp.form.HasValidationComponent;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Adds an "overlay" to a component that is triggered by a validation event for
 * JTextComponents. When an error is triggered, the background color of the
 * component is changed to the color set in {@link #setErrorColor(java.awt.Color)}. (The
 * default color is a very light red.)
 *
 * @author oliverh
 */
public class ColorValidationInterceptorFactory implements FormComponentInterceptorFactory
{

    private static final Color DEFAULT_ERROR_COLOR = new Color(255, 220, 220);

    private static final Color DEFAULT_WARNING_COLOR = new Color(255, 255, 160);

    private Map<Severity, Color> colorMap = new HashMap<Severity, Color>();

    public ColorValidationInterceptorFactory()
    {
        colorMap.put(Severity.ERROR, DEFAULT_ERROR_COLOR);
        colorMap.put(Severity.WARNING, DEFAULT_WARNING_COLOR);
    }

    public void setColorMap(Map<Severity, Color> colorMap)
    {
        this.colorMap = colorMap;
    }

    public void setErrorColor(Color color)
    {
        colorMap.put(Severity.ERROR, color);
    }

    public void setWarningColor(Color color)
    {
        colorMap.put(Severity.WARNING, color);
    }

    public FormComponentInterceptor getInterceptor(FormModel formModel)
    {
        return new ColorValidationInterceptor(formModel);
    }

    private class ColorValidationInterceptor extends ValidationInterceptor
    {

        public ColorValidationInterceptor(FormModel formModel)
        {
            super(formModel);
        }

        public void processComponent(String propertyName, JComponent component) {
            JComponent innerComponent = getInnerComponent(component);
            if (innerComponent instanceof JTextComponent) {
                ColorChanger colorChanger = new ColorChanger((JTextComponent)innerComponent);
                registerMessageReceiver(propertyName, colorChanger);
            }
        }

        @Override
        protected JComponent getInnerComponent(JComponent component)
        {
            if (component instanceof JXDatePicker)
                return ((JXDatePicker)component).getEditor();
            if (component instanceof HasValidationComponent)
                return ((HasValidationComponent)component).getValidationComponent();

            return super.getInnerComponent(component);
        }
    }

    /**
     * The colors used with each component are set by a ComponentUI class. When
     * the state of a component changes, the ComponentUI class will react on
     * this and set the appropriate colors <b>if and only if</b> the current
     * color on the component is implementing ResourceUI. (~ was created by the
     * ComponentUI itself)
     *
     * That's why we have to remember this color instead of defining our own
     * "normal" color which wouldn't be replaced by the inactive/active color
     * scheme used with editable/enabled.
     *
     * The only thing to check actually is when the enable/editable states are
     * changed (should be after this colorChanger).
     *
     * @see javax.swing.plaf.basic.BasicTextFieldUI#update(java.awt.Graphics, JComponent)
     *
     * @author jh
     *
     */
    private class ColorChanger implements Messagable
    {
        /**
         * Reference to the component to switch colors.
         */
        private JTextComponent component;

        /**
         * Constructor.
         *
         * @param component
         *            component on which to change color.
         */
        public ColorChanger(JTextComponent component)
        {
            this.component = component;
        }


        /**
         * Set the color according to the enabled state. When enabled, check
         * component's enabled and editable state for correct background color.
         */
        public void setMessage(Message message)
        {
            Color colorToSet = message == null ? null : colorMap.get(message.getSeverity());
            if (colorToSet != null)
                component.setBackground(colorToSet);
            else if (!component.isEnabled())
                component.setBackground( UIManager.getColor("TextField.disabledBackground"));
            else if (!component.isEditable())
                component.setBackground( UIManager.getColor("TextField.inactiveBackground"));
            else
                component.setBackground( UIManager.getColor("TextField.background"));
        }

        public void addPropertyChangeListener(PropertyChangeListener listener)
        {
        }

        public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
        {
        }

        public void removePropertyChangeListener(PropertyChangeListener listener)
        {
        }

        public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
        {
        }
    }
}
