package org.valkyriercp.widget.table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.comparator.ComparableComparator;
import org.valkyriercp.application.support.MessageResolver;
import org.valkyriercp.util.MessageConstants;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * PropertyColumnTableDescription is the default implementation of {@link TableDescription} which uses class
 * introspection to create columns. Each column is based on a property of the class and thus should be
 * accessible through an isXXX or getXXX method.
 * </p>
 *
 * <p>
 * Additionally writing capabilities are available by providing a specific {@link javax.swing.table.TableCellEditor}. When
 * using this table description with a detail form and a backing formModel, take a look at the
 * {@link ValueModelTableCellEditor}.
 * </p>
 *
 * <p>
 * Nested properties can be accessed using the dot notation: 'propertyA.propertyB'. At any point in this
 * chaining getting a null value will stop evaluating the expression and returns null.
 * </p>
 *
 * Typical usage of a {@link PropertyColumnTableDescription}:
 *
 * <pre>
 * PropertyColumnTableDescription tableDesc = new PropertyColumnTableDescription(&quot;id&quot;, MyType.class);
 * tableDesc.addPropertyColumn(&quot;propertyA&quot;);
 * tableDesc.addPropertyColumn(&quot;propertyB&quot;).addRenderer(MySpecificTableCellRenderer);
 * tableDesc.addPropertyColumn(&quot;propertyC&quot;).addMinWidth(25).addMaxWidth(100);
 * </pre>
 *
 * <p>
 * More possibilities and additional addXXX methods can be found in the {@link PropertyColumn} class.
 * </p>
 *
 * NOTE: this class provides a number of addPropertyColumn(...) methods to add columns. As there are many
 * features on a column, this led to a huge number of these constructions. To simplify the
 * {@link PropertyColumnTableDescription} class and the adding of new features, a new way of column creation
 * has been implemented with the addXXX methods on the {@link PropertyColumn} class. New features will be
 * exclusive to the addXXX methods.
 *
 */
@Configurable
public class PropertyColumnTableDescription implements TableDescription
{

    /** Logging facility. */
    static Log log = LogFactory.getLog(PropertyColumnTableDescription.class);

    /** Expected number of columns, used to create the backing collections. */
    private static final int DEFAULT_SIZE = 10;

    /** List of columns for this table description. */
    private List<PropertyColumn> columns;

    /** Id for this table description used to create message keys. */
    private final String id;

    /** Type of a row item. Used to fetch the get/set methods. */
    private final Class entityClass;

    /** Comparator to use as default (when list is set or other specific sorting is removed). */
    private Comparator defaultComparator;

    /**
     * A table can contain one column with checkboxes that holds a selection. At the moment the specific table
     * implementation is responsible of filtering the table rows and checking which rows are selected.
     */
    private boolean hasSelectColumn = false;

    @Autowired
    private MessageResolver messageResolver;

    /**
     * @see #PropertyColumnTableDescription(String, Class, int, Comparator)
     */
    public PropertyColumnTableDescription(Class forEntityType)
    {
        this(null, forEntityType);
    }

    /**
     * @see #PropertyColumnTableDescription(String, Class, int, Comparator)
     */
    public PropertyColumnTableDescription(Class forEntityType, Comparator defaultComparator)
    {
        this(null, forEntityType, defaultComparator);
    }

    /**
     * @see #PropertyColumnTableDescription(String, Class, int, Comparator)
     */
    public PropertyColumnTableDescription(final String id, Class forEntityType, Comparator defaultComparator)
    {
        this(id, forEntityType, DEFAULT_SIZE, defaultComparator);
    }

    /**
     * @see #PropertyColumnTableDescription(String, Class, int, Comparator)
     */
    public PropertyColumnTableDescription(final String id, Class forEntityType)
    {
        this(id, forEntityType, new ComparableComparator());
    }

    /**
     * @see #PropertyColumnTableDescription(String, Class, int, Comparator)
     */
    public PropertyColumnTableDescription(final String id, Class forEntityType, int numberOfColumns)
    {
        this(id, forEntityType, numberOfColumns, new ComparableComparator());
    }

