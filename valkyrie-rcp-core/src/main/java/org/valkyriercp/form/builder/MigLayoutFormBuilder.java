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

import com.google.common.collect.Maps;
import net.miginfocom.swing.MigLayout;
import org.valkyriercp.form.binding.BindingFactory;
import org.valkyriercp.form.binding.swing.SwingBindingFactory;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.HashMap;
import java.util.Map;

public class MigLayoutFormBuilder extends AbstractFormBuilder {

    private JPanel panel;

    private Map<String, Map<Object, Object>> bindingContexts = new HashMap<String, Map<Object, Object>>();

    public Map<String, Map<Object, Object>> getBindingContexts() {
        return bindingContexts;
    }

    public void setBindingContexts(Map<String, Map<Object, Object>> bindingContexts) {
        this.bindingContexts = bindingContexts;
    }

    public void addBindingContextParameter(String propertyPath, Object key, Object value) {
        if (!getBindingContexts().containsKey(propertyPath))
            getBindingContexts().put(propertyPath, new HashMap<Object, Object>());
        Map<Object, Object> context = getBindingContexts().get(propertyPath);
        context.put(key, value);
    }

    public MigLayoutFormBuilder(BindingFactory bindingFactory, MigLayout layout) {
        this(bindingFactory, layout, new JPanel());
    }

    public MigLayoutFormBuilder(BindingFactory bindingFactory) {
        this(bindingFactory, new MigLayout(), new JPanel());
    }

    public MigLayoutFormBuilder(BindingFactory bindingFactory, MigLayout layout, JPanel panel) {
        super(bindingFactory);
        setLayout(layout, panel);
    }

    private void setLayout(MigLayout layout, JPanel panel) {
        this.panel = panel;
        panel.setLayout(layout);
    }

    public void setBorder(Border border) {
        this.panel.setBorder(border);
    }

    public JComponent addProperty(String property) {
        return addPropertyWithBinder(property, null, null, Maps.<String, Object>newHashMap());
    }

    public JComponent addProperty(String property, String migLayoutProperties) {
        return addPropertyWithBinder(property, null, migLayoutProperties, Maps.<String, Object>newHashMap());
    }

    public JComponent addPropertyWithBinder(String property, String binderId) {
        return addPropertyWithBinder(property, binderId, null, Maps.<String, Object>newHashMap());
    }

    public JComponent addPropertyWithBinder(String property, String binderId, String migLayoutProperties) {
        return addPropertyWithBinder(property, binderId, migLayoutProperties, Maps.<String, Object>newHashMap());
    }

    public JComponent addPropertyWithBinder(String property, String binderId, Map<String, Object> context) {
        return addPropertyWithBinder(property, binderId, null, context);
    }

    public JComponent addPropertyWithBinder(String property, String binderId, String migLayoutProperties, Map<String, Object> context) {
        JComponent propertyComponent;
        if (bindingContexts.get(property) != null)
            if (binderId == null)
                propertyComponent = getBindingFactory().createBinding(property, bindingContexts.get(property)).getControl();
            else
                propertyComponent = ((SwingBindingFactory) getBindingFactory()).createBinding(property,
                        binderId, bindingContexts.get(property)).getControl();
        else if (binderId == null)
            propertyComponent = getBindingFactory().createBinding(property, context).getControl();
        else
            propertyComponent = ((SwingBindingFactory) getBindingFactory()).createBinding(property,
                    binderId, context).getControl();

        addComponent(propertyComponent, migLayoutProperties);
        return propertyComponent;
    }

    public void addComponent(JComponent component) {
        addComponent(component, null);
    }

    public void addComponent(JComponent component, String migLayoutProperties) {
        if (migLayoutProperties == null)
            this.panel.add(component);
        else
            this.panel.add(component, migLayoutProperties);
    }

    public JLabel addLabel(String property) {
        return addLabel(property, null, null);
    }

    public JLabel addLabel(String property, JComponent forComponent) {
        return addLabel(property, forComponent, null);
    }

    public JLabel addLabel(String property, JComponent forComponent, String migLayoutProperties) {
        JLabel labelComponent = createLabelFor(property, forComponent);
        if (migLayoutProperties == null) {
            this.panel.add(labelComponent);
        } else {
            this.panel.add(labelComponent, migLayoutProperties);
        }
        return labelComponent;
    }

    public JComponent[] addPropertyAndLabel(String property) {
        return addPropertyAndLabelWithBinder(property, null, Maps.<String, Object>newHashMap());
    }

    public JComponent[] addPropertyAndLabel(String property, String migLayoutProperties) {
        return addPropertyAndLabelWithBinder(property, null, migLayoutProperties, Maps.<String, Object>newHashMap());
    }

    public JComponent[] addPropertyAndLabelWithBinder(String property, String binderId) {
        return addPropertyAndLabelWithBinder(property, binderId, null, Maps.<String, Object>newHashMap());
    }

    public JComponent[] addPropertyAndLabelWithBinder(String property, String binderId, String migLayoutProperties) {
        return addPropertyAndLabelWithBinder(property, binderId, migLayoutProperties, Maps.<String, Object>newHashMap());
    }

    public JComponent[] addPropertyAndLabelWithBinder(String property, String binderId, Map<String, Object> context) {
        return addPropertyAndLabelWithBinder(property, binderId, null, context);
    }

    public JComponent[] addPropertyAndLabelWithBinder(String property, String binderId, String migLayoutProperties, Map<String, Object> context) {
        JLabel label = addLabel(property);
        JComponent component = addPropertyWithBinder(property, binderId, migLayoutProperties, context);
        label.setLabelFor(component);
        return new JComponent[]{label, component};
    }

    public void addHorizontalSeparator()
    {
        addComponent(new JSeparator());
    }

    public void addHorizontalSeparator(String migLayoutProperties)
    {
        addComponent(new JSeparator(), migLayoutProperties);
    }

    public void addVerticalSeparator()
    {
        addComponent(new JSeparator());
    }

    public void addVerticalSeparator(String migLayoutProperties)
    {
        addComponent(new JSeparator(SwingConstants.VERTICAL), migLayoutProperties);
    }

    public void addTitledHorizontalSeparator(String titleKey)
    {
        addComponent(getComponentFactory().createLabeledSeparator(titleKey));
    }

    public void addTitledHorizontalSeparator(String titleKey, String migLayoutProperties)
    {
        addComponent(getComponentFactory().createLabeledSeparator(titleKey), migLayoutProperties);
    }

    public JPanel getPanel() {
        getBindingFactory().getFormModel().revert();
        return this.panel;
    }


}
