package org.valkyriercp.widget.editor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;
import org.valkyriercp.application.StatusBar;
import org.valkyriercp.application.session.ApplicationSession;
import org.valkyriercp.application.support.StatusBarProgressMonitor;
import org.valkyriercp.binding.validation.support.DefaultValidationMessage;
import org.valkyriercp.binding.validation.support.DefaultValidationResultsModel;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.core.DefaultMessage;
import org.valkyriercp.core.Severity;
import org.valkyriercp.form.AbstractForm;
import org.valkyriercp.form.FilterForm;
import org.valkyriercp.util.MessageConstants;
import org.valkyriercp.widget.AbstractWidget;
import org.valkyriercp.widget.Widget;
import org.valkyriercp.widget.editor.provider.DataProvider;
import org.valkyriercp.widget.editor.provider.DataProviderEvent;
import org.valkyriercp.widget.editor.provider.DataProviderListener;
import org.valkyriercp.widget.editor.provider.MaximumRowsExceededException;
import org.valkyriercp.widget.table.TableDescription;
import org.valkyriercp.widget.table.TableWidget;
import org.valkyriercp.widget.table.glazedlists.GlazedListTableWidget;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * DefaultDataEditorWidget is a basic implementation of a
 * {@link AbstractDataEditorWidget}.
 */
