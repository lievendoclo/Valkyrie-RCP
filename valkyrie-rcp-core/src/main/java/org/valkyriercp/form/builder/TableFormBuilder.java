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

import org.springframework.util.Assert;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.BindingFactory;
import org.valkyriercp.form.binding.swing.ComboBoxBinder;
import org.valkyriercp.layout.TableLayoutBuilder;
import org.valkyriercp.rules.constraint.Constraint;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A TableFormBuilder builds a form by using a {@link TableLayoutBuilder}
 *
 * @author oliverh
 * @author Mathias Broekelmann
 */
public class TableFormBuilder extends AbstractFormBuilder {

    private static final String VALIGN_TOP = TableLayoutBuilder.VALIGN + "=top";

    private TableLayoutBuilder builder;

    private String labelAttributes = TableLayoutBuilder.DEFAULT_LABEL_ATTRIBUTES;

    /**
     * Creates an instances of the TableFormBuilder by using a {@link BindingFactory}
     *
     * @param bindingFactory
     *            the binding factory to use to create field bindings.
     */
    public TableFormBuilder(BindingFactory bindingFactory) {
        this(bindingFactory, null);
    }

    /**
     * Creates an instances of the TableFormBuilder by using a {@link BindingFactory} and a given {@ TableLayoutBuilder}
     *
     * @param bindingFactory
     *            the binding factory to use to create field bindings.
     */
    public TableFormBuilder(BindingFactory bindingFactory, TableLayoutBuilder tableLayoutBuilder) {
        super(bindingFactory);
        this.builder = tableLayoutBuilder;
    }

    /**
     * adds a row to the form. All subsequent field additions will be placed in a new row
     */
    public void row() {
        getLayoutBuilder().relatedGapRow();
    }

    /**
     * Adds the field to the form. {@link #createDefaultBinding(String)} is used to create the binding for the field
     *
     * @param fieldName
     *            the name of the field to add
     * @return an array containing the label and the component which where added to the form
     */
    public JComponent[] add(String fieldName) {
        return add(fieldName, "");
    }

    /**
     * Adds the field binding to the form.
     *
     * @param binding
     *            the field binding to add
     * @return an array containing the label and the component of the binding which where added to the form
     */
    public JComponent[] add(Binding binding) {
        return add(binding, "");
    }

    /**
     * Adds the field to the form. {@link #createDefaultBinding(String)} is used to create the binding for the field
     *
     * @param fieldName
     *            the name of the field to add
     * @param attributes
     *            optional layout attributes for the component. See {@link TableLayoutBuilder} for syntax details
     * @return an array containing the label and the component which where added to the form
     */
    public JComponent[] add(String fieldName, String attributes) {
        return addBinding(createDefaultBinding(fieldName), attributes, getLabelAttributes());
    }

    /**
     * Adds the field binding to the form.
     *
     * @param binding
     *            the field binding to add
     * @param attributes
     *            optional layout attributes for the component. See {@link TableLayoutBuilder} for syntax details
     * @return an array containing the label and the component which where added to the form
     */
    public JComponent[] add(Binding binding, String attributes) {
        return addBinding(binding, attributes, getLabelAttributes());
    }

    /**
     * Adds the field to the form by using the provided component.
     *
     * @param fieldName
     *            the name of the field to add
     * @param component
     *            the component for the field
     * @return an array containing the label and the component which where added to the form
     */
    public JComponent[] add(String fieldName, JComponent component) {
        return add(fieldName, component, "");
    }

    /**
     * Adds the field to the form by using the provided component. {@link #createBinding(String, JComponent)} is used to
     * create the binding of the field
     *
     * @param fieldName
     *            the name of the field to add
     * @param component
     *            the component for the field
     * @param attributes
     *            optional layout attributes for the component. See {@link TableLayoutBuilder} for syntax details
     * @return an array containing the label and the component which where added to the form
     */
    public JComponent[] add(String fieldName, JComponent component, String attributes) {
        return addBinding(createBinding(fieldName, component), attributes, getLabelAttributes());
    }

    /**
     * Adds the field to the form by using a selector component. {@link #createSelector(String, Constraint)} is used to
     * create the component for the selector
     *
     * @param fieldName
     *            the name of the field to add
     * @param filter
     *            optional filter constraint for the items of the selector
     * @return an array containing the label and the selector component which where added to the form
     *
     */
    public JComponent[] addSelector(String fieldName, Constraint filter) {
        return addSelector(fieldName, filter, "");
    }

    /**
     * Adds the field to the form by using a selector component.
     *
     * @param fieldName
     *            the name of the field to add
     * @param filter
     *            optional filter constraint for the items of the selector
     * @param attributes
     *            optional layout attributes for the selector component. See {@link TableLayoutBuilder} for syntax
     *            details
     * @return an array containing the label and the selector component which where added to the form
     *
     */
    public JComponent[] addSelector(String fieldName, Constraint filter, String attributes) {
        Map context = new HashMap();
        context.put(ComboBoxBinder.FILTER_KEY, filter);
        return addBinding(getBindingFactory().createBinding(JComboBox.class, fieldName), attributes,
                getLabelAttributes());
    }

