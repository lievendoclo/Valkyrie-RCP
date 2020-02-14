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
package org.valkyriercp.form.binding.swing.editor;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXPanel;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.dialog.ApplicationDialog;
import org.valkyriercp.form.HasValidationComponent;
import org.valkyriercp.form.binding.support.CustomBinding;
import org.valkyriercp.text.SelectAllFocusListener;
import org.valkyriercp.util.HasInnerComponent;
import org.valkyriercp.widget.TitledWidgetApplicationDialog;
import org.valkyriercp.widget.editor.AbstractDataEditorWidget;
import org.valkyriercp.widget.editor.DataEditorWidgetViewCommand;
import org.valkyriercp.widget.editor.DefaultDataEditorWidget;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

public class LookupBinding<T> extends CustomBinding {

    public static final String ON_ABOUT_TO_CHANGE = "on-about-to-change";

    /**
     * Parameter used to pass to the dataEditorCommand in order to skip initialization of the dataEditor.
     */
    public static final String NO_INITIALIZE_DATA_EDITOR = "no-initialize-dataeditor";

    /** The following are masks that trigger the dataEditor pop-up. */

    /**
     * Pop-up dataEditor when unique match is found.
     */
    public static final int AUTOPOPUPDIALOG_UNIQUE_MATCH = 1;

    /**
     * Pop-up dataEditor when no match is found.
     */
    public static final int AUTOPOPUPDIALOG_NO_MATCH = 1 << 1;

    /**
     * Pop-up dataEditor when multiple matches are found.
     */
    public static final int AUTOPOPUPDIALOG_MULTIPLE_MATCH = 1 << 2;

    /**
     * Always pop-up.
     */
    public static final int AUTOPOPUPDIALOG_ALWAYS = AUTOPOPUPDIALOG_UNIQUE_MATCH + AUTOPOPUPDIALOG_NO_MATCH
            + AUTOPOPUPDIALOG_MULTIPLE_MATCH;

    /**
     * Pop-up if no unique match is found. This is considered to be the sensible default.
     */
    public static final int AUTOPOPUPDIALOG_NO_UNIQUE_MATCH = AUTOPOPUPDIALOG_NO_MATCH
            + AUTOPOPUPDIALOG_MULTIPLE_MATCH;

    /**
     * Internal mask used to determine the pop-up behavior.
     */
    private int autoPopupDialog = AUTOPOPUPDIALOG_NO_UNIQUE_MATCH;

    /**
     * Standard on option to use with parameters.
     */
    public static final Boolean ON = Boolean.TRUE;

    /**
     * Standard off option to use with parameters.
     */
    public static final Boolean OFF = Boolean.FALSE;

    /**
     * Default id to configure the dialog.
     */
    public static final String DEFAULT_SELECTDIALOG_ID = "foreignKeySelectDialog";

    /**
     * Default id to configure the command.
     */
    public static final String DEFAULT_SELECTDIALOG_COMMAND_ID = "foreignKeyPropertyEditorCommand";

    /**
     * DataEditor that will be used to find matches.
     */
    private final DefaultDataEditorWidget dataEditor;

    /**
     * Should changes be reverted when focus is lost and no value was selected?
     */
    private boolean revertValueOnFocusLost = true;

    /**
     * Id to configure dialog.
     */
    private String selectDialogId = DEFAULT_SELECTDIALOG_ID;

    /**
     * Id to configure the dialog command.
     */
    private String selectDialogCommandId = DEFAULT_SELECTDIALOG_COMMAND_ID;

    /**
     * Map of parameters to pass to the command, configuring its behavior.
     */
    private final Map<Object, Object> parameters;

    /**
     * The button to access the dataEditor dialog.
     */
    private AbstractButton dataEditorButton;

    /**
     * The command to access the dataEditor dialog.
     */
    private ActionCommand dataEditorCommand;

    /**
     * The textComponent for this referable.
     */
    private JComponent keyField;
    private PropertyChangeMonitor propertyChangeMonitor = new PropertyChangeMonitor();

    /**
     * Id to configure the view command.
     */
    private String dataEditorViewCommandId;

