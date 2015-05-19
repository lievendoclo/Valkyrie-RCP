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
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.ListListModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

/**
 * A list whose contents are dynamically refreshable.
 *
 * @author Keith Donald
 */
public class DynamicListModel extends ListListModel implements PropertyChangeListener {
    private static final Log logger = LogFactory.getLog(DynamicListModel.class);

    private ValueModel listItemsValueModel;

    public DynamicListModel(ValueModel listItemsValueModel) {
        super();
        setListItemsValueModel(listItemsValueModel);
    }

    public void setListItemsValueModel(ValueModel valueModel) {
        if (this.listItemsValueModel == valueModel) {
            return;
        }
        if (this.listItemsValueModel != null) {
            this.listItemsValueModel.removeValueChangeListener(this);
        }
        this.listItemsValueModel = valueModel;
        if (this.listItemsValueModel != null) {
            doAdd((Collection)valueModel.getValue());
            this.listItemsValueModel.addValueChangeListener(this);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (logger.isDebugEnabled()) {
            logger.debug("Backing collection of items changed; refreshing list model.");
        }
        doAdd((Collection)listItemsValueModel.getValue());
    }

    private void doAdd(Collection c) {
        clear();
        if (c != null) {
            addAll(c);
        }
        sort();
    }

}