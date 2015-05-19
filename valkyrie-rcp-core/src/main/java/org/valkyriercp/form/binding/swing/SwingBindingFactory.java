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
package org.valkyriercp.form.binding.swing;

import org.springframework.beans.support.PropertyComparator;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.ConfigurableFormModel;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.form.support.DefaultFormModel;
import org.valkyriercp.binding.value.ObservableList;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.BufferedCollectionValueModel;
import org.valkyriercp.binding.value.support.ValueHolder;
import org.valkyriercp.component.ShuttleList;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBindingFactory;
import org.valkyriercp.list.BeanPropertyValueComboBoxEditor;
import org.valkyriercp.list.BeanPropertyValueListRenderer;
import org.valkyriercp.rules.closure.Closure;

import javax.swing.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A convenient implementation of <code>BindingFactory</code>. Provides a set
 * of methods that address the typical binding requirements of Swing based
 * forms.
 *
 * @author Oliver Hutchison
 */
public class SwingBindingFactory extends AbstractBindingFactory {

    public SwingBindingFactory(FormModel formModel) {
        super(formModel);
    }

    public Binding createBoundTextField(String formProperty) {
        return createBinding(JTextField.class, formProperty);
    }

    public Binding createBoundTextArea(String formProperty) {
        return createBinding(JTextArea.class, formProperty);
    }

    public Binding createBoundTextArea(String formProperty, int rows, int columns) {
        Map context = createContext(TextAreaBinder.ROWS_KEY, new Integer(rows));
        context.put(TextAreaBinder.COLUMNS_KEY, new Integer(columns));
        return createBinding(JTextArea.class, formProperty, context);
    }

    public Binding createBoundFormattedTextField(String formProperty) {
        return createBinding(JFormattedTextField.class, formProperty);
    }

    public Binding createBoundFormattedTextField(String formProperty, JFormattedTextField.AbstractFormatterFactory formatterFactory) {
        Map context = createContext(FormattedTextFieldBinder.FORMATTER_FACTORY_KEY, formatterFactory);
        return createBinding(JFormattedTextField.class, formProperty, context);
    }

    public Binding createBoundSpinner(String formProperty) {
        return createBinding(JSpinner.class, formProperty);
    }

    public Binding createBoundLabel(String formProperty) {
        return createBinding(JLabel.class, formProperty);
    }

    public Binding createBoundToggleButton(String formProperty) {
        return createBinding(JToggleButton.class, formProperty);
    }

    public Binding createBoundCheckBox(String formProperty) {
        return createBinding(JCheckBox.class, formProperty);
    }

    public Binding createBoundComboBox(String formProperty) {
        return createBinding(JComboBox.class, formProperty);
    }

    /**
     *
     * @param formProperty the property to be bound
     * @param selectableItems a Collection or array containing the list of items
     * that may be selected
     */
    public Binding createBoundComboBox(String formProperty, Object selectableItems) {
        Map context = createContext(ComboBoxBinder.SELECTABLE_ITEMS_KEY, selectableItems);
        return createBinding(JComboBox.class, formProperty, context);
    }

    public Binding createBoundComboBox(String formProperty, ValueModel selectableItemsHolder) {
        return createBoundComboBox(formProperty, (Object)selectableItemsHolder);
    }

    public Binding createBoundComboBox(String formProperty, String selectableItemsProperty, String renderedItemProperty) {
        return createBoundComboBox(formProperty, getFormModel().getValueModel(selectableItemsProperty),
                renderedItemProperty);
    }

    public Binding createBoundComboBox(String formProperty, Object selectableItems, String renderedProperty) {
        Map context = createContext(ComboBoxBinder.SELECTABLE_ITEMS_KEY, selectableItems);
        context.put(ComboBoxBinder.RENDERER_KEY, new BeanPropertyValueListRenderer(renderedProperty));
        context.put(ComboBoxBinder.EDITOR_KEY, new BeanPropertyEditorClosure(renderedProperty));
        context.put(ComboBoxBinder.COMPARATOR_KEY, new PropertyComparator(renderedProperty, true, true));
        return createBinding(JComboBox.class, formProperty, context);
    }

    public Binding createBoundComboBox(String formProperty, ValueModel selectableItemsHolder, String renderedProperty) {
        return createBoundComboBox(formProperty, (Object)selectableItemsHolder, renderedProperty);
    }

