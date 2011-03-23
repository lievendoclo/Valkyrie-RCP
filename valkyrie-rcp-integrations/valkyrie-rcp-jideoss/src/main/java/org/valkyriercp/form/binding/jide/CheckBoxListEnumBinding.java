package org.valkyriercp.form.binding.jide;

import com.jidesoft.swing.CheckBoxList;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.value.support.ListListModel;
import org.valkyriercp.component.EnumListRenderer;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Binding for a list of Enum values. Shows all possible enum values in a checkbox list and binds on the selected ones.
 */
public class CheckBoxListEnumBinding extends CheckBoxListBinding<Enum> {
    private Logger log = LoggerFactory.getLogger(getClass());

    private CheckBoxList list;
    private Class<Enum> enumClass;
    private List<Enum> possibleValues;

    private boolean scrollPaneNeeded;

    protected CheckBoxListEnumBinding(FormModel formModel, String formPropertyPath, Class<Enum> enumClass) {
        super(formModel, formPropertyPath);
        this.enumClass = enumClass;
    }

    public List<Enum> getPossibleValues() {
        List<Enum> out = new ArrayList<Enum>();
        for (Enum e : enumClass.getEnumConstants()) {
            String desc = applicationConfig.messageResolver().getMessage(enumClass.getName() + "." + e.name());
            if (!StringUtils.isEmpty(desc)) {
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

