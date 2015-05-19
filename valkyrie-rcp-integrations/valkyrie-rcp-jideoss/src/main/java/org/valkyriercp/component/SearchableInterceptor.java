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

import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.TableSearchable;
import org.valkyriercp.form.builder.AbstractFormComponentInterceptor;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * Interceptor that installs the <code>Searchable</code> functionality provided by Jide OSS.
 * <p />
 * This class implements singleton.
 *
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Arg????ello (JAF)</a>
 */
public final class SearchableInterceptor extends AbstractFormComponentInterceptor {

    /**
     * The singleton instance.
     */
    private static final SearchableInterceptor INSTANCE = new SearchableInterceptor();

    /**
     * This class implements singleton.
     */
    private SearchableInterceptor() {

    }

    /**
     * Installs a <code>Searchable</code> into the given combobox.
     *
     * @param comboBox
     *            the target combobox.
     * @return the target combobox.
     */
    private JComboBox installSearchable(JComboBox comboBox) {

        SearchableUtils.installSearchable(comboBox);

        return comboBox;
    }

    /**
     * Installs a <code>Searchable</code> into the given list.
     *
     * @param list
     *            the target list.
     * @return the target list.
     */
    private JList installSearchable(JList list) {

        SearchableUtils.installSearchable(list);

        return list;
    }

    /**
     * Installs a <code>Searchable</code> into the given table.
     *
     * @param table
     *            the target table.
     * @return the target table.
     */
    private JTable installSearchable(JTable table) {

        final TableSearchable tableSearchable = SearchableUtils.installSearchable(table);

        // Search for all columns
        tableSearchable.setMainIndex(-1);

        return table;
    }

    /**
     * Installs a <code>Searchable</code> into the given text component.
     *
     * @param <T>
     *            the type of the text component.
     *
     * @param textComponent
     *            the target text component.
     * @return the text component.
     */
    private <T extends JTextComponent> T installSearchable(T textComponent) {

        SearchableUtils.installSearchable(textComponent);

        return textComponent;
    }

    /**
     * Installs a <code>Searchable</code> into the given component.
     *
     * @param propertyName
     *            the property name.
     * @param component
     *            the target component.
     */
    @Override
    public void processComponent(String propertyName, JComponent component) {

        if (component instanceof JComboBox) {
            this.installSearchable((JComboBox) component);
        } else if (component instanceof JList) {
            this.installSearchable((JList) component);
        } else if (component instanceof JTable) {
            this.installSearchable((JTable) component);
        } else if (component instanceof JTextComponent) {
            this.installSearchable((JTextComponent) component);
        }
    }

    /**
     * Gets the singleton instance.
     *
     * @return the singleton instance.
     */
    public static SearchableInterceptor getInstance() {

        return SearchableInterceptor.INSTANCE;
    }
}