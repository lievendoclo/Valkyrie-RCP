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

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.form.support.NestedPropertyChangeListener;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.BindingFactory;
import org.valkyriercp.form.binding.swing.SwingBindingFactory;

import javax.swing.*;
import javax.swing.border.Border;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * <p>
 * Layout builder based on the <a href="http://www.jgoodies.com">JGoodies</a> Framework.
 * </p>
 * <p/>
 * <p>
 * Shortcuts have been provided to directly place a local and a component. For this, you provide the coordinates
 * for the label (x, y), and the component will be places (x + 2, y). If width or height spanning is provided,
 * this will be applied to the component.
 * </p>
 * <p/>
 * <pre>
 * FormBuilder builder = new FormBuilder(getBindingFactory(), myLayout);
 * //... add everything ...
 * JPanel myFirstPanel = builder.getPanel();
 * // set new layout, new panel
 * builder.setPanel(mySecondLayout);
 * //... add everything ...
 * JPanel mySecondPanel = builder.getPanel();
 * </pre>
 *
 * @author jh
 */
public class FormLayoutFormBuilder extends AbstractFormBuilder
{

    public static final String ALIGN_LEFT_TOP = "left, top";
    public static final String ALIGN_LEFT_CENTER = "left, center";
    public static final String ALIGN_LEFT_BOTTOM = "left, bottom";
    public static final String ALIGN_RIGHT_TOP = "right, top";
    public static final String ALIGN_RIGHT_CENTER = "right, center";
    public static final String ALIGN_RIGHT_BOTTOM = "right, bottom";

    private FormLayout layout;
    private JPanel panel;
    private CellConstraints cc;
    private String labelAttributes = ALIGN_LEFT_CENTER;
    private String componentAttributes = null;

    private Map<String, Map<Object, Object>> bindingContexts = new HashMap<String, Map<Object, Object>>();

    private int row;

    private static final int DEFAULT_ROW_INCREMENT = 2;


    public Map<String, Map<Object, Object>> getBindingContexts() {
        return bindingContexts;
    }

    public void setBindingContexts(Map<String, Map<Object, Object>> bindingContexts) {
        this.bindingContexts = bindingContexts;
    }

    public void addBindingContextParameter(String propertyPath, Object key, Object value) {
        if(!getBindingContexts().containsKey(propertyPath))
            getBindingContexts().put(propertyPath, new HashMap<Object, Object>());
        Map<Object, Object> context = getBindingContexts().get(propertyPath);
        context.put(key, value);
    }

    /**
     * Constructor.
     *
     * @param bindingFactory BindingFactory.
     * @param layout         JGoodies FormLayout
     */
    public FormLayoutFormBuilder(BindingFactory bindingFactory, FormLayout layout)
    {
        this(bindingFactory, layout, new JPanel());
    }

    /**
     * Constructor.
     *
     * @param bindingFactory BindingFactory.
     * @param layout         JGoodies FormLayout
     * @param panel          JPanel on which the builder will place the components.
     */
    public FormLayoutFormBuilder(BindingFactory bindingFactory, FormLayout layout, JPanel panel)
    {
        super(bindingFactory);
        setLayout(layout, panel);
    }

    /**
     * Move to the next line, minding the line gap
     *
     * @return This FormBuilder
     */
    public FormLayoutFormBuilder nextRow()
    {
        return nextRow(FormFactory.DEFAULT_ROWSPEC);
    }

    public FormLayoutFormBuilder nextRow(String rowSpec)
    {
        return nextRow(RowSpec.decode(rowSpec));
    }

    public FormLayoutFormBuilder nextRow(RowSpec rowSpec)
    {
        row += DEFAULT_ROW_INCREMENT;
        correctNumberOfRows(row, rowSpec);
        return this;
    }

    private void correctNumberOfRows(int requiredNrOfRows)
    {
        correctNumberOfRows(requiredNrOfRows, FormFactory.DEFAULT_ROWSPEC);
    }

    private void correctNumberOfRows(int requiredNrOfRows, RowSpec rowSpec)
    {
        int rowCount = layout.getRowCount();
        // if not initialized, set on first row
        if (row == -1 && requiredNrOfRows == -1)
        {
            requiredNrOfRows = 1;
            row = 1;
        }

        if (requiredNrOfRows > rowCount)
        {
            for (int i = rowCount; i < requiredNrOfRows; i = i + DEFAULT_ROW_INCREMENT)
            {
                if (i != 0)
                    layout.appendRow(FormFactory.LINE_GAP_ROWSPEC);
                layout.appendRow(rowSpec);
            }
        }
    }

