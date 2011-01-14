package org.valkyriercp.form.binding.swing;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.list.AbstractFilteredListModel;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * TODO: support for filters
 *
 * @author Oliver Hutchison
 */
public class ComboBoxBinding extends AbstractListBinding {

    private Object emptySelectionValue;

    private BoundComboBoxModel boundModel;

    public ComboBoxBinding(FormModel formModel, String formPropertyPath) {
        this(new JComboBox(), formModel, formPropertyPath, null);
    }

    public ComboBoxBinding(JComboBox comboBox, FormModel formModel, String formPropertyPath) {
        this(comboBox, formModel, formPropertyPath, null);
    }

    public ComboBoxBinding(JComboBox comboBox, FormModel formModel, String formPropertyPath, Class requiredSourceClass) {
        super(comboBox, formModel, formPropertyPath, requiredSourceClass);
    }

    protected void doBindControl(ListModel bindingModel) {
        boundModel = new BoundComboBoxModel(bindingModel);
        getComboBox().setModel(boundModel);
    }

    protected ListModel getDefaultModel() {
        return getComboBox().getModel();
    }

    public ListCellRenderer getRenderer() {
        return getComboBox().getRenderer();
    }

    public JComboBox getComboBox() {
        return (JComboBox) getComponent();
    }

    public void setRenderer(ListCellRenderer renderer) {
        getComboBox().setRenderer(renderer);
    }

    public void setEditor(ComboBoxEditor comboBoxEditor) {
        getComboBox().setEditor(comboBoxEditor);
    }

    public ComboBoxEditor getEditor() {
        return getComboBox().getEditor();
    }

    private class BoundComboBoxModel extends AbstractFilteredListModel implements ComboBoxModel, PropertyChangeListener {

        public BoundComboBoxModel(ListModel listModel) {
            super(listModel);
            getValueModel().addValueChangeListener(this);
        }

        public int getSize() {
            if (emptySelectionValue != null) {
                return super.getSize() + 1;
            }
            return super.getSize();
        }

        public Object getElementAt(int index) {
            if (emptySelectionValue != null) {
                if (index == 0)
                    return emptySelectionValue;
                return super.getElementAt(index - 1);
            }
            return super.getElementAt(index);
        }

        private boolean updateSelectedItem() {
            Object selectedItem = getSelectedItem();
            if (selectedItem != null) {
                boolean found = false;
                for (int i = 0, size = getSize(); i < size && !found; i++) {
                    found = selectedItem.equals(getElementAt(i));
                }
                if (!found) {
                    setSelectedItem(null);
                    return true;
                }
            }
            return false;
        }

        public void contentsChanged(ListDataEvent e) {
            if (updateSelectedItem()) {
                fireContentsChanged(this, -1, -1);
            } else {
                super.contentsChanged(e);
            }
        }

        public void intervalRemoved(ListDataEvent e) {
            if (updateSelectedItem()) {
                fireContentsChanged(this, -1, -1);
            } else {
                super.intervalRemoved(e);
            }
        }

        public void setSelectedItem(Object selectedItem) {
            if (selectedItem == emptySelectionValue) {
                selectedItem = null;
            }
            getValueModel().setValueSilently(selectedItem, this);
            fireContentsChanged(this, -1, -1);
        }

        public Object getSelectedItem() {
            Object value = getValue();
            if(emptySelectionValue != null && value == null) {
                return emptySelectionValue;
            }
            return value;
        }

        public void propertyChange(PropertyChangeEvent evt) {
            fireContentsChanged(this, -1, -1);
        }

        public void emptySelectionValueChanged() {
            fireContentsChanged(this, -1, -1);
        }
    }

    /**
     * @param value
     */
    public void setEmptySelectionValue(Object value) {
        if (value != emptySelectionValue && boundModel != null) {
            emptySelectionValue = value;
            boundModel.emptySelectionValueChanged();
        } else {
            emptySelectionValue = value;
        }
    }

    public Object getEmptySelectionValue() {
        return emptySelectionValue;
    }
}
