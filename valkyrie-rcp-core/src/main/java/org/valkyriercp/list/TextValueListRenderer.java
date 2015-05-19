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
