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
package org.valkyriercp.layout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.valkyriercp.component.GridBagLayoutDebugPanel;
import org.valkyriercp.factory.ComponentFactory;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This provides an easy way to create panels using a {@link java.awt.GridBagLayout}.
 * <p />
 *
 * Usage is:
 *
 * <pre>
 * GridBagLayoutBuilder builder = new GridBagLayoutBuilder();
 *
 * builder.appendRightLabel(&quot;label.field1&quot;).appendField(field1);
 * builder.appendRightLabel(&quot;label.field2&quot;).appendField(field2);
 * builder.nextLine();
 *
 * builder.appendRightLabel(&quot;label.field3&quot;).appendField(field3);
 * // because &quot;field3&quot; is the last component on this line, but the panel has
 * // 4 columns, &quot;field3&quot; will span 3 columns
 * builder.nextLine();
 *
 * // once everything's been put into the builder, ask it to build a panel
 * // to use in the UI.
 * JPanel panel = builder.getPanel();
 * </pre>
 *
 * @author Jim Moore
 * @see #setAutoSpanLastComponent(boolean)
 * @see #setShowGuidelines(boolean)
 * @see #setComponentFactory(ComponentFactory)
 */
public class GridBagLayoutBuilder implements LayoutBuilder {
    private static final Log LOG = LogFactory.getLog(GridBagLayoutBuilder.class);

    private Insets defaultInsets = new Insets(0, 0, 4, 4);

    private boolean showGuidelines = false;

    private boolean autoSpanLastComponent = true;

    private int currentCol;

    private int currentRow;

    private java.util.List rows;

    private java.util.List currentRowList;

    private int maxCol = 0;

    private static final Item NULL_ITEM = new Item(null, null);

    public GridBagLayoutBuilder() {
        super();
        init();
    }

    private void init() {
        currentCol = 0;
        currentRow = 0;
        rows = new ArrayList();
        currentRowList = new ArrayList();
    }

    /**
     * Returns the default {@link Insets}used when adding components
     */
    public Insets getDefaultInsets() {
        return defaultInsets;
    }

    /**
     * Sets the default {@link Insets}used when adding components
     */
    public void setDefaultInsets(Insets defaultInsets) {
        this.defaultInsets = defaultInsets;
    }

    /**
     * Returns the current row (zero-based) that the builder is putting
     * components in
     */
    public int getCurrentRow() {
        return currentRow;
    }

    /**
     * Returns the current column (zero-based) that the builder is putting
     * components in
     */
    public int getCurrentCol() {
        return currentCol;
    }

    public ComponentFactory getComponentFactory() {
        return ValkyrieRepository.getInstance().getApplicationConfig().componentFactory();
    }

    /**
     * Appends the given component to the end of the current line, using the
     * default insets and no expansion
     *
     * @param component the component to add to the current line
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder append(Component component) {
        return append(component, 1, 1);
    }

    /**
     * Appends the given component to the end of the current line, using the
     * default insets and no expansion
     *
     * @param component the component to add to the current line
     * @param colSpan   the number of columns to span
     * @param rowSpan   the number of rows to span
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder append(Component component, int colSpan, int rowSpan) {
        return append(component, colSpan, rowSpan, 0.0, 0.0);
    }

    /**
     * Appends the given component to the end of the current line, using the
     * default insets
     *
     * @param component the component to add to the current line
     * @param colSpan   the number of columns to span
     * @param rowSpan   the number of rows to span
     * @param expandX   should the component "grow" horrizontally?
     * @param expandY   should the component "grow" vertically?
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder append(Component component, int colSpan, int rowSpan, boolean expandX, boolean expandY) {
        return append(component, colSpan, rowSpan, expandX, expandY, defaultInsets);
    }

    /**
     * Appends the given component to the end of the current line
     *
     * @param component the component to add to the current line
     * @param colSpan   the number of columns to span
     * @param rowSpan   the number of rows to span
     * @param expandX   should the component "grow" horrizontally?
     * @param expandY   should the component "grow" vertically?
     * @param insets    the insets to use for this component
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder append(Component component, int colSpan, int rowSpan, boolean expandX, boolean expandY,
            Insets insets) {
        if (expandX && expandY)
            return append(component, colSpan, rowSpan, 1.0, 1.0, insets);
        else if (expandX)
            return append(component, colSpan, rowSpan, 1.0, 0.0, insets);
        else if (expandY)
            return append(component, colSpan, rowSpan, 0.0, 1.0, insets);
        else
            return append(component, colSpan, rowSpan, 0.0, 0.0, insets);
    }

    /**
     * Appends the given component to the end of the current line
     *
     * @param component the component to add to the current line
     * @param x         the column to put the component
     * @param y         the row to put the component
     * @param colSpan   the number of columns to span
     * @param rowSpan   the number of rows to span
     * @param expandX   should the component "grow" horrizontally?
     * @param expandY   should the component "grow" vertically?
     * @param insets    the insets to use for this component
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder append(Component component, int x, int y, int colSpan, int rowSpan, boolean expandX,
            boolean expandY, Insets insets) {
        if (expandX && expandY)
            return append(component, x, y, colSpan, rowSpan, 1.0, 1.0, insets);
        else if (expandX)
            return append(component, x, y, colSpan, rowSpan, 1.0, 0.0, insets);
        else if (expandY)
            return append(component, x, y, colSpan, rowSpan, 0.0, 1.0, insets);
        else
            return append(component, x, y, colSpan, rowSpan, 0.0, 0.0, insets);
    }

    /**
     * Appends the given component to the end of the current line, using the
     * default insets
     *
     * @param component the component to add to the current line
     * @param colSpan   the number of columns to span
     * @param rowSpan   the number of rows to span
     * @param xweight   the "growth weight" horrizontally
     * @param yweight   the "growth weight" horrizontally
     *
     * @return "this" to make it easier to string together append calls
     *
     * @see GridBagConstraints#weightx
     * @see GridBagConstraints#weighty
     */
    public GridBagLayoutBuilder append(Component component, int colSpan, int rowSpan, double xweight, double yweight) {
        return append(component, colSpan, rowSpan, xweight, yweight, defaultInsets);
    }

