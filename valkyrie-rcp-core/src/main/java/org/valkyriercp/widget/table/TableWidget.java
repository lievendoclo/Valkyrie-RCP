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

import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.widget.Widget;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import java.util.Collection;
import java.util.List;
import java.util.Observer;

/**
 * TableWidget describes the needed functionality for widgets
 * that produce tables.
 *
 * The TableWidget manages a data-list of objects whose properties
 * are shown in one or more different columns of the table.
 *
 * Some methods of TableWidget (remove, select) assume correct implementation
 * of {@link java.lang.Object#equals(java.lang.Object)} and
 * {@link java.lang.Object#hashCode()} for the data-list's objects.
 */
public interface TableWidget<T> extends Widget
{
    /**
     * Sets the rows to be shown in the table
     *
     * @param newRows
     *            The list of row objects
     */
    void setRows(Collection<T> newRows);

    /**
     * Returns the current list of objects behind the table. It does not
     * take into account local filtering and/or sorting.
     *
     * @return List the list with objects
     * @see #getVisibleRows()
     */
    List<T> getRows();

    /**
     * Returns the current list of objects visible in the table. It takes into
     * account local filtering and/or sorting.
     *
     * @return List the visible rows
     * @see #getRows()
     */
    List<T> getVisibleRows();

    /**
     * Numbers of rows in the table
     *
     * @return
     */
    int nrOfRows();

    /**
     * @return true if the data-list is empty
     */
    boolean isEmpty();

    /**
     * Local addition of a single object to the data-list of the table
     *
     * @param newObject
     *            The object to add
     */
    void addRowObject(T newObject);

    /**
     * Local addition of a collection of objects to the data-list of the table
     *
     * @param rows
     *            The collection of objects to add
     */
    void addRows(Collection<T> rows);

    /**
     * Local removal of a single object to the data-list of the table
     *
     * @param objectToRemove
     *            The object to remove
     */
    void removeRowObject(T objectToRemove);

    /**
     * Selects the row of a given object and notifies the registered selection
     * observers. The second argument allows 1 observer to be excluded from
     * the notification (needed if the observer is the caller).
     *
     * @param toPointTo
     *            row-object that needs to be selected
     * @param originatingObserver
     *            Optional observer that doesn't want to be notified of the
     *            selection
     * @return int index of the object in the shown list
     *
     * @see #addSelectionObserver(java.util.Observer)
     */
    int selectRowObject(T toPointTo, Observer originatingObserver);

    /**
     * Selects a row on the given index
     *
     * @param index
     *            index of the row that needs to be selected
     * @param originatingObserver
     *            Optional observer that doesn't want to be notified of the
     *            selection
     */
    void selectRowObject(int index, Observer originatingObserver);

    /**
     * Adds an array of objects to the current selection
     *
     * @param rows
     *            Array with row objects
     * @param originatingObserver
     *            Optional observer that doesn't want to be notified of the
     *            selection
     */
    void addSelection(T[] rows, Observer originatingObserver);

    /**
     * Replaces an object in the table
     *
     * @param oldObject
     *            The object to be replaced
     * @param newObject
     *            The replacing object
     * @param originatingObserver
     *            Optional observer that doesn't want to be notified of the
     *            replacement
     */
    void replaceRowObject(T oldObject, T newObject, Observer originatingObserver);

    /**
     * Replaces a collection of objects in the table.
     *
     * @param oldObject
     *           The collection of objects to be replaced
     * @param newObject
     *           The replacing collection of objects
     */
    void replaceRows(Collection<T> oldObject, Collection<T> newObject);

    /**
     * Deselects all rows
     */
    void unSelectAll();

    /**
     * @return Array of objects that are currently selected
     */
    Object[] getSelectedRows();

    /**
     * @return true if there is an active selection
     */
    boolean hasSelection();

    /**
     * Registers a listener that will receive events on selection of an object
     * in the table.
     *
     * @see #selectRowObject(Object, Observer)
     */
    void addSelectionObserver(Observer observer);

    /**
     * Removes a selection listener
     *
     * @param observer
     */
    void removeSelectionObserver(Observer observer);

    /**
     * Registers a listener for changes made to TableModel
     *
     * @param listener
     */
    void addTableModelListener(TableModelListener listener);

    /**
     * Remove the given listener from the TableModel.
     *
     * @param listener
     */
    void removeTableModelListener(TableModelListener listener);

    /**
     * Forces a re-read of the data-list for the table
     */
    void updateTable();

    /*
     * VISUAL COMPONENTS & COMMANDS
     */

    /**
     * Indices of the navigate-commands in the list
     * {@link #getNavigationCommands()}.
     */
    public static final int NAVIGATE_FIRST = 0;
    public static final int NAVIGATE_PREVIOUS = 1;
    public static final int NAVIGATE_NEXT = 2;
    public static final int NAVIGATE_LAST = 3;

    /**
     * Id's for the selection commands.
     */
    public static final String SELECT_ALL_ID = "select.all";
    public static final String SELECT_NONE_ID = "select.none";
    public static final String SELECT_INVERSE_ID = "select.inverse";

    /**
     * Command Id's for the navigation commands for the table
     * {@link #getNavigationCommands()}.
     */
    public static final String NAVIGATE_LASTROW_CMDID = "lastrow";
    public static final String NAVIGATE_NEXTROW_CMDID = "nextrow";
    public static final String NAVIGATE_PREVIOUSROW_CMDID = "previousrow";
    public static final String NAVIGATE_FIRSTROW_CMDID = "firstrow";

    /**
     * Spring-RCP commands to navigate through the table
     *
     * @see #NAVIGATE_FIRST
     * @see #NAVIGATE_PREVIOUS
     * @see #NAVIGATE_NEXT
     * @see #NAVIGATE_LAST
     */
    List<AbstractCommand> getNavigationCommands();

    /**
     * The {@link #getNavigationCommands() navigation-commands} as button bar
     */
    JComponent getNavigationButtonBar();

    /**
     * Buttonbar with three buttons: select all, select none and select inverse
     */
    JComponent getSelectButtonBar();

    /**
     * Combination of the navigation button bar and the selection button bar
     */
    JComponent getButtonBar();

    /**
     * Returns the table, getComponent() does not need to return only table
     * (a scrollpane can be returned too containing the table), as this method
     * will enable you to get to the table.
     *
     * @return The table
     * @see Widget#getComponent()
     */
    JTable getTable();

    /**
     * @return A textfield that enables full-text filtering n the contents of the table
     *         or null if this feature is not supported
     */
    JTextField getTextFilterField();

    /**
     * @return A label on which the index, the selected row count and the row count is shown
     */
    public JLabel getListSummaryLabel();
}
