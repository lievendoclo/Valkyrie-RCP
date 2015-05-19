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
package org.valkyriercp.table.support;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.gui.AbstractTableComparatorChooser;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.util.concurrent.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.valkyriercp.application.StatusBar;
import org.valkyriercp.application.event.LifecycleApplicationEvent;
import org.valkyriercp.command.ActionCommandExecutor;
import org.valkyriercp.command.GuardedActionCommandExecutor;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.factory.AbstractControlFactory;
import org.valkyriercp.util.PopupMenuMouseListener;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * This class provides a standard table representation for a set of objects with
 * properties of the objects presented in the columns of the table. The table
 * created offers the following features:
 * <ol>
 * <li>It uses Glazed Lists as the underlying data model and this provides for
 * multi-column sorting and text filtering.</li>
 * <li>It handles row selection.</>
 * <li>It offers simple, delegated handling of how to handle a double-click on a
 * row, by setting a command executor. See
 * {@link #setDoubleClickHandler(ActionCommandExecutor)}.</li>
 * <li>It supports display of a configured pop-up context menu.</li>
 * <li>It can report on row counts (after filtering) and selection counts to a
 * status bar</li>
 * </ol>
 * <p>
 * Several I18N messages are needed for proper reporting to a configured status
 * bar. The message keys used are:
 * <p>
 * <table border="1">
 * <tr>
 * <td><b>Message key </b></td>
 * <td><b>Usage </b></td>
 * </tr>
 * <tr>
 * <td><i>modelId</i>.objectName.singular</td>
 * <td>The singular name of the objects in the table</td>
 * </tr>
 * <tr>
 * <td><i>modelId</i>.objectName.plural</td>
 * <td>The plural name of the objects in the table</td>
 * </tr>
 * <tr>
 * <td><i>[modelId]</i>.objectTable.showingAll.message</td>
 * <td>The message to show when all objects are being shown, that is no objects
 * have been filtered. This is typically something like
 * "Showing all nn contacts". The message takes the number of objects nd the
 * object name (singular or plural) as parameters.</td>
 * </tr>
 * <tr>
 * <td><i>[modelId]</i>.objectTable.showingN.message</td>
 * <td>The message to show when some of the objects have been filtered from the
 * display. This is typically something like "Showing nn contacts of nn". The
 * message takes the shown count, the total count, and the object name (singular
 * or plural) as parameters.</td>
 * </tr>
 * <tr>
 * <td><i>[modelId]</i>.objectTable.selectedN.message</td>
 * <td>The message to append to the filter message when the selection is not
 * empty. Typically something like ", nn selected". The message takes the number
 * of selected entries as a parameter.</td>
 * </tr>
 * </table>
 * <p>
 * Note that the message keys that show the model id in brackets, like this
 * <i>[modelId]</i>, indicate that the model id is optional. If no message is
 * found using the model id, then the key will be tried without the model id and
 * the resulting string will be used. This makes it easy to construct one single
 * message property that can be used on numerous tables.
 * <p>
 * <em>Note:</em> If you are using application events to inform UI components of
 * changes to domain objects, then instances of this class have to be wired into
 * the event distribution. To do this, you should construct instances (of
 * concrete subclasses) in the application context. They will automatically be
 * wired into the epplication event mechanism because this class implements
 * {@link org.springframework.context.ApplicationListener}.
 * 
 * @author Larry Streepy
 */
public abstract class AbstractObjectTable extends AbstractControlFactory
// implements ApplicationListener
{

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final String modelId;

	private String objectSingularName;

	private String objectPluralName;

	private Object[] initialData;

	private String[] columnPropertyNames;

	private GlazedTableModel model;

	private SortedList baseList;

	private EventList finalEventList;

	private ActionCommandExecutor doubleClickHandler;

	private CommandGroup popupCommandGroup;

	private StatusBar statusBar;

	private AbstractTableComparatorChooser tableSorter;

	public static final String SHOWINGALL_MSG_KEY = "objectTable.showingAll.message";

	public static final String SHOWINGN_MSG_KEY = "objectTable.showingN.message";

	public static final String SELECTEDN_MSG_KEY = "objectTable.selectedN.message";

	/**
	 * Constructor.
	 * 
	 * @param modelId
	 *            used for generating message keys
	 */
	public AbstractObjectTable(String modelId, String[] columnPropertyNames) {
		this.modelId = modelId;
		setColumnPropertyNames(columnPropertyNames);
		init();
        postConstruct();
	}

	protected void postConstruct() {
		// this will cause a memory leak! as there is currently no support for a
		// call to removeApplicationListener
		((ConfigurableApplicationContext) getApplicationConfig()
				.applicationContext())
				.addApplicationListener(new ApplicationListener() {
					@Override
					public void onApplicationEvent(ApplicationEvent event) {
						AbstractObjectTable.this.onApplicationEvent(event);
					}
				});
	}

	/**
	 * Set the initial data to display.
	 * 
	 * @param initialData
	 *            Array of objects to display
	 */
	public void setInitialData(Object[] initialData) {
		this.initialData = initialData;
	}

	/**
	 * Get the initial data to display. If none has been set, then return the
	 * default initial data.
	 * 
	 * @return initial data to display
	 * @see #getDefaultInitialData()
	 */
	public Object[] getInitialData() {
		if (initialData == null) {
			initialData = getDefaultInitialData();
		}
		return initialData;
	}

	/**
	 * Get the base event list for the table model. This can be used to build
	 * layered event models for filtering.
	 * 
	 * @return base event list
	 */
	public EventList getBaseEventList() {
		if (baseList == null) {
			// Construct on demand
			Object[] data = getInitialData();
			if (logger.isDebugEnabled()) {
				logger.debug("Table data: got " + data.length + " entries");
			}
			// Construct the event list of all our data and layer on the sorting
			EventList rawList = GlazedLists.eventList(Arrays.asList(data));
			int initialSortColumn = getInitialSortColumn();
			if (initialSortColumn >= 0) {
				String sortProperty = getColumnPropertyNames()[initialSortColumn];
				baseList = new SortedList(rawList, new PropertyComparator(
						sortProperty, false, true));
			} else {
				baseList = new SortedList(rawList);
			}
		}
		return baseList;
	}

	/**
	 * Set the event list to be used for constructing the table model. The event
	 * list provided MUST have been constructed from the list returned by
	 * {@link #getBaseEventList()} or this table will not work properly.
	 * 
	 * @param finalEventList
	 *            list to use
	 */
	public void setFinalEventList(EventList finalEventList) {
		this.finalEventList = finalEventList;
	}

	/**
	 * Get the event list to be use for constructing the table model.
	 * 
	 * @return final event list
	 */
	public EventList getFinalEventList() {
		if (finalEventList == null) {
			finalEventList = getBaseEventList();
		}
		return finalEventList;
	}

	/**
	 * Get the data model for the table.
	 * <p>
	 * <em>Note:</em> This method returns null unless {@link #getTable()} or is
	 * called
	 * 
	 * @return model the table model which is used for the table
	 */
	public GlazedTableModel getTableModel() {
		return model;
	}

	/**
	 * Get the names of the properties to display in the table columns.
	 * 
	 * @return array of columnproperty names
	 */
	public String[] getColumnPropertyNames() {
		return columnPropertyNames;
	}

	/**
	 * Set the names of the properties to display in the table columns.
	 * 
	 * @param columnPropertyNames
	 */
	public void setColumnPropertyNames(String[] columnPropertyNames) {
		this.columnPropertyNames = columnPropertyNames;
	}

	/**
	 * @return the doubleClickHandler
	 */
	public ActionCommandExecutor getDoubleClickHandler() {
		return doubleClickHandler;
	}

	/**
	 * Set the handler (action executor) that should be invoked when a row in
	 * the table is double-clicked.
	 * 
	 * @param doubleClickHandler
	 *            the doubleClickHandler to set
	 */
	public void setDoubleClickHandler(ActionCommandExecutor doubleClickHandler) {
		this.doubleClickHandler = doubleClickHandler;
	}

	/**
	 * Returns the sorter which is used to sort the content of the table
	 * 
	 * @return the sorter, null if {@link #getTable()} is not called before
	 */
	protected AbstractTableComparatorChooser getTableSorter() {
		return tableSorter;
	}

	/**
	 * @return the popupCommandGroup
	 */
	public CommandGroup getPopupCommandGroup() {
		return popupCommandGroup;
	}

	/**
	 * Set the command group that should be used to construct the popup menu
	 * when a user initiates the UI gesture to show the context menu. If this is
	 * null, then no popup menu will be shown.
	 * 
	 * @param popupCommandGroup
	 *            the popupCommandGroup to set
	 */
	public void setPopupCommandGroup(CommandGroup popupCommandGroup) {
		this.popupCommandGroup = popupCommandGroup;
	}

	/**
	 * Set the status bar associated with this table. If non-null, then any time
	 * the final event list on this table changes, then the status bar will be
	 * updated with the current object counts.
	 * 
	 * @param statusBar
	 *            to update
	 */
	public void setStatusBar(StatusBar statusBar) {
		this.statusBar = statusBar;
		updateStatusBar();
	}

	/**
	 * @return the modelId
	 */
	public String getModelId() {
		return modelId;
	}

	/**
	 * Initialize our internal values.
	 */
	protected void init() {
		// Get all our messages
		objectSingularName = getApplicationConfig().messageResolver().getMessage(
				modelId + ".objectName.singular");
		objectPluralName = getApplicationConfig().messageResolver().getMessage(
				modelId + ".objectName.plural");
	}

	protected JComponent createControl() {
		// Contstruct the table model and table to display the data
		EventList finalEventList = getFinalEventList();
		model = createTableModel(finalEventList);

		JTable table = getApplicationConfig().componentFactory().createTable(model);
		table.setSelectionModel(new EventSelectionModel(finalEventList));
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Install the sorter
		Assert.notNull(baseList);
		tableSorter = createTableSorter(table, baseList);

		// Allow the derived type to configure the table
		configureTable(table);

		int initialSortColumn = getInitialSortColumn();
		if (initialSortColumn >= 0) {
			tableSorter.clearComparator();
			tableSorter.appendComparator(initialSortColumn, 0, false);
		}

		// Add the context menu listener
		table.addMouseListener(new ContextPopupMenuListener());

		// Add our mouse handlers to setup our desired selection mechanics
		table.addMouseListener(new DoubleClickListener());

		// Keep our status line up to date with the selections and filtering
		StatusBarUpdateListener statusBarUpdateListener = new StatusBarUpdateListener();
		table.getSelectionModel().addListSelectionListener(
				statusBarUpdateListener);
		getFinalEventList().addListEventListener(statusBarUpdateListener);

		return table;
	}

	/**
	 * Configure the newly created table as needed. Install any needed column
	 * sizes, renderers, and comparators. The default implementation does
	 * nothing.
	 * 
	 * @param table
	 *            The table to configure
	 */
	protected void configureTable(JTable table) {
	}

	/**
	 * Get the default set of objects for this table.
	 * 
	 * @return Array of data for the table
	 */
	protected abstract Object[] getDefaultInitialData();

	/**
	 * Returns the created JTable.
	 */
	protected JTable getTable() {
		return (JTable) getControl();
	}

	protected AbstractTableComparatorChooser createTableSorter(JTable table,
			SortedList sortedList) {
		return new TableComparatorChooser(table, sortedList,
				isMultipleColumnSort());
	}

	protected boolean isMultipleColumnSort() {
		return true;
	}

	/**
	 * Handle a double click on a row of the table. The row will already be
	 * selected.
	 */
	protected void onDoubleClick() {
		// Dispatch this to the doubleClickHandler, if any
		if (doubleClickHandler != null) {
			boolean okToExecute = true;
			if (doubleClickHandler instanceof GuardedActionCommandExecutor) {
				okToExecute = ((GuardedActionCommandExecutor) doubleClickHandler)
						.isEnabled();
			}

			if (okToExecute) {
				doubleClickHandler.execute();
			}
		}
	}

	/**
	 * Construct the table model for this table. The default implementation of
	 * this creates a GlazedTableModel using an Advanced format.
	 * 
	 * @param eventList
	 *            on which to build the model
	 * @return table model
	 */
	protected GlazedTableModel createTableModel(EventList eventList) {
		return new GlazedTableModel(eventList, getColumnPropertyNames(),
				modelId) {
			protected TableFormat createTableFormat() {
				return new DefaultAdvancedTableFormat();
			}
		};
	}

	/**
	 * Determine if the event should be handled on this table. If
	 * <code>true</code> is returned (the default), then the list holding the
	 * table data will be scanned for the object and updated appropriately
	 * depending on then event type.
	 * 
	 * @param event
	 *            to inspect
	 * @return boolean true if the object should be handled, false otherwise
	 * @see #handleDeletedObject(Object)
	 * @see #handleNewObject(Object)
	 * @see #handleUpdatedObject(Object)
	 */
	protected boolean shouldHandleEvent(ApplicationEvent event) {
		return true;
	}

	/**
	 * Create the context popup menu, if any, for this table. The default
	 * operation is to create the popup from the command group if one has been
	 * specified. If not, then null is returned.
	 * 
	 * @return popup menu to show, or null if none
	 */
	protected JPopupMenu createPopupContextMenu() {
		return (getPopupCommandGroup() != null) ? getPopupCommandGroup()
				.createPopupMenu() : null;
	}

	/**
	 * Create the context popup menu, if any, for this table. The default
	 * operation is to create the popup from the command group if one has been
	 * specified. If not, then null is returned.
	 * 
	 * @param e
	 *            the event which contains information about the current
	 *            context.
	 * @return popup menu to show, or null if none
	 */
	protected JPopupMenu createPopupContextMenu(MouseEvent e) {
		return createPopupContextMenu();
	}

	/**
	 * Get the default sort column. Defaults to 0.
	 * 
	 * @return column to sort on
	 */
	protected int getInitialSortColumn() {
		return 0;
	}

	/**
	 * Get the selection model.
	 * 
	 * @return selection model
	 */
	public ListSelectionModel getSelectionModel() {
		return getTable().getSelectionModel();
	}

	/**
	 * Executes the runnable with a write lock on the event list.
	 * 
	 * @param runnable
	 *            its run method is executed while holding a write lock for the
	 *            event list.
	 * 
	 * @see #getFinalEventList()
	 */
	protected void runWithWriteLock(Runnable runnable) {
		runWithLock(runnable, getFinalEventList().getReadWriteLock()
				.writeLock());
	}

	/**
	 * Executes the runnable with a read lock on the event list.
	 * 
	 * @param runnable
	 *            its run method is executed while holding a read lock for the
	 *            event list.
	 * 
	 * @see #getFinalEventList()
	 */
	protected void runWithReadLock(Runnable runnable) {
		runWithLock(runnable, getFinalEventList().getReadWriteLock().readLock());
	}

	private void runWithLock(Runnable runnable, Lock lock) {
		Assert.notNull(runnable);
		Assert.notNull(lock);
		lock.lock();
		try {
			runnable.run();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Handle the creation of a new object.
	 * 
	 * @param object
	 *            New object to handle
	 */
	protected void handleNewObject(final Object object) {
		runWithWriteLock(new Runnable() {
			public void run() {
				getFinalEventList().add(object);
			}
		});
	}

	/**
	 * Handle an updated object in this table. Locate the existing entry (by
	 * equals) and replace it in the underlying list.
	 * 
	 * @param object
	 *            Updated object to handle
	 */
	protected void handleUpdatedObject(final Object object) {
		runWithWriteLock(new Runnable() {
			public void run() {
				int index = getFinalEventList().indexOf(object);
				if (index >= 0) {
					getFinalEventList().set(index, object);
				}
			}
		});
	}

	/**
	 * Handle the deletion of an object in this table. Locate this entry (by
	 * equals) and delete it.
	 * 
	 * @param object
	 *            Updated object being deleted
	 */
	protected void handleDeletedObject(final Object object) {
		runWithWriteLock(new Runnable() {
			public void run() {
				int index = getFinalEventList().indexOf(object);
				if (index >= 0) {
					getFinalEventList().remove(index);
				}
			}
		});
	}

	/**
	 * Update the status bar with the current display counts.
	 */
	protected void updateStatusBar() {
		if (statusBar != null) {
			int all = getBaseEventList().size();
			int showing = getFinalEventList().size();
			String msg;
			if (all == showing) {
				String[] keys = new String[] {
						modelId + "." + SHOWINGALL_MSG_KEY, SHOWINGALL_MSG_KEY };
				msg = getApplicationConfig().messageResolver().getMessage(
						keys,
						new Object[] {
								"" + all,
								(all != 1) ? objectPluralName
										: objectSingularName });
			} else {
				String[] keys = new String[] {
						modelId + "." + SHOWINGN_MSG_KEY, SHOWINGN_MSG_KEY };

				msg = getApplicationConfig().messageResolver().getMessage(
						keys,
						new Object[] {
								"" + showing,
								(showing != 1) ? objectPluralName
										: objectSingularName, "" + all });
			}
			// Now add the selection info
			int nselected = getTable().getSelectedRowCount();
			if (nselected > 0) {
				String[] keys = new String[] {
						modelId + "." + SELECTEDN_MSG_KEY, SELECTEDN_MSG_KEY };

				msg += getApplicationConfig().messageResolver().getMessage(keys,
						new Object[] { "" + nselected });
			}
			statusBar.setMessage(msg.toString());
		}
	}

	/**
	 * Handle an application event. This will notify us of object adds, deletes,
	 * and modifications. Update our table model accordingly.
	 * 
	 * @param e
	 *            event to process
	 */
	public void onApplicationEvent(ApplicationEvent e) {
		if (e instanceof LifecycleApplicationEvent) {
			LifecycleApplicationEvent le = (LifecycleApplicationEvent) e;
			if (shouldHandleEvent(e)) {
				if (le.getEventType() == LifecycleApplicationEvent.CREATED) {
					handleNewObject(le.getObject());
				} else if (le.getEventType() == LifecycleApplicationEvent.MODIFIED) {
					handleUpdatedObject(le.getObject());
				} else if (le.getEventType() == LifecycleApplicationEvent.DELETED) {
					handleDeletedObject(le.getObject());
				}
			}
		}
	}

	final class ContextPopupMenuListener extends PopupMenuMouseListener {
		protected JPopupMenu getPopupMenu(MouseEvent e) {
			return createPopupContextMenu(e);
		}
	}

	final class DoubleClickListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			// If the user right clicks on a row other than the selection,
			// then move the selection to the current row
			if (e.getButton() == MouseEvent.BUTTON3) {
				int rowUnderMouse = getTable().rowAtPoint(e.getPoint());
				if (rowUnderMouse != -1
						&& !getTable().isRowSelected(rowUnderMouse)) {
					// Select the row under the mouse
					getSelectionModel().setSelectionInterval(rowUnderMouse,
							rowUnderMouse);
				}
			}
		}

		/**
		 * Handle double click.
		 */
		public void mouseClicked(MouseEvent e) {
			// If the user double clicked on a row, then call onDoubleClick
			if (e.getClickCount() == 2) {
				onDoubleClick();
			}
		}
	}

	final class StatusBarUpdateListener implements ListSelectionListener,
			ListEventListener {
		public void valueChanged(ListSelectionEvent e) {
			updateStatusBar();
		}

		public void listChanged(ListEvent listChanges) {
			updateStatusBar();
		}
	}
}
