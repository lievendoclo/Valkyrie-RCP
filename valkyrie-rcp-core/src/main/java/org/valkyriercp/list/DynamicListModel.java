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