    /**
     * Appends the given component to the end of the current line
     *
     * @param component the component to add to the current line
     * @param colSpan   the number of columns to span
     * @param rowSpan   the number of rows to span
     * @param xweight   the "growth weight" horrizontally
     * @param yweight   the "growth weight" horrizontally
     * @param insets    the insets to use for this component
     *
     * @return "this" to make it easier to string together append calls
     *
     * @see GridBagConstraints#weightx
     * @see GridBagConstraints#weighty
     */
    public GridBagLayoutBuilder append(Component component, int colSpan, int rowSpan, double xweight, double yweight,
            Insets insets) {
        return append(component, getCurrentCol(), getCurrentRow(), colSpan, rowSpan, xweight, yweight, insets);
    }

    /**
     * Appends a label and field to the end of the current line.
     * <p />
     *
     * The label will be to the left of the field, and be right-justified.
     * <br />
     * The field will "grow" horizontally as space allows.
     * <p />
     *
     * @param propertyName the name of the property to create the controls for
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendLabeledField(String propertyName, final JComponent field,
            LabelOrientation labelOrientation) {
        return appendLabeledField(propertyName, field, labelOrientation, 1);
    }

    /**
     * Appends a label and field to the end of the current line.
     * <p />
     *
     * The label will be to the left of the field, and be right-justified.
     * <br />
     * The field will "grow" horizontally as space allows.
     * <p />
     *
     * @param propertyName the name of the property to create the controls for
     * @param colSpan      the number of columns the field should span
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendLabeledField(String propertyName, final JComponent field,
            LabelOrientation labelOrientation, int colSpan) {
        return appendLabeledField(propertyName, field, labelOrientation, colSpan, 1, true, false);
    }

    /**
     * Appends a label and field to the end of the current line.<p />
     *
     * The label will be to the left of the field, and be right-justified.<br />
     * The field will "grow" horizontally as space allows.<p />
     *
     * @param propertyName the name of the property to create the controls for
     * @param colSpan      the number of columns the field should span
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendLabeledField(String propertyName, final JComponent field,
            LabelOrientation labelOrientation, int colSpan, int rowSpan, boolean expandX, boolean expandY) {
        JLabel label = createLabel(propertyName);
        return appendLabeledField(label, field, labelOrientation, colSpan, rowSpan, expandX, expandY);
    }

    protected JLabel createLabel(String propertyName) {
        return getComponentFactory().createLabel(propertyName);
    }

    /**
     * Appends a label and field to the end of the current line.<p />
     *
     * The label will be to the left of the field, and be right-justified.<br />
     * The field will "grow" horizontally as space allows.<p />
     *
     * @param label   the label to associate and layout with the field
     * @param colSpan the number of columns the field should span
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendLabeledField(final JLabel label, final JComponent field,
            LabelOrientation labelOrientation, int colSpan, int rowSpan, boolean expandX, boolean expandY) {
        label.setLabelFor(field);

        final int col = getCurrentCol();
        final int row = getCurrentRow();
        final Insets insets = getDefaultInsets();

        if (labelOrientation == LabelOrientation.LEFT || labelOrientation == null) {
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            append(label, col, row, 1, 1, false, expandY, insets);
            append(field, col + 1, row, colSpan, rowSpan, expandX, expandY, insets);
        }
        else if (labelOrientation == LabelOrientation.RIGHT) {
            label.setHorizontalAlignment(SwingConstants.LEFT);
            append(field, col, row, colSpan, rowSpan, expandX, expandY, insets);
            append(label, col + colSpan, row, 1, rowSpan, false, expandY, insets);
        }
        else if (labelOrientation == LabelOrientation.TOP) {
            label.setHorizontalAlignment(SwingConstants.LEFT);
            append(label, col, row, colSpan, 1, expandX, false, insets);
            append(field, col, row + 1, colSpan, rowSpan, expandX, expandY, insets);
        }
        else if (labelOrientation == LabelOrientation.BOTTOM) {
            label.setHorizontalAlignment(SwingConstants.LEFT);
            append(field, col, row, colSpan, rowSpan, expandX, expandY, insets);
            append(label, col, row + rowSpan, colSpan, 1, expandX, false, insets);
        }

        return this;
    }

    /**
     * Appends the given component to the end of the current line
     *
     * @param component the component to add to the current line
     * @param x         the column to put the component
     * @param y         the row to put the component
     * @param colSpan   the number of columns to span
     * @param rowSpan   the number of rows to span
     * @param xweight   the "growth weight" horrizontally
     * @param yweight   the "growth weight" horrizontally
     * @param insets    the insets to use for this component
     *
     * @return "this" to make it easier to string together append calls
     *
     * @see GridBagConstraints#weightx
     * @see GridBagConstraints#weighty
     */
    public GridBagLayoutBuilder append(Component component, int x, int y, int colSpan, int rowSpan, double xweight,
            double yweight, Insets insets) {
        final java.util.List rowList = getRow(y);
        ensureCapacity(rowList, Math.max(x, maxCol) + 1);

        final int col = bypassPlaceholders(rowList, x);

        insertPlaceholdersIfNeeded(rowSpan, y, col, component, colSpan);

        final GridBagConstraints gbc = createGridBagConstraint(col, y, colSpan, rowSpan, xweight, yweight, insets);

        rowList.set(col, new Item(component, gbc));
        if (LOG.isDebugEnabled())
            LOG.debug(getDebugString(component, gbc));

        // keep track of the largest column this has seen...
        this.maxCol = Math.max(this.maxCol, col);

        currentCol = col + colSpan;

        return this;
    }

