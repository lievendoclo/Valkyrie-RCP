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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.convert.ConversionException;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class ListBinding extends AbstractListBinding {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final Object[] EMPTY_VALUES = new Object[0];

    private final ListSelectionListener selectionListener = new SelectionListener();

    private final PropertyChangeListener valueModelListener = new ValueModelListener();

    private ConversionExecutor conversionExecutor;

    boolean selectingValues;

    public ListBinding(JList list, FormModel formModel, String formFieldPath, Class requiredSourceClass) {
        super(list, formModel, formFieldPath, requiredSourceClass);
    }

    public JList getList() {
        return (JList) getComponent();
    }

    public void setSelectionMode(int selectionMode) {
        Assert.isTrue(ListSelectionModel.SINGLE_SELECTION == selectionMode || isPropertyConversionExecutorAvailable());
        getList().setSelectionMode(selectionMode);
    }

    public int getSelectionMode() {
        return getList().getSelectionMode();
    }

    /**
     * Returns a conversion executor which converts a value of the given sourceType into the fieldType
     *
     * @return true if a converter is available, otherwise false
     *
     * @see #getPropertyType()
     */
    protected ConversionExecutor getPropertyConversionExecutor() {
        if (conversionExecutor == null) {
            conversionExecutor = getConversionService().getConversionExecutor(Object[].class, getPropertyType());
        }
        return conversionExecutor;
    }

    protected boolean isPropertyConversionExecutorAvailable() {
        try {
            getConversionService().getConversionExecutor(Object[].class, getPropertyType());
        } catch (IllegalArgumentException e) {
            return false;
        } catch (ConversionException e) {
            return false;
        }
        return true;
    }

    protected void updateSelectedItemsFromSelectionModel() {
        if (getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
            Object singleValue = getList().getSelectedValue();
            Class propertyType = getPropertyType();
            if(singleValue == null || propertyType.isAssignableFrom(singleValue.getClass())) {
                getValueModel().setValueSilently(singleValue, valueModelListener);
            } else {
                getValueModel().setValueSilently(convertValue(singleValue, propertyType), valueModelListener);
            }
        } else {
            Object[] values = getList().getSelectedValues();
            getValueModel().setValueSilently(convertSelectedValues(values), valueModelListener);
        }
    }

    /**
     * Converts the given values into a type that matches the fieldType
     *
     * @param selectedValues
     *            the selected values
     * @return the value which can be assigned to the type of the field
     */
    protected Object convertSelectedValues(Object[] selectedValues) {
        return getPropertyConversionExecutor().execute(selectedValues);
    }

    protected void doBindControl(ListModel bindingModel) {
        JList list = getList();
        list.setModel(bindingModel);
        list.getSelectionModel().addListSelectionListener(selectionListener);
        getValueModel().addValueChangeListener(valueModelListener);
        if (!isPropertyConversionExecutorAvailable() && getSelectionMode() != ListSelectionModel.SINGLE_SELECTION) {
            if (logger.isWarnEnabled()) {
                logger.warn("Selection mode for list field " + getProperty() + " forced to single selection."
                        + " If multiple selection is needed use a collection type (List, Collection, Object[])"
                        + " or provide a suitable converter to convert Object[] instances to property type "
                        + getPropertyType());
            }
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        updateSelectedItemsFromValueModel();
    }

    /**
     * Updates the selection model with the selected values from the value model.
     */
    protected void updateSelectedItemsFromValueModel() {
        Object value = getValue();
        Object[] selectedValues = EMPTY_VALUES;
        if (value != null) {
            selectedValues = (Object[]) convertValue(value, Object[].class);
        }

        // flag is used to avoid a round trip while we are selecting the values
        selectingValues = true;
        try {
            ListSelectionModel selectionModel = getList().getSelectionModel();
            selectionModel.setValueIsAdjusting(true);
            try {
                int[] valueIndexes = determineValueIndexes(selectedValues);
                int selectionMode = getSelectionMode();
                if (selectionMode == ListSelectionModel.SINGLE_SELECTION && valueIndexes.length > 1) {
                    getList().setSelectedIndex(valueIndexes[0]);
                } else {
                    getList().setSelectedIndices(valueIndexes);
                }

                // update value model if selectedValues contain elements which where not found in the list model
                // elements
                if (valueIndexes.length != selectedValues.length && !isReadOnly() && isEnabled()
                        || (selectionMode == ListSelectionModel.SINGLE_SELECTION && valueIndexes.length > 1)) {
                    updateSelectedItemsFromSelectionModel();
                }
            } finally {
                selectionModel.setValueIsAdjusting(false);
            }
        } finally {
            selectingValues = false;
        }
    }

    /**
     * @param values
     * @return
     */
    protected int[] determineValueIndexes(Object[] values) {
        int[] indexes = new int[values.length];
        if (values.length == 0)
            return indexes;

        Collection lookupValues = new HashSet(Arrays.asList(values));
        ListModel model = getList().getModel();
        int i = 0;
        for (int index = 0, size = model.getSize(); index < size && !lookupValues.isEmpty(); index++) {
            if (lookupValues.remove(model.getElementAt(index))) {
                indexes[i++] = index;
            }
        }
        int[] result;
        if (i != values.length) {
            result = new int[i];
            System.arraycopy(indexes, 0, result, 0, i);
        } else {
            result = indexes;
        }
        return result;
    }

    public void setRenderer(ListCellRenderer renderer) {
        getList().setCellRenderer(renderer);
    }

    public ListCellRenderer getRenderer() {
        return getList().getCellRenderer();
    }

    protected class SelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            if (!selectingValues && !e.getValueIsAdjusting()) {
                updateSelectedItemsFromSelectionModel();
            }
        }

    }

    protected class ValueModelListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            updateSelectedItemsFromValueModel();
        }

    }

    protected ListModel getDefaultModel() {
        return getList().getModel();
    }
}