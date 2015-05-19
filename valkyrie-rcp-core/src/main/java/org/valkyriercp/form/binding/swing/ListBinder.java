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

import javax.swing.*;
import java.util.Map;

/**
 * @author Oliver Hutchison
 */
public class ListBinder extends AbstractListBinder {
    public static final String RENDERER_KEY = "renderer";

    public static final String SELECTION_MODE_KEY = "selectionMode";

    private ListCellRenderer renderer;

    private Integer selectionMode;

    public ListBinder() {
        this(null, new String[] { SELECTABLE_ITEMS_KEY, COMPARATOR_KEY, RENDERER_KEY, FILTER_KEY, SELECTION_MODE_KEY });
    }

    public ListBinder(String[] supportedContextKeys) {
        this(null, supportedContextKeys);
    }

    public ListBinder(Class requiredSourceClass, String[] supportedContextKeys) {
        super(requiredSourceClass, supportedContextKeys);
    }

    protected AbstractListBinding createListBinding(JComponent control, FormModel formModel, String formPropertyPath) {
        Assert.isInstanceOf(JList.class, control);
        return new ListBinding((JList) control, formModel, formPropertyPath, getRequiredSourceClass());
    }

    protected void applyContext(AbstractListBinding binding, Map context) {
        super.applyContext(binding, context);
        ListBinding listBinding = (ListBinding) binding;
        if (context.containsKey(RENDERER_KEY)) {
            listBinding.setRenderer((ListCellRenderer) decorate(context.get(RENDERER_KEY), listBinding.getRenderer()));
        } else if (renderer != null) {
            listBinding.setRenderer((ListCellRenderer) decorate(renderer, listBinding.getRenderer()));
        }
        if (context.containsKey(SELECTION_MODE_KEY)) {
            Object contextSelectionMode = context.get(SELECTION_MODE_KEY);
            if (contextSelectionMode instanceof Integer) {
                listBinding.setSelectionMode(((Integer) contextSelectionMode).intValue());
            } else {
                try {
                    listBinding.setSelectionMode(((Integer) ListSelectionModel.class.getField(
                            (String) contextSelectionMode).get(null)).intValue());
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Unable to access selection mode field in ListSelectionModel", e);
                } catch (NoSuchFieldException e) {
                    throw new IllegalArgumentException("Unknown selection mode '" + contextSelectionMode + "'", e);
                }
            }
        } else if (selectionMode != null) {
            listBinding.setSelectionMode(selectionMode.intValue());
        }
    }

    protected void applyContext(ListBinding binding, Map context) {
        if (context.containsKey(SELECTION_MODE_KEY)) {
        }
    }

    protected JComponent createControl(Map context) {
        return getComponentFactory().createList();
    }

    public ListCellRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(ListCellRenderer renderer) {
        this.renderer = renderer;
    }

    public Integer getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(Integer selectionMode) {
        this.selectionMode = selectionMode;
    }

}