    private void insertPlaceholdersIfNeeded(final int rowSpan, final int y, final int col, final Component component,
            final int colSpan) {
        if (rowSpan > 1) {
            growRowsIfNeeded(rowSpan);
            for (int i = 1; i < (y + rowSpan); i++) {
                final java.util.List row = getRow(i);
                ensureCapacity(row, col + colSpan + 1);
                if (row.get(col) != null) {
                    // sanity check -- shouldn't ever happen
                    throw new IllegalStateException("Trying to overwrite another component: " + component + ", " + col
                            + " " + y);
                }
                for (int j = 0; j < colSpan; j++) {
                    row.set(col + j, NULL_ITEM);
                }
            }
        }
    }

    private java.util.List getRow(final int i) {
        ensureCapacity(rows, i + 1);
        java.util.List row = (java.util.List)rows.get(i);
        if (row == null) {
            row = new ArrayList();
            rows.set(i, row);
        }
        return row;
    }

    private int bypassPlaceholders(final java.util.List list, final int col) {
        int theCol = col;
        while (theCol < list.size() && list.get(theCol) != null) {
            theCol++;
        }
        return theCol;
    }

    private void ensureCapacity(java.util.List list, int minSize) {
        for (int i = list.size(); i < minSize; i++) {
            list.add(null);
        }
    }

    private void growRowsIfNeeded(final int rowSpan) {
        final int minNeededSize = currentRow + rowSpan;
        ensureCapacity(rows, minNeededSize);
        final int delta = minNeededSize - rows.size();
        if (delta > 0) {
            rows.set(currentRow, currentRowList);
            for (int i = 0; i < delta; i++) {
                rows.set(currentRow + i, new ArrayList());
            }
        }
    }

