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
package org.valkyriercp.form.binding.jide;

import com.jidesoft.swing.CheckBoxList;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.value.support.ListListModel;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

public class CheckBoxListBinding<T> extends CustomBinding {
    private CheckBoxList list;
    private List<T> possibleValues;

    private boolean scrollPaneNeeded;

    public CheckBoxListBinding(FormModel formModel, String formPropertyPath, List<T> possibleValues) {
        super(formModel, formPropertyPath, List.class);
        this.possibleValues = possibleValues;
    }

    protected CheckBoxListBinding(FormModel formModel, String formPropertyPath) {
        super(formModel, formPropertyPath, List.class);
    }

    private CheckBoxList createList() {
        final CheckBoxList list = new CheckBoxList(new ListListModel(getPossibleValues()));
        list.setCellRenderer(getRenderer());
        return list;
    }

    public List<T> getPossibleValues() {
        return possibleValues;
    }

    public ListCellRenderer getRenderer() {
        return new DefaultListCellRenderer();
    }

    @Override
    protected void valueModelChanged(Object newValue) {
        List<T> selected = (List<T>) newValue;
        if (selected == null || selected.size() == 0) {
            list.getCheckBoxListSelectionModel().clearSelection();
            list.revalidate();
        } else {
            list.getCheckBoxListSelectionModel().clearSelection();
            for (T value : selected) {
                 list.addCheckBoxListSelectedValue(value, false);
            }
//            int[] indices = new int[selected.size()];
//            int i = 0;
//            for (Enum anEnum : selected) {
//                indices[i++] = possibleValues.indexOf(anEnum);
//            }
//            list.setCheckBoxListSelectedIndices(indices);
//            list.revalidate();
        }
    }

    @Override
    protected JComponent doBindControl() {
        list = createList();
        list.getCheckBoxListSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                controlValueChanged(getSelected(list));
            }
        });
        if(isScrollPaneNeeded())
            return new JScrollPane(list);
        else
            return list;
    }

    private List<T> getSelected(CheckBoxList list) {
        Object[] selected = list.getCheckBoxListSelectedValues();
        List<T> selectedValues = new ArrayList<T>(selected.length);
        for (Object aSelected : selected) {
            T enumValue = (T) aSelected;
            selectedValues.add(enumValue);
        }
        return selectedValues;
    }

    @Override
    protected void readOnlyChanged() {
        list.setEnabled(!isReadOnly());
    }

    @Override
    protected void enabledChanged() {
        list.setEnabled(isEnabled());
    }

    public boolean isScrollPaneNeeded() {
        return scrollPaneNeeded;
    }

    public void setScrollPaneNeeded(boolean scrollPaneNeeded) {
        this.scrollPaneNeeded = scrollPaneNeeded;
    }
}