    /**
     * Constructor creating a {@link PropertyColumnTableDescription} based on the given type. Each column
     * created will need to have a corresponding property path on this type.
     *
     * @param id
     *            id used to fetch messages for the column headers.
     * @param forEntityType
     *            type of the row item.
     * @param numberOfColumns
     *            expected number of columns.
     * @param defaultComparator
     *            comparator for the default sorting. Initial table will be sorted according to this
     *            comparator as well as when all other specific sorting is removed.
     *
     */
    public PropertyColumnTableDescription(final String id, Class forEntityType, int numberOfColumns,
            Comparator defaultComparator)
    {
        this.id = id;
        this.entityClass = forEntityType;
        this.defaultComparator = defaultComparator;
        this.columns = new ArrayList<PropertyColumn>(numberOfColumns);
    }

    /**
     * @see #addPropertyColumn(String, Class)
     */
    public PropertyColumn addPropertyColumn(String propertyName)
    {
        return addPropertyColumn(propertyName, (Class<?>) null);
    }

    /**
     * Create and add a column for the given property. Property type is passed or determined (when
     * <code>null</code>) by examining the {@link Accessor} and headerKeys are added based upon the id of
     * the {@link PropertyColumnTableDescription}, the propertyName and the postfix "HEADER".
     *
     * @param propertyName
     *            name of the property.
     * @param propertyType
     *            type of the property. If <code>null</code> a type will be determined by examining the
     *            accessor method.
     */
    public PropertyColumn addPropertyColumn(String propertyName, Class<?> propertyType)
    {
        String[] headerKeys = messageResolver.getMessageKeys(this.id, propertyName, MessageConstants.HEADER);
        Accessor accessor = ClassUtils.getAccessorForProperty(entityClass, propertyName);
        if (propertyType == null)
            propertyType = accessor.getPropertyType();
        PropertyColumn propertyColumn = new PropertyColumn(propertyName, accessor, propertyType);
        if (String.class.isAssignableFrom(propertyColumn.getType()))
            propertyColumn.setFilterColumn(true);
        propertyColumn.setHeaderKeys(headerKeys);
        columns.add(propertyColumn);
        return propertyColumn;
    }

    public void setPropertyColumns(Collection<PropertyColumn> propertyColumns)
    {
        this.columns = new ArrayList<PropertyColumn>(propertyColumns);
        for (PropertyColumn propertyColumn : columns)
        {
            Accessor accessorForProperty = ClassUtils.getAccessorForProperty(entityClass, propertyColumn.getPropertyName());
            propertyColumn.setAccessor(accessorForProperty);
            if(propertyColumn.getComparator() == null)
                propertyColumn.setComparator(getDefaultComparator());
            propertyColumn.setHeaderKeys(messageResolver.getMessageKeys(this.id, propertyColumn.getPropertyName(), MessageConstants.HEADER));
        }
    }

    /**
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withComparator(Comparator)
     * @see PropertyColumn#withFixedWidth(int)
     */
    public PropertyColumn addPropertyColumn(String propertyName, int width, Comparator comparator)
    {
        return addPropertyColumn(propertyName).withFixedWidth(width).withComparator(comparator);
    }

    /**
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withRenderer(javax.swing.table.TableCellRenderer)
     */
    public PropertyColumn addPropertyColumn(String propertyName, TableCellRenderer renderer)
    {
        return addPropertyColumn(propertyName).withRenderer(renderer);
    }

    /**
     * WARNING: propertyType is discarded, it should be fetched from the entityType through introspection.
     *
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withEditor(javax.swing.table.TableCellEditor)
     */
    public void addPropertyColumn(String propertyName, Class propertyType, TableCellEditor editor)
    {
        addPropertyColumn(propertyName).withEditor(editor);
    }

    /**
     * WARNING: propertyType is discarded, it should be fetched from the entityType through introspection.
     *
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withRenderer(TableCellRenderer)
     */
    public void addPropertyColumn(String propertyName, Class propertyType, TableCellRenderer renderer)
    {
        addPropertyColumn(propertyName).withRenderer(renderer);
    }

    /**
     * WARNING: propertyType is discarded, it should be fetched from the entityType through introspection.
     *
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withMinWidth(int)
     * @see PropertyColumn#withMaxWidth(int)
     */
    public void addPropertyColumn(String propertyName, Class propertyType, int minWidth, int maxWidth)
    {
        addPropertyColumn(propertyName).withMinWidth(minWidth).withMaxWidth(maxWidth);
    }

    /**
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withFixedWidth(int)
     */
    public void addPropertyColumn(String propertyName, int width)
    {
        addPropertyColumn(propertyName).withFixedWidth(width);
    }

    /**
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withFixedWidth(int)
     * @see PropertyColumn#withVisible(boolean)
     */
    public void addPropertyColumn(String propertyName, int width, boolean visible)
    {
        addPropertyColumn(propertyName).withFixedWidth(width).withVisible(visible);
    }

