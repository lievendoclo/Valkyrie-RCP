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
package org.valkyriercp.component;

import org.valkyriercp.application.support.MessageResolver;
import org.valkyriercp.form.binding.swing.EnumComboBoxBinder;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EnumListRenderer extends JLabel implements ListCellRenderer
{
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

    private Class enumType;

    public EnumListRenderer(Class enumType)
    {
        super();
        this.enumType = enumType;
        setOpaque(true);
        setBorder(getNoFocusBorder());
    }

    private static Border getNoFocusBorder()
    {
        if (System.getSecurityManager() != null)
        {
            return SAFE_NO_FOCUS_BORDER;
        }
        else
        {
            return noFocusBorder;
        }
    }

    public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus)
    {
        Enum valueEnum = (Enum) value;

        if (isSelected)
        {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }
        else
        {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        String label = "";
        Icon icon;
        if (value == null || value == EnumComboBoxBinder.NullEnum.NULL)
        {
            label = getMessageResolver().getMessage(enumType.getName() + ".null");
            icon = getIconSource().getIcon(enumType.getName() + ".null");
        }
        else
        {
            label = getMessageResolver().getMessage(enumType.getName() + "." + valueEnum.name());
            icon = getIconSource().getIcon(enumType.getName() + "." + valueEnum.name());
        }
        setText(label);
        setIcon(icon);
        setEnabled(list.isEnabled());
        setFont(list.getFont());

        Border border = null;
        if (cellHasFocus)
        {
            if (isSelected)
            {
                border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
            }
            if (border == null)
            {
                border = UIManager.getBorder("List.focusCellHighlightBorder");
            }
        }
        else
        {
            border = getNoFocusBorder();
        }
        setBorder(border);

        return this;
    }

    protected MessageResolver getMessageResolver() {
        return ValkyrieRepository.getInstance().getApplicationConfig().messageResolver();
    }

    protected IconSource getIconSource() {
        return ValkyrieRepository.getInstance().getApplicationConfig().iconSource();
    }

}

