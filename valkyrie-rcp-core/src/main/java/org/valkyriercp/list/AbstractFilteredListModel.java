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
package org.valkyriercp.list;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author Keith Donald
 */
public abstract class AbstractFilteredListModel extends AbstractListModel implements ListDataListener {
    protected final Log logger = LogFactory.getLog(getClass());

    protected ListModel filteredModel;

    public AbstractFilteredListModel(ListModel model) {
        Assert.notNull(model);
        this.filteredModel = model;
        this.filteredModel.addListDataListener(this);
    }

    public ListModel getFilteredModel() {
        return filteredModel;
    }

    public void setFilteredModel(ListModel model) {
        Assert.notNull(model);
        this.filteredModel.removeListDataListener(this);
        this.filteredModel = model;
        this.filteredModel.addListDataListener(this);
        fireContentsChanged(this, -1, -1);
    }

    public Object getElementAt(int index) {
        return filteredModel.getElementAt(getElementIndex(index));
    }

    public int getSize() {
        return filteredModel.getSize();
    }

    public void contentsChanged(ListDataEvent e) {
        fireContentsChanged(e.getSource(), e.getIndex0(), e.getIndex1());
    }

    public void intervalAdded(ListDataEvent e) {
        fireIntervalAdded(e.getSource(), e.getIndex0(), e.getIndex1());
    }

    public void intervalRemoved(ListDataEvent e) {
        fireIntervalRemoved(e.getSource(), e.getIndex0(), e.getIndex1());
    }

    /**
     * Returns the element index for a filtered index. This implementation returns the given value filteredIndex
     *
     * @param filteredIndex
     *            the filtered index
     * @return the value of filteredIndex
     */
    public int getElementIndex(int filteredIndex) {
        return filteredIndex;
    }
}
