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

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Custom panel that presents a "shuttle" list pair. One list is the "source"
 * and the second list holds the "chosen" values from the source list. Buttons
 * between the lists are used to move entries back and forth. By default, only
 * the chosen list is displayed along with an Edit button. Pressing the edit
 * button exposes the source list and the movement buttons.
 * <p>
 * This component essentially provides an alternate UI for a JList. It uses the
 * same type of model and selection list. The selection is rendered as two lists
 * instead of one list with highlighted entries. Those elements in the model
 * that are not selected are shown in the source list and those that are
 * selected are shown in the chosen list.
 * <p>
 * Normal selection model listeners are used to report changes to interested
 * objects.
 *
 * @author lstreepy
 * @author Benoit Xhenseval (Small modifications for text + icons config)
 * @author Geoffrey De Smet
 */
public class ShuttleList extends JPanel {

    private boolean showEditButton = false;

    private JList helperList = new JList();

    private JList sourceList = new JList();

    private JLabel sourceLabel = new JLabel();

    private JPanel sourcePanel = new JPanel(new BorderLayout());

    private JPanel chosenPanel = new JPanel(new BorderLayout());

    private JList chosenList = new JList();

    private JLabel chosenLabel = new JLabel();

    private JScrollPane helperScroller = new JScrollPane(helperList);

    private JPanel buttonPanel;

    private JButton editButton;

    private ListModel dataModel;

    private Comparator comparator;

    private boolean panelsShowing;

    private static final long serialVersionUID = -6038138479095186130L;

    private JButton leftToRight;

    private JButton allLeftToRight;

    private JButton rightToLeft;

    private JButton allRightToLeft;

    /**
     * Simple constructor.
     */
    public ShuttleList() {
        // The binder actually determines the default
        this(true);
    }

    public ShuttleList(boolean showEditButton) {
        this.showEditButton = showEditButton;
        this.panelsShowing = !showEditButton;
        buildComponent();
    }

    /**
     * Returns the object that renders the list items.
     *
     * @return the <code>ListCellRenderer</code>
     * @see #setCellRenderer
     */
    public ListCellRenderer getCellRenderer() {
        return sourceList.getCellRenderer();
    }

    /**
     * Sets the delegate that's used to paint each cell in the list.
     * <p>
     * The default value of this property is provided by the ListUI delegate,
     * i.e. by the look and feel implementation.
     * <p>
     *
     * @param cellRenderer the <code>ListCellRenderer</code> that paints list
     *        cells
     * @see #getCellRenderer
     */
    public void setCellRenderer(ListCellRenderer cellRenderer) {
        // Apply this to both lists
        sourceList.setCellRenderer(cellRenderer);
        chosenList.setCellRenderer(cellRenderer);
        helperList.setCellRenderer(cellRenderer);
    }

    /**
     * Returns the data model.
     *
     * @return the <code>ListModel</code> that provides the displayed list of
     *         items
     */
    public ListModel getModel() {
        return dataModel;
    }

    /**
     * Sets the model that represents the contents or "value" of the list and
     * clears the list selection.
     *
     * @param model the <code>ListModel</code> that provides the list of items
     *        for display
     * @exception IllegalArgumentException if <code>model</code> is
     *            <code>null</code>
     */
    public void setModel(ListModel model) {
        helperList.setModel(model);

        dataModel = model;
        clearSelection();

        // Once we have a model, we can properly size the two display lists
        // They should be wide enough to hold the widest string in the model.
        // So take the width of the source list since it currently has all the
        // data.
        Dimension d = helperScroller.getPreferredSize();
        chosenPanel.setPreferredSize(d);
        sourcePanel.setPreferredSize(d);
    }

    /**
     * Sets the preferred number of rows in the list that can be displayed
     * without a scrollbar.
     *
     * @param visibleRowCount an integer specifying the preferred number of
     *        visible rows
     */
    public void setVisibleRowCount(int visibleRowCount) {
        sourceList.setVisibleRowCount(visibleRowCount);
        chosenList.setVisibleRowCount(visibleRowCount);
        helperList.setVisibleRowCount(visibleRowCount);

        // Ok, since we've haven't set a preferred size on the helper scroller,
        // we can use it's current preferred size for our two control lists.
        Dimension d = helperScroller.getPreferredSize();
        chosenPanel.setPreferredSize(d);
        sourcePanel.setPreferredSize(d);

    }