    /**
     * Returns the current row. If the layout is initialized, the row is set to -1. Calling getRow() will
     * result in setting the row to 1 to allow direct usage of getRow().
     *
     * @return the current row
     */
    public int getRow()
    {
        if (row == -1) // init row
            row = 1;

        return row;
    }

    /**
     * Sets the current row
     *
     * @param row The current row
     */
    public void setRow(int row)
    {
        this.row = row;
        correctNumberOfRows(row);
    }

    /**
     * Create a new panel with the provided layout en default DIALOG_BORDER.
     *
     * @param layout JGoodies FormLayout
     */
    public void setLayout(FormLayout layout)
    {
        setLayout(layout, new JPanel());
    }

    /**
     * Set a panel with the provided layout layout.
     *
     * @param layout JGoodies FormLayout
     * @param panel  JPanel on which the builder will place the components.
     */
    public void setLayout(FormLayout layout, JPanel panel)
    {
        this.panel = panel;
        this.layout = layout;
        panel.setLayout(layout);
        cc = new CellConstraints();
        row = -1;
    }

    /**
     * Set the border for the panel.
     *
     * @param border The border for the panel
     */
    public void setBorder(Border border)
    {
        this.panel.setBorder(border);
    }

    /**
     * Add a binder to column 1 and the current row. Equals to builder.addBinding(component, 1,
     * builder.getRow()).
     *
     * @param binding The binding to add
     * @see #addBinding(Binding, int, int)
     */
    public JComponent addBinding(Binding binding)
    {
        return addBinding(binding, 1, row);
    }

    /**
     * Add a binder to a column and the current row. Equals to builder.addBinding(component, column,
     * builder.getRow()).
     *
     * @param binding The binding to add
     * @param column  The column on which the binding must be added
     * @return The component produced by the binding
     * @see #addBinding(Binding, int, int)
     */
    public JComponent addBinding(Binding binding, int column)
    {
        return addBinding(binding, column, row);
    }

    /**
     * Add a binder to a column and a row. Equals to builder.addBinding(component, column,
     * row).
     *
     * @param binding The binding to add
     * @param column  The column on which the binding must be added
     * @param column  The row on which the binding must be added
     * @return The component produced by the binding
     * @see #addBinding(Binding, int, int, int, int)
     */
    public JComponent addBinding(Binding binding, int column, int row)
    {
        return this.addBinding(binding, column, row, 1, 1);
    }

    /**
     * Add a binder to a column and a row with width and height spanning.
     */
    public JComponent addBinding(Binding binding, int column, int row, int widthSpan, int heightSpan)
    {
        ((SwingBindingFactory) getBindingFactory()).interceptBinding(binding);
        JComponent component = binding.getControl();
        addComponent(component, column, row, widthSpan, heightSpan);
        return component;
    }

    /**
     * Add a property to column 1 and the current row. Equals to builder.addBinding(component, 1,
     * builder.getRow()).
     *
     * @see #addProperty(String, int, int)
     */
    public JComponent addProperty(String property)
    {
        return addProperty(property, 1, row);
    }

    /**
     * Add a property to a column and the current row. Equals to builder.addBinding(component, column,
     * builder.getRow()).
     *
     * @see #addProperty(String, int, int)
     */
    public JComponent addProperty(String property, int column)
    {
        return addProperty(property, column, row);
    }

    public JComponent addProperty(String property, String binderId)
    {
        return addProperty(property, binderId, 1, row);
    }

    public JComponent addProperty(String property, String binderId, Map<?, ?> contextMap)
    {
        return addProperty(property, binderId, contextMap, 1, row);
    }

    public JComponent addProperty(String property, String binderId, int column)
    {
        return addProperty(property, binderId, column, row);
    }

    public JComponent addProperty(String property, String binderId, Map<?, ?> contextMap, int column)
    {
        return addProperty(property, binderId, contextMap, column, row);
    }

    public JComponent addProperty(String property, int column, int row)
    {
        return this.addProperty(property, column, row, 1, 1);
    }

    public JComponent addProperty(String property, String binderId, int column, int row)
    {
        return this.addProperty(property, binderId, column, row, 1, 1);
    }

