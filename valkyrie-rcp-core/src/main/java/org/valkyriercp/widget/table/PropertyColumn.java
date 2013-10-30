package org.valkyriercp.widget.table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.valkyriercp.application.support.MessageResolver;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.BindingFactory;
import org.valkyriercp.form.binding.swing.SwingBindingFactory;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.Comparator;

/**
 * A column descriptor to access a specific property. Minimal configuration includes the propertyName and an
 * {@link Accessor}. Other features can be set using the normal bean setters and getters or through a number
 * of chaining methods. The latter is particularly useful to create columns by instantiating the column with
 * the minimum requirements and then adding all settings in one go.
 * 
 * @author Jan Hoskens
 * @since 0.5.0
 */
@Configurable
public class PropertyColumn
{

    /** No specific width set. */
    public static final int UNSPECIFIED_WIDTH = -1;

    /** Type of the property. */
    private Class<?> type;

    /** Accessor for the property. */
    private Accessor accessor;

    /** Name of the property. */
    private String propertyName;

    /** Header keys used to fetch the column header label from the message resource. */
    private String[] headerKeys;

    /** Maximum width of the column. */
    private int maxWidth = UNSPECIFIED_WIDTH;

    /** Minimum width of the column. */
    private int minWidth = UNSPECIFIED_WIDTH;

    /** Is it possible to resize column? */
    private boolean resizable = true;

    /** A specific renderer for the column. */
    private TableCellRenderer renderer;

    /** A specific editor for the column. */
    private TableCellEditor editor;

    /** Comparator to use when sorting according to this column. */
    private Comparator<?> comparator;

    /** Is this column a select column? */
    private boolean isSelectColumn = false;

    /** Can this column be used when filtering locally? */
    private boolean isFilterColumn = false;

    /** Is the column initially visible? */
    private boolean visible = true;

    /** Header title fetched from header keys or manually set. */
    private String header = null;

    @Autowired
    private MessageResolver messageResolver;

    public PropertyColumn(final String propertyName)
    {
        this.propertyName = propertyName;
    }
    
    /**
     * Minimal construction requires a propertyName and an {@link Accessor}.
     * 
     * @param propertyName
     *            name of the property.
     * @param accessor
     *            read-access for the property.
     */
    public PropertyColumn(final String propertyName, final Accessor accessor, final Class<?> propertyType)
    {
        this.propertyName = propertyName;
        this.accessor = accessor;
        this.type = propertyType;
    }

    /**
     * Constructor taking all possible arguments.
     * 
     * @deprecated Please use the other system (minimal constructor + adding necessary elements)
     */
    public PropertyColumn(final Class<?> type, final Accessor accessor, final String propertyName,
            final String[] headerKeys, final int minWidth, final int maxWidth, final boolean resizable,
            TableCellRenderer renderer, TableCellEditor editor, final Comparator<?> comparator,
            final boolean isSelectColumn, final boolean visible)
    {
        this.type = type;
        this.accessor = accessor;
        this.propertyName = propertyName;
        this.headerKeys = headerKeys;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.resizable = resizable;
        this.editor = editor;
        this.renderer = renderer;
        this.comparator = comparator;
        this.isSelectColumn = isSelectColumn;
        this.visible = visible;
    }

    /**
     * Set the accessor to use when retrieving the property value.
     * 
     * @param accessor the {@link Accessor} to read the property value.
     */
    public void setAccessor(Accessor accessor)
    {
        this.accessor = accessor;
    }
    
    /**
     * The type of the property.
     * 
     * @param type the type of the property.
     */
    public void setType(Class<?> type)
    {
        this.type = type;
    }
    
    /**
     * Returns the header to be used as column title. If no explicit header was set, the headerKeys are used
     * to fetch a message from the available resources.
     */
    public String getHeader()
    {
        if (this.header == null)
        {
            if (this.headerKeys == null)
            {
                this.headerKeys = new String[2];
                this.headerKeys[0] = getPropertyName() + ".header";
                this.headerKeys[1] = getPropertyName();
            }
            this.header = messageResolver.getMessage(new DefaultMessageSourceResolvable(this.headerKeys, null,
                    this.headerKeys[this.headerKeys.length - 1]));
        }
        // JTableHeader has a reusable defaultHeaderRenderer on which the default height must be correct.
        // when painting, the columns headers are processed in order and height is being calculated, 
        // if label is null or empty string header height is 4 and thus leaves us with a very small
        // table-header, fix this by returning a space (-> font-size is incorporated)
        return "".equals(this.header) ? " " : this.header;
    }

    /**
     * Returns the header keys that are used to fetch the column title if the header property is not set.
     */
    public String[] getHeaderKeys()
    {
        return headerKeys == null ? null : headerKeys.clone();
    }