    /**
     * Enable the command that is used to switch to the dataEditor view of this referable.
     */
    private boolean enableViewCommand = false;

    /**
     * The command that allows to switch to the dataEditor view of this referable.
     */
    private ReferableDataEditorViewCommand referableDataEditorViewCommand;

    private boolean loadDetailedObject = false;

    private Object filter;
    
    private Function<T, String> objectLabelFunction;
    
    private Function<String, ? extends Object> createFilterFromFieldFunction;

    private Dimension dialogSize;

    private LookupBindingComponent editor;
    
    public LookupBinding(DefaultDataEditorWidget dataEditor, FormModel formModel, String formPropertyPath, Class<?> requiredClass) {
        super(formModel, formPropertyPath, requiredClass);
        this.dataEditor = dataEditor;
        // a parameter hashMap with a key to not initialize the dataEditor anymore
        // this will prevent initialize from being called twice: when losing focus (searching for
        // matches) and when auto-pop-up is on.
        this.parameters = new HashMap<Object, Object>();
        this.parameters.put(NO_INITIALIZE_DATA_EDITOR, ON);
        referableDataEditorViewCommand = new ReferableDataEditorViewCommand();
        formModel.getValueModel(formPropertyPath).addValueChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                referableDataEditorViewCommand.setEnabled(evt.getNewValue() != null);
                if (evt.getNewValue() != null)
                    referableDataEditorViewCommand.setSelectedObject(evt.getNewValue());
            }
        });
        getApplicationConfig().commandConfigurer().configure(referableDataEditorViewCommand);
    }

    public Function<T, String> getObjectLabelFunction() {
        return objectLabelFunction;
    }

    public void setObjectLabelFunction(Function<T, String> objectLabelFunction) {
        this.objectLabelFunction = objectLabelFunction;
    }

    public Function<String, ? extends Object> getCreateFilterFromFieldFunction() {
        return createFilterFromFieldFunction;
    }

    public void setCreateFilterFromFieldFunction(Function<String, ? extends Object> createFilterFromFieldFunction) {
        this.createFilterFromFieldFunction = createFilterFromFieldFunction;
    }

    /**
     * Returns the parameter map that is passed to the dataEditor command. This allows for eg turning
     * initialization of the dataEditor on/off.
     */
    protected Map<Object, Object> getParameters() {
        return parameters;
    }

    /**
     * Returns the id for the TitledApplicationDialog that shows up when pressing the button.
     */
    protected String getSelectDialogId() {
        return this.selectDialogId;
    }

    /**
     * Set the id for the TitledApplicationDialog that shows up when pressing the button.
     */
    public void setSelectDialogId(String selectDialogId) {
        this.selectDialogId = selectDialogId;
    }

    /**
     * Returns the id for the command that shows the dialog.
     */
    protected String getSelectDialogCommandId() {
        return this.selectDialogCommandId;
    }

    /**
     * Set the id for the command that shows the dialog.
     */
    public void setSelectDialogCommandId(String selectDialogCommandId) {
        this.selectDialogCommandId = selectDialogCommandId;
    }

    /**
     * Returns the mask defining the behavior of the pop-up.
     */
    protected int getAutoPopupDialog() {
        return this.autoPopupDialog;
    }

    /**
     * Set the mask defining the pop-up behavior.
     *
     * @see #AUTOPOPUPDIALOG_ALWAYS
     * @see #AUTOPOPUPDIALOG_UNIQUE_MATCH
     * @see #AUTOPOPUPDIALOG_MULTIPLE_MATCH
     * @see #AUTOPOPUPDIALOG_NO_MATCH
     * @see #AUTOPOPUPDIALOG_NO_UNIQUE_MATCH
     */
    public void setAutoPopupdialog(int autoPopupDialog) {
        this.autoPopupDialog = autoPopupDialog;
    }

    /**
     * Return <code>true</code> if the value should be reverted when focus is lost and no value is selected.
     */
    protected boolean revertValueOnFocusLost() {
        return this.revertValueOnFocusLost;
    }

    /**
     * Set to <code>true</code> if the value should be reverted when focus is lost and no value is selected.
     */
    public void setRevertValueOnFocusLost(boolean revertValueOnFocusLost) {
        this.revertValueOnFocusLost = revertValueOnFocusLost;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Sets the textComponent to reflect the label of the object
     */
    @Override
    protected void valueModelChanged(Object newValue) {
        if (newValue == null)
            setKeyComponentText(null);
        else
            setKeyComponentText(getObjectLabel(newValue));
        readOnlyChanged();
    }

    public String getObjectLabel(Object o) {
        return getObjectLabelFunction().apply((T) o);
    }

    @Override
    protected JComponent doBindControl() {
        MigLayout layout = new MigLayout("fill, insets 0");
        editor = new LookupBindingComponent(layout, this);
        editor.setKeyComponent(getKeyComponent());
        editor.setDataEditorButton(getDataEditorButton());
        editor.add(editor.getKeyComponent(), "push,grow");
        editor.add(editor.getDataEditorButton(), "w 40px!");
        if (isEnableViewCommand()) {
            AbstractButton viewButton = referableDataEditorViewCommand.createButton();
            viewButton.setFocusable(false);
            editor.setViewButton(viewButton);
            editor.add(editor.getViewButton(), "w 40px!");
        }
        editor.setFocusable(true);
        valueModelChanged(getValue());
        return editor;
    }

    /**
     * Returns a JTextComponent to display the key, creates it if necessary.
     *
     * @deprecated use {@link #getKeyComponent()} instead or to access the text directly, use
     *             {@link #getKeyComponentText()} and {@link #setKeyComponentText(String)}.
     */
    @Deprecated
    protected JTextComponent getOrCreateKeyTextComponent() {
        return (JTextComponent) getKeyComponent();
    }

    protected JComponent getKeyComponent() {
        if (keyField == null)
            keyField = createKeyComponent();

        return keyField;
    }

    protected JComponent createKeyComponent() {
        JTextField textField = new JTextField();
        // Focustraversal keys moeten afgezet worden, anders wordt de keylistener niet getriggered.
        textField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                new HashSet<AWTKeyStroke>());
        textField.addKeyListener(createKeyListener());
        textField.addFocusListener(new SelectAllFocusListener(textField));
        textField.addFocusListener(createFocusListener());
        return textField;
    }

    /**
     * Return the text that is shown on the keyComponent and that should be used to lookup the referable. Most
     * commonly this string contains the label of the referable.
     */
    protected String getKeyComponentText() {
        if (getKeyComponent() instanceof JTextComponent)
            return ((JTextComponent) getKeyComponent()).getText();

        return "";
    }

    /**
     * Set the text of the referable label on the key component (normally the textField).
     */
    protected void setKeyComponentText(String text) {
        if (getKeyComponent() instanceof JTextComponent)
            ((JTextComponent) getKeyComponent()).setText(text);
    }

    /**
     * Create a keyListener that reacts on tabs. Pop-up the dialog as defined in the
     * {@link #getAutoPopupDialog()} mask.
     */
    protected TabKeyListener createKeyListener() {
        return new TabKeyListener() {

            @Override
            public void onTabKey(Component component) {
                String textFieldValue = getKeyComponentText();
                boolean empty = "".equals(textFieldValue.trim());
                Object ref = LookupBinding.this.getValue();
                // if something was filled in and it doesn't match the internal value
                if (!empty && ((ref == null) || !textFieldValue.equals(getObjectLabel(ref)))) {
                    // call the dataEditor to fire the search
                    Object result = initializeDataEditor();

                    //no match
                    if (result == null || ((result instanceof java.util.List) && (((java.util.List<?>) result).size() == 0))) {
                        if (!revertValueOnFocusLost())
                            getValueModel().setValue(createFilterFromString(textFieldValue));
                        if ((getAutoPopupDialog() & AUTOPOPUPDIALOG_NO_MATCH) == AUTOPOPUPDIALOG_NO_MATCH)
                            getDataEditorCommand().execute(parameters);
                    }
                    // multiple matches
                    else if ((result instanceof java.util.List) && (((java.util.List<?>) result).size() > 1)) {
                        if (!revertValueOnFocusLost())
                            getValueModel().setValue(createFilterFromString(textFieldValue));
                        if ((getAutoPopupDialog() & AUTOPOPUPDIALOG_MULTIPLE_MATCH) == AUTOPOPUPDIALOG_MULTIPLE_MATCH)
                            getDataEditorCommand().execute(parameters);
                    }
                    // exact match
                    else {
                        // in dit geval krijg je een object uit de lijst terug, dit is niet gedetaileerd,
                        // daarom moet het eventueel gedetaileerd geladen worden.
                        setValue(result, true);
                        if ((getAutoPopupDialog() & AUTOPOPUPDIALOG_UNIQUE_MATCH) == AUTOPOPUPDIALOG_UNIQUE_MATCH)
                            getDataEditorCommand().execute(parameters);
                    }


                }
                // nothing filled in, underlying value isn't empty and we should not revert, set null
                else if (!revertValueOnFocusLost() && empty && ref != null) {
                    getValueModel().setValue(null);
                }
                getDataEditorButton().transferFocus();
            }
        };
    }

    /**
     * Create a focus listener to attach to the textComponent and dataEditorButton that will decide what
     * happens with the changed value. Here a revert can be done if no value is selected or a new value can be
     * created as needed.
     *
     * @return a focus listener.
     */
    protected FocusListener createFocusListener() {
        return new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                String textFieldValue = getKeyComponentText();
                boolean empty = "".equals(textFieldValue.trim());
                Object ref = LookupBinding.this.getValue();

                if (evaluateFocusLost(e)) {
                    // Revert if value isn't empty
                    if (revertValueOnFocusLost()) {
                        if (empty)
                            getValueModel().setValue(null);
                        else
                            valueModelChanged(LookupBinding.super.getValue());
                    }
                    // Create new referable if value isn't empty
                    else {
                        if (empty && (ref != null))
                            getValueModel().setValue(null);
                        else if (!empty && ((ref == null) || !textFieldValue.equals(getObjectLabel(ref))))
                            getValueModel().setValue(createFilterFromString(textFieldValue));

                    }
                }
            }
        };
    }

    protected boolean evaluateFocusLost(FocusEvent e) {
        Component oppositeComponent = e.getOppositeComponent();
        return (oppositeComponent != getDataEditorButton()) && (oppositeComponent != getKeyComponent());
    }

    @Override
    protected void readOnlyChanged() {
        if (getKeyComponent() instanceof JTextComponent)
            ((JTextComponent) getKeyComponent()).setEditable(isEnabled() && !isReadOnly());

        getDataEditorCommand().setEnabled(isEnabled() && !isReadOnly());
    }

    @Override
    protected void enabledChanged() {
        getKeyComponent().setEnabled(isEnabled());
        readOnlyChanged();
    }

    /**
     * Initialize the dataEditor by passing the search referable as search parameter.
     *
     * @return a single object if the search has an unique match, a list if multiple matches occurred or
     *         <code>null</code> if nothing was found.
     */
    protected Object initializeDataEditor() {
        final String textFieldValue = getKeyComponentText();
        Object ref = super.getValue();
        if ((ref != null) && textFieldValue.equals(getObjectLabel(ref)))
            return getDataEditor().setSelectedSearch(ref);

        return getDataEditor().setSelectedSearch(createFilterFromString(textFieldValue));
    }

    /**
     * Create an empty referable that is used to pass onto the dataEditor search method and that is used to
     * set onto the valueModel if this binding is set to not revert upon yielding a <code>null</code> search
     * result.
     *
     * @param textFieldValue the value of the textComponent.
     * @return a Referable that represents the state of this binding when no real object is available.
     */
    protected Object createFilterFromString(final String textFieldValue) {
        return createFilterFromFieldFunction.apply(textFieldValue);
    }

    /**
     * Get/create the button to open the dataEditor in selection mode
     */
    protected AbstractButton getDataEditorButton() {
        if (dataEditorButton == null) {
            dataEditorButton = getDataEditorCommand().createButton();
            dataEditorButton.setFocusable(false);
            //dataEditorButton.addFocusListener(createFocusListener());
        }

        return dataEditorButton;
    }

    /**
     * Return the dataEditorCommand.
     */
    protected final ActionCommand getDataEditorCommand() {
        if (dataEditorCommand == null)
            dataEditorCommand = createDataEditorCommand();
        return dataEditorCommand;
    }


    /**
     * Create the dataEditorCommand.
     */
    protected ActionCommand createDataEditorCommand() {
        ActionCommand selectDialogCommand = new ActionCommand(getSelectDialogCommandId()) {

            private ApplicationDialog dataEditorDialog;

            @Override
            protected void doExecuteCommand() {
                if (LookupBinding.this.propertyChangeMonitor.proceedOnChange()) {
                    if (dataEditorDialog == null) {
                        dataEditorDialog = new TitledWidgetApplicationDialog(getDataEditor(),
                                TitledWidgetApplicationDialog.SELECT_CANCEL_MODE) {

                            protected boolean onFinish() {
                                if (getDataEditor().canClose())
                                    return LookupBinding.this.onFinish();
                                return false;
                            }

                            @Override
                            protected boolean onSelectNone() {
                                getDataEditor().getTableWidget().unSelectAll();
                                return super.onSelectNone();
                            }

                            @Override
                            protected void onCancel() {
                                if (getDataEditor().canClose())
                                    super.onCancel();
                            }
                        };
                        dataEditorDialog.setParentComponent(getDataEditorButton());
                        getDataEditor().setSelectMode(AbstractDataEditorWidget.ON);
                        getApplicationConfig().applicationObjectConfigurer().configure(dataEditorDialog, getSelectDialogId());
                    }
                    if (getParameter(NO_INITIALIZE_DATA_EDITOR) != ON)
                        initializeDataEditor();
                    if (getDialogSize() != null) {
                        dataEditorDialog.getDialog().setMinimumSize(getDialogSize());
                    }
                    dataEditorDialog.showDialog();
                }
            }
        };
        getApplicationConfig().commandConfigurer().configure(selectDialogCommand);
        return selectDialogCommand;
    }

    /**
     * Return the dataEditor used to select a referable.
     */
    protected DefaultDataEditorWidget getDataEditor() {
        return this.dataEditor;
    }

    /**
     * When a value is selected, set it on the valueModel.
     *
     * @return <code>true</code> if successful.
     */
    protected boolean onFinish() {
        setValue(getDataEditor().getSelectedRowObject(), false);
        return true;
    }

    private void setValue(Object value, boolean doLoadDetailedObject) {
        if (value != null && !loadDetailedObject) {
            value = getDataEditor().getDataProvider().getSimpleObject(value);
        } else if (value != null && doLoadDetailedObject) {
            value = getDataEditor().getDataProvider().getDetailObject(value, false);
        }

        getValueModel().setValue(value);
    }

    private static class PropertyChangeMonitor extends JComponent {

        private static final long serialVersionUID = -5117792596024956433L;

        public boolean proceedOnChange() {
            boolean proceedNotVetoed = true;
            try {
                fireVetoableChange(ON_ABOUT_TO_CHANGE, false, true);
            } catch (PropertyVetoException e) {
                proceedNotVetoed = false;
            }
            return proceedNotVetoed;
        }
    }

    private class ReferableDataEditorViewCommand extends ActionCommand {

        private Object selectedObject;

        public ReferableDataEditorViewCommand() {
            super("referableDataEditorViewCommand");

        }

        public void setSelectedObject(Object selectedObject) {
            this.selectedObject = selectedObject;
        }

        @Override
        protected void doExecuteCommand() {
            Assert.notNull(dataEditorViewCommandId);
            DataEditorWidgetViewCommand command = (DataEditorWidgetViewCommand) getApplicationConfig().commandManager().getCommand(dataEditorViewCommandId);
            executeViewDataEditorCommand(command, filter, selectedObject);
        }

        public void executeViewDataEditorCommand(DataEditorWidgetViewCommand command, Object filter,
                                                        Object defaultSelectedObject) {
            org.springframework.util.Assert.notNull(command, "Command mag niet null zijn!");
            Map<String, Object> dataEditorParameters = new HashMap<String, Object>(2);
            dataEditorParameters.put(DefaultDataEditorWidget.PARAMETER_FILTER, filter);
            dataEditorParameters.put(DefaultDataEditorWidget.PARAMETER_DEFAULT_SELECTED_OBJECT,
                    defaultSelectedObject);
            Map<String, Object> commandParameters = new HashMap<String, Object>(1);
            commandParameters.put(DefaultDataEditorWidget.PARAMETER_MAP, dataEditorParameters);
            command.execute(commandParameters);
        }
    }

    /**
     * Set the id used to configure the viewCommand.
     */
    public void setDataEditorViewCommandId(String dataEditorViewCommandId) {
        this.dataEditorViewCommandId = dataEditorViewCommandId;
    }

    /**
     * Returns the id used to configure the viewCommand.
     */
    public String getDataEditorViewCommandId() {
        return dataEditorViewCommandId;
    }

    /**
     * Enable the viewCommand that switches the view to the dataEditor of this referable.
     */
    public void setEnableViewCommand(boolean enableViewCommand) {
        this.enableViewCommand = enableViewCommand;
    }

    /**
     * Returns <code>true</code> if the viewCommand should be shown. Default value is <code>false</code>.
     */
    public boolean isEnableViewCommand() {
        return enableViewCommand;
    }


    public boolean isLoadDetailedObject() {
        return loadDetailedObject;
    }

    public void setLoadDetailedObject(boolean loadDetailedObject) {
        this.loadDetailedObject = loadDetailedObject;
    }

    public void setFilter(Object filter) {
        this.filter = filter;
    }

    public Object getFilter() {
        return filter;
    }

    /**
     * Helper class to build a {@link java.awt.event.KeyListener} that reacts on tabs. Implement the
     * {@link #onShiftTabKey(Component)} and/or {@link #onTabKey(Component)} methods as needed.
     *
     * @author Jan Hoskens
     */
    protected static class TabKeyListener extends KeyAdapter {

        /**
         * Action to do when tab is pressed. Default behavior is to transfer focus.
         *
         * @param component the component which should handle the tab.
         */
        public void onTabKey(Component component) {
            component.transferFocus();
        }

        /**
         * Action to do when shift-tab is pressed. Default behavior is to transfer focus.
         *
         * @param component the component which should handle the shift-tab.
         */
        public void onShiftTabKey(Component component) {
            component.transferFocusBackward();
        }

        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == '\t' && !e.isShiftDown()) {
                onTabKey(e.getComponent());
            } else if (e.getKeyChar() == '\t' && e.isShiftDown()) {
                onShiftTabKey(e.getComponent());
            }
        }
    }

    public Dimension getDialogSize() {
        return dialogSize;
    }

    public void setDialogSize(Dimension dialogSize) {
        this.dialogSize = dialogSize;
    }
    
    public static class LookupBindingComponent extends JXPanel implements HasValidationComponent, HasInnerComponent {
        private JComponent keyComponent;
        private AbstractButton dataEditorButton;
        private AbstractButton viewButton;
        private LookupBinding parent;

        public LookupBindingComponent(LayoutManager layout, LookupBinding parent) {
            super(layout);
            this.parent = parent;
        }


        public AbstractButton getDataEditorButton() {
            return dataEditorButton;
        }

        public void setDataEditorButton(AbstractButton dataEditorButton) {
            this.dataEditorButton = dataEditorButton;
        }

        public JComponent getKeyComponent() {
            return keyComponent;
        }

        public void setKeyComponent(JComponent keyComponent) {
            this.keyComponent = keyComponent;
        }

        public AbstractButton getViewButton() {
            return viewButton;
        }

        public void setViewButton(AbstractButton viewButton) {
            this.viewButton = viewButton;
        }


        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            getKeyComponent().setEnabled(enabled);
            getDataEditorButton().setEnabled(enabled);
        }

        @Override
        public JComponent getValidationComponent() {
            return getKeyComponent();
        }

        @Override
        public synchronized void addVetoableChangeListener(VetoableChangeListener listener) {
            parent.propertyChangeMonitor.addVetoableChangeListener(listener);
        }

        @Override
        public boolean requestFocusInWindow() {
            return getKeyComponent().requestFocusInWindow();
        }

        @Override
        public JComponent getInnerComponent() {
            return getKeyComponent();
        }
    }
}