    /**
     * Set the comparator to use for comparing list elements.
     *
     * @param comparator to use
     */
    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    /**
     * Set the icon to use on the edit button. If no icon is specified, then
     * just the label will be used otherwise the text will be a tooltip.
     *
     * @param editIcon Icon to use on edit button
     */
    public void setEditIcon(Icon editIcon, String text) {
        if (editIcon != null) {
            editButton.setIcon(editIcon);
            if (text != null) {
                editButton.setToolTipText(text);
            }
            editButton.setText("");
        } else {
            editButton.setIcon(null);
            if (text != null) {
                editButton.setText(text);
            }
        }
    }

    /**
     * Add labels on top of the 2 lists. If not present, do not show the labels.
     *
     * @param chosenLabel
     * @param sourceLabel
     */
    public void setListLabels(String chosenLabel, String sourceLabel) {
        if (chosenLabel != null) {
            this.chosenLabel.setText(chosenLabel);
            this.chosenLabel.setVisible(true);
        } else {
            this.chosenLabel.setVisible(false);
        }

        if (sourceLabel != null) {
            this.sourceLabel.setText(sourceLabel);
            this.sourceLabel.setVisible(true);
        } else {
            this.sourceLabel.setVisible(false);
        }

        Dimension d = chosenList.getPreferredSize();
        Dimension d1 = this.chosenLabel.getPreferredSize();
        Dimension dChosenPanel = chosenPanel.getPreferredSize();

        dChosenPanel.width = Math.max(d.width, Math.max(dChosenPanel.width, d1.width));
        chosenPanel.setPreferredSize(dChosenPanel);

        Dimension dSourceList = sourceList.getPreferredSize();
        Dimension dSource = this.sourceLabel.getPreferredSize();
        Dimension dSourcePanel = sourcePanel.getPreferredSize();
        dSourcePanel.width = Math.max(dSource.width, Math.max(dSourceList.width, dSourcePanel.width));
        sourcePanel.setPreferredSize(dSourcePanel);

        Dimension fullPanelSize = getPreferredSize();
        fullPanelSize.width =
                dSourcePanel.width + dChosenPanel.width + (editButton != null ? editButton.getPreferredSize().width : 0)
                        + (buttonPanel != null ? buttonPanel.getPreferredSize().width : 0) + 20;
        setPreferredSize(fullPanelSize);
    }