    /**
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withFixedWidth(int)
     * @see PropertyColumn#withRenderer(TableCellRenderer)
     * @see PropertyColumn#withVisible(boolean)
     */
    public void addPropertyColumn(String propertyName, int width, TableCellRenderer renderer, boolean visible)
    {
        addPropertyColumn(propertyName).withFixedWidth(width).withRenderer(renderer).withVisible(visible);
    }

    /**
     * WARNING: propertyType is discarded, it should be fetched from the entityType through introspection.
     *
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withFixedWidth(int)
     * @see PropertyColumn#withRenderer(TableCellRenderer)
     * @see PropertyColumn#withVisible(boolean)
     */
    public void addPropertyColumn(String propertyName, Class propertyType, int width,
            TableCellRenderer renderer, boolean visible)
    {
        addPropertyColumn(propertyName).withFixedWidth(width).withRenderer(renderer).withVisible(visible);
    }

    /**
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withFixedWidth(int)
     * @see PropertyColumn#withRenderer(TableCellRenderer)
     */
    public void addPropertyColumn(String propertyName, int width, TableCellRenderer renderer)
    {
        addPropertyColumn(propertyName).withFixedWidth(width).withRenderer(renderer);
    }

    /**
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withMinWidth(int)
     * @see PropertyColumn#withMaxWidth(int)
     */
    public void addPropertyColumn(String propertyName, int minWidth, int maxWidth)
    {
        addPropertyColumn(propertyName).withMinWidth(minWidth).withMaxWidth(maxWidth);
    }

    /**
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withMinWidth(int)
     * @see PropertyColumn#withMaxWidth(int)
     * @see PropertyColumn#withResizable(boolean)
     * @see PropertyColumn#withRenderer(TableCellRenderer)
     */
    public void addPropertyColumn(String propertyName, int minWidth, int maxWidth, boolean resizable,
            TableCellRenderer renderer)
    {
        addPropertyColumn(propertyName).withMinWidth(minWidth).withMaxWidth(maxWidth).withResizable(resizable)
                .withRenderer(renderer);
    }

    /**
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withMinWidth(int)
     * @see PropertyColumn#withMaxWidth(int)
     * @see PropertyColumn#withResizable(boolean)
     * @see PropertyColumn#withRenderer(TableCellRenderer)
     * @see PropertyColumn#withComparator(Comparator)
     */
    public void addPropertyColumn(String propertyName, int minWidth, int maxWidth, boolean resizable,
            TableCellRenderer renderer, Comparator comparator)
    {
        addPropertyColumn(propertyName).withMinWidth(minWidth).withMaxWidth(maxWidth).withResizable(resizable)
                .withRenderer(renderer).withComparator(comparator);
    }

    /**
     * WARNING: propertyType is discarded, it should be fetched from the entityType through introspection.
     *
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withMinWidth(int)
     * @see PropertyColumn#withMaxWidth(int)
     * @see PropertyColumn#withResizable(boolean)
     * @see PropertyColumn#withRenderer(TableCellRenderer)
     */
    public void addPropertyColumn(String propertyName, Class propertyType, int minWidth, int maxWidth,
            boolean resizable, TableCellRenderer renderer)
    {
        addPropertyColumn(propertyName).withMinWidth(minWidth).withMaxWidth(maxWidth).withResizable(resizable)
                .withRenderer(renderer);
    }

    /**
     * WARNING: propertyType is discarded, it should be fetched from the entityType through introspection.
     *
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withComparator(Comparator)
     * @see PropertyColumn#withMinWidth(int)
     * @see PropertyColumn#withMaxWidth(int)
     * @see PropertyColumn#withResizable(boolean)
     * @see PropertyColumn#withRenderer(TableCellRenderer)
     */
    public void addPropertyColumn(String propertyName, Class propertyType, int minWidth, int maxWidth,
            boolean resizable, TableCellRenderer renderer, Comparator comparator)
    {
        addPropertyColumn(propertyName).withMinWidth(minWidth).withMaxWidth(maxWidth).withResizable(resizable)
                .withRenderer(renderer).withComparator(comparator);
    }

