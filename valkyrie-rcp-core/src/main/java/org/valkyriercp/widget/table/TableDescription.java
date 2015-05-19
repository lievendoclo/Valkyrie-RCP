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
package org.valkyriercp.widget.table;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.Comparator;


/**
 * TableDescription
 */
public interface TableDescription
{
    /**
     * @return Type van row objects
     */
    Class getDataType();

    /**
     * De properties to be included in the text filter
     */
    String[] getPropertiesInTextFilter();

    /**
     * Datatype for a column
     */
    Class getType(int propertyIndex);

    /**
     * Column header.
     */
    String getHeader(int propertyIndex);

    /**
     * Value of a columnn for a certain row object
     */
    Object getValue(Object rowObject, int propertyIndex);

    /**
     * Sets the value of a column of a certain row object
     */
    void setValue(Object rowObject, int propertyIndex, Object newValue);

    /**
     * Maximum width for a column
     */
    int getMaxColumnWidth(int propertyIndex);

    /**
     * Minimum width for a column
     */
    int getMinColumnWidth(int propertyIndex);

    /**
     * If the column is resizable
     */
    boolean isResizable(int propertyIndex);

    /**
     * The column renderer for a column
     */
    TableCellRenderer getColumnRenderer(int propertyIndex);

    /**
     * The cell editorr for a column
     */
    TableCellEditor getColumnEditor(int propertyIndex);

    /**
     * Whether this column is a selection column
     */
    boolean isSelectColumn(int propertyIndex);

    /**
     * The comparator for a column
     */
    Comparator getColumnComparator(int propertyIndex);

    /**
     * The default comparator
     */
    Comparator getDefaultComparator();

    /**
     * Returns the column count
     */
    int getColumnCount();

    /**
     * @return TRUE if the table has a selection column
     */
    boolean hasSelectColumn();

    /**
     * Whether the column is initially visible or not
     */
    boolean isVisible(int propertyIndex);

}
