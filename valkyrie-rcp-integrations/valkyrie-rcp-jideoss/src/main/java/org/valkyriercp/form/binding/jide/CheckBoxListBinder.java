package org.valkyriercp.form.binding.jide;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class CheckBoxListBinder<T> implements Binder {
    private List<T> selectableValues;
    private boolean scrollPaneNeeded;

    public CheckBoxListBinder(List<T> selectableValues) {
        this.selectableValues = selectableValues;
    }

    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context) {
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
