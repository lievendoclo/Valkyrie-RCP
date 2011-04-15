package org.valkyriercp.form.binding.jide;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;
import java.util.Map;

/**
 * Binder for a list of Enum values. Shows all possible enum values in a checkbox list and binds on the selected ones.
 */
public class CheckBoxListEnumBinder implements Binder {
    private Class<? extends Enum> enumClass;

    private boolean scrollPaneNeeded = true;

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context) {
        CheckBoxListEnumBinding binding = new CheckBoxListEnumBinding(formModel, formPropertyPath, enumClass);
        binding.setScrollPaneNeeded(scrollPaneNeeded);
        return binding;
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