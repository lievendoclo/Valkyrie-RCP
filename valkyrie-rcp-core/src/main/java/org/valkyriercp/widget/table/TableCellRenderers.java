package org.valkyriercp.widget.table;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.valkyriercp.application.support.MessageResolver;
import org.valkyriercp.image.IconSource;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.text.*;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

/**
 * Voorziet een paar eenvoudige renderers voor gebruiksgemak.
 */
public class TableCellRenderers
{

    public static final TableCellRenderer CENTER_ALIGNED_RENDERER = new AlignedRenderer(SwingConstants.CENTER);

    public static final TableCellRenderer RIGHT_ALIGNED_RENDERER = new AlignedRenderer(SwingConstants.RIGHT);

    public static final TableCellRenderer TOP_ALIGNED_RENDERER = new AlignedRenderer(SwingConstants.LEFT,
            SwingConstants.TOP);
    public static final TableCellRenderer BOTTOM_ALIGNED_RENDERER = new AlignedRenderer(SwingConstants.LEFT,
            SwingConstants.BOTTOM);
    public static final TableCellRenderer PERCENTAGE_RENDERER = new PercentageRenderer();
    public static final TableCellRenderer MONEY_RENDERER = new BigDecimalRenderer(NumberFormat
            .getCurrencyInstance(Locale.getDefault()));
    public static final TableCellRenderer LEFT_ALIGNED_HEADER_RENDERER = new AlignedTableHeaderRenderer(
            SwingConstants.LEFT);
    public static final TableCellRenderer CENTER_ALIGNED_HEADER_RENDERER = new AlignedTableHeaderRenderer(
            SwingConstants.CENTER);
    public static final TableCellRenderer RIGHT_ALIGNED_HEADER_RENDERER = new AlignedTableHeaderRenderer(
            SwingConstants.RIGHT);
    public static final TableCellRenderer ENUM_RENDERER = new EnumTableCellRenderer();

    public static final TableCellRenderer FLAT_NUMBER_RENDERER = new FlatNumberRenderer();

    public static class FlatNumberRenderer extends DefaultTableRenderer
    {

        private static NumberFormat format = NumberFormat.getIntegerInstance();
        static
        {
            format.setGroupingUsed(false);
        }

        public FlatNumberRenderer()
        {
            super(new FormatStringValue(format));
        }
    }

    public static class AlignedRenderer extends DefaultTableCellRenderer
    {

        public AlignedRenderer(int horizontalAlignment)
        {
            super();
            setHorizontalAlignment(horizontalAlignment);
        }

        public AlignedRenderer(int horizontalAlignment, int verticalAlignment)
        {
            super();
            setHorizontalAlignment(horizontalAlignment);
            setVerticalAlignment(verticalAlignment);
        }

        DateFormat formatter;

        @Override
        public void setValue(Object value)
        {
            if (value != null && value instanceof Date)
            {
                if (formatter == null)
                {
                    formatter = DateFormat.getDateInstance();
                }
                setText(formatter.format(value));
            }
            else
            {
                super.setValue(value);
            }
        }

    }

    public static class PercentageRenderer extends DefaultTableCellRenderer
    {

        private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        private static final Format nonFractionalFormat = new DecimalFormat("###     %", symbols);
        private static final Format fractionalFormat = new DecimalFormat("##0.00%", symbols);
        private static final BigDecimal multiplyFactor = new BigDecimal("100");

        public PercentageRenderer()
        {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        protected void setValue(Object value)
        {
            if (value instanceof BigDecimal)
            {
                BigDecimal percentage = ((BigDecimal) value).multiply(multiplyFactor);
                if (percentage.doubleValue() == percentage.intValue())
                {
                    super.setValue(nonFractionalFormat.format(value));
                }
                else
                {
                    super.setValue(fractionalFormat.format(value));
                }
            }
            else
            {
                super.setValue(value);
            }
        }
    }

    public static class BigDecimalRenderer extends DefaultTableCellRenderer
    {

        private final BigDecimal multiplyFactor;
        private final Format format;

        public BigDecimalRenderer(Format format)
        {
            this(null, format);
        }

        public BigDecimalRenderer(BigDecimal multiplyFactor)
        {
            this(multiplyFactor, NumberFormat.getNumberInstance());
        }

        public BigDecimalRenderer(BigDecimal multiplyFactor, Format format)
        {
            this(multiplyFactor, format, SwingConstants.RIGHT);
        }

        public BigDecimalRenderer(BigDecimal multiplyFactor, Format format, int horizontalAlignment)
        {
            this.multiplyFactor = multiplyFactor;
            this.format = format;
            setHorizontalAlignment(horizontalAlignment);
        }

        @Override
        protected void setValue(Object value)
        {
            if (value instanceof BigDecimal)
            {
                if (multiplyFactor != null)
                {
                    value = ((BigDecimal) value).multiply(multiplyFactor);
                }
                super.setValue(format.format(value));
            }
            else
            {
                super.setValue(value);
            }
        }
    }

    public static class AlignedTableHeaderRenderer extends DefaultTableCellRenderer
    {

