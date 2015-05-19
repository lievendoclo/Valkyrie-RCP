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
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.component.ShuttleList;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;
import org.valkyriercp.list.BeanPropertyValueListRenderer;

import javax.swing.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Binder for handling ShuttleList component. Use the following keys to
 * configure the produced binding:
 * <dt><code>SELECTABLE_ITEMS_HOLDER_KEY</code></dt>
 * <dd> to specify the list of "available" source values (this may be a
 * Collection or an array).</dd>
 * <p>
 * <dt><code>SELECTED_ITEMS_HOLDER_KEY</code></dt>
 * <dd>to specify the value holder into which the selected items will be placed
 * (this may be a Collection or an array). Initially, this set must contain only
 * values that exist in the selectable set.</dd>
 * <p>
 * <dt><code>SELECTED_ITEM_TYPE_KEY</code></dt>
 * <dd>to specify the underlying type of the elements in the selected and
 * selectable value sets.</dd>
 * <p>
 * <dt><code>COMPARATOR_KEY</code></dt>
 * <dd>to specify the Comparator to use for comparing elements in the selected
 * and selectable value sets.</dd>
 * <p>
 * <dt><code>RENDERER_KEY</code></dt>
 * <dd>to specify the a {@link javax.swing.ListCellRenderer} for elements of the value
 * sets. This is typically used if the String value of the elements is not
 * appropriate for use in the shuttle lists.</dd>
 * <p>
 * <dt><code>FORM_ID</code></dt>
 * <dd>to specify formId in which this ShuttleList appears, this allow
 * form-specific settings like the texts and icon.</dd>
 *
 * In order to have this Binder selected properly when adding ShuttleList
 * components to a form, you will need to have the following context
 * configuration:
 *
 * <pre>
 * &lt;bean id=&quot;binderSelectionStrategy&quot; class=&quot;org.springframework.richclient.form.binding.swing.SwingBinderSelectionStrategy&quot;&gt;
 *     &lt;property name=&quot;bindersForControlTypes&quot;&gt;
 *         &lt;map&gt;
 *             &lt;entry&gt;
 *                 &lt;key&gt;
 *                     &lt;value type=&quot;java.lang.Class&quot;&gt;org.springframework.richclient.components.ShuttleList&lt;/value&gt;
 *                 &lt;/key&gt;
 *                 &lt;bean class=&quot;org.springframework.richclient.components.ShuttleListBinder&quot; /&gt;
 *             &lt;/entry&gt;
 *         &lt;/map&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 *
 * <p>
 * Also, see {@link org.valkyriercp.form.binding.swing.SwingBindingFactoryProvider} and
 * {@link org.valkyriercp.form.binding.swing.SwingBindingFactoryProvider} for how to configure and use that binding
 * factory, which offers convenience methods for constructing a bound shuttle
 * list. With the provider, factory, and binder registered, the following code
 * can be used to add a bound shuttle list to a form:
 *
 * <pre>
 * final SandboxSwingBindingFactory bf = (SandboxSwingBindingFactory) getBindingFactory();
 * TableFormBuilder formBuilder = new TableFormBuilder( bf );
 * ...
 * String[] languages = new String[] { &quot;java&quot;, &quot;perl&quot;, &quot;ruby&quot;, &quot;C#&quot; };
 * List languagesList = Arrays.asList( languages );
 * formBuilder.add( bf.createBoundShuttleList( &quot;languageSkills&quot;, languagesList ), &quot;align=left&quot; );
 * </pre>
 *
 * @author Larry Streepy
 * @author Benoit Xhenseval
 */
public class ShuttleListBinder extends AbstractBinder {

    public static final String SELECTABLE_ITEMS_HOLDER_KEY = "selectableItemsHolder";

    public static final String SELECTED_ITEMS_HOLDER_KEY = "selectedItemHolder";

    public static final String SELECTED_ITEM_TYPE_KEY = "selectedItemType";

    public static final String MODEL_KEY = "model";

    public static final String FORM_ID = "formId";

    public static final String COMPARATOR_KEY = "comparator";

    public static final String RENDERER_KEY = "renderer";

    private boolean showEditButton = true;

    public void setShowEditButton(boolean showEditButton) {
        this.showEditButton = showEditButton;
    }

    /**
     * Utility method to construct the context map used to configure instances
     * of {@link ShuttleListBinding} created by this binder.
     * <p>
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
     */
    public static Map createBindingContext( FormModel formModel, String selectionFormProperty,
            ValueModel selectableItemsHolder, String renderedProperty ) {

        final Map context = new HashMap(4);

        context.put(ShuttleListBinder.FORM_ID, formModel.getId());

        final ValueModel selectionValueModel = formModel.getValueModel(selectionFormProperty);
        context.put(SELECTED_ITEMS_HOLDER_KEY, selectionValueModel);

        final Class selectionPropertyType = formModel.getFieldMetadata(selectionFormProperty).getPropertyType();
        if( selectionPropertyType != null ) {
            context.put(SELECTED_ITEM_TYPE_KEY, selectionPropertyType);
        }

        context.put(SELECTABLE_ITEMS_HOLDER_KEY, selectableItemsHolder);

        if( renderedProperty != null ) {
            context.put(RENDERER_KEY, new BeanPropertyValueListRenderer(renderedProperty));
            context.put(COMPARATOR_KEY, new PropertyComparator(renderedProperty, true, true));
        }

        return context;
    }

    /**
     * Constructor.
     */
    public ShuttleListBinder() {
        super(null, new String[] { SELECTABLE_ITEMS_HOLDER_KEY, SELECTED_ITEMS_HOLDER_KEY, SELECTED_ITEM_TYPE_KEY,
                MODEL_KEY, COMPARATOR_KEY, RENDERER_KEY, FORM_ID });
    }

    /**
     * Constructor allowing the specification of additional/alternate context keys.  This
     * is for use by derived classes.
     *
     * @param supportedContextKeys Context keys supported by subclass
     */
    protected ShuttleListBinder( final String[] supportedContextKeys ) {
        super(null, supportedContextKeys);
    }

    /**
     * @inheritDoc
     */
    protected Binding doBind( JComponent control, FormModel formModel, String formPropertyPath, Map context ) {
        Assert.isTrue(control instanceof ShuttleList, formPropertyPath);
        ShuttleListBinding binding = new ShuttleListBinding((ShuttleList) control, formModel, formPropertyPath);
        applyContext(binding, context);
        return binding;
    }

    /**
     * Apply the values from the context to the specified binding.
     *
     * @param binding Binding to update
     * @param context Map of context values to apply to the binding
     */
    protected void applyContext( ShuttleListBinding binding, Map context ) {
        if( context.containsKey(MODEL_KEY) ) {
            binding.setModel((ListModel) context.get(MODEL_KEY));
        }
        if( context.containsKey(SELECTABLE_ITEMS_HOLDER_KEY) ) {
            binding.setSelectableItemsHolder((ValueModel) context.get(SELECTABLE_ITEMS_HOLDER_KEY));
        }
        if( context.containsKey(SELECTED_ITEMS_HOLDER_KEY) ) {
            binding.setSelectedItemsHolder((ValueModel) context.get(SELECTED_ITEMS_HOLDER_KEY));
        }
        if( context.containsKey(RENDERER_KEY) ) {
            binding.setRenderer((ListCellRenderer) context.get(RENDERER_KEY));
        }
        if( context.containsKey(COMPARATOR_KEY) ) {
            binding.setComparator((Comparator) context.get(COMPARATOR_KEY));
        }
        if( context.containsKey(SELECTED_ITEM_TYPE_KEY) ) {
            binding.setSelectedItemType((Class) context.get(SELECTED_ITEM_TYPE_KEY));
        }
        if( context.containsKey(FORM_ID) ) {
            binding.setFormId((String) context.get(FORM_ID));
        }
    }

    /**
     * @inheritDoc
     */
    protected JComponent createControl( Map context ) {
        ShuttleList shuttleList = new ShuttleList(showEditButton);
        return shuttleList;
    }

}

