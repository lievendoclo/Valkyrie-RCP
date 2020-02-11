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

import org.valkyriercp.convert.ConversionException;
import org.valkyriercp.convert.ConversionService;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.form.binding.support.AbstractBinding;
import org.valkyriercp.list.AbstractFilteredListModel;
import org.valkyriercp.list.DefaultFilteredListModel;
import org.valkyriercp.list.SortedListModel;
import org.valkyriercp.rules.constraint.Constraint;
import org.valkyriercp.util.ReflectiveVisitorHelper;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Mathias Broekelmann
 *
 */
public abstract class AbstractListBinding extends AbstractBinding {

    private JComponent component;

    final ReflectiveVisitorHelper visitorHelper = new ReflectiveVisitorHelper();

    private final SelectableItemsVisitor selectableItemsVisitor = new SelectableItemsVisitor();

    private Object selectableItems;

    private final FilterConstraint filterConstraint = new FilterConstraint();

    private final BindingComparator bindingComparator = new BindingComparator();

    private ListModel bindingModel;

    private AbstractFilteredListModel filteredModel;

    public AbstractListBinding(JComponent component, FormModel formModel, String formPropertyPath,
            Class requiredSourceClass) {
        super(formModel, formPropertyPath, requiredSourceClass);
        this.component = component;
    }

    protected void enabledChanged() {
        component.setEnabled(!isReadOnly() && isEnabled());
    }

    protected void readOnlyChanged() {
        enabledChanged();
    }

    public Object getSelectableItems() {
        return selectableItems;
    }

    public JComponent getComponent() {
        return component;
    }

    public final void setSelectableItems(Object selectableItems) {
        Assert.notNull(selectableItems);
        if (!selectableItems.equals(this.selectableItems)) {
            this.selectableItems = selectableItems;
            selectableItemsChanged();
        }
    }

    protected ConversionService getConversionService() {
        return ValkyrieRepository.getInstance().getApplicationConfig().conversionService();
    }

    public final void setComparator(Comparator comparator) {
        bindingComparator.setComparator(comparator);
    }

    public Comparator getComparator() {
        return bindingComparator.getComparator();
    }

    public final void setFilter(Constraint filter) {
        filterConstraint.setFilter(filter);
    }

    protected final JComponent doBindControl() {
        doBindControl(getBindingModel());
        return getComponent();
    }

    protected abstract void doBindControl(ListModel bindingModel);

    protected void selectableItemsChanged() {
        if (filteredModel != null) {
            filteredModel.setFilteredModel(createModel());
        }
    }

    protected ListModel createModel() {
        return (ListModel) visitorHelper.invokeVisit(selectableItemsVisitor, selectableItems);
    }

    protected AbstractFilteredListModel getFilteredModel() {
        if (filteredModel == null) {
            filteredModel = createFilteredModel(createModel(), filterConstraint);
        }
        return filteredModel;
    }

    protected ListModel getBindingModel() {
        if (bindingModel == null) {
            bindingModel = createSortedListModel(getFilteredModel(), bindingComparator);
        }
        return bindingModel;
    }

    protected ListModel createSortedListModel(ListModel listModel, Comparator comparator) {
        return new SortedListModel(listModel, comparator);
    }

    protected AbstractFilteredListModel createFilteredModel(ListModel model, Constraint constraint) {
        return new DefaultFilteredListModel(model, constraint);
    }

    /**
     * Converts the given object value into the given targetClass
     *
     * @param value
     *            the value to convert
     * @param targetClass
     *            the target class to convert the value to
     * @return the converted value
     *
     * @throws org.valkyriercp.convert.ConversionException
     *             if the value can not be converted
     */
    protected Object convertValue(Object value, Class targetClass) throws ConversionException {
        Assert.notNull(value);
        Assert.notNull(targetClass);
        return getConversionService().getConversionExecutor(value.getClass(), targetClass).execute(value);
    }

    protected abstract ListModel getDefaultModel();

    public Constraint getFilter() {
        return filterConstraint.getFilter();
    }

    class SelectableItemsVisitor {

        ListModel visit(ValueModel valueModel) {
            Assert.notNull(valueModel.getValue(),
                    "value of ValueModel must not be null. Use an empty Collection or Array");
            ListModel model = (ListModel) visitorHelper.invokeVisit(this, valueModel.getValue());
            return new ValueModelFilteredListModel(model, valueModel);
        }

        ListModel visit(Object object) {
            return (ListModel) convertValue(object, ListModel.class);
        }

        ListModel visitNull() {
            return getDefaultModel();
        }
    }

    class ValueModelFilteredListModel extends AbstractFilteredListModel implements PropertyChangeListener {

        private final ValueModel valueModel;

        public ValueModelFilteredListModel(ListModel model, ValueModel valueModel) {
            super(model);
            this.valueModel = valueModel;
            valueModel.addValueChangeListener(this);
        }

        public void propertyChange(PropertyChangeEvent evt) {
            setFilteredModel((ListModel) visitorHelper.invokeVisit(selectableItemsVisitor, valueModel.getValue()));
        }

    }

    class FilterConstraint extends Observable implements Constraint, Observer {

        private Constraint filter;

        public boolean test(Object argument) {
            if (filter != null) {
                return filter.test(argument);
            }
            return true;
        }

        public Constraint getFilter() {
            return filter;
        }

        public void setFilter(Constraint filter) {
            if (filter != this.filter || (filter != null && !filter.equals(this.filter))) {
                if (this.filter instanceof Observable) {
                    ((Observable) this.filter).deleteObserver(this);
                }
                this.filter = filter;
                if (filter instanceof Observable) {
                    ((Observable) this.filter).addObserver(this);
                }
                update();
            }
        }

        public void update() {
            setChanged();
            notifyObservers();
        }

        public void update(Observable o, Object arg) {
            update();
        }
    }

    class BindingComparator extends Observable implements Comparator, Observer {

        private Comparator comparator;

        public int compare(Object o1, Object o2) {
            if (comparator != null) {
                return comparator.compare(o1, o2);
            }
            return 0;
        }

        public void setComparator(Comparator comparator) {
            if (comparator != this.comparator || (comparator != null && !comparator.equals(this.comparator))) {
                if (this.comparator instanceof Observable) {
                    ((Observable) this.comparator).deleteObserver(this);
                }
                this.comparator = comparator;
                if (comparator instanceof Observable) {
                    ((Observable) this.comparator).addObserver(this);
                }
                update();
            }
        }

        public Comparator getComparator() {
            return comparator;
        }

        void update() {
            setChanged();
            notifyObservers();
        }

        public void update(Observable o, Object arg) {
            update();
        }

    }
}