    /**
     * This method will most likely move over to FormModel
     *
     * @deprecated
     */
    public ObservableList createBoundListModel(String formProperty) {
        final ConfigurableFormModel formModel = ((ConfigurableFormModel)getFormModel());
        ValueModel valueModel = formModel.getValueModel(formProperty);
        if (!(valueModel instanceof BufferedCollectionValueModel)) {
            // XXX: HACK!
            valueModel = new BufferedCollectionValueModel((((DefaultFormModel) formModel).getFormObjectPropertyAccessStrategy()).getPropertyValueModel(
                    formProperty), formModel.getFieldMetadata(formProperty).getPropertyType());
            formModel.add(formProperty, valueModel);
        }
        return (ObservableList)valueModel.getValue();
    }

    public Binding createBoundList(String formProperty) {
        Map context = createContext(ListBinder.SELECTABLE_ITEMS_KEY, createBoundListModel(formProperty));
        return createBinding(JList.class, formProperty, context);
    }

    /**
     * Binds the values specified in the collection contained within
     * <code>selectableItems</code> to a {@link JList}, with any
     * user selection being placed in the form property referred to by
     * <code>selectionFormProperty</code>.  Each item in the list will be
     * rendered as a String.  Note that the selection in the
     * bound list will track any changes to the
     * <code>selectionFormProperty</code>.  This is especially useful to
     * preselect items in the list - if <code>selectionFormProperty</code>
     * is not empty when the list is bound, then its content will be used
     * for the initial selection.  This method uses default behavior to
     * determine the selection mode of the resulting <code>JList</code>:
     * if <code>selectionFormProperty</code> refers to a
     * {@link java.util.Collection} type property, then
     * {@link javax.swing.ListSelectionModel#MULTIPLE_INTERVAL_SELECTION} will
     * be used, otherwise
     * {@link javax.swing.ListSelectionModel#SINGLE_SELECTION} will be used.
     *
     * @param selectionFormProperty form property to hold user's selection.
     *                              This property must either be compatible
     *                              with the item objects contained in
     *                              <code>selectableItemsHolder</code> (in
     *                              which case only single selection makes
     *                              sense), or must be a
     *                              <code>Collection</code> type, which allows
     *                              for multiple selection.
     * @param selectableItems 		a Collection or array containing the items
     *                              with which to populate the list.
     * @return
     */
    public Binding createBoundList(String selectionFormProperty, Object selectableItems) {
        return createBoundList(selectionFormProperty, new ValueHolder(selectableItems));
    }

    public Binding createBoundList(String selectionFormProperty, Object selectableItems, String renderedProperty) {
        return createBoundList(selectionFormProperty, new ValueHolder(selectableItems), renderedProperty);
    }

    /**
     * Binds the values specified in the collection contained within
     * <code>selectableItemsHolder</code> to a {@link JList}, with any
     * user selection being placed in the form property referred to by
     * <code>selectionFormProperty</code>.  Each item in the list will be
     * rendered by looking up a property on the item by the name contained
     * in <code>renderedProperty</code>, retrieving the value of the property,
     * and rendering that value in the UI.  Note that the selection in the
     * bound list will track any changes to the
     * <code>selectionFormProperty</code>.  This is especially useful to
     * preselect items in the list - if <code>selectionFormProperty</code>
     * is not empty when the list is bound, then its content will be used
     * for the initial selection.  This method uses default behavior to
     * determine the selection mode of the resulting <code>JList</code>:
     * if <code>selectionFormProperty</code> refers to a
     * {@link java.util.Collection} type property, then
     * {@link javax.swing.ListSelectionModel#MULTIPLE_INTERVAL_SELECTION} will
     * be used, otherwise
     * {@link javax.swing.ListSelectionModel#SINGLE_SELECTION} will be used.
     *
     * @param selectionFormProperty form property to hold user's selection.
     *                              This property must either be compatible
     *                              with the item objects contained in
     *                              <code>selectableItemsHolder</code> (in
     *                              which case only single selection makes
     *                              sense), or must be a
     *                              <code>Collection</code> type, which allows
     *                              for multiple selection.
     * @param selectableItemsHolder <code>ValueModel</code> containing the
     *                              items with which to populate the list.
     * @param renderedProperty      the property to be queried for each item
     *                              in the list, the result of which will be
     *                              used to render that item in the UI
     *
     * @return
     */
    public Binding createBoundList(String selectionFormProperty, ValueModel selectableItemsHolder,
            String renderedProperty) {
        return createBoundList(selectionFormProperty, selectableItemsHolder, renderedProperty, null);
    }