    public JComponent addProperty(String property, String binderId, Map<?, ?> contextMap, int column, int row)
    {
        return this.addProperty(property, binderId, contextMap, column, row, 1, 1);
    }

    public JComponent addProperty(String property, int column, int row, int widthSpan, int heightSpan)
    {
        JComponent propertyComponent;
        if(bindingContexts.get(property) != null)
            propertyComponent = getBindingFactory().createBinding(property, bindingContexts.get(property)).getControl();
        else
            propertyComponent = getBindingFactory().createBinding(property).getControl();
        addComponent(propertyComponent, column, row, widthSpan, heightSpan);
        return propertyComponent;
    }

    public JComponent addProperty(String property, String binderId, int column, int row, int widthSpan,
                                  int heightSpan)
    {
        return addProperty(property, binderId, Collections.EMPTY_MAP, column, row, widthSpan, heightSpan);
    }

    public JComponent addProperty(String property, String binderId, Map<?, ?> contextMap, int column, int row, int widthSpan,
                                  int heightSpan)
    {
        JComponent propertyComponent = ((SwingBindingFactory) getBindingFactory()).createBinding(property,
                binderId, contextMap).getControl();
        addComponent(propertyComponent, column, row, widthSpan, heightSpan);
        return propertyComponent;
    }

    public JLabel addLabel(String property)
    {
        return addLabel(property, 1, row);
    }

    public JLabel addLabel(String property, int column)
    {
        return addLabel(property, column, row);
    }

    public JLabel addLabel(String property, int column, int row)
    {
        return addLabel(property, null, column, row);
    }

    public JLabel addLabel(String property, int column, int row, String attributes)
    {
        return addLabel(property, null, column, row, 1, 1, attributes);
    }

    public JLabel addLabel(String property, JComponent forComponent, int column, int row)
    {
        return addLabel(property, forComponent, column, row, 1, 1, this.labelAttributes);
    }

    public JLabel addLabel(String property, JComponent forComponent, int column, int row, int widthSpan,
                           int heightSpan, String attributes)
    {
        JLabel labelComponent = createLabelFor(property, forComponent);
        this.row = row;
        if (this.row == -1)
        {
            this.row = 1;
        }
        correctNumberOfRows(this.row);
        this.panel.add(labelComponent, cc.xywh(column, this.row, widthSpan, heightSpan, attributes));
        return labelComponent;
    }

    public void addComponent(JComponent component)
    {
        addComponent(component, 1, row, 1, 1);
    }

    public void addComponent(JComponent component, int column)
    {
        addComponent(component, column, row, 1, 1);
    }

    public void addComponent(JComponent component, int column, int row)
    {
        addComponent(component, column, row, 1, 1);
    }

    public void addComponent(JComponent component, int column, int row, int widthSpan, int heightSpan)
    {
        addComponent(component, column, row, widthSpan, heightSpan, this.componentAttributes);
    }

    public void addComponent(JComponent component, int column, int row, int widthSpan, int heightSpan,
                             String attributes)
    {
        this.row = row;
        if (this.row == -1)
        {
            this.row = 1;
        }
        correctNumberOfRows(this.row);
        if (attributes == null)
            this.panel.add(component, cc.xywh(column, this.row, widthSpan, heightSpan));
        else
            this.panel.add(component, cc.xywh(column, this.row, widthSpan, heightSpan, attributes));
    }

    public JComponent[] addPropertyAndLabel(String property)
    {
        return addPropertyAndLabel(property, 1, row, this.labelAttributes);
    }

    public JComponent[] addPropertyAndLabel(String property, int column)
    {
        return addPropertyAndLabel(property, column, row, this.labelAttributes);
    }

    public JComponent[] addPropertyAndLabel(String property, String binderId)
    {
        return addPropertyAndLabel(property, binderId, 1, row, this.labelAttributes);
    }

    public JComponent[] addPropertyAndLabel(String property, String binderId, Map<?, ?> context)
    {
        return addPropertyAndLabel(property, binderId, context, 1, row, this.labelAttributes);
    }

    public JComponent[] addPropertyAndLabel(String property, int column, String binderId)
    {
        return addPropertyAndLabel(property, binderId, column, row, this.labelAttributes);
    }