public class DefaultDataEditorWidget extends AbstractDataEditorWidget
        implements
        DataProviderListener,
        PropertyChangeListener
{

    private static Log log = LogFactory.getLog(DefaultDataEditorWidget.class);

    /**
     * Detailform of this dataeditor (under table).
     */
    private AbstractForm detailForm;

    /**
     * Filterform of this dataeditor (next to table).
     */
    private FilterForm filterForm;

    /**
     * Table with data objects
     */
    private TableWidget tableWidget;

    /**
     * Constant to be used to embed a dataEditor parameterMap in a command parameterMap.
     */
    public static final String PARAMETER_MAP = "dataEditorParameters";

    /**
     * Parameter to provide a filter.
     */
    public static final String PARAMETER_FILTER = "filter";

    /**
     * Parameter to provide a default selected object.
     */
    public static final String PARAMETER_DEFAULT_SELECTED_OBJECT = "defaultSelectedObject";

    /**
     * DataProvider manages data access and determines CRUD capabilities.
     */
    private DataProvider dataProvider;

    /**
     * {@link org.valkyriercp.binding.validation.ValidationResultsModel} combining results from embedded forms and other messages.
     */
    private final DefaultValidationResultsModel validationResultsModel = new DefaultValidationResultsModel();

    private ListRetrievingWorker listWorker;

    private final MaximumRowsExceededMessage maximumRowsExceededMessage = new MaximumRowsExceededMessage();

    protected static class MaximumRowsExceededMessage extends DefaultValidationMessage
    {

        private String message;

        public MaximumRowsExceededMessage()
        {
            super("maximumRowsExceeded", Severity.WARNING, "maximumRowsExceeded");
        }

        @Override
        public String getMessage()
        {
            return message;
        }

        public void setMessage(String message)
        {
            this.message = message;
        }

    }

    /**
     * {@link org.jdesktop.swingworker.SwingWorker} which retrieves list from back-end and fills table with result.
     * <p/>
     * Remember to set criteria and launch this class in a synchronised block.
     */
    private class ListRetrievingWorker extends SwingWorker<List<Object>, String>
    {

        /**
         * The filter criteria to use.
         */
        protected Object filterCriteria;

        /**
         * Additional parameters that are coupled with this instance/run.
         */
        protected Map<String, Object> parameters;

        @Override
        protected List<Object> doInBackground() throws Exception
        {
            return getDataProvider().getList(filterCriteria);
        }

        /**
         * Set the rows in the table.
         */
        @Override
        protected void done()
        {
            try
            {
                listWorkerDone(get(), parameters);
            }
            catch (InterruptedException e)
            {
                // someone cancelled the retrieval?
            }
            catch (ExecutionException e)
            {
                if (e.getCause() instanceof MaximumRowsExceededException)
                {
                    MaximumRowsExceededException mre = (MaximumRowsExceededException) e.getCause();
                    setRows(Collections.EMPTY_LIST);
                    validationResultsModel.removeMessage(maximumRowsExceededMessage);
                    maximumRowsExceededMessage.setMessage(applicationConfig.messageResolver().getMessage("MaximumRowsExceededException.notice", new Object[]{mre.getNumberOfRows(), mre.getMaxRows()}));
                    validationResultsModel.addMessage(maximumRowsExceededMessage);
                    if (getToggleFilterCommand() != null)
                    {
                        getToggleFilterCommand().doShow();
                    }
                }
                else
                {
                    throw new RuntimeException(e);
                }
            }
            finally
            {
                applicationConfig.windowManager().getActiveWindow().getStatusBar().getProgressMonitor().done();
                //                getFilterForm().getCommitCommand().setEnabled(true);
                //                getRefreshCommand().setEnabled(true);
                listWorker = null;
            }
        }
    }

    /**
     * This method is called on the gui-thread when the worker ends. As default it will check for the
     * PARAMETER_DEFAULT_SELECTED_OBJECT parameter in the map.
     *
     * @param rows       fetched by the listWorker.
     * @param parameters a map of parameters specific to this listWorker instance.
     */
    protected void listWorkerDone(List<Object> rows, Map<String, Object> parameters)
    {
        setRows(rows);
        // remove maximumRowsExceededMessages if needed
        validationResultsModel.removeMessage(maximumRowsExceededMessage);
        if ((rows == null) || (rows.size() == 0))
        {
            return;
        }

        Object defaultSelectedObject = null;
        if (parameters.containsKey(PARAMETER_DEFAULT_SELECTED_OBJECT))
        {
            defaultSelectedObject = parameters.get(PARAMETER_DEFAULT_SELECTED_OBJECT);
        }

        if (defaultSelectedObject == null)
        {
            tableWidget.selectRowObject(0, null);
        }
        else
        {
            tableWidget.selectRowObject(defaultSelectedObject, null);
        }
    }

    /**
     * Default constructor. Add id, {@link DataProvider}, {@link org.valkyriercp.form.Form}s and listView later.
     *
     * @see #setDataProvider(DataProvider)
     * @see #setDetailForm(AbstractForm)
     * @see #setFilterForm(FilterForm)
     * @see #setId(String)
     * @see #setTableWidget(TableDescription)
     * @see #setTableWidget(TableWidget)
     */
    public DefaultDataEditorWidget(String id)
    {
        setId(id);
    }

    @PostConstruct
    private void configureDataEditorObject() {
        applicationConfig.applicationObjectConfigurer().configure(this, getId());
    }

//    /**
//     * Constructor with id and {@link DataProvider}. Add {@link org.valkyriercp.form.Form}s and listView later.
//     *
//     * @param id       used to fetch messages/icons.
//     * @param provider provides the data manipulation and possible CRUD options.
//     * @see #setDetailForm(AbstractForm)
//     * @see #setFilterForm(FilterForm)
//     * @see #setTableWidget(TableDescription)
//     * @see #setTableWidget(TableWidget)
//     */
//    public DefaultDataEditorWidget(String id, DataProvider provider)
//    {
//        this(id, provider, null, null, null);
//    }
//
//    public DefaultDataEditorWidget(DataProvider provider, AbstractForm form, TableDescription tableDesc,
//                                   FilterForm filterForm)
//    {
//        this(null, provider, form, tableDesc, filterForm);
//    }
//
//    /**
//     * Constructor allowing to set all major components at once.
//     *
//     * @param id         used to fetch messages/icons.
//     * @param provider   provides the data manipulation and possible CRUD options.
//     * @param form       used to display and edit one row detail.
//     * @param tableDesc  describes the columns of the table to build.
//     * @param filterForm optional form used to filter the data.
//     */
//    public DefaultDataEditorWidget(String id, DataProvider provider, AbstractForm form,
//                                   TableDescription tableDesc, FilterForm filterForm)
//    {
//        setId(id);
//        setDataProvider(provider);
//        setDetailForm(form);
//        setTableWidget(tableDesc);
//        setFilterForm(filterForm);
//    }

    @Override
    public void setTitle(String title)
    {
        super.setTitle(title);
        tableWidget.getTable().setName(title);
    }

    /**
     * Returns only the detail form widget
     */
    @Override
    public Widget createDetailWidget()
    {
        return new AbstractWidget()
        {

            @Override
            public void onAboutToShow()
            {
                DefaultDataEditorWidget.this.onAboutToShow();
            }

            @Override
            public void onAboutToHide()
            {
                DefaultDataEditorWidget.this.onAboutToHide();
            }

            public JComponent getComponent()
            {
                return getDetailForm().getControl();
            }

            @Override
            public List<? extends AbstractCommand> getCommands()
            {
                return Arrays.asList(getDetailForm().getCommitCommand());
            }

            @Override
            public String getId() {
                return DefaultDataEditorWidget.this.getId() + "." + getDetailForm().getId();
            }
        };
    }

    /**
     * Set the form that will handle one detail item.
     */
    protected void setDetailForm(AbstractForm detailForm)
    {
        if (this.detailForm != null)
        {
            validationResultsModel.remove(this.detailForm.getFormModel().getValidationResults());
        }

        this.detailForm = detailForm;

        if (this.detailForm != null)
        {
            validationResultsModel.add(this.detailForm.getFormModel().getValidationResults());
        }
    }

    @Override
    public AbstractForm getDetailForm()
    {
        return this.detailForm;
    }

    /**
     * Set the form to use as filter.
     *
     * @see DataProvider#supportsFiltering()
     */
    protected void setFilterForm(FilterForm filterForm)
    {
        if (this.filterForm != null)
        {
            validationResultsModel.remove(this.filterForm.getFormModel().getValidationResults());
        }

        this.filterForm = filterForm;

        if (this.filterForm != null)
        {
            validationResultsModel.add(filterForm.getFormModel().getValidationResults());
        }
    }

    @Override
    public FilterForm getFilterForm()
    {
        return this.filterForm;
    }

    public void setFilterModel(Object model)
    {
        getFilterForm().setFormObject(model);
    }

    /**
     * Create a {@link GlazedListTableWidget} based on the given {@link TableDescription} to be used as
     * listView.
     *
     * @param tableDescription description of columns used to create the table.
     */
    protected void setTableWidget(TableDescription tableDescription)
    {
        if (tableDescription != null)
        {
            TableWidget tableWidget = new GlazedListTableWidget(null, tableDescription);
            setTableWidget(tableWidget);
        }
    }

    /**
     * Set the listView of this dataEditor.
     */
    protected void setTableWidget(TableWidget tableWidget)
    {
        if (this.tableWidget != null)
        {
            this.tableWidget.removeSelectionObserver(tableSelectionObserver);
        }

        this.tableWidget = tableWidget;

        if (this.tableWidget != null)
        {
            this.tableWidget.addSelectionObserver(tableSelectionObserver);
        }
    }

    @Override
    public TableWidget getTableWidget()
    {
        return this.tableWidget;
    }

    /**
     * Set the provider to use for data manipulation.
     */
    protected void setDataProvider(DataProvider provider)
    {
        if ((this.dataProvider != null)
                && (this.dataProvider.getRefreshPolicy() == DataProvider.RefreshPolicy.ON_USER_SWITCH))
        {
            applicationConfig.applicationSession().removePropertyChangeListener(ApplicationSession.USER, this);
        }

        this.dataProvider = provider;

        if ((this.dataProvider != null)
                && (this.dataProvider.getRefreshPolicy() == DataProvider.RefreshPolicy.ON_USER_SWITCH))
        {
            applicationConfig.applicationSession().addPropertyChangeListener(ApplicationSession.USER, this);
        }
    }

    public DataProvider getDataProvider()
    {
        return dataProvider;
    }

    @Override
    protected boolean isUpdateRowSupported()
    {
        return this.dataProvider.supportsUpdate();
    }

    @Override
    protected boolean isAddRowSupported()
    {
        return this.dataProvider.supportsCreate();
    }

    @Override
    protected boolean isCloneRowSupported()
    {
        return this.dataProvider.supportsClone() && this.dataProvider.supportsCreate();
    }

    @Override
    protected boolean isFilterSupported()
    {
        return this.dataProvider.supportsFiltering();
    }

    @Override
    protected boolean isRemoveRowsSupported()
    {
        return this.dataProvider.supportsDelete();
    }

    /**
     * Executes filter and fills table in specific manner:
     * <p/>
     * <ul>
     * <li>set baseCriteria if needed</li>
     * <li>set searchCriteria on filterForm</li>
     * <li>set searchCriteria on worker</li>
     * <li>pass parameter map to worker</li>
     * <li>launch worker to retrieve list from back-end and fill table</li>
     * <li>when done, set list and execute additional code taking the parameters into account</li>
     * </ul>
     *
     * @param parameters a number of parameters that can influence this run. Should be a non-modifiable map or a
     *                   specific instance.
     */
    @Override
    public synchronized void executeFilter(Map<String, Object> parameters)
    {
        if (listWorker == null)
        {
            if (dataProvider.supportsBaseCriteria())
            {
                dataProvider.setBaseCriteria(getBaseCriteria());
            }

            StatusBar statusBar = applicationConfig.windowManager().getActiveWindow().getStatusBar();
            statusBar.getProgressMonitor().taskStarted(
                    applicationConfig.messageResolver().getMessage("statusBar", "loadTable", MessageConstants.LABEL),
                    StatusBarProgressMonitor.UNKNOWN);
            //            getFilterForm().getCommitCommand().setEnabled(false);
            //            getRefreshCommand().setEnabled(false);

            listWorker = new ListRetrievingWorker();
            if (dataProvider.supportsFiltering())
            {
                if (parameters.containsKey(PARAMETER_FILTER))
                {
                    setFilterModel(parameters.get(PARAMETER_FILTER));
                }

                listWorker.filterCriteria = getFilterForm().getFilterCriteria();
            }

            listWorker.parameters = parameters;
            log.debug("Execute Filter with criteria: " + listWorker.filterCriteria + " and parameters: "
                    + parameters);
            listWorker.execute();
        }
    }

    /**
     * @see #executeFilter(Map)
     */
    @Override
    public void executeFilter()
    {
        executeFilter(Collections.EMPTY_MAP);
    }

    /**
     * <b>Warning!</b> this can block threads for an extended period, make sure you're aware of this.
     * <p/>
     * <p>
     * Alternative: {@link #executeFilter()} will launch separate worker and fills table.
     * </p>
     *
     * @param criteria
     * @return
     */
    protected List getList(Object criteria)
    {
        if (this.dataProvider.supportsBaseCriteria())
        {
            this.dataProvider.setBaseCriteria(getBaseCriteria());
        }
        try
        {
            List dataSet = this.dataProvider.getList(criteria);
            setRows(dataSet);
            setMessage(null);
            return dataSet;
        }
        catch (MaximumRowsExceededException mre)
        {
            setRows(Collections.EMPTY_LIST);
            setMessage(new DefaultMessage(applicationConfig.messageResolver().getMessage("MaximumRowsExceededException.notice", new Object[] {mre.getNumberOfRows(), mre.getMaxRows()}), Severity.WARNING));
            if (getToggleFilterCommand() != null)
            {
                getToggleFilterCommand().doShow();
            }
            return null;
        }
    }

    /**
     * Internal fill method of the datatable
     * <p/>
     * WARNING: not threadsafe, please call me on the EDT!
     */
    protected void setRows(List dataSet)
    {
        tableWidget.setRows(dataSet);
    }

    protected Object getBaseCriteria()
    {
        return null;
    }

    @Override
    protected Object loadEntityDetails(Object baseObject, boolean forceLoad)
    {
        return this.dataProvider.getDetailObject(baseObject, forceLoad);
    }

    public Object loadSimpleEntity(Object baseObject)
    {
        Object returnValue = this.dataProvider.getSimpleObject(baseObject);
        if (returnValue == null)
        {
            throw new NullPointerException("Returnvalue for dataprovider simple was null");
        }
        return returnValue;
    }

    @Override
    protected Object saveEntity(Object dirtyObject)
    {
        if (!this.dataProvider.supportsUpdate())
        {
            return null;
        }
        return this.dataProvider.update(dirtyObject);
    }

    @Override
    protected void newRow(Object newClone)
    {
        if (newClone == null)
        {
            super.newRow(getDataProvider().newInstance(
                    getFilterForm() == null ? null : getFilterForm().getFormObject()));
        }
        else
        {
            super.newRow(newClone);
        }
    }

    @Override
    protected Object createNewEntity(Object newObject)
    {
        if (!this.dataProvider.supportsCreate())
        {
            return null;
        }
        return this.dataProvider.create(newObject);
    }

    @Override
    protected Object cloneEntity(Object sampleObject)
    {
        if (!this.dataProvider.supportsClone())
        {
            return null;
        }
        return this.dataProvider.clone(sampleObject);
    }

    @Override
    protected void removeEntity(Object objectToRemove)
    {
        if (!this.dataProvider.supportsDelete())
        {
            return;
        }
        this.dataProvider.delete(objectToRemove);
    }

    public void update(Observable o, Object arg)
    {
        if (arg instanceof DataProviderEvent)
        {
            DataProviderEvent obsAct = (DataProviderEvent) arg;
            int act = obsAct.getEventType();
            if (act == DataProviderEvent.EVENT_TYPE_NEW)
            {
                this.tableWidget.addRowObject(obsAct.getNewEntity());
            }
            else if (act == DataProviderEvent.EVENT_TYPE_UPDATE)
            {
                replaceRowObject(obsAct.getOldEntity(), obsAct.getNewEntity());
            }
            else if (act == DataProviderEvent.EVENT_TYPE_DELETE)
            {
                this.tableWidget.removeRowObject(obsAct.getOldEntity());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAboutToShow()
    {
        log.debug(getId() + ": onAboutToShow with refreshPolicy: " + dataProvider.getRefreshPolicy());
        super.onAboutToShow();

        dataProvider.addDataProviderListener(this);
        registerListeners();
        if (detailForm instanceof Widget)
        {
            ((Widget) detailForm).onAboutToShow();
        }

        tableWidget.onAboutToShow();
        // lazy loading, if no list is present, load when widget is shown
        // include RefreshPolicy given by DataProvider
        if ((dataProvider.getRefreshPolicy() != DataProvider.RefreshPolicy.NEVER) && (tableWidget.isEmpty()))
        {
            executeFilter();
        }
        else if (!tableWidget.hasSelection())
        {
            tableWidget.selectRowObject(0, this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAboutToHide()
    {
        log.debug(getId() + ": onAboutToHide with refreshPolicy: " + dataProvider.getRefreshPolicy());
        super.onAboutToHide();

        this.dataProvider.removeDataProviderListener(this);
        unRegisterListeners();
        if (detailForm instanceof Widget)
        {
            ((Widget) detailForm).onAboutToHide();
        }

        if (dataProvider.getRefreshPolicy() == DataProvider.RefreshPolicy.ALLWAYS)
        {
            getTableWidget().setRows(Collections.EMPTY_LIST);
        }
    }

    protected void registerListeners()
    {
    }

    protected void unRegisterListeners()
    {
    }

    /**
     * note: differs from previous method to allow setting of formObject on filterForm. Will probably end up
     * in refactoring of dataeditorwidget.
     *
     * @param criteria formObject to set on FilterForm.
     * @return
     */
    public Object setSelectedSearch(Object criteria)
    {
        // filterField leegmaken
        if (tableWidget.getTextFilterField() != null)
        {
            tableWidget.getTextFilterField().setText("");
        }
        // if Referable == null, empty filterForm and execute filter
        if (criteria == null)
        {
            if (dataProvider.supportsFiltering())
            {
                getFilterForm().getNewFormObjectCommand().execute();
            }
            executeFilter();
            return null;
        }
        List resultList = getList(criteria);
        if (dataProvider.supportsFiltering())
        {
            // adapt filterForm to reflect referable criteria
            if ((resultList == null) || (resultList.size() > 0)) // fill in referable
            {
                getFilterForm().setFormObject(criteria);
            }
            else
            { // empty filterForm and execute
                getFilterForm().getNewFormObjectCommand().execute();
                executeFilter();
            }
        }
        if (resultList != null && resultList.size() == 1)
        {
            // return the detailObject
            //            return loadEntityDetails(resultList.get(0));
            return loadSimpleEntity(resultList.get(0));
        }
        return resultList;
    }

    public void refreshSelectedObject()
    {
        Object selected = getSelectedRowObject();
        getTableWidget().selectRowObject(-1, null);
        getTableWidget().selectRowObject(selected, null);
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        if ((dataProvider.getRefreshPolicy() == DataProvider.RefreshPolicy.ALLWAYS)
                || (dataProvider.getRefreshPolicy() == DataProvider.RefreshPolicy.ON_USER_SWITCH))
        {
            log.debug("USER changed event, refreshPolicy= " + dataProvider.getRefreshPolicy());

            if ((evt.getNewValue() == null))
            {
                setRows(Collections.EMPTY_LIST);
            }
            else if (isShowing())
            {
                executeFilter();
            }
        }
    }

    @Override
    protected final DefaultValidationResultsModel getValidationResults()
    {
        return validationResultsModel;
    }
}
