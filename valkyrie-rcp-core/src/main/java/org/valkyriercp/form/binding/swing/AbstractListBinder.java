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

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;
import org.valkyriercp.rules.closure.Closure;
import org.valkyriercp.rules.constraint.Constraint;

import javax.swing.*;
import java.util.Comparator;
import java.util.Map;

/**
 * Abstract base class for list bindings.
 * <p>
 * The property <code>selectableItems</code> is used as a source of list values. the value of
 * <code>selectableItems</code> is converted to {@link javax.swing.ListModel} by using the conversion service
 * <p>
 * The items of <code>selectableItems</code> can be filtered by defining a constraint for <code>filter</code> and
 * sorted by defining <code>comparator</code>. Dynamic filtering and sorting is supported if the values of
 * <code>filter</code> or <code>comparator</code> implements {@link java.util.Observable}
 * <p>
 * Every value for <code>selectableItems</code>, <code>filter</code> and <code>comparator</code> can be defined
 * for all created binding instances or by using a context map where the keys <code>SELECTABLE_ITEMS_KEY</code>,
 * <code>COMPARATOR_KEY</code> and <code>FILTER_KEY</code> can be used for specifying the context dependent values.
 * <p>
 * If the context values of <code>SELECTABLE_ITEMS_KEY</code>, <code>COMPARATOR_KEY</code> and
 * <code>FILTER_KEY</code> implement {@link Closure} it will be used to create the instance for the value by passing
 * the value of <code>selectableItems</code>, <code>filter</code> or <code>comparator</code> as the argument.<br/>
 * Please take into account that the argument for the closure can be null if the property is not set. This feature is
 * usefull to chain filter constraints.
 * <p>
 * Subclasses must implement {@link #createListBinding(javax.swing.JComponent, FormModel, String)} which creates the instance for
 * {@link AbstractListBinding}. {@link #applyContext(AbstractListBinding, java.util.Map)} can be overwritten to apply additional
 * context values
 *
 * @author Mathias Broekelmann
 *
 */
public abstract class AbstractListBinder extends AbstractBinder {

    /**
     * key for defining a context dependent selectableItems value. See class description for details.
     */
    public static final String SELECTABLE_ITEMS_KEY = "selectableItems";

    /**
     * key for defining a context dependent comparator value that is used to sort the elements of selectableItems.
     * Values must be instances of {@link java.util.Comparator}. See class description for more details.
     */
    public static final String COMPARATOR_KEY = "comparator";

    /**
     * key for defining a context dependent filter constraint value that is used to filter the elements of
     * selectableItems. Values must be instances of {@link Constraint}. See class description for more details.
     */
    public static final String FILTER_KEY = "filter";

    private Object selectableItems;

    private Comparator comparator;

    private Constraint filter;

    /**
     * Creates a new instance by defining a type for the form fields and using the default context keys
     * <code>SELECTABLE_ITEMS_KEY</code>, <code>COMPARATOR_KEY</code> and <code>FILTER_KEY</code>
     *
     * @param requiredSourceClass
     *            the type of the form fields to bind, if null form fields can have any type
     */
    public AbstractListBinder(Class requiredSourceClass) {
        this(requiredSourceClass, new String[] { SELECTABLE_ITEMS_KEY, COMPARATOR_KEY, FILTER_KEY });
    }

    /**
     * Creates a new instance by defining a type for the form fields and using the given context keys
     *
     * @param requiredSourceClass
     *            the type of the form fields to bind, if null form fields can have any type
     * @param supportedContextKeys
     *            the keys which can be defined as context values
     *
     * @throws NullPointerException
     *             if supportedContextKeys is null
     */
    public AbstractListBinder(Class requiredSourceClass, String[] supportedContextKeys) {
        super(requiredSourceClass, supportedContextKeys);
    }

    /**
     * Returns the {@link Comparator} which is used for bindings. The value can be overwritten with a context value for
     * <code>COMPARATOR_KEY</code>
     *
     * @return the comparator. If null no comparator is defined
     */
    public Comparator getComparator() {
        return comparator;
    }

    /**
     * Defines the {@link Comparator} which is used for bindings. The value can be overwritten with a context value for
     * <code>COMPARATOR_KEY</code>
     *
     * @param comparator
     *            the comparator. If null no comparator will be used
     */
    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    /**
     * Returns the {@link Constraint} which is used as a filter for the selectable items. The value can be overwritten
     * with a context value for <code>FILTER_KEY</code>
     *
     * @return the filter. If null no filter is defined
     */
    public Constraint getFilter() {
        return filter;
    }

    /**
     * Defines the {@link Constraint} which is used as a filter for the selectable items. The value can be overwritten
     * with a context value for <code>FILTER_KEY</code>
     *
     * @param filter
     *            the filter constraint. If null no filter will be used
     */
    public void setFilter(Constraint filter) {
        this.filter = filter;
    }

    /**
     * Returns the selectable items which where used as a source for the selectable items. The value can be overwritten
     * with a context value for <code>SELECTABLE_ITEMS_KEY</code>
     *
     * @return the selectable items. If null no selectable items will be used
     */
    public Object getSelectableItems() {
        return selectableItems;
    }

    /**
     * Defines the selectable items which where used as a source for the selectable items. The value can be overwritten
     * with a context value for <code>SELECTABLE_ITEMS_KEY</code>
     *
     * @param selectableItems
     *            the selectable items. If null no selectable items will be used
     */
    public void setSelectableItems(Object selectableItems) {
        this.selectableItems = selectableItems;
    }

    /**
     * Creates the binding and applies any context values
     */
    protected final Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        AbstractListBinding binding = createListBinding(control, formModel, formPropertyPath);
        Assert.notNull(binding);
        applyContext(binding, context);
        return binding;
    }

    /**
     * Called to create the binding instance
     *
     * @param control
     *            the control to bind
     * @param formModel
     *            the formmodel with the value of the <code>formPropertyPath</code> field
     * @param formPropertyPath
     *            the field path to bind
     * @return the binding instance. Must not be null
     */
    protected abstract AbstractListBinding createListBinding(JComponent control, FormModel formModel,
            String formPropertyPath);

    /**
     * Applies any context or preset value.
     *
     * @param binding
     *            the binding to apply the values
     * @param context
     *            contains context dependent values
     */
    protected void applyContext(AbstractListBinding binding, Map context) {
        if (context.containsKey(SELECTABLE_ITEMS_KEY)) {
            binding.setSelectableItems(decorate(context.get(SELECTABLE_ITEMS_KEY), selectableItems));
        } else if (selectableItems != null) {
            binding.setSelectableItems(selectableItems);
        }
        if (context.containsKey(COMPARATOR_KEY)) {
            binding.setComparator((Comparator) decorate(context.get(COMPARATOR_KEY), comparator));
        } else if (comparator != null) {
            binding.setComparator(comparator);
        }
        if (context.containsKey(FILTER_KEY)) {
            binding.setFilter((Constraint) decorate(context.get(FILTER_KEY), filter));
        } else if (filter != null) {
            binding.setFilter(filter);
        }
    }

    /**
     * Decorates an object instance if the <code>closure</code> value is an instance of {@link Closure}.
     *
     * @param closure
     *            the closure which is used to decorate the object.
     * @param object
     *            the value to decorate
     * @return the decorated instance if <code>closure</code> implements {@link Closure}, otherwise the value of
     *         <code>object</code>
     */
    protected Object decorate(Object closure, Object object) {
        if (closure instanceof Closure) {
            return ((Closure) closure).call(object);
        }
        return closure;
    }
}