        private int align = SwingConstants.CENTER;

        public AlignedTableHeaderRenderer(int align)
        {
            this.align = align;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column)
        {
            if (table != null)
            {
                JTableHeader header = table.getTableHeader();
                if (header != null)
                {
                    setForeground(header.getForeground());
                    setBackground(header.getBackground());
                    setFont(header.getFont());
                }
            }

            setText((value == null) ? "" : value.toString() + " ");
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setHorizontalAlignment(align);
            return this;
        }
    }

    @Configurable
    public static class EnumTableCellRenderer extends DefaultTableCellRenderer
    {
        @Autowired
        private MessageResolver messageResolver;

        @Autowired
        private IconSource iconSource;

        public EnumTableCellRenderer()
        {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column)
        {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value == null)
            {
                setValue("");
                setIcon(null);
            }
            else
            {
                if (value instanceof Enum)
                {
                    Enum valueEnum = (Enum) value;
                    Class<? extends Enum> valueClass = valueEnum.getClass();
                    setValue(messageResolver.getMessage(valueClass.getName() + "." + valueEnum.name()));
                    setIcon(iconSource.getIcon(valueClass.getName() + "." + valueEnum.name()));
                }
                else
                {
                    setValue(value);
                }
            }
            return this;
        }
    }

    public static class ListPropertyCellRenderer extends JPanel implements TableCellRenderer
    {

        protected String property;

        protected int verticalAlignment;

        protected int horizontalAlignment;

        protected Format format;

        protected float alignmentX;

        private static Border border = new EmptyBorder(1, 2, 1, 2);

        public ListPropertyCellRenderer(String property)
        {
            this(property, SwingConstants.LEFT, SwingConstants.CENTER);
        }

        public ListPropertyCellRenderer(String property, int horizontalAlignment, int verticalAlignment)
        {
            this(property, horizontalAlignment, verticalAlignment, null);
        }

        public ListPropertyCellRenderer(String property, int horizontalAlignment, int verticalAlignment,
                Format format)
        {
            this.property = property;
            this.horizontalAlignment = horizontalAlignment;
            this.verticalAlignment = verticalAlignment;
            this.format = format;
            switch (horizontalAlignment)
            {
                case SwingConstants.LEFT :
                    alignmentX = (float) 0.0;
                    break;

                case SwingConstants.CENTER :
                    alignmentX = (float) 0.5;
                    break;

                case SwingConstants.RIGHT :
                    alignmentX = (float) 1.0;
                    break;

                default :
                    throw new IllegalArgumentException("Illegal horizontal alignment value");
            }

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(true);
            setBorder(border);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column)
        {
            removeAll();
            invalidate();

            Color fg = table.getForeground();
            Color bg = table.getBackground();

            if (isSelected)
            {
                fg = table.getSelectionForeground();
                bg = table.getSelectionBackground();
            }

            Font font = table.getFont();

            setFont(font);

            if (hasFocus)
            {
                Border border = null;
                if (isSelected)
                {
                    border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
                }
                if (border == null)
                {
                    border = UIManager.getBorder("Table.focusCellHighlightBorder");
                }
                setBorder(border);

                if (!isSelected && table.isCellEditable(row, column))
                {
                    Color col;
                    col = UIManager.getColor("Table.focusCellForeground");
                    if (col != null)
                    {
                        fg = col;
                    }
                    col = UIManager.getColor("Table.focusCellBackground");
                    if (col != null)
                    {
                        bg = col;
                    }
                }
            }
            else
            {
                setBorder(border);
            }

            super.setForeground(fg);
            super.setBackground(bg);

            if (verticalAlignment != SwingConstants.TOP)
            {
                add(Box.createVerticalGlue());
            }

            Object[] values;
            if (value instanceof Collection)
            {
                values = ((Collection) value).toArray();
            }
            else
                throw new IllegalArgumentException("Value must be an instance of Collection.");

            for (int i = 0; i < values.length; i++)
            {
                Object o = values[i];
                Object line;
                try
                {
                    line = PropertyUtils.getProperty(o, property);
                }
                catch (NestedNullException e)
                {
                    line = null;
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Error reading property " + property + " from object " + o, e);
                }
                JLabel lineLabel = new JLabel();
                lineLabel.setForeground(fg);
                lineLabel.setFont(font);
                setValue(lineLabel, line, i);
                add(lineLabel);
            }

            int height_wanted = (int) getPreferredSize().getHeight();
            if (height_wanted > table.getRowHeight(row))
                table.setRowHeight(row, height_wanted);

            if (verticalAlignment != SwingConstants.BOTTOM)
            {
                add(Box.createVerticalGlue());
            }
            return this;
        }

        protected void setValue(JLabel l, Object value, int lineNumber)
        {
            if (format != null && value != null)
                value = format.format(value);
            l.setText(value == null ? " " : value.toString());
            l.setHorizontalAlignment(horizontalAlignment);
            l.setAlignmentX(alignmentX);
            l.setOpaque(false);
        }

    }
}

