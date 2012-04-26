package org.valkyriercp.form.binding.jide;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class CheckBoxListBinder<T> implements Binder {
    public static final String SELECTABLE_VALUES_KEY = "selectableValues";
    public static final String SCROLLPANE_NEEDED_KEY = "scrollPaneNeeded";
    
    private List<T> selectableValues;
    private boolean scrollPaneNeeded;

    public CheckBoxListBinder(List<T> selectableValues) {
        this.selectableValues = selectableValues;
    }

    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context) {
        Boolean scrollPaneNeeded;
        if(context.containsKey(SCROLLPANE_NEEDED_KEY)) {
            scrollPaneNeeded = (Boolean) context.get(SCROLLPANE_NEEDED_KEY);
        } else {
            scrollPaneNeeded = this.scrollPaneNeeded;
        }
        List<T> selectableValues;
        if(context.containsKey(SELECTABLE_VALUES_KEY)) {
            selectableValues = (List<T>) context.get(SELECTABLE_VALUES_KEY);
        } else {
            selectableValues = this.selectableValues;
        }
        CheckBoxListBinding<T> tCheckBoxListBinding = new CheckBoxListBinding<T>(formModel, formPropertyPath, selectableValues);
        
        tCheckBoxListBinding.setScrollPaneNeeded(scrollPaneNeeded);
        return tCheckBoxListBinding;
    }

    @Override
    public Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        throw new UnsupportedOperationException("This binder creates its own control");
    }

    public boolean isScrollPaneNeeded() {
        return scrollPaneNeeded;
    }

    public void setScrollPaneNeeded(boolean scrollPaneNeeded) {
        this.scrollPaneNeeded = scrollPaneNeeded;
    }
}