    /**
     * Binds the values specified in the collection contained within
     * <code>selectableItemsHolder</code> to a {@link JList}, with any
     * user selection being placed in the form property referred to by
     * <code>selectionFormProperty</code>.  Each item in the list will be
     * rendered as a String.  Note that the selection in the
     * bound list will track any changes to the
     * <code>selectionFormProperty</code>.  This is especially useful to
     * preselect items in the list - if <code>selectionFormProperty</code>
     * is not empty when the list is bound, then its content will be used
     * for the initial selection.  This method uses default behavior to
     * determine the selection mode of the resulting <code>JList</code>:
     * if <code>selectionFormProperty</code> refers to a
     * {@link java.util.Collection} type property, then
     * {@link javax.swing.ListSelectionModel#MULTIPLE_INTERVAL_SELECTION} will
     * be used, otherwise
     * {@link javax.swing.ListSelectionModel#SINGLE_SELECTION} will be used.
     *
     * @param selectionFormProperty form property to hold user's selection.
     *                              This property must either be compatible
     *                              with the item objects contained in
     *                              <code>selectableItemsHolder</code> (in
     *                              which case only single selection makes
     *                              sense), or must be a
     *                              <code>Collection</code> type, which allows
     *                              for multiple selection.
     * @param selectableItemsHolder <code>ValueModel</code> containing the
     *                              items with which to populate the list.
     *
     * @return
     */
    public Binding createBoundList(String selectionFormProperty, ValueModel selectableItemsHolder) {
        return createBoundList(selectionFormProperty, selectableItemsHolder, null, null);
    }

    /**
     * Binds the value(s) specified in <code>selectableItems</code> to
     * a {@link JList}, with any
     * user selection being placed in the form property referred to by
     * <code>selectionFormProperty</code>.  Each item in the list will be
     * rendered by looking up a property on the item by the name contained
     * in <code>renderedProperty</code>, retrieving the value of the property,
     * and rendering that value in the UI.  Note that the selection in the
     * bound list will track any changes to the
     * <code>selectionFormProperty</code>.  This is especially useful to
     * preselect items in the list - if <code>selectionFormProperty</code>
     * is not empty when the list is bound, then its content will be used
     * for the initial selection.
     *
     * @param selectionFormProperty form property to hold user's selection.
     *                              This property must either be compatible
     *                              with the item objects contained in
     *                              <code>selectableItemsHolder</code> (in
     *                              which case only single selection makes
     *                              sense), or must be a
     *                              <code>Collection</code> type, which allows
     *                              for multiple selection.
     * @param selectableItems       <code>Object</code> containing the
     *                              item(s) with which to populate the list.
     *                              Can be an instance Collection, Object[],
     *                              a ValueModel or Object
     * @param renderedProperty      the property to be queried for each item
     *                              in the list, the result of which will be
     *                              used to render that item in the UI.
     *                              May be null, in which case the selectable
     *                              items will be rendered as strings.
     * @param forceSelectMode       forces the list selection mode.  Must be
     *                              one of the constants defined in
     *                              {@link javax.swing.ListSelectionModel} or
     *                              <code>null</code> for default behavior.
     *                              If <code>null</code>, then
     *                              {@link javax.swing.ListSelectionModel#MULTIPLE_INTERVAL_SELECTION}
     *                              will be used if
     *                              <code>selectionFormProperty</code> refers
     *                              to a {@link java.util.Collection} type
     *                              property, otherwise
     *                              {@link javax.swing.ListSelectionModel#SINGLE_SELECTION}
     *                              will be used.
     *
     * @return
     */
    public Binding createBoundList(String selectionFormProperty, Object selectableItems,
            String renderedProperty, Integer forceSelectMode) {
        final Map context = new HashMap();
        if (forceSelectMode != null) {
            context.put(ListBinder.SELECTION_MODE_KEY, forceSelectMode);
        }
        context.put(ListBinder.SELECTABLE_ITEMS_KEY, selectableItems);
        if (renderedProperty != null) {
            context.put(ListBinder.RENDERER_KEY, new BeanPropertyValueListRenderer(renderedProperty));
            context.put(ListBinder.COMPARATOR_KEY, new PropertyComparator(renderedProperty, true, true));
        }
        return createBinding(JList.class, selectionFormProperty, context);
    }

    /**
     * Binds the values specified in the collection contained within
     * <code>selectableItemsHolder</code> to a {@link ShuttleList}, with any
     * user selection being placed in the form property referred to by
     * <code>selectionFormProperty</code>. Each item in the list will be
     * rendered by looking up a property on the item by the name contained in
     * <code>renderedProperty</code>, retrieving the value of the property,
     * and rendering that value in the UI.
     * <p>
     * Note that the selection in the bound list will track any changes to the
     * <code>selectionFormProperty</code>. This is especially useful to
     * preselect items in the list - if <code>selectionFormProperty</code> is
     * not empty when the list is bound, then its content will be used for the
     * initial selection.
     *
     * @param selectionFormProperty form property to hold user's selection. This
     *        property must be a <code>Collection</code> or array type.
     * @param selectableItemsHolder <code>ValueModel</code> containing the
     *        items with which to populate the list.
     * @param renderedProperty the property to be queried for each item in the
     *        list, the result of which will be used to render that item in the
     *        UI. May be null, in which case the selectable items will be
     *        rendered as strings.
     * @return constructed {@link Binding}. Note that the bound control is of
     *         type {@link ShuttleList}. Access this component to set specific
     *         display properties.
     */
    public Binding createBoundShuttleList( String selectionFormProperty, ValueModel selectableItemsHolder,
            String renderedProperty ) {
        Map context = ShuttleListBinder.createBindingContext(getFormModel(), selectionFormProperty,
                selectableItemsHolder, renderedProperty);
        return createBinding(ShuttleList.class, selectionFormProperty, context);
    }

