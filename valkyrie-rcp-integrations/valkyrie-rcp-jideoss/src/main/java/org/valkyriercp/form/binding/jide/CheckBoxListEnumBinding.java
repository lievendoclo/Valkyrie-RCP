package org.valkyriercp.form.binding.jide;

import com.google.common.base.Strings;
import com.jidesoft.swing.CheckBoxList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.component.EnumListRenderer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Binding for a list of Enum values. Shows all possible enum values in a checkbox list and binds on the selected ones.
 */
public class CheckBoxListEnumBinding extends CheckBoxListBinding<Enum> {
    private Logger log = LoggerFactory.getLogger(getClass());

    private CheckBoxList list;
    private Class<? extends Enum> enumClass;
    private List<Enum> possibleValues;

    private boolean scrollPaneNeeded;

    protected CheckBoxListEnumBinding(FormModel formModel, String formPropertyPath, Class<? extends Enum> enumClass) {
        super(formModel, formPropertyPath);
        this.enumClass = enumClass;
    }

    public List<Enum> getPossibleValues() {
        List<Enum> out = new ArrayList<Enum>();
        for (Enum e : enumClass.getEnumConstants()) {
            String desc = getApplicationConfig().messageResolver().getMessage(enumClass.getName() + "." + e.name());
            if (!Strings.isNullOrEmpty(desc)) {
                out.add(e);
            } else {
                log.warn("No message found for: " + e + ", ignoring!");
            }
        }
        return out;
    }

    @Override
    public ListCellRenderer getRenderer() {
        return new EnumListRenderer(enumClass);
    }
}

