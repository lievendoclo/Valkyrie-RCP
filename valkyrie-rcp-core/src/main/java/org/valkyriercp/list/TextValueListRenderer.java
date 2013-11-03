package org.valkyriercp.list;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract base class for ListCellRenderer that convert the cell value into a
 * String.
 * <p>
 * Subclasses need to override <code>getTextValue</code> which is responsible
 * for the conversion.
 *
 * @author oliverh
 */
public abstract class TextValueListRenderer extends DefaultListCellRenderer {

    /**
     * Template method to convert cell value into a String.
     *
     * @param value
     *            the cell value
     * @return the representation of value that should be rendered by this
     *         ListCellRenderer
     */
    protected abstract String getTextValue(Object value);

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
        setText(getTextValue(value));
        return this;
    }
}