    /**
     * Appends the given label to the end of the current line. The label does
     * not "grow."
     *
     * @param label the label to append
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendLabel(JLabel label) {
        return appendLabel(label, 1);
    }

    /**
     * Appends the given label to the end of the current line. The label does
     * not "grow."
     *
     * @param label   the label to append
     * @param colSpan the number of columns to span
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendLabel(JLabel label, int colSpan) {
        return append(label, colSpan, 1, false, false);
    }

    /**
     * Appends a right-justified label to the end of the given line, using the
     * provided string as the key to look in the
     * {@link #setComponentFactory(ComponentFactory) ComponentFactory's}message
     * bundle for the text to use.
     *
     * @param labelKey the key into the message bundle; if not found the key is used
     *                 as the text to display
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendRightLabel(String labelKey) {
        return appendRightLabel(labelKey, 1);
    }

    /**
     * Appends a right-justified label to the end of the given line, using the
     * provided string as the key to look in the
     * {@link #setComponentFactory(ComponentFactory) ComponentFactory's}message
     * bundle for the text to use.
     *
     * @param labelKey the key into the message bundle; if not found the key is used
     *                 as the text to display
     * @param colSpan  the number of columns to span
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendRightLabel(String labelKey, int colSpan) {
        final JLabel label = createLabel(labelKey);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return appendLabel(label, colSpan);
    }

    /**
     * Appends a left-justified label to the end of the given line, using the
     * provided string as the key to look in the
     * {@link #setComponentFactory(ComponentFactory) ComponentFactory's}message
     * bundle for the text to use.
     *
     * @param labelKey the key into the message bundle; if not found the key is used
     *                 as the text to display
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendLeftLabel(String labelKey) {
        return appendLeftLabel(labelKey, 1);
    }

    /**
     * Appends a left-justified label to the end of the given line, using the
     * provided string as the key to look in the
     * {@link #setComponentFactory(ComponentFactory) ComponentFactory's}message
     * bundle for the text to use.
     *
     * @param labelKey the key into the message bundle; if not found the key is used
     *                 as the text to display
     * @param colSpan  the number of columns to span
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendLeftLabel(String labelKey, int colSpan) {
        final JLabel label = createLabel(labelKey);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return appendLabel(label, colSpan);
    }

    /**
     * Appends the given component to the end of the current line. The component
     * will "grow" horizontally as space allows.
     *
     * @param component the item to append
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendField(Component component) {
        return appendField(component, 1);
    }

    /**
     * Appends the given component to the end of the current line. The component
     * will "grow" horizontally as space allows.
     *
     * @param component the item to append
     * @param colSpan   the number of columns to span
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendField(Component component, int colSpan) {
        return append(component, colSpan, 1, true, false);
    }

    /**
     * Appends a seperator (usually a horizonal line). Has an implicit
     * {@link #nextLine()}before and after it.
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendSeparator() {
        return appendSeparator(null);
    }

    /**
     * Appends a seperator (usually a horizonal line) using the provided string
     * as the key to look in the
     * {@link #setComponentFactory(ComponentFactory) ComponentFactory's}message
     * bundle for the text to put along with the seperator. Has an implicit
     * {@link #nextLine()}before and after it.
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder appendSeparator(String labelKey) {
        if (this.currentRowList.size() > 0) {
            nextLine();
        }
        final JComponent separator = getComponentFactory().createLabeledSeparator(labelKey);
        return append(separator, 1, 1, true, false).nextLine();
    }

    /**
     * Ends the current line and starts a new one
     *
     * @return "this" to make it easier to string together append calls
     */
    public GridBagLayoutBuilder nextLine() {
        currentRow++;
        this.currentCol = 0;

        return this;
    }

    private GridBagConstraints createGridBagConstraint(int x, int y, int colSpan, int rowSpan, double xweight,
            double yweight, Insets insets) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = colSpan;
        gbc.gridheight = rowSpan;
        gbc.weightx = xweight;
        gbc.weighty = yweight;
        gbc.insets = insets;

        // in theory other ones can be used, but I've never seen why...
        gbc.fill = GridBagConstraints.BOTH;