    /**
     * WARNING: propertyType is discarded, it should be fetched from the entityType through introspection.
     *
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withMinWidth(int)
     * @see PropertyColumn#withMaxWidth(int)
     * @see PropertyColumn#withResizable(boolean)
     */
    public void addPropertyColumn(String propertyName, Class propertyType, String[] headerKeys, int minWidth,
            int maxWidth, boolean resizable, Boolean isInTextFilter)
    {
        addPropertyColumn(propertyName).withHeaderKeys(headerKeys).withMinWidth(minWidth).withMaxWidth(maxWidth)
                .withResizable(resizable).withFilterColumn(isInTextFilter);
    }

    /**
     * WARNING: propertyType is discarded, it should be fetched from the entityType through introspection.
     *
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withMinWidth(int)
     * @see PropertyColumn#withMaxWidth(int)
     * @see PropertyColumn#withResizable(boolean)
     * @see PropertyColumn#withRenderer(TableCellRenderer)
     */
    public void addPropertyColumn(String propertyName, Class propertyType, String[] headerKeys, int minWidth,
            int maxWidth, boolean resizable, TableCellRenderer renderer, Boolean isInTextFilter)
    {
        addPropertyColumn(propertyName).withHeaderKeys(headerKeys).withMinWidth(minWidth).withMaxWidth(maxWidth)
                .withResizable(resizable).withRenderer(renderer).withFilterColumn(isInTextFilter);
    }

    /**
     * WARNING: propertyType is discarded, it should be fetched from the entityType through introspection.
     *
     * @deprecated
     * @see #addPropertyColumn(String)
     * @see PropertyColumn#withComparator(Comparator)
     * @see PropertyColumn#withMinWidth(int)
     * @see PropertyColumn#withMaxWidth(int)
     * @see PropertyColumn#withResizable(boolean)
     * @see PropertyColumn#withRenderer(TableCellRenderer)
     */
    public void addPropertyColumn(String propertyName, Class propertyType, String[] headerKeys, int minWidth,
            int maxWidth, boolean resizable, TableCellRenderer renderer, Boolean isInTextFilter,
            Comparator comparator)
    {
        addPropertyColumn(propertyName).withHeaderKeys(headerKeys).withMinWidth(minWidth).withMaxWidth(maxWidth)
                .withResizable(resizable).withRenderer(renderer).withComparator(comparator).withFilterColumn(
                        isInTextFilter);
    }

    public void addSelectPropertyColumn(String propertyName)
    {
        addSelectPropertyColumn(propertyName, PropertyColumn.UNSPECIFIED_WIDTH);
    }

    public void addSelectPropertyColumn(String propertyName, Comparator<?> comparator)
    {
        addSelectPropertyColumn(propertyName, PropertyColumn.UNSPECIFIED_WIDTH,
                PropertyColumn.UNSPECIFIED_WIDTH, comparator);
    }

    public void addSelectPropertyColumn(String propertyName, int width)
    {
        addSelectPropertyColumn(propertyName, width, width);
    }

    public void addSelectPropertyColumn(String propertyName, int minWidth, int maxWidth)
    {
        addSelectPropertyColumn(propertyName, minWidth, maxWidth, null);
    }

    public void addSelectPropertyColumn(String propertyName, int minWidth, int maxWidth, Comparator comparator)
    {
        addSelectPropertyColumn(propertyName, messageResolver.getMessageKeys(this.id, propertyName,
                MessageConstants.HEADER), minWidth, maxWidth, true, comparator);
    }

    public void addSelectPropertyColumn(String propertyName, String[] headerKeys, int minWidth, int maxWidth,
            boolean resizable, Comparator comparator)
    {
        if (hasSelectColumn)
            throw new IllegalArgumentException("Already a selectColumn specified, cannot set " + propertyName
                    + " as selectColumn");
        this.hasSelectColumn = true;
        Accessor propertyAccessor = ClassUtils.getWriterForProperty(entityClass, propertyName);
        JCheckBox cellEditorComponent = new JCheckBox();
        cellEditorComponent.setHorizontalAlignment(SwingConstants.CENTER);
        columns.add(0, new PropertyColumn(Boolean.class, propertyAccessor, propertyName, headerKeys,
                minWidth, maxWidth, resizable, null, new DefaultCellEditor(cellEditorComponent), comparator,
                true, true));
    }