    public JComponent[] addPropertyAndLabel(String property, int column, int row)
    {
        return addPropertyAndLabel(property, column, row, this.labelAttributes);
    }

    public JComponent[] addPropertyAndLabel(String property, String binderId, int column, int row)
    {
        return addPropertyAndLabel(property, binderId, column, row, this.labelAttributes);
    }

    public JComponent[] addPropertyAndLabel(String property, int column, int row, String attributes)
    {
        return addPropertyAndLabel(property, column, row, 1, attributes);
    }

    public JComponent[] addPropertyAndLabel(String property, String binderId, int column, int row,
                                            String attributes)
    {
        return addPropertyAndLabel(property, binderId, column, row, 1, attributes);
    }

    public JComponent[] addPropertyAndLabel(String property, String binderId, Map<?, ?> context, int column, int row,
                                            String attributes)
    {
        return addPropertyAndLabel(property, binderId, context, column, row, 1, attributes);
    }

    public JComponent[] addPropertyAndLabel(String property, int column, int row, int widthSpan)
    {
        return addPropertyAndLabel(property, column, row, widthSpan, this.labelAttributes);
    }

    public JComponent[] addPropertyAndLabel(String property, int column, int row, int widthSpan,
                                            String attributes)
    {
        return addPropertyAndLabel(property, column, row, widthSpan, 1, attributes);
    }

    public JComponent[] addPropertyAndLabel(String property, String binderId, int column, int row,
                                            int widthSpan, String attributes)
    {
        return addPropertyAndLabel(property, binderId, column, row, widthSpan, 1, attributes);
    }

    public JComponent[] addPropertyAndLabel(String property, String binderId, Map<?, ?> context,  int column, int row,
                                            int widthSpan, String attributes)
    {
        return addPropertyAndLabel(property, binderId, context, column, row, widthSpan, 1, attributes);
    }

    public JComponent[] addPropertyAndLabel(String property, int column, int row, int widthSpan,
                                            int heightSpan)
    {
        return addPropertyAndLabel(property, column, row, widthSpan, heightSpan, this.labelAttributes);
    }

    public JComponent[] addPropertyAndLabel(String property, String binderId, int column, int row,
                                            int widthSpan, int heightSpan)
    {
        return addPropertyAndLabel(property, binderId, column, row, widthSpan, heightSpan,
                this.labelAttributes);
    }

    public JComponent[] addPropertyAndLabel(String property, int column, int row, int widthSpan,
                                            int heightSpan, String attributes)
    {
        JComponent component = addProperty(property, column + 2, row, widthSpan, heightSpan);
        JLabel label = addLabel(property, component, column, row, 1, 1, attributes);
        return new JComponent[]{label, component};
    }

    public JComponent[] addPropertyAndLabel(String property, String binderId, Map<?, ?> context, int column, int row,
                                            int widthSpan, int heightSpan, String attributes)
    {
        JComponent component = addProperty(property, binderId, context, column + 2, row, widthSpan, heightSpan);
        JLabel label = addLabel(property, component, column, row, 1, 1, attributes);
        return new JComponent[]{label, component};
    }

    public JComponent[] addPropertyAndLabel(String property, String binderId, int column, int row,
                                            int widthSpan, int heightSpan, String attributes)
    {
        JComponent component = addProperty(property, binderId, column + 2, row, widthSpan, heightSpan);
        JLabel label = addLabel(property, component, column, row, 1, 1, attributes);
        return new JComponent[]{label, component};
    }

    public JPanel getPanel()
    {
        getBindingFactory().getFormModel().revert();
        return this.panel;
    }

    public void setLabelAttributes(String attributes)
    {
        this.labelAttributes = attributes;
    }

    public void setComponentAttributes(String attributes)
    {
        this.componentAttributes = attributes;
    }

    public JComponent[] addTextArea(String propertyName, int column, int row)
    {
        return addTextArea(propertyName, column, row, 1, 1);
    }

    public JComponent[] addTextArea(String propertyName, int column)
    {
        return addTextArea(propertyName, column, row, 1, 1);
    }

    public JComponent[] addTextArea(String propertyName)
    {
        return addTextArea(propertyName, 1, row, 1, 1);
    }