    /**
     * Build our component panel.
     *
     * @return component
     */
    protected JComponent buildComponent() {
        helperList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        setLayout(gbl);

        editButton = new JButton("Edit...");
        editButton.setIconTextGap(0);
        editButton.setMargin(new Insets(2, 4, 2, 4));

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                togglePanels();
            }
        });

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 0, 3);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbl.setConstraints(editButton, gbc);
        add(editButton);

        sourceList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        sourceList.addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moveLeftToRight();
                }
            }
        });

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        sourcePanel.add(BorderLayout.NORTH, sourceLabel);
        JScrollPane sourceScroller = new JScrollPane(sourceList);
        sourcePanel.add(BorderLayout.CENTER, sourceScroller);
        gbl.setConstraints(sourcePanel, gbc);
        add(sourcePanel);

        // JPanel buttonPanel = new ControlButtonPanel();
        JPanel buttonPanel = buildButtonPanel();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        gbl.setConstraints(buttonPanel, gbc);
        add(buttonPanel);

        chosenList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        chosenList.addKeyListener(new KeyAdapter() {
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moveRightToLeft();
                }
            }
        });
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        chosenPanel.add(BorderLayout.NORTH, chosenLabel);
        JScrollPane chosenScroller = new JScrollPane(chosenList);
        chosenPanel.add(BorderLayout.CENTER, chosenScroller);
        gbl.setConstraints(chosenPanel, gbc);
        add(chosenPanel);

        editButton.setVisible(showEditButton);
        this.buttonPanel.setVisible(panelsShowing);
        sourcePanel.setVisible(panelsShowing);

        return this;
    }

    /**
     * Construct the control button panel.
     *
     * @return JPanel
     *
     */
    protected JPanel buildButtonPanel() {
        buttonPanel = new JPanel();

        leftToRight = new JButton(">");
        allLeftToRight = new JButton(">>");
        rightToLeft = new JButton("<");
        allRightToLeft = new JButton("<<");
        Font smallerFont = leftToRight.getFont().deriveFont(9.0F);
        leftToRight.setFont(smallerFont);
        allLeftToRight.setFont(smallerFont);
        rightToLeft.setFont(smallerFont);
        allRightToLeft.setFont(smallerFont);

        Insets margin = new Insets(2, 4, 2, 4);
        leftToRight.setMargin(margin);
        allLeftToRight.setMargin(margin);
        rightToLeft.setMargin(margin);
        allRightToLeft.setMargin(margin);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        buttonPanel.setLayout(gbl);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(leftToRight, gbc);
        gbl.setConstraints(allLeftToRight, gbc);
        gbl.setConstraints(rightToLeft, gbc);
        gbl.setConstraints(allRightToLeft, gbc);

        buttonPanel.add(leftToRight);
        buttonPanel.add(allLeftToRight);
        buttonPanel.add(rightToLeft);
        buttonPanel.add(allRightToLeft);

        leftToRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                moveLeftToRight();
            }
        });
        allLeftToRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                moveAllLeftToRight();
            }
        });
        rightToLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                moveRightToLeft();
            }
        });
        allRightToLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                moveAllRightToLeft();
            }
        });

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        // buttonPanel.setBackground( Color.lightGray );
        return buttonPanel;
    }

    /**
     * Toggle the panel visibility. This will hide/show the source list and
     * movement buttons.
     */
    public void togglePanels() {
        panelsShowing = !panelsShowing;
        sourcePanel.setVisible(panelsShowing);
        buttonPanel.setVisible(panelsShowing);
    }

    /**
     * Move the selected items in the source list to the chosen list. I.e., add
     * the items to our selection model.
     */
    protected void moveLeftToRight() {
        // Loop over the selected items and locate them in the data model, Add
        // these to the selection.
        Object[] sourceSelected = sourceList.getSelectedValues();
        int nSourceSelected = sourceSelected.length;
        int[] currentSelection = helperList.getSelectedIndices();
        int[] newSelection = new int[currentSelection.length + nSourceSelected];
        System.arraycopy(currentSelection, 0, newSelection, 0, currentSelection.length);
        int destPos = currentSelection.length;

        for (int i = 0; i < sourceSelected.length; i++) {
            newSelection[destPos++] = indexOf(sourceSelected[i]);
        }

        helperList.setSelectedIndices(newSelection);
        update();
    }

    /**
     * Move all the source items to the chosen side. I.e., select all the items.
     */
    protected void moveAllLeftToRight() {
        int sz = dataModel.getSize();
        int[] selected = new int[sz];
        for (int i = 0; i < sz; i++) {
            selected[i] = i;
        }
        helperList.setSelectedIndices(selected);
        update();
    }

    /**
     * Move the selected items in the chosen list to the source list. I.e.,
     * remove them from our selection model.
     */
    protected void moveRightToLeft() {
        Object[] chosenSelectedValues = chosenList.getSelectedValues();
        int nChosenSelected = chosenSelectedValues.length;
        int[] chosenSelected = new int[nChosenSelected];

        if (nChosenSelected == 0) {
            return; // Nothing to move
        }

        // Get our current selection
        int[] currentSelected = helperList.getSelectedIndices();
        int nCurrentSelected = currentSelected.length;

        // Fill the chosenSelected array with the indices of the selected chosen
        // items
        for (int i = 0; i < nChosenSelected; i++) {
            chosenSelected[i] = indexOf(chosenSelectedValues[i]);
        }

        // Construct the new selected indices. Loop through the current list
        // and compare to the head of the chosen list. If not equal, then add
        // to the new list. If equal, skip it and bump the head pointer on the
        // chosen list.

        int newSelection[] = new int[nCurrentSelected - nChosenSelected];
        int newSelPos = 0;
        int chosenPos = 0;

        for (int i = 0; i < nCurrentSelected; i++) {
            int currentIdx = currentSelected[i];
            if (chosenPos < nChosenSelected && currentIdx == chosenSelected[chosenPos]) {
                chosenPos += 1;
            } else {
                newSelection[newSelPos++] = currentIdx;
            }
        }

        // Install the new selection
        helperList.setSelectedIndices(newSelection);
        update();
    }

    /**
     * Move all the chosen items back to the source side. This simply sets our
     * selection back to empty.
     */
    protected void moveAllRightToLeft() {
        clearSelection();
    }

    /**
     * Get the index of a given object in the underlying data model.
     *
     * @param o Object to locate
     * @return index of object in model, -1 if not found
     */
    protected int indexOf(final Object o) {
        final int size = dataModel.getSize();
        for (int i = 0; i < size; i++) {
            if (comparator == null) {
                if (o.equals(dataModel.getElementAt(i))) {
                    return i;
                }
            } else if (comparator.compare(o, dataModel.getElementAt(i)) == 0) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Update the two lists based on the current selection indices.
     */
    protected void update() {
        int sz = dataModel.getSize();
        int[] selected = helperList.getSelectedIndices();
        ArrayList sourceItems = new ArrayList(sz);
        ArrayList chosenItems = new ArrayList(selected.length);

        // Start with the source items filled from our data model
        for (int i = 0; i < sz; i++) {
            sourceItems.add(dataModel.getElementAt(i));
        }

        // Now move the selected items to the chosen list
        for (int i = selected.length - 1; i >= 0; i--) {
            chosenItems.add(sourceItems.remove(selected[i]));
        }

        Collections.reverse(chosenItems); // We built it backwards

        // Now install the two new lists
        sourceList.setListData(sourceItems.toArray());
        chosenList.setListData(chosenItems.toArray());
    }

    // ========================
    // List Selection handling
    // ========================

    /**
     * Returns the value of the current selection model.
     *
     * @return the <code>ListSelectionModel</code> that implements list
     *         selections
     */
    public ListSelectionModel getSelectionModel() {
        return helperList.getSelectionModel();
    }

    /**
     * Adds a listener to the list that's notified each time a change to the
     * selection occurs.
     *
     * @param listener the <code>ListSelectionListener</code> to add
     */
    public void addListSelectionListener(ListSelectionListener listener) {
        helperList.addListSelectionListener(listener);
    }

    /**
     * Removes a listener from the list that's notified each time a change to
     * the selection occurs.
     *
     * @param listener the <code>ListSelectionListener</code> to remove
     */
    public void removeListSelectionListener(ListSelectionListener listener) {
        helperList.removeListSelectionListener(listener);
    }

    /**
     * Clear the selection. This will populate the source list with all the
     * items from the model and empty the chosen list.
     */
    public void clearSelection() {
        helperList.clearSelection();
        update();
    }

    /**
     * Selects a set of cells.
     *
     * @param indices an array of the indices of the cells to select
     */
    public void setSelectedIndices(int[] indices) {
        helperList.setSelectedIndices(indices);
        update();
    }

    /**
     * Returns an array of the values for the selected cells. The returned
     * values are sorted in increasing index order.
     *
     * @return the selected values or an empty list if nothing is selected
     */
    public Object[] getSelectedValues() {
        return helperList.getSelectedValues();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        helperList.setEnabled(enabled);
        sourceList.setEnabled(enabled);
        chosenList.setEnabled(enabled);
        buttonPanel.setEnabled(enabled);
        leftToRight.setEnabled(enabled);
        allLeftToRight.setEnabled(enabled);
        rightToLeft.setEnabled(enabled);
        allRightToLeft.setEnabled(enabled);
    }
}
