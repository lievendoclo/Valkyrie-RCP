package org.valkyriercp.list;

import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.core.Guarded;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SingleListSelectionGuard implements PropertyChangeListener {
    private ValueModel selectionIndexHolder;

    private Guarded guarded;

    public SingleListSelectionGuard(ValueModel selectionIndexHolder, Guarded guarded) {
        this.selectionIndexHolder = selectionIndexHolder;
        this.selectionIndexHolder.addValueChangeListener(this);
        this.guarded = guarded;
        propertyChange(null);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        Integer value = (Integer)selectionIndexHolder.getValue();
        if (value == null || value.intValue() == -1) {
            guarded.setEnabled(false);
        }
        else {
            guarded.setEnabled(true);
        }
    }
}