    public JComponent[] addTextArea(String propertyName, int column, int row, int widthSpan, int heightSpan)
    {
        return addTextArea(propertyName, column, row, widthSpan, heightSpan,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public JComponent[] addTextArea(String propertyName, int column, int row, int widthSpan, int heightSpan,
                                    int vsbPolicy, int hsbPolicy)
    {
        JComponent textArea = createTextArea(propertyName);
        createBinding(propertyName, textArea);
        JScrollPane scrollPane = new JScrollPane(textArea, vsbPolicy, hsbPolicy);
        addComponent(scrollPane, column, row, widthSpan, heightSpan);
        return new JComponent[]{textArea, scrollPane};
    }

    public JComponent[] addTextAreaAndLabel(String propertyName, int column, int row)
    {
        return addTextAreaAndLabel(propertyName, column, row, 1, 1, this.labelAttributes);
    }

    public JComponent[] addTextAreaAndLabel(String propertyName, int column, int row, String attributes)
    {
        return addTextAreaAndLabel(propertyName, column, row, 1, 1, attributes);
    }

    public JComponent[] addTextAreaAndLabel(String propertyName, int column, int row, int widthSpan,
                                            int heightSpan)
    {
        return addTextAreaAndLabel(propertyName, column, row, widthSpan, heightSpan, this.labelAttributes);
    }

    public JComponent[] addTextAreaAndLabel(String propertyName, int column, int row, int widthSpan,
                                            int heightSpan, String attributes)
    {
        JLabel label = addLabel(propertyName, column, row, attributes);
        JComponent[] components = addTextArea(propertyName, column + 2, row, widthSpan, heightSpan);
        return new JComponent[]{label, components[0], components[1]};
    }

    public JComponent addPasswordField(String propertyName, int column)
    {
        return addPasswordField(propertyName, column, row, 1, 1);
    }

    public JComponent addPasswordField(String propertyName)
    {
        return addPasswordField(propertyName, 1, row);
    }

    public JComponent addPasswordField(String propertyName, int column, int row)
    {
        return addPasswordField(propertyName, column, row, 1, 1);
    }

    public JComponent addPasswordField(String propertyName, int column, int row, int widthSpan, int heightSpan)
    {
        JComponent passwordField = createBinding(propertyName, createPasswordField(propertyName))
                .getControl();
        addComponent(passwordField, column, row, widthSpan, heightSpan);
        return passwordField;
    }

    public JComponent[] addPasswordFieldAndLabel(String propertyName, int column)
    {
        return addPasswordFieldAndLabel(propertyName, column, row, 1, 1);
    }

    public JComponent[] addPasswordFieldAndLabel(String propertyName)
    {
        return addPasswordFieldAndLabel(propertyName, 1, row);
    }

    public JComponent[] addPasswordFieldAndLabel(String propertyName, int column, int row)
    {
        return addPasswordFieldAndLabel(propertyName, column, row, 1, 1, this.labelAttributes);
    }

    public JComponent[] addPasswordFieldAndLabel(String propertyName, int column, int row, String attributes)
    {
        return addPasswordFieldAndLabel(propertyName, column, row, 1, 1, attributes);
    }

    public JComponent[] addPasswordFieldAndLabel(String propertyName, int column, int row, int widthSpan,
                                                 int heightSpan)
    {
        return addPasswordFieldAndLabel(propertyName, column, row, widthSpan, heightSpan,
                this.labelAttributes);
    }

    public JComponent[] addPasswordFieldAndLabel(String propertyName, int column, int row, int widthSpan,
                                                 int heightSpan, String attributes)
    {
        JLabel label = addLabel(propertyName, column, row, attributes);
        JComponent component = addPasswordField(propertyName, column + 2, row, widthSpan, heightSpan);
        return new JComponent[]{label, component};
    }

    public void addHorizontalSeparator(int column, int row, int widthSpan)
    {
        addComponent(new JSeparator(), column, row, widthSpan, 1);
    }

    public void addHorizontalSeparator()
    {
        addComponent(new JSeparator(), 1, row, 1, 1);
    }

    public void addHorizontalSeparator(int widthSpan)
    {
        addComponent(new JSeparator(), 1, row, widthSpan, 1);
    }

    public void addHorizontalSeparator(int column, int widthSpan)
    {
        addComponent(new JSeparator(), column, row, widthSpan, 1);
    }

    public void addHorizontalSeparator(String text)
    {
        addComponent(getComponentFactory().createLabeledSeparator(text), 1, row, 1, 1);
    }

    public void addHorizontalSeparator(String text, int widthSpan)
    {
        addComponent(getComponentFactory().createLabeledSeparator(text), 1, row, widthSpan, 1);
    }

    public void addHorizontalSeparator(String text, int column, int widthSpan)
    {
        addComponent(getComponentFactory().createLabeledSeparator(text), column, row, widthSpan, 1);
    }

    public void addVerticalSeparator(int column, int row, int heightSpan)
    {
        addComponent(new JSeparator(SwingConstants.VERTICAL), column, row, 1, heightSpan);
    }

    public void addVerticalSeparator()
    {
        addComponent(new JSeparator(SwingConstants.VERTICAL), 1, row, 1, 1);
    }

    public void addVerticalSeparator(int heightSpan)
    {
        addComponent(new JSeparator(SwingConstants.VERTICAL), 1, row, 1, heightSpan);
    }

    public void addVerticalSeparator(int column, int heightSpan)
    {
        addComponent(new JSeparator(SwingConstants.VERTICAL), column, row, 1, heightSpan);
    }

    public JComponent addNestedPropertyReadOnly(String property, String nestedProperty)
    {
        return addNestedPropertyReadOnly(property, nestedProperty, 1, row);
    }

    public JComponent addNestedPropertyReadOnly(String property, String nestedProperty, int column)
    {
        return addNestedPropertyReadOnly(property, nestedProperty, column, row);
    }

    public JComponent addNestedPropertyReadOnly(String property, String nestedProperty, int column, int row)
    {
        return addNestedPropertyReadOnly(property, nestedProperty, column, row, 1, 1);
    }

    public JComponent addNestedPropertyReadOnly(String property, String nestedProperty, int column, int row,
                                                int widthSpan, int heightSpan)
    {
        final JTextField nestedPropertyField = new JTextField();
        nestedPropertyField.setEditable(false);
        getFormModel().getValueModel(property).addValueChangeListener(
                new NestedPropertyChangeListener(nestedPropertyField, nestedProperty));
        getFormModelEnabledListener().add(nestedPropertyField);
        addComponent(nestedPropertyField, column, row, widthSpan, heightSpan);
        return nestedPropertyField;
    }

    public JComponent[] addNestedPropertyReadOnlyAndLabel(String property, String nestedProperty)
    {
        return addNestedPropertyReadOnlyAndLabel(property, nestedProperty, 1, row);
    }

    public JComponent[] addNestedPropertyReadOnlyAndLabel(String property, String nestedProperty, int column)
    {
        return addNestedPropertyReadOnlyAndLabel(property, nestedProperty, column, row);
    }

    public JComponent[] addNestedPropertyReadOnlyAndLabel(String property, String nestedProperty, int column, int row)
    {
        return addNestedPropertyReadOnlyAndLabel(property, nestedProperty, column, row, 1, 1);
    }

    public JComponent[] addNestedPropertyReadOnlyAndLabel(String property, String nestedProperty, int column, int row,
                                                          int widthSpan, int heightSpan)
    {
        final JTextField nestedPropertyField = new JTextField();
        nestedPropertyField.setEditable(false);
        getFormModel().getValueModel(property).addValueChangeListener(
                new NestedPropertyChangeListener(nestedPropertyField, nestedProperty));
        getFormModelEnabledListener().add(nestedPropertyField);
        JComponent comp = addNestedPropertyReadOnly(property, nestedProperty, column + 2, row, widthSpan, heightSpan);
        JLabel label = addLabel(property + "." + nestedProperty, comp, column, row, 1, 1, this.labelAttributes);
        return new JComponent[] { label, comp };
    }

    private FormModelEnabledListener formModelEnabledListener;

    public FormModelEnabledListener getFormModelEnabledListener()
    {
        if (formModelEnabledListener == null)
        {
            formModelEnabledListener = new FormModelEnabledListener();
            getFormModel().addPropertyChangeListener(FormModel.ENABLED_PROPERTY, formModelEnabledListener);
        }
        return formModelEnabledListener;
    }

    public static class FormModelEnabledListener implements PropertyChangeListener
    {

        private List<JComponent> components = new ArrayList<JComponent>();

        public void propertyChange(PropertyChangeEvent evt)
        {
            for (JComponent component : components)
            {
                component.setEnabled((Boolean) evt.getNewValue());
            }
        }

        public void add(JComponent component)
        {
            components.add(component);
        }

    }
}