    /**
     * Chaining method to set header keys.
     * 
     * @param headerKeys
     *            keys used to fetch the column title from the resources if no header is explicitly set.
     * @return <code>this</code>
     */
    public PropertyColumn withHeaderKeys(String[] headerKeys)
    {
        setHeaderKeys(headerKeys);
        return this;
    }

    /**
     * Set the keys used to fetch the column title from the resources if no header is explicitly set.
     */
    public void setHeaderKeys(String[] headerKeys)
    {
        this.headerKeys = headerKeys;
    }

    /**
     * Returns the maximum width for this column.
     */
    public int getMaxWidth()
    {
        return maxWidth;
    }

    /**
     * Chaining method to set the maximum width.
     * 
     * @param maxWidth
     *            maximum width for this column
     * @return <code>this</code>
     */
    public PropertyColumn withMaxWidth(int maxWidth)
    {
        setMaxWidth(maxWidth);
        return this;
    }

    /**
     * Set the maximum width of this column.
     */
    public void setMaxWidth(int maxWidth)
    {
        this.maxWidth = maxWidth;
    }

    /**
     * Returns the minimum width for this column.
     */
    public int getMinWidth()
    {
        return minWidth;
    }

    /**
     * Chaining method to set the minimum width.
     * 
     * @param minWidth
     *            minimum width for this column
     * @return <code>this</code>
     */
    public PropertyColumn withMinWidth(int minWidth)
    {
        setMinWidth(minWidth);
        return this;
    }

    /**
     * Set the minimum width for this column.
     */
    public void setMinWidth(int minWidth)
    {
        this.minWidth = minWidth;
    }

    /**
     * Chaining method to set the minimum and maximum width to the same value. This will create a fixed width
     * column.
     * 
     * @param width
     *            fixed width for this column
     * @return <code>this</code>
     */
    public PropertyColumn withFixedWidth(int width)
    {
        setMinWidth(width);
        setMaxWidth(width);
        return this;
    }

    public boolean isResizable()
    {
        return resizable;
    }

    public PropertyColumn withResizable(boolean resizable)
    {
        setResizable(resizable);
        return this;
    }

    public void setResizable(boolean resizable)
    {
        this.resizable = resizable;
    }

    public PropertyColumn withFilterColumn(boolean isFilterColumn)
    {
        setFilterColumn(isFilterColumn);
        return this;
    }

    public void setFilterColumn(boolean isFilterColumn)
    {
        this.isFilterColumn = isFilterColumn;
    }

    public boolean isFilterColumn()
    {
        return isFilterColumn;
    }

    public TableCellRenderer getRenderer()
    {
        return renderer;
    }

    public PropertyColumn withRenderer(TableCellRenderer renderer)
    {
        setRenderer(renderer);
        return this;
    }

    public void setRenderer(TableCellRenderer renderer)
    {
        this.renderer = renderer;
    }

    public TableCellEditor getEditor()
    {
        return editor;
    }

    public PropertyColumn withEditor(TableCellEditor editor)
    {
        setEditor(editor);
        return this;
    }

//    public PropertyColumn withEditor(BindingFactory bindingFactory)
//    {
//        return withEditor(new ValueModelTableCellEditor(bindingFactory.getFormModel(), getPropertyName(),
//                bindingFactory.createBinding(getType(), getPropertyName()).getControl()));
//    }
//
//    public PropertyColumn withEditor(FormModel formModel)
//    {
//        return withEditor(new SwingBindingFactory(formModel));
//    }
//
//    public PropertyColumn withEditor(AbstractDataEditorWidget dataEditor)
//    {
//        FormModel formModel = dataEditor.getDetailForm().getFormModel();
//        BindingFactory bindingFactory = dataEditor.getDetailForm().getBindingFactory();
//        return withEditor(new ValueModelTableCellEditor(formModel, getPropertyName(),
//                bindingFactory.createBinding(getType(), getPropertyName()).getControl(), dataEditor.getUpdateCommand()));
//    }
    
    public void setEditor(TableCellEditor editor)
    {
        this.editor = editor;
    }

    public Comparator<?> getComparator()
    {
        return comparator;
    }

    public PropertyColumn withComparator(Comparator<?> comparator)
    {
        setComparator(comparator);
        return this;
    }

    public void setComparator(Comparator<?> comparator)
    {
        this.comparator = comparator;
    }

    public PropertyColumn withSelectColumn(boolean isSelectColumn)
    {
        setSelectColumn(isSelectColumn);
        return this;
    }

    public boolean isSelectColumn()
    {
        return isSelectColumn;
    }

    public void setSelectColumn(boolean isSelectColumn)
    {
        this.isSelectColumn = isSelectColumn;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public PropertyColumn withVisible(boolean visible)
    {
        setVisible(visible);
        return this;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public Class<?> getType()
    {
        return type;
    }

    public Accessor getAccessor()
    {
        return accessor;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }


}