    /**
     * Adds the field to the form by using a password component. {@link #createPasswordField(String)} is used to create
     * the component for the password field
     *
     * @param fieldName
     *            the name of the field to add
     * @return an array containing the label and the password component which where added to the form
     *
     * @see #createPasswordField(String)
     */
    public JComponent[] addPasswordField(String fieldName) {
        return addPasswordField(fieldName, "");
    }

    /**
     * Adds the field to the form by using a password component. {@link #createPasswordField(String)} is used to create
     * the component for the password field
     *
     * @param fieldName
     *            the name of the field to add
     * @param attributes
     *            optional layout attributes for the password component. See {@link TableLayoutBuilder} for syntax
     *            details
     * @return an array containing the label and the password component which where added to the form
     *
     * @see #createPasswordField(String)
     */
    public JComponent[] addPasswordField(String fieldName, String attributes) {
        return addBinding(createBinding(fieldName, createPasswordField(fieldName)), attributes, getLabelAttributes());
    }

    /**
     * Adds the field to the form by using a text area component which is wrapped inside a scrollpane.
     * <p>
     * Note: this method ensures that the the label of the textarea has a top vertical alignment if <code>valign</code>
     * is not defined in the default label attributes
     *
     * @param fieldName
     *            the name of the field to add
     * @return an array containing the label, the textarea and the scrollpane which where added to the form
     *
     * @see #createTextArea(String)
     */
    public JComponent[] addTextArea(String fieldName) {
        return addTextArea(fieldName, "");
    }

    /**
     * Adds the field to the form by using a text area component which is wrapped inside a scrollpane.
     * {@link #createTextArea(String)} is used to create the component for the text area field
     * <p>
     * Note: this method ensures that the the label of the textarea has a top vertical alignment if <code>valign</code>
     * is not defined in the default label attributes
     *
     * @param fieldName
     *            the name of the field to add
     * @param attributes
     *            optional layout attributes for the scrollpane. See {@link TableLayoutBuilder} for syntax details
     * @return an array containing the label, the textarea and the scrollpane and which where added to the form
     *
     * @see #createTextArea(String)
     */
    public JComponent[] addTextArea(String fieldName, String attributes) {
        JComponent textArea = createTextArea(fieldName);
        String labelAttributes = getLabelAttributes();
        if (labelAttributes == null) {
            labelAttributes = VALIGN_TOP;
        } else if (!labelAttributes.contains(TableLayoutBuilder.VALIGN)) {
            labelAttributes += " " + VALIGN_TOP;
        }
        return addBinding(createBinding(fieldName, textArea), new JScrollPane(textArea), attributes, labelAttributes);
    }

    /**
     * Adds the field to the form by using the default binding. The component will be placed inside a scrollpane.
     *
     * @param fieldName
     *            the name of the field to add
     * @return an array containing the label, the component of the field binding and the scrollpane which where added to
     *         the form
     */
    public JComponent[] addInScrollPane(String fieldName) {
        return addInScrollPane(fieldName, "");
    }

    /**
     * Adds the field to the form by using the default binding. The component will be placed inside a scrollpane.
     *
     * @param fieldName
     *            the name of the field to add
     * @param attributes
     *            optional layout attributes for the scrollpane. See {@link TableLayoutBuilder} for syntax details
     * @return an array containing the label, the component of the field binding and the scrollpane binding which where
     *         added to the form
     *
     * @see #createScrollPane(String, JComponent)
     */
    public JComponent[] addInScrollPane(String fieldName, String attributes) {
        return addInScrollPane(createDefaultBinding(fieldName), attributes);
    }

    /**
     * Adds the field binding to the form. The component will be placed inside a scrollpane.
     *
     * @param binding
     *            the binding to use
     * @return an array containing the label, the component of the field binding and the scrollpane and the component of
     *         the binding which where added to the form
     *
     * @see #createScrollPane(String, JComponent)
     */
    public JComponent[] addInScrollPane(Binding binding) {
        return addInScrollPane(binding, "");
    }

    /**
     * Adds the field binding to the form. The component will be placed inside a scrollpane.
     * {@link #createScrollPane(String, JComponent)} is used to create the component for the scrollpane
     *
     * @param binding
     *            the binding to use
     * @param attributes
     *            optional layout attributes for the scrollpane. See {@link TableLayoutBuilder} for syntax details
     * @return an array containing the label, the component of the field binding and the scrollpane and the component of
     *         the binding which where added to the form
     *
     * @see #createScrollPane(String, JComponent)
     */
    public JComponent[] addInScrollPane(Binding binding, String attributes) {
        Assert.isTrue(getFormModel() == binding.getFormModel(),
                "Binding's form model must match FormBuilder's form model");
        return add(binding.getProperty(), createScrollPane(binding.getProperty(), binding.getControl()), attributes);
    }