    /**
     * Binds the values specified in the collection contained within
     * <code>selectableItems</code> (which will be wrapped in a
     * {@link ValueHolder} to a {@link ShuttleList}, with any user selection
     * being placed in the form property referred to by
     * <code>selectionFormProperty</code>. Each item in the list will be
     * rendered by looking up a property on the item by the name contained in
     * <code>renderedProperty</code>, retrieving the value of the property,
     * and rendering that value in the UI.
     * <p>
     * Note that the selection in the bound list will track any changes to the
     * <code>selectionFormProperty</code>. This is especially useful to
     * preselect items in the list - if <code>selectionFormProperty</code> is
     * not empty when the list is bound, then its content will be used for the
     * initial selection.
     *
     * @param selectionFormProperty form property to hold user's selection. This
     *        property must be a <code>Collection</code> or array type.
     * @param selectableItems Collection or array containing the items with
     *        which to populate the selectable list (this object will be wrapped
     *        in a ValueHolder).
     * @param renderedProperty the property to be queried for each item in the
     *        list, the result of which will be used to render that item in the
     *        UI. May be null, in which case the selectable items will be
     *        rendered as strings.
     * @return constructed {@link Binding}. Note that the bound control is of
     *         type {@link ShuttleList}. Access this component to set specific
     *         display properties.
     */
    public Binding createBoundShuttleList( String selectionFormProperty, Object selectableItems, String renderedProperty ) {
        return createBoundShuttleList(selectionFormProperty, new ValueHolder(selectableItems), renderedProperty);
    }

    /**
     * Binds the values specified in the collection contained within
     * <code>selectableItems</code> (which will be wrapped in a
     * {@link ValueHolder} to a {@link ShuttleList}, with any user selection
     * being placed in the form property referred to by
     * <code>selectionFormProperty</code>. Each item in the list will be
     * rendered as a String.
     * <p>
     * Note that the selection in the bound list will track any changes to the
     * <code>selectionFormProperty</code>. This is especially useful to
     * preselect items in the list - if <code>selectionFormProperty</code> is
     * not empty when the list is bound, then its content will be used for the
     * initial selection.
     *
     * @param selectionFormProperty form property to hold user's selection. This
     *        property must be a <code>Collection</code> or array type.
     * @param selectableItems Collection or array containing the items with
     *        which to populate the selectable list (this object will be wrapped
     *        in a ValueHolder).
     * @return constructed {@link Binding}. Note that the bound control is of
     *         type {@link ShuttleList}. Access this component to set specific
     *         display properties.
     */
    public Binding createBoundShuttleList( String selectionFormProperty, Object selectableItems ) {
        return createBoundShuttleList(selectionFormProperty, new ValueHolder(selectableItems), null);
    }

    /**
     * @see #createBinding(String, String, Map)
     */
    public Binding createBinding(String propertyPath, String binderId)
    {
        return this.createBinding(propertyPath, binderId, Collections.EMPTY_MAP);
    }

    /**
     * Create a binding based on a specific binder id.
     *
     * @param propertyPath Path to property
     * @param binderId Id of the binder
     * @param context Context data (can be empty map)
     * @return Specific binding
     */
    public Binding createBinding(String propertyPath, String binderId, Map context)
    {
        Assert.notNull(context, "Context must not be null");
        Binder binder = ((SwingBinderSelectionStrategy)getBinderSelectionStrategy()).getIdBoundBinder(binderId);
        Binding binding = binder.bind(getFormModel(), propertyPath, context);
        interceptBinding(binding);
        return binding;
    }

    protected static class BeanPropertyEditorClosure implements Closure {

        private final String renderedProperty;

        public BeanPropertyEditorClosure(String renderedProperty) {
            this.renderedProperty = renderedProperty;
        }

        public Object call(Object argument) {
            Assert.isInstanceOf(ComboBoxEditor.class, argument);
            return new BeanPropertyValueComboBoxEditor((ComboBoxEditor) argument, renderedProperty);
        }

        String getRenderedProperty() {
            return renderedProperty;
        }

    }
}