    public void addSelectPropertyColumn(String propertyName, String[] headerKeys, int minWidth, int maxWidth,
            boolean resizable, Comparator comparator, boolean visible)
    {
        if (hasSelectColumn)
            throw new IllegalArgumentException("Already a selectColumn specified, cannot set " + propertyName
                    + " as selectColumn");
        this.hasSelectColumn = true;
        Accessor propertyAccessor = ClassUtils.getWriterForProperty(entityClass, propertyName);
        JCheckBox cellEditorComponent = new JCheckBox();
        cellEditorComponent.setHorizontalAlignment(SwingConstants.CENTER);
        columns.add(0, new PropertyColumn(Boolean.class, propertyAccessor, propertyName, headerKeys,
                minWidth, maxWidth, resizable, null, new DefaultCellEditor(cellEditorComponent), comparator,
                true, visible));
    }

    /**
     * @inheritDoc
     */
    public Class<?> getDataType()
    {
        return entityClass;
    }

    /**
     * @inheritDoc
     */
    public Comparator<?> getDefaultComparator()
    {
        return defaultComparator;
    }

    /**
     * Set the comparator to use as default (when table is filled or other specific sorting is removed).
     */
    public void setDefaultComparator(Comparator<?> defaultComparator)
    {
        this.defaultComparator = defaultComparator;
    }

    /**
     * @inheritDoc
     */
    public boolean hasSelectColumn()
    {
        return hasSelectColumn;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getPropertiesInTextFilter()
    {
        List<String> filterProperties = new ArrayList<String>(getColumnCount());
        for (PropertyColumn column : columns)
        {
            if (column.isFilterColumn())
                filterProperties.add(column.getPropertyName());
        }
        return filterProperties.toArray(new String[filterProperties.size()]);
    }

    /**
     * {@inheritDoc}
     */
    public int getColumnCount()
    {
        return this.columns.size();
    }

    /**
     * Returns the column at the provided index.
     *
     * @param propertyIndex
     *            column index.
     * @return PropertyColumn the corresponding column.
     */
    private PropertyColumn getPropertyColumn(int propertyIndex)
    {
        return this.columns.get(propertyIndex);
    }

    /**
     * {@inheritDoc}
     */
    public Object getValue(Object rowObject, int propertyIndex)
    {
        try
        {
            return getPropertyColumn(propertyIndex).getAccessor().getValue(rowObject);
        }
        catch (Exception e)
        {
            log.warn("Error reading property " + propertyIndex + " from object " + rowObject, e);
            throw new RuntimeException("Error reading property " + propertyIndex + " from object "
                    + rowObject, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setValue(Object rowObject, int propertyIndex, Object newValue)
    {
        try
        {
            Accessor accessor = getPropertyColumn(propertyIndex).getAccessor();
            if (accessor instanceof Writer)
                ((Writer) accessor).setValue(rowObject, newValue);
        }
        catch (Exception e)
        {
            log.warn("Error writing property " + propertyIndex + " to object " + rowObject
                    + " new value: " + newValue, e);
            throw new RuntimeException("Error writing property " + propertyIndex + " to object " + rowObject
                    + " new value: " + newValue, e);
        }
    }

    public String getHeader(int propertyIndex)
    {
        return getPropertyColumn(propertyIndex).getHeader();
    }

    /**
     * {@inheritDoc}
     */
    public Class getType(int propertyIndex)
    {
        return getPropertyColumn(propertyIndex).getType();
    }

    /**
     * @inheritDoc
     */
    public int getMinColumnWidth(int propertyIndex)
    {
        return getPropertyColumn(propertyIndex).getMinWidth();
    }

    /**
     * @inheritDoc
     */
    public int getMaxColumnWidth(int propertyIndex)
    {
        return getPropertyColumn(propertyIndex).getMaxWidth();
    }

    /**
     * @inheritDoc
     */
    public boolean isResizable(int propertyIndex)
    {
        return getPropertyColumn(propertyIndex).isResizable();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isVisible(int propertyIndex)
    {
        return getPropertyColumn(propertyIndex).isVisible();
    }

    /**
     * @inheritDoc
     */
    public TableCellRenderer getColumnRenderer(int propertyIndex)
    {
        return getPropertyColumn(propertyIndex).getRenderer();
    }

    /**
     * @inheritDoc
     */
    public TableCellEditor getColumnEditor(int propertyIndex)
    {
        return getPropertyColumn(propertyIndex).getEditor();
    }

    /**
     * @inheritDoc
     */
    public boolean isSelectColumn(int propertyIndex)
    {
        return getPropertyColumn(propertyIndex).isSelectColumn();
    }

    /**
     * @inheritDoc
     */
    public Comparator getColumnComparator(int propertyIndex)
    {
        return getPropertyColumn(propertyIndex).getComparator();
    }
}