package org.valkyriercp.widget.editor;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.valkyriercp.application.support.DefaultButtonFocusListener;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.form.NewFormObjectAware;
import org.valkyriercp.binding.validation.support.DefaultValidationResultsModel;
import org.valkyriercp.command.ActionCommandInterceptor;
import org.valkyriercp.command.CommandConfigurer;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.SplitPaneExpansionToggleCommand;
import org.valkyriercp.component.Focussable;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.form.*;
import org.valkyriercp.util.MessageConstants;
import org.valkyriercp.util.ObjectUtils;
import org.valkyriercp.widget.AbstractTitledWidget;
import org.valkyriercp.widget.SelectionWidget;
import org.valkyriercp.widget.TitledWidget;
import org.valkyriercp.widget.Widget;
import org.valkyriercp.widget.editor.provider.DataProviderEventSource;
import org.valkyriercp.widget.editor.provider.DataProviderListener;
import org.valkyriercp.widget.table.TableWidget;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * AbstractDataEditorWidget implements a basic editor screen, based on 3 parts:
 * <p/>
 * <ol>
 * <li>a table with sortable columns and a local quick search</li>
 * <li>a filter to reduce the dataset</li>
 * <li>a detail section for the details of 1 entity</li>
 * </ol>
 */
public abstract class AbstractDataEditorWidget extends AbstractTitledWidget
        implements
        TitledWidget,
        SelectionWidget
{

    /**
     * Log facility.
     */
    private static final Log log = LogFactory.getLog(AbstractDataEditorWidget.class);

    private static final String QUICKADD = "quickAdd";

    protected static final String UPDATE_COMMAND_ID = "update";

    protected static final String CREATE_COMMAND_ID = "create";

    private static final String TOGGLE_DETAIL_COMMAND_ID = "opendetail";

    private static final String REMOVE_CONTINUE_AFTER_ERROR = "remove.continue_after_error";

    private static final String REMOVE_CONFIRMATION_ID = "remove.confirmation";

    private static final String DBLCLICKSELECTS = "dblclick_for_edit";

    public static final String UNSAVEDCHANGES_WARNING_ID = "unsavedchanges.warning";

    public static final String UNSAVEDCHANGES_UNCOMMITTABLE_WARNING_ID = "unsavedchanges.uncommittable.warning";

    public static final RowSpec FILL_ROW_SPEC = new RowSpec(RowSpec.FILL, Sizes.PREFERRED,
            FormSpec.DEFAULT_GROW);

    public static final ColumnSpec FILL_NOGROW_COLUMN_SPEC = new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT,
            FormSpec.NO_GROW);

    public static final ColumnSpec FILL_COLUMN_SPEC = new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT,
            FormSpec.DEFAULT_GROW);

    public static final boolean ON = true;

    public static final boolean OFF = false;

    private boolean selectMode = OFF;

    private boolean multipleSelectionInSelectMode = OFF;

    private String searchString;

    private Object selectedRowObject;

    private final CellConstraints cc = new CellConstraints();

    private SplitPaneExpansionToggleCommand toggleDetailCommand;

    private SplitPaneExpansionToggleCommand toggleFilterCommand;

    private ActionCommand editRowCommand;

    private ActionCommand addRowCommand;

    private ActionCommand cloneRowCommand;

    private ActionCommand removeRowsCommand;

    private ActionCommand executeFilterCommand;

    private ActionCommand refreshCommand;

    private ActionCommand clearFilterCommand;

    private ActionCommand emptyFilterCommand;

    private ActionCommand selectionCommand;

    private ActionCommand printCommand;

    private ActionCommand updateCommand;

    private ActionCommand createRowCommand;

    private CardLayout saveUpdateSwitcher;

    private JPanel saveUpdatePanel;

    private JTextField textFilterField;

    private Map dataProviderSources = null;

    @Autowired
    private CommandConfigurer commandConfigurer;

    /**
     * Observer listening to changes in the table selection.
     */
    protected Observer tableSelectionObserver;

    /**
     * Default constructor will initialise the necessary listeners/observers.
     */
    public AbstractDataEditorWidget()
    {
        tableSelectionObserver = createListSelectionObserver();
    }

    /**
     * Creates the observer that listens to selections in the listView. Normally forwards the selection to the
     * detailForm.
     */
    protected Observer createListSelectionObserver()
    {
        return new ListSelectionObserver();
    }

    /**
     * Set the select mode of this dataEditor.
     */
    public void setSelectMode(boolean selectMode)
    {
        this.selectMode = selectMode;
    }

    public boolean isSelectMode()
    {
        return selectMode;
    }

    /**
     * Set the local text filter field value
     *
     * @param queryString filterText.
     */
    public void setSearchString(String queryString)
    {
        this.searchString = queryString;
        if (this.textFilterField != null)
        {
            this.textFilterField.setText(this.searchString);
        }
    }

    public String getSearchString()
    {
        return searchString;
    }

    public Object getSelectedRowObject()
    {
        return selectedRowObject;
    }

    public void setSelectedRowObject(Object selectedObject)
    {
        getTableWidget().selectRowObject(selectedObject, null);
    }

    public abstract Object setSelectedSearch(Object searchCriteria);

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent createWidgetContent()
    {
        return createDataEditorWidget();
    }

    protected final JComponent createDataEditorWidget()
    {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        if (isSelectMode() == ON)
        {
            DefaultButtonFocusListener.setDefaultButton(getTableWidget().getComponent(), getSelectCommand());
            getTableWidget().getTable().getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "none");
        }

        if ((isSelectMode() == ON) && (selectedRowObject == null))
        {
            splitPane.setDividerLocation(Integer.MAX_VALUE);
            this.toggleDetailCommand = new SplitPaneExpansionToggleCommand(TOGGLE_DETAIL_COMMAND_ID,
                    splitPane, true);
        }
        else
        {
            splitPane.setDividerLocation(-1);
            this.toggleDetailCommand = new SplitPaneExpansionToggleCommand(TOGGLE_DETAIL_COMMAND_ID,
                    splitPane, false);
        }

        splitPane.setLastDividerLocation(-1);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(getTableResizeWeight());

        splitPane.setLeftComponent(getTableFilterPanel());
        splitPane.setRightComponent(getDetailPanel());

        getDetailForm().setEnabled(getTableWidget().hasSelection());

        createAddRowCommand();
        createRemoveRowCommand();
        createCloneRowCommand();

        return splitPane;
    }

    protected double getTableResizeWeight()
    {
        return 0.75;
    }

    private JComponent getControlPanel()
    {
        AbstractCommand localToggleDetailCommand = getToggleDetailCommand();
        AbstractCommand helpCommand = getHelpCommand();
        AbstractCommand[] commands = getControlCommands();

        ColumnSpec[] columnSpecs = new ColumnSpec[1 + (commands.length + 1) * 2];
        columnSpecs[0] = FILL_NOGROW_COLUMN_SPEC;// open-detail btn
        columnSpecs[1] = FormFactory.UNRELATED_GAP_COLSPEC; // gap
        columnSpecs[2] = FILL_NOGROW_COLUMN_SPEC;// help btn
        columnSpecs[3] = FILL_COLUMN_SPEC; // glue space

        for (int i = 0; i < commands.length; i++)
        {
            // print | select-and-close | cancel
            columnSpecs[4 + (i * 2)] = FILL_NOGROW_COLUMN_SPEC;
            if (i != commands.length - 1)
            {
                // gap, but not after last
                columnSpecs[5 + (i * 2)] = FormFactory.UNRELATED_GAP_COLSPEC;
            }
        }
        RowSpec[] rowSpecs = new RowSpec[]{FILL_ROW_SPEC};
        FormLayout formLayout = new FormLayout(columnSpecs, rowSpecs);
        JPanel buttonPanel = new JPanel(formLayout);

        buttonPanel.add(localToggleDetailCommand.createButton(), cc.xy(1, 1));

        buttonPanel.add(helpCommand.createButton(), cc.xy(3, 1));

        for (int i = 0; i < commands.length; i++)
        {
            buttonPanel.add(commands[i].createButton(), cc.xy(5 + i * 2, 1));
        }

        return buttonPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public java.util.List<AbstractCommand> getCommands()
    {
        return Arrays.asList(getToggleDetailCommand());
    }

    protected JComponent getDetailPanel()
    {
        ColumnSpec[] columnSpecs = new ColumnSpec[]{FILL_COLUMN_SPEC};
        RowSpec[] rowSpecs = new RowSpec[]{FormFactory.LINE_GAP_ROWSPEC, // gap
                FormFactory.DEFAULT_ROWSPEC, // buttons for detailpanel
                FormFactory.LINE_GAP_ROWSPEC, // gap
                FILL_ROW_SPEC
                // detailpanel itself (form)
        };
        JPanel detailPanel = new JPanel(new FormLayout(columnSpecs, rowSpecs));

        Form detailForm = getDetailForm();
        newSingleLineResultsReporter(this);
        detailPanel.add(getDetailControlPanel(), cc.xy(1, 2));
        detailPanel.add(detailForm.getControl(), cc.xy(1, 4));

        // force form readonly if adding & updating is not supported
        if (!isAddRowSupported() && !isUpdateRowSupported())
        {
            detailForm.getFormModel().setReadOnly(true);
        }

        return detailPanel;
    }

    private JComponent getDetailControlPanel()
    {
        ColumnSpec[] columnSpecs = new ColumnSpec[]{FILL_NOGROW_COLUMN_SPEC, // edit buttons
                FILL_COLUMN_SPEC, // glue
                FILL_NOGROW_COLUMN_SPEC, // navigation buttons
                FILL_COLUMN_SPEC, // glue
                new ColumnSpec(ColumnSpec.RIGHT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW) // list summary
        };

        RowSpec[] rowSpecs = new RowSpec[]{FILL_ROW_SPEC};
        FormLayout formLayout = new FormLayout(columnSpecs, rowSpecs);
        // coupled glue space around nav buttons!
        formLayout.setColumnGroups(new int[][]{{2, 4}});
        JPanel buttonPanel = new JPanel(formLayout);

        JComponent editButtons = getEditButtons();
        JComponent tableButtonBar = getTableWidget().getButtonBar();
        if (editButtons != null)
        {
            buttonPanel.add(editButtons, cc.xy(1, 1));
        }
        if (tableButtonBar != null)
        {
            buttonPanel.add(tableButtonBar, cc.xy(3, 1));
        }

        buttonPanel.add(getTableWidget().getListSummaryLabel(), cc.xy(5, 1));

        return buttonPanel;
    }

    protected JComponent getEditButtons()
    {
        if (!isAddRowSupported() && !isUpdateRowSupported())
        {
            return null;
        }

        ColumnSpec[] columnSpecs = new ColumnSpec[]{FILL_NOGROW_COLUMN_SPEC, // save
                FormFactory.RELATED_GAP_COLSPEC, // gap
                FILL_NOGROW_COLUMN_SPEC, // undo
                FormFactory.RELATED_GAP_COLSPEC, // gap
                FormFactory.DEFAULT_COLSPEC, // separator
                FormFactory.RELATED_GAP_COLSPEC, // gap
                FILL_NOGROW_COLUMN_SPEC, // quickadd
        };

        RowSpec[] rowSpecs = new RowSpec[]{FILL_ROW_SPEC};
        FormLayout formLayout = new FormLayout(columnSpecs, rowSpecs);
        JPanel buttonPanel = new JPanel(formLayout);
        buttonPanel.add(getCommitComponent(), cc.xy(1, 1));
        buttonPanel.add(getRevertCommand().createButton(), cc.xy(3, 1));
        if (isAddRowSupported())
        {
            buttonPanel.add(new JSeparator(SwingConstants.VERTICAL), cc.xy(5, 1));
            buttonPanel.add(createQuickAddCheckBox(), cc.xy(7, 1));
        }
        return buttonPanel;
    }

    protected JComponent getCommitComponent()
    {
        if (isAddRowSupported() && isUpdateRowSupported())
        {
            saveUpdateSwitcher = new CardLayout();
            saveUpdatePanel = new JPanel(saveUpdateSwitcher);
            saveUpdatePanel.add(getCreateCommand().createButton(), CREATE_COMMAND_ID);
            saveUpdatePanel.add(getUpdateCommand().createButton(), UPDATE_COMMAND_ID);
            return saveUpdatePanel;
        }
        if (isAddRowSupported())
        {
            DefaultButtonFocusListener.setDefaultButton(getDetailForm().getControl(), getCreateCommand());
            return getCreateCommand().createButton();
        }

        DefaultButtonFocusListener.setDefaultButton(getDetailForm().getControl(), getUpdateCommand());
        return getUpdateCommand().createButton();
    }

    /**
     * Convenience method to retrieve the action command that should be used when changes are made in the detailForm.
     * This can be update or add, according to whether a row is selected and changed or no row is selected.
     *
     * @return the command that should be used to save changes in the form.
     */
    protected ActionCommand getCommitCommand()
    {
        if ((selectedRowObject == null) && (isAddRowSupported()))
            return getCreateCommand();

        else if (isUpdateRowSupported())
            return getUpdateCommand();

        return null;
    }

    /**
     * Returns the save command, lazily creates one if needed.
     */
    public ActionCommand getUpdateCommand()
    {
        if (updateCommand == null)
        {
            updateCommand = createUpdateCommand();
        }
        return updateCommand;
    }

    /**
     * Creates the save command.
     *
     * @see #doUpdate()
     */
    protected ActionCommand createUpdateCommand()
    {
        ActionCommand command = new ActionCommand(UPDATE_COMMAND_ID)
        {

            @Override
            protected void doExecuteCommand()
            {
                doUpdate();
            }
        };
        command.setSecurityControllerId(getId() + "." + UPDATE_COMMAND_ID);
        getApplicationConfig().commandConfigurer().configure(command);
        getDetailForm().addGuarded(command, FormGuard.LIKE_COMMITCOMMAND);
        return command;
    }

    /**
     * Save the changes made in the detailForm according to following steps:
     * <p/>
     * <ol>
     * <li>commit form</li>
     * <li>formObject sent to back-end</li>
     * <li>changes are handled in back-end</li>
     * <li>changed object is returned to client</li>
     * <li>old object is replaced by changed object</li>
     * </ol>
     */
    protected void doUpdate()
    {
        getDetailForm().commit();
        Object savedObject = null;
        try
        {
            savedObject = saveEntity(getDetailForm().getFormObject());
            setDetailFormObject(savedObject, tableSelectionObserver, false);
        }
        catch (RuntimeException e)
        {
            Object changedObject = getDetailForm().getFormObject();
            // the following actually requests the object from the back-end
            boolean success = setDetailFormObject(changedObject, tableSelectionObserver, true);
            // set the changes back on the model if the object could be set on the form model
            if (success)
                ObjectUtils.mapObjectOnFormModel(getDetailForm().getFormModel(), changedObject);
            throw e;
        }
    }

    /**
     * Returns the create command, lazily creates one if needed.
     */
    public ActionCommand getCreateCommand()
    {
        if (createRowCommand == null)
        {
            createRowCommand = createCreateCommand();
        }
        return createRowCommand;
    }

    /**
     * Creates the create command.
     *
     * @see #doCreate()
     */
    protected ActionCommand createCreateCommand()
    {
        ActionCommand command = new ActionCommand(CREATE_COMMAND_ID)
        {

            @Override
            protected void doExecuteCommand()
            {
                doCreate();
            }
        };
        command.setSecurityControllerId(getId() + "." + CREATE_COMMAND_ID);
        getApplicationConfig().commandConfigurer().configure(command);
        getDetailForm().addGuarded(command, FormGuard.LIKE_COMMITCOMMAND);
        return command;
    }

    /**
     * Creates a new data object according to following steps:
     * <p/>
     * <ol>
     * <li>form commit</li>
     * <li>formObject sent to back-end</li>
     * <li>back-end creates item</li>
     * <li>back-end returns new item to client</li>
     * <li>new item is selected in dataEditor if possible</li>
     * </ol>
     */
    protected void doCreate()
    {
        getDetailForm().commit();
        Object newObject = null;
        try
        {
            newObject = createNewEntity(getDetailForm().getFormObject());
            // select row only if user hasn't made another selection in
            // table and this commit is triggered by the save-changes
            // dialog and if not in quick add mode
            if (newObject != null && !getTableWidget().hasSelection())
            {
                if (getTableWidget().selectRowObject(newObject, null) == -1)
                {
                    // select row wasn't succesfull, maybe search string
                    // was filled in?
                    setSearchString(null);
                    getTableWidget().selectRowObject(newObject, null);
                }
            }
        }
        catch (RuntimeException e)
        {
            Object changedFormObject = getDetailForm().getFormObject();
            newRow(null);
            ObjectUtils.mapObjectOnFormModel(getDetailForm().getFormModel(), changedFormObject);
            throw e;
            // make form dirty, show error in messagepane and throw exception to display error dialog
        }
    }

    protected AbstractCommand getRevertCommand()
    {
        return getDetailForm().getRevertCommand();
    }

    abstract protected DefaultValidationResultsModel getValidationResults();

    public ValidationResultsReporter newSingleLineResultsReporter(Messagable messagable)
    {
        return new SimpleValidationResultsReporter(getValidationResults(), messagable);
    }

    protected JComponent createQuickAddCheckBox()
    {
        quickAddCheckBox = new JCheckBox(getApplicationConfig().messageResolver().getMessage(getId(), QUICKADD, MessageConstants.TITLE));
        quickAddCheckBox.setFocusable(false);

        getCreateCommand().addCommandInterceptor(new ActionCommandInterceptor()
        {

            public boolean preExecution(ActionCommand command)
            {
                return true; // proceed
            }

            public void postExecution(ActionCommand command)
            {
                if (quickAddCheckBox.isSelected())
                {
                    getAddRowCommand().execute();
                }
            }
        });

        quickAddCheckBox.setEnabled(getAddRowCommand().isEnabled());
        getAddRowCommand().addPropertyChangeListener(AbstractCommand.ENABLED_PROPERTY_NAME,
                new PropertyChangeListener()
                {

                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        Object newValue = evt.getNewValue();
                        quickAddCheckBox.setEnabled(((Boolean) newValue).booleanValue());
                    }
                });

        return quickAddCheckBox;
    }

    protected JComponent getTableFilterPanel()
    {
        ColumnSpec[] columnSpecs = new ColumnSpec[]{FILL_COLUMN_SPEC};
        RowSpec[] rowSpecs = new RowSpec[]{
                // buttons for list and filter
                FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
                // splitpane with list and filter
                FILL_ROW_SPEC};
        JPanel top = new JPanel(new FormLayout(columnSpecs, rowSpecs));

        final JTable table = getTableWidget().getTable();
        if (isUpdateRowSupported())
        {
            String tooltip = getApplicationConfig().messageResolver().getMessage(getId(), DBLCLICKSELECTS, MessageConstants.CAPTION);
            table.setToolTipText(tooltip);
        }

        CommandGroup tableGroup = getTablePopupMenuCommandGroup();

        table.addMouseListener(new TableMouseListener(table, tableGroup.createPopupMenu()));

        JComponent tableScroller = getTableWidget().getComponent();
        JComponent tableAndOptionalFilter = tableScroller;
        if (isFilterSupported()) // add filter too via splitpane
        {
            JSplitPane splitPane = new JSplitPane();
            if (this.selectMode == ON)
            {
                splitPane.setDividerLocation(-1);
                this.toggleFilterCommand = new SplitPaneExpansionToggleCommand("openfilter", splitPane, false);
            }
            else
            {
                splitPane.setDividerLocation(Integer.MAX_VALUE);
                this.toggleFilterCommand = new SplitPaneExpansionToggleCommand("openfilter", splitPane, true);
            }

            splitPane.setLastDividerLocation(Integer.MAX_VALUE);
            splitPane.setOneTouchExpandable(true);
            splitPane.setResizeWeight(1.0);

            this.toggleFilterCommand.setEnabled(isFilterSupported());

            splitPane.setLeftComponent(tableScroller);
            JComponent filterPanel = getFilterPanel();
            splitPane.setRightComponent(filterPanel);
            tableAndOptionalFilter = splitPane;
        }

        top.add(getTableFilterControlPanel(), cc.xy(1, 1));
        top.add(tableAndOptionalFilter, cc.xy(1, 3));

        return top;
    }

    /**
     * Returns the commandGroup that should be used to create the popup menu for the table.
     */
    protected CommandGroup getTablePopupMenuCommandGroup()
    {
        return getApplicationConfig().commandManager().createCommandGroup(Lists.newArrayList(getEditRowCommand(), "separator",
                getAddRowCommand(), getCloneRowCommand(), getRemoveRowsCommand(), "separator",
                getRefreshCommand(), "separator", getCopySelectedRowsToClipboardCommand()));
    }

    private JComponent getFilterPanel()
    {
        if (!isFilterSupported())
        {
            return null;
        }

        ColumnSpec[] columnSpecs = new ColumnSpec[]{FILL_COLUMN_SPEC};
        RowSpec[] rowSpecs = new RowSpec[]{new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW), // form gewrapped in een scrollpane.
                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, // buttons
        };
        JPanel filterPanel = new JPanel(new FormLayout(columnSpecs, rowSpecs));

        JComponent filterFormControl = getFilterForm().getControl();
        filterFormControl.setBorder(Borders.DIALOG_BORDER);

        final JScrollPane filterScroller = new JScrollPane(filterFormControl,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        filterScroller.getHorizontalScrollBar().getModel().addChangeListener(new ChangeListener()
        {

            public void stateChanged(ChangeEvent e)
            {
                if (filterScroller.getVerticalScrollBar().isVisible())
                {
                    filterScroller.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
                }
                else
                {
                    filterScroller.setBorder(null);
                }
            }
        });

        // setting minimum size: always keep width of panel + scrollbar width
        Dimension scrollDimension = new Dimension(filterScroller.getPreferredSize().width
                + filterScroller.getVerticalScrollBar().getPreferredSize().width, 50);
        filterScroller.setMinimumSize(scrollDimension);

        filterPanel.add(filterScroller, cc.xy(1, 1));
        JComponent filterControlPanel = getFilterControlPanel();
        filterControlPanel.setBorder(Borders.DIALOG_BORDER);
        filterPanel.add(filterControlPanel, cc.xy(1, 3));

        // register a default command to use when focus is on the filterPanel
        DefaultButtonFocusListener.setDefaultButton(filterPanel, getExecuteFilterCommand());

        return filterPanel;
    }

    private JComponent getFilterControlPanel()
    {
        CommandGroup controlCommands = getApplicationConfig().commandManager().createCommandGroup(Lists.newArrayList(
                getExecuteFilterCommand(), getEmptyFilterCommand()));

        return controlCommands.createButtonBar((Size) null, (Border) null);
    }

    private JCheckBox quickAddCheckBox;

    private ActionCommand copySelectedRowsCommand;

    private JComponent getTableFilterControlPanel()
    {
        CommandGroup tableFilterControlCommands = isFilterSupported()
                ? getTableFilterControlCommands()
                : null;
        ColumnSpec[] columnSpecs = getTableColumnSpecs(tableFilterControlCommands != null
                && tableFilterControlCommands.size() > 0);
        RowSpec[] rowSpecs = new RowSpec[]{FILL_ROW_SPEC};
        FormLayout formLayout = new FormLayout(columnSpecs, rowSpecs);
        JPanel buttonPanel = new JPanel(formLayout);
        int columnCounter = 1;
        if (isAddRowSupported())
        {
            buttonPanel.add(getAddRowCommand().createButton(), cc.xy(columnCounter, 1));
            columnCounter += 2;
        }
        if (isRemoveRowsSupported())
        {
            buttonPanel.add(getRemoveRowsCommand().createButton(), cc.xy(columnCounter, 1));
            columnCounter += 2;
        }
        if (isAddRowSupported() || isRemoveRowsSupported())
        {
            buttonPanel.add(new JSeparator(SwingConstants.VERTICAL), cc.xy(columnCounter, 1));
            columnCounter += 2;
        }
        buttonPanel.add(getRefreshCommand().createButton(), cc.xy(columnCounter, 1));
        columnCounter += 2;
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL), cc.xy(columnCounter, 1));
        columnCounter += 2;
        textFilterField = getTableWidget().getTextFilterField();
        if (textFilterField != null)
        {
            textFilterField.setText(searchString);
            textFilterField.setPreferredSize(new Dimension(50, 20));
            buttonPanel.add(textFilterField, cc.xy(columnCounter, 1));
            columnCounter += 2;
        }
        if (isFilterSupported() && tableFilterControlCommands.size() > 0)
        {
            buttonPanel.add(new JSeparator(SwingConstants.VERTICAL), cc.xy(columnCounter, 1));
            columnCounter += 2;
            buttonPanel.add(tableFilterControlCommands.createButtonBar(new ColumnSpec("fill:pref:nogrow"),
                    new RowSpec("fill:default:nogrow"), null), cc.xy(columnCounter, 1));
        }

        return buttonPanel;
    }

    protected CommandGroup getTableFilterControlCommands()
    {
        CommandGroup group = new CommandGroup();
        group.add(getToggleFilterCommand());
        group.add(getClearFilterCommand());
        return group;
    }

    private ColumnSpec[] getTableColumnSpecs(boolean addFilterCommands)
    {
        java.util.List<ColumnSpec> columnSpecs = new ArrayList<ColumnSpec>();
        if (isAddRowSupported())
        {
            columnSpecs.add(FILL_NOGROW_COLUMN_SPEC);// add btn
            columnSpecs.add(FormFactory.RELATED_GAP_COLSPEC); // gap
        }
        if (isRemoveRowsSupported())
        {
            columnSpecs.add(FILL_NOGROW_COLUMN_SPEC); // remove btn
            columnSpecs.add(FormFactory.RELATED_GAP_COLSPEC); // gap
        }
        if (isAddRowSupported() || isRemoveRowsSupported())
        {
            columnSpecs.add(FormFactory.DEFAULT_COLSPEC); // separator //
            // --------------------
            columnSpecs.add(FormFactory.RELATED_GAP_COLSPEC); // gap
        }
        columnSpecs.add(FILL_NOGROW_COLUMN_SPEC); // refresh btn
        columnSpecs.add(FormFactory.RELATED_GAP_COLSPEC); // gap
        columnSpecs.add(FormFactory.DEFAULT_COLSPEC); // separator
        // --------------------
        columnSpecs.add(FormFactory.RELATED_GAP_COLSPEC); // gap
        columnSpecs.add(FILL_COLUMN_SPEC); // glue space |>> glazed-list-text
        // filter
        if (addFilterCommands)
        {
            columnSpecs.add(FormFactory.RELATED_GAP_COLSPEC); // gap
            columnSpecs.add(FormFactory.DEFAULT_COLSPEC); // separator
            // --------------------
            columnSpecs.add(FormFactory.RELATED_GAP_COLSPEC); // gap
            columnSpecs.add(FILL_NOGROW_COLUMN_SPEC); // filter commands panel
        }
        return columnSpecs.toArray(new ColumnSpec[]{});
    }

    private final class TableMouseListener extends MouseAdapter
    {

        private final JTable table;
        private final JPopupMenu menu;

        private TableMouseListener(JTable table, JPopupMenu menu)
        {
            this.table = table;
            this.menu = menu;
        }

        private void handlePopupTrigger(MouseEvent e)
        {
            if (e.isPopupTrigger())
            {
                Point p = e.getPoint();
                int row = this.table.rowAtPoint(p);
                int column = this.table.columnAtPoint(p);
                // The autoscroller can generate drag events outside the
                // Table's range.
                if (!this.table.isRowSelected(row) && (column != -1))
                {
                    this.table.changeSelection(row, column, e.isControlDown(), e.isShiftDown());
                }

                this.menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            handlePopupTrigger(e);
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            handlePopupTrigger(e);
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e))
            {
                if (selectionCommand != null)
                {
                    selectionCommand.execute();
                }
                else if (isUpdateRowSupported())
                {
                    getEditRowCommand().execute();
                }
            }
        }
    }

    class RowObjectReplacer implements Runnable
    {

        private final Object oldObject;

        private final Object newObject;

        public RowObjectReplacer(final Object oldObject, final Object newObject)
        {
            this.oldObject = oldObject;
            this.newObject = newObject;
        }

        public void run()
        {
            getTableWidget().replaceRowObject(oldObject, newObject, tableSelectionObserver);
        }
    }

    protected void onRowSelection(Object rowObject)
    {
        if (rowObject instanceof Object[])
        {
            getEditRowCommand().setEnabled(false);
            getCloneRowCommand().setEnabled(false);
        }
        else
        {
            getEditRowCommand().setEnabled(true && isUpdateRowSupported());
            getCloneRowCommand().setEnabled(true && isCloneRowSupported());
        }
    }

    protected void newRow(Object newClone)
    {
        getTableWidget().unSelectAll();
        selectedRowObject = null;
        AbstractForm detailForm = getDetailForm();

        if (detailForm instanceof NewFormObjectAware)
        {
            ((NewFormObjectAware) detailForm).setNewFormObject(newClone);
        }
        else
        {
            detailForm.setFormObject(newClone);
        }

        if (saveUpdateSwitcher != null)
        {
            DefaultButtonFocusListener.setDefaultButton(detailForm.getControl(), getCreateCommand());
            saveUpdateSwitcher.show(saveUpdatePanel, CREATE_COMMAND_ID);
        }
        if (detailForm instanceof Focussable)
        {
            ((Focussable) detailForm).grabFocus();
        }
    }

    protected void removeRows()
    {
        Object[] selectedRows = getTableWidget().getSelectedRows();

        if (selectedRows.length == 0)
        {
            return;
        }

        int answer = getApplicationConfig().dialogFactory().showWarningDialog(getComponent(), REMOVE_CONFIRMATION_ID,
                new Object[]{Integer.valueOf(selectedRows.length)}, JOptionPane.YES_NO_OPTION);
        int nextSelectionIndex = getTableWidget().getTable().getSelectionModel().getMinSelectionIndex();

        for (int i = 0; i < selectedRows.length && (answer == JOptionPane.YES_OPTION); i++)
        {
            Object objectToRemove = selectedRows[i];

            try
            {
                removeEntity(objectToRemove);
            }
            catch (RuntimeException e)
            {
                log.error("Error removing row in DataEditor of type " + this.getClass().getName(), e);

                getApplicationConfig().registerableExceptionHandler().uncaughtException(Thread.currentThread(), e);

                int remaining = selectedRows.length - i - 1;
                if (remaining > 0)
                {
                    String ttl = getApplicationConfig().messageResolver()
                            .getMessage(getId(), REMOVE_CONTINUE_AFTER_ERROR, MessageConstants.TITLE);
                    String errMsg = getApplicationConfig().messageResolver().getMessage(getId(), REMOVE_CONTINUE_AFTER_ERROR,
                            MessageConstants.TEXT, new Object[]{Integer.valueOf(remaining)});
                    answer = JOptionPane.showConfirmDialog(getComponent(), errMsg, ttl,
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                }
            }
        }
        int nrOfRows = getTableWidget().nrOfRows();
        if (nrOfRows > 0 && (getTableWidget().getSelectedRows().length == 0))
        {
            if (nextSelectionIndex >= nrOfRows)
            {
                nextSelectionIndex = nrOfRows - 1;
            }
            getTableWidget().selectRowObject(nextSelectionIndex, null);
        }
    }

    public AbstractCommand getToggleDetailCommand()
    {
        return toggleDetailCommand;
    }

    public SplitPaneExpansionToggleCommand getToggleFilterCommand()
    {
        return toggleFilterCommand;
    }

    protected ActionCommand getAddRowCommand()
    {
        if (this.addRowCommand == null)
        {
            this.addRowCommand = createAddRowCommand();
        }
        return this.addRowCommand;
    }
    protected ActionCommand createAddRowCommand()
    {
        ActionCommand addRowCommand = new ActionCommand("addrow")
        {

            @Override
            protected void doExecuteCommand()
            {
                if (canClose())
                {
                    newRow(null);
                    toggleDetailCommand.doShow();
                }
            }
        };
        addRowCommand.setSecurityControllerId(getId() + ".addrow");
        commandConfigurer.configure(addRowCommand);
        addRowCommand.setEnabled(isAddRowSupported());
        return addRowCommand;
    }

    protected ActionCommand getEditRowCommand()
    {
        if (this.editRowCommand == null)
        {
            this.editRowCommand = createEditRowCommand();
        }
        return this.editRowCommand;
    }

    protected ActionCommand createEditRowCommand()
    {
        ActionCommand editRow = new ActionCommand("editrow")
        {

            @Override
            protected void doExecuteCommand()
            {
                toggleDetailCommand.doShow();
                if (getDetailForm() instanceof Focussable)
                {
                    ((Focussable) getDetailForm()).grabFocus();
                }
            }
        };
        editRow.setSecurityControllerId(getId() + ".editrow");
        commandConfigurer.configure(editRow);
        editRow.setEnabled(isUpdateRowSupported());
        return editRow;
    }

    protected ActionCommand getCloneRowCommand()
    {
        if (this.cloneRowCommand == null)
        {
            this.cloneRowCommand = createCloneRowCommand();
        }
        return this.cloneRowCommand;
    }

    protected ActionCommand createCloneRowCommand()
    {
        ActionCommand cloneRow = new ActionCommand("clonerow")
        {

            @Override
            protected void doExecuteCommand()
            {
                Object newClone = cloneEntity(selectedRowObject);
                newRow(newClone);
                toggleDetailCommand.doShow();
            }

        };
        cloneRow.setSecurityControllerId(getId() + ".clonerow");
        commandConfigurer.configure(cloneRow);
        cloneRow.setEnabled(isCloneRowSupported());
        return cloneRow;
    }

    protected ActionCommand getRemoveRowsCommand()
    {
        if (this.removeRowsCommand == null)
        {
            this.removeRowsCommand = createRemoveRowCommand();
        }
        return this.removeRowsCommand;
    }

    protected ActionCommand createRemoveRowCommand()
    {
        ActionCommand removeRowCommand = new ActionCommand("removerow")
        {

            @Override
            protected void doExecuteCommand()
            {
                removeRows();
            }
        };
        removeRowCommand.setSecurityControllerId(getId() + ".removerow");
        commandConfigurer.configure(removeRowCommand);
        removeRowCommand.setEnabled(isRemoveRowsSupported());
        return removeRowCommand;
    }

    public ActionCommand getRefreshCommand()
    {
        if (this.refreshCommand == null)
        {
            this.refreshCommand = createRefreshCommand();
        }
        return this.refreshCommand;
    }

    public ActionCommand getCopySelectedRowsToClipboardCommand()
    {
        if (this.copySelectedRowsCommand == null)
        {
            this.copySelectedRowsCommand = createCopySelectedRowsToClipboardCommand();
        }
        return this.copySelectedRowsCommand;
    }

    private ActionCommand createCopySelectedRowsToClipboardCommand()
    {
        ActionCommand command = new ActionCommand("copyToClipboard")
        {

            @Override
            protected void doExecuteCommand()
            {
                String clipboardContent = createTabDelimitedSelectedRowsContent();
                StringSelection selection = new StringSelection(clipboardContent);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            }

            private String createTabDelimitedSelectedRowsContent()
            {
                java.util.List<java.util.List<String>> formattedRowList = new ArrayList<java.util.List<String>>();
                JXTable jxTable = (JXTable) getTableWidget().getTable();
                java.util.List<String> headerList = new ArrayList<String>();
                for (TableColumn tableColumn : jxTable.getColumns())
                {
                    Object headerValue = tableColumn.getHeaderValue();
                    headerList.add(headerValue == null ? "" : headerValue.toString());
                }
                formattedRowList.add(headerList);
                for (int rowIndex : jxTable.getSelectedRows())
                {
                    java.util.List<String> columnList = new ArrayList<String>();
                    for (TableColumn tableColumn : jxTable.getColumns())
                    {
                        Object unformattedValue = jxTable.getModel().getValueAt(rowIndex,
                                tableColumn.getModelIndex());
                        int columnViewIndex = jxTable.convertColumnIndexToView(tableColumn.getModelIndex());
                        TableCellRenderer renderer = jxTable.getCellRenderer(rowIndex, columnViewIndex);
                        Component component = renderer.getTableCellRendererComponent(jxTable,
                                unformattedValue, false, false, rowIndex, columnViewIndex);

                        columnList.add(getFormattedValue(component));
                    }
                    formattedRowList.add(columnList);
                }

                StringBuilder builder = new StringBuilder(formattedRowList.size() * 200);
                for (java.util.List<String> row : formattedRowList)
                {
                    builder.append(Joiner.on("\t").join(row.iterator()) + "\n");
                }
                return builder.toString();
            }

            private String getFormattedValue(Component component)
            {
                if (component instanceof JLabel)
                {
                    return ((JLabel) component).getText();
                }
                else if (component instanceof JTextComponent)
                {
                    return ((JTextComponent) component).getText();
                }
                else if (component instanceof JToggleButton)
                {
                    JToggleButton button = (JToggleButton) component;
                    return getApplicationConfig().messageResolver().getMessage("boolean.yesno." + button.isSelected());
                }
                else
                {
                    return component.toString();
                }
            }

        };

        return command;
    }

    protected ActionCommand createRefreshCommand()
    {
        return makeExecuteFilterCommand("refresh", false);
    }

    public ActionCommand getClearFilterCommand()
    {
        if (this.clearFilterCommand == null)
        {
            this.clearFilterCommand = createClearFilterCommand();
        }
        return this.clearFilterCommand;
    }

    protected ActionCommand createClearFilterCommand()
    {
        ActionCommand newCommand = makeClearFilterCommand("clearfilter", true);
        newCommand.setEnabled(isFilterSupported());
        return newCommand;
    }

    private ActionCommand makeClearFilterCommand(String id, final boolean refreshAndHideAfterClear)
    {
        ActionCommand newCommand = new ActionCommand(id)
        {

            @Override
            protected void doExecuteCommand()
            {
                getFilterForm().resetCriteria();
                if (refreshAndHideAfterClear)
                {
                    executeFilter();
                    AbstractDataEditorWidget.this.toggleFilterCommand.doHide();
                }
            }
        };
        commandConfigurer.configure(newCommand);
        return newCommand;
    }

    public ActionCommand getExecuteFilterCommand()
    {
        if (this.executeFilterCommand == null)
        {
            this.executeFilterCommand = createExecuteFilterCommand();
        }
        return this.executeFilterCommand;
    }

    protected ActionCommand createExecuteFilterCommand()
    {
        ActionCommand newCommand = makeExecuteFilterCommand("executefilter", true);
        newCommand.setEnabled(isFilterSupported());
        return newCommand;
    }

    private ActionCommand makeExecuteFilterCommand(String id, final boolean commitFilter)
    {
        ActionCommand newCommand = new ActionCommand(id)
        {

            @Override
            protected void doExecuteCommand()
            {
                if (textFilterField != null)
                {
                    textFilterField.setText("");
                }
                if (commitFilter && isFilterSupported())
                {
                    getFilterForm().commit();
                }
                executeFilter();
            }
        };
        if (isFilterSupported())
        {
            new FormGuard(getFilterForm().getFormModel(), newCommand, FormGuard.ON_NOERRORS);
        }
        commandConfigurer.configure(newCommand);
        return newCommand;
    }

    protected ActionCommand getEmptyFilterCommand()
    {
        if (this.emptyFilterCommand == null)
        {
            this.emptyFilterCommand = createEmptyFilterCommand();
        }
        return this.emptyFilterCommand;
    }

    protected ActionCommand createEmptyFilterCommand()
    {
        ActionCommand newCommand = makeClearFilterCommand("emptyfilter", false);
        newCommand.setEnabled(isFilterSupported());
        return newCommand;
    }

    protected AbstractCommand getHelpCommand()
    {
        return getApplicationConfig().commandManager().createDummyCommand("help", "Behulpzaam");
    }

    private AbstractCommand getCloseCommand()
    {
        return getApplicationConfig().commandManager().createDummyCommand("exit", "deuren toe.");
    }

    protected Object[] getFilterCriteria()
    {
        Object[] criteria;
        criteria = new Object[1];
        criteria[0] = getFilterForm().getFilterCriteria();
        return criteria;
    }

    protected ActionCommand getSelectCommand()
    {
        return getApplicationConfig().commandManager().createDummyCommand("select", "Chosen!");
    }

    protected AbstractCommand[] getControlCommands()
    {
        if (isSelectMode())
        {
            return new AbstractCommand[]{getSelectCommand(), // select
                    getCloseCommand()
                    // close
            };
        }

        return new AbstractCommand[]{
                getCloseCommand()
                // close
        };
    }

    protected abstract boolean isFilterSupported();

    protected abstract boolean isUpdateRowSupported();

    protected abstract boolean isAddRowSupported();

    protected abstract boolean isCloneRowSupported();

    protected abstract boolean isRemoveRowsSupported();

    protected abstract FilterForm getFilterForm();

    public abstract AbstractForm getDetailForm();

    public abstract Widget createDetailWidget();

    public abstract TableWidget getTableWidget();

    protected abstract void executeFilter();

    public abstract void executeFilter(Map<String, Object> parameters);

    protected final Object loadEntityDetails(Object baseObject)
    {
        return loadEntityDetails(baseObject, false);
    }

    /**
     * Fetch the detailed object from the back-end. If the baseObject is already detailed, the baseObject can
     * be returned directly if and only if no forceLoad is requested. This logic is also apparent in the
     * {@link org.valkyriercp.widget.editor.provider.DataProvider} class.
     *
     * @param baseObject object containing enough information to fetch a detailed version.
     * @param forceLoad  if <code>true</code> always load the detailed object from the back-end, if
     *                   <code>false</code> a shortcut can be implemented by returning the baseObject directly.
     * @return the detailed object
     */
    protected abstract Object loadEntityDetails(Object baseObject, boolean forceLoad);

    protected abstract Object saveEntity(Object committedObject);

    protected abstract Object createNewEntity(Object committedObject);

    protected abstract Object cloneEntity(Object sampleObject);

    protected abstract void removeEntity(Object objectToRemove);

    @Override
    public boolean canClose()
    {
        boolean userBreak = false;
        int answer = JOptionPane.NO_OPTION;

        FormModel detailFormModel = getDetailForm().getFormModel();

        if(detailFormModel.isDirty() && !(getCreateCommand().isAuthorized() && saveUpdatePanel.getComponents()[0].isVisible()) && !(getUpdateCommand().isAuthorized() && saveUpdatePanel.getComponents()[1].isVisible())) {
            detailFormModel.revert();
        }
        
        if (detailFormModel.isEnabled() && detailFormModel.isDirty())
        {
            if (detailFormModel.isCommittable())
            {
                answer = getApplicationConfig().dialogFactory().showWarningDialog(getComponent(), UNSAVEDCHANGES_WARNING_ID,
                        JOptionPane.YES_NO_CANCEL_OPTION);
            }
            else // form is uncomittable, change it or revert it
            {
                answer = getApplicationConfig().dialogFactory().showWarningDialog(getComponent(),
                        UNSAVEDCHANGES_UNCOMMITTABLE_WARNING_ID, JOptionPane.YES_NO_OPTION);
                // the following might seem strange, but it aligns the answer with the other part of this if construction
                // if we said 'yes keep editing': don't discard changes, continue editing to save it later on == CANCEL in previous if
                // if we said 'no discard changes': discard changed and switch to other row == NO in previous if
                answer = answer == JOptionPane.YES_OPTION ? JOptionPane.CANCEL_OPTION : JOptionPane.NO_OPTION;
            }

            switch (answer)
            {
                case JOptionPane.CANCEL_OPTION:
                    userBreak = true;
                    break;
                case JOptionPane.YES_OPTION:
                    getCommitCommand().execute();
                    break;
                case JOptionPane.NO_OPTION:
                    detailFormModel.revert();
                    break;
            }
        }

        return !userBreak;
    }

    private boolean setDetailFormObject(Object rowObject, Observer reportingObserver, boolean forceLoad)
    {
        // quick check to avoid multiple runs
        if (!forceLoad && (rowObject == selectedRowObject))
        {
            return true;
        }

        if (saveUpdateSwitcher != null)
        {
            DefaultButtonFocusListener.setDefaultButton(getDetailForm().getControl(), getUpdateCommand());
            saveUpdateSwitcher.show(saveUpdatePanel, UPDATE_COMMAND_ID);
        }

        // check on current detailObject, if user isn't ready to switch, select
        // previous object again
        if (!canClose())
        {
            getTableWidget().selectRowObject(selectedRowObject, reportingObserver);
            return false;
        }

        boolean success = true;

        // nothing is stopping us from setting the newly selected object
        if (rowObject != null)
        {
            Object detailedObject = loadEntityDetails(rowObject, forceLoad);
            // if null, remove from list, set rowObject null and fall through (exception will be displayed)
            if (detailedObject == null)
            {
                getTableWidget().removeRowObject(rowObject);
                rowObject = null;
                success = false;
            }
            else if (detailedObject != rowObject)
            {
                replaceRowObject(rowObject, detailedObject);
                rowObject = detailedObject;
            }
        }
        getDetailForm().setFormObject(rowObject);
        selectedRowObject = rowObject;
        onRowSelection(rowObject);
        return success;
    }

    protected void replaceRowObject(Object oldRowObject, Object newRowObject)
    {
        EventQueue.invokeLater(new RowObjectReplacer(oldRowObject, newRowObject));
    }

    public final void setDataProviderEventSources(java.util.List dataProviderEventSources)
    {
        if (this.dataProviderSources == null)
        {
            this.dataProviderSources = new HashMap();
        }
        for (Iterator sourceIter = dataProviderEventSources.iterator(); sourceIter.hasNext();)
        {
            DataProviderEventSource source = (DataProviderEventSource) sourceIter.next();
            this.dataProviderSources.put(source.getClass(), source);
        }
    }

    public final void addDataProviderListener(Class dataProviderEventSource, DataProviderListener listener)
    {
        Object source = this.dataProviderSources.get(dataProviderEventSource);
        if (source != null)
        {
            ((DataProviderEventSource) source).addDataProviderListener(listener);
        }
    }

    public final void removeDataProviderListener(Class dataProviderEventSource, DataProviderListener listener)
    {
        Object source = this.dataProviderSources.get(dataProviderEventSource);
        if (source != null)
        {
            ((DataProviderEventSource) source).removeDataProviderListener(listener);
        }
    }

    /**
     * @inheritDoc
     */
    public Object getSelection()
    {
        return getTableWidget().getSelectedRows();
    }

    /**
     * @inheritDoc
     */
    public void setSelectionCommand(ActionCommand command)
    {
        setSelectMode(AbstractDataEditorWidget.ON);
        selectionCommand = command;
        enableSelectButton(getSelection());
    }

    public void setMultipleSelectionInSelectMode(boolean multipleSelection)
    {
        multipleSelectionInSelectMode = multipleSelection;
    }

    /**
     * @inheritDoc
     */
    public void removeSelectionCommand()
    {
        selectionCommand = null;
        setSelectMode(AbstractDataEditorWidget.OFF);
    }

    private void enableSelectButton(Object selection)
    {
        if (selectionCommand != null)
        {
            if (selection == null)
            {
                selectionCommand.setEnabled(false);
            }
            else if (multipleSelectionInSelectMode == ON)
            {
                selectionCommand.setEnabled(true);
            }
            else
            {
                selectionCommand.setEnabled(!(selection instanceof Object[])
                        || (((Object[]) selection).length == 1));
            }
        }
    }

    private class ListSelectionObserver implements Observer
    {

        public void update(Observable o, Object rowObject)
        {
            AbstractDataEditorWidget.this.setDetailFormObject(rowObject instanceof Object[]
                    ? null
                    : rowObject, tableSelectionObserver, false);
            enableSelectButton(rowObject);
        }
    }
}