        return gbc;
    }

    /**
     * Should this show "guidelines"? Useful for debugging layouts.
     */
    public void setShowGuidelines(boolean showGuidelines) {
        this.showGuidelines = showGuidelines;
    }

    /**
     * Creates and returns a JPanel with all the given components in it, using
     * the "hints" that were provided to the builder.
     *
     * @return a new JPanel with the components laid-out in it
     */
    public JPanel getPanel() {
        if (this.currentRowList.size() > 0) {
            this.rows.add(this.currentRowList);
        }

        final JPanel panel = this.showGuidelines ? new GridBagLayoutDebugPanel() : new JPanel(new GridBagLayout());

        final int lastRowIndex = this.rows.size() - 1;
        for (int currentRowIndex = 0; currentRowIndex <= lastRowIndex; currentRowIndex++) {
            final java.util.List row = getRow(currentRowIndex);
            addRow(row, currentRowIndex, lastRowIndex, panel);
        }
        return panel;
    }

    private void addRow(final java.util.List row, final int currentRowIndex, final int lastRowIndex, final JPanel panel) {
        final int lastColIndex = row.size() - 1;

        for (int currentColIndex = 0; currentColIndex <= lastColIndex; currentColIndex++) {
            final Item item = (Item)row.get(currentColIndex);

            if (item != null && item != NULL_ITEM) {
                final GridBagConstraints gbc = item.gbc;

                if (gbc.gridy + gbc.gridheight - 1 == lastRowIndex) {
					formatLastRow(gbc);
				}

				if (gbc.gridx + gbc.gridwidth - 1 == lastColIndex) {
					formatLastColumn(gbc, currentColIndex);
				}

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Adding to panel: " + getDebugString(item.component, gbc));
                }
                panel.add(item.component, gbc);
            }
        }
    }

    private String getDebugString(Component component, GridBagConstraints gbc) {
        final StringBuffer buffer = new StringBuffer();

        if (component instanceof JComponent) {
            final JComponent jcomp = (JComponent)component;
            final String name = jcomp.getName();
            if (name != null && !"".equals(jcomp.getName())) {
                buffer.append(name);
            }
            else {
                if (jcomp instanceof JLabel) {
                    buffer.append(((JLabel)jcomp).getText());
                }
                else {
                    buffer.append(jcomp.toString());
                }
            }
        }
        else {
            buffer.append(component.toString());
        }

        buffer.append(", ");
        buffer.append("GridBagConstraint[");
        buffer.append("anchor=").append(gbc.anchor).append(",");
        buffer.append("fill=").append(gbc.fill).append(",");
        buffer.append("gridheight=").append(gbc.gridheight).append(",");
        buffer.append("gridwidth=").append(gbc.gridwidth).append(",");
        buffer.append("gridx=").append(gbc.gridx).append(",");
        buffer.append("gridy=").append(gbc.gridy).append(",");
        buffer.append("weightx=").append(gbc.weightx).append(",");
        buffer.append("weighty=").append(gbc.weighty).append("]");
        return buffer.toString();
    }

    private void formatLastRow(final GridBagConstraints gbc) {
        // remove any insets at the bottom of the GBC
        final Insets oldInset = gbc.insets;
        gbc.insets = new Insets(oldInset.top, oldInset.left, 0, oldInset.right);
    }

    /**
     * Should the last column before a {@link #nextLine()}automaticly span to
     * the end of the panel?
     * <p />
     *
     * For example, if you have
     *
     * <pre>
     * append(a).append(b).append(c).nextLine();
     * append(d).append(e).nextLine();
     * </pre>
     *
     * then "e" would automaticly span two columns.
     *
     * @param autoSpanLastComponent default is true
     */
    public void setAutoSpanLastComponent(boolean autoSpanLastComponent) {
        this.autoSpanLastComponent = autoSpanLastComponent;
    }

    private void formatLastColumn(final GridBagConstraints gbc, final int currentColIndex) {
        // remove any insets at the right of the GBC
        final Insets oldInset = gbc.insets;
        gbc.insets = new Insets(oldInset.top, oldInset.left, oldInset.bottom, 0);

        if (this.autoSpanLastComponent) {
            // increase the gridwidth if needed
            final int colSpan = (this.maxCol - currentColIndex) + 1;
            if (colSpan > gbc.gridwidth) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Increasing gridwidth from " + gbc.gridwidth + " to " + colSpan);
                }
                gbc.gridwidth = colSpan;
            }
        }
    }

    //*************************************************************************
    //
    // INNER CLASSES
    //
    //*************************************************************************

    private static class Item {
        public Component component;

        public GridBagConstraints gbc;

        public Item(Component component, GridBagConstraints gbc) {
            this.component = component;
            this.gbc = gbc;
        }
    }

}