    /**
     * Adds a labeled separator to the form.
     *
     * @param text
     *            the key for the label. Must not be null
     */
    public JComponent addSeparator(String text) {
		return addSeparator(text, "");
    }

    /**
     * Adds a labeled separator to the form
     *
     * @param text
     *            the key for the label. Must not be null
     * @param attributes
     *            optional attributes. See {@link TableLayoutBuilder} for syntax details
     */
    public JComponent addSeparator(String text, String attributes) {
        JComponent separator = getComponentFactory().createLabeledSeparator(text);
		getLayoutBuilder().cell(separator, attributes);
		return separator;
    }

    /**
     * Returns the layout builder which is used to build the layout of the added fields and labels
     *
     * @return The form containing the added fields, components and labels in the defined layout. Not null
     */
    public TableLayoutBuilder getLayoutBuilder() {
        if (builder == null) {
            builder = new TableLayoutBuilder(getComponentFactory().createPanel());
        }
        return builder;
    }

    /**
     * Returns the form which has been created by this builder
     *
     * @return The form containing the added fields and labels in the defined layout. Not null
     */
    public JComponent getForm() {
        getBindingFactory().getFormModel().revert();
        return getLayoutBuilder().getPanel();
    }

    /**
     * returns the default label layout attributes for the form.
     *
     * @return layout attributes for the labels, can be null.
     */
    public String getLabelAttributes() {
        return labelAttributes;
    }

    /**
     * defines the default label layout attributes for the form.
     *
     * @param labelAttributes
     *            layout attributes for the labels, if null no layout attributes will be applied to the labels. See
     *            {@link TableLayoutBuilder} for syntax details.
     */
    public void setLabelAttributes(String labelAttributes) {
        this.labelAttributes = labelAttributes;
    }

    /**
     * adds a field binding to the form. This method does not use the default label attributes which may have been set
     * through {@link #setLabelAttributes(String)}
     *
     * @param binding
     *            the binding of the field
     * @param attributes
     *            optional layout attributes for the label. If null no layout attributes will be applied to the label.
     *            See {@link TableLayoutBuilder} for syntax details
     * @return an array containing the label and the component of the binding
     */
    public JComponent[] addBinding(Binding binding, String attributes, String labelAttributes) {
        return addBinding(binding, binding.getControl(), attributes, labelAttributes);
    }

    /**
     * adds a field binding to the form
     *
     * @param binding
     *            the binding of the field
     * @param wrappedControl
     *            the optional wrapped component. If null the component of the binding is used. This Parameter should be
     *            used if the component of the binding is being wrapped inside this component
     * @param attributes
     *            optional layout attributes for the label. If null no layout attributes will be applied to the label.
     *            See {@link TableLayoutBuilder} for syntax details
     * @return an array containing the label, the component of the field binding and the wrapped component
     */
    public JComponent[] addBinding(Binding binding, JComponent wrappedControl, String attributes) {
        return addBinding(binding, wrappedControl, attributes, getLabelAttributes());
    }

    /**
     * adds a field binding to the form
     *
     * @param binding
     *            the binding of the field
     * @param wrappedComponent
     *            the optional wrapped component. If null the component of the binding is used. This Parameter should be
     *            used if the component of the binding is being wrapped inside this component
     * @param attributes
     *            optional layout attributes for the wrapped component. If null no layout attributes will be applied to
     *            the component. See {@link TableLayoutBuilder} for syntax details
     * @param attributes
     *            optional layout attributes for the label. If null no layout attributes will be applied to the label.
     *            See {@link TableLayoutBuilder} for syntax details
     * @return an array containing the label, the component of the field binding and the wrapped component
     */
    public JComponent[] addBinding(Binding binding, JComponent wrappedComponent, String attributes,
            String labelAttributes) {
        Assert.notNull(binding, "binding is null");
        Assert.isTrue(getFormModel() == binding.getFormModel(),
                "Binding's form model must match FormBuilder's form model");
        JComponent component = binding.getControl();
        final JLabel label = createLabelFor(binding.getProperty(), component);
        if (wrappedComponent == null) {
            wrappedComponent = component;
        }
        TableLayoutBuilder layoutBuilder = getLayoutBuilder();
        if (!layoutBuilder.hasGapToLeft()) {
            layoutBuilder.gapCol();
        }
        layoutBuilder.cell(label, labelAttributes);
        layoutBuilder.labelGapCol();
        layoutBuilder.cell(wrappedComponent, attributes);
        return new JComponent[] { label, component, wrappedComponent };
    }
}
