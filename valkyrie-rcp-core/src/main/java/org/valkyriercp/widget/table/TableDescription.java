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
