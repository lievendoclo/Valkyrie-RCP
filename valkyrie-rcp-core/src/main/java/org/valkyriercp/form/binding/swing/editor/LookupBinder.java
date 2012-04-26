package org.valkyriercp.form.binding.swing.editor;

import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.widget.editor.DefaultDataEditorWidget;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

// TODO: add configuration possibilities for context map based configuration
@Configurable
public class LookupBinder<T> implements Binder
{
    private int autoPopupDialog = LookupBinding.AUTOPOPUPDIALOG_NO_UNIQUE_MATCH;
    private boolean revertValueOnFocusLost = true;
    private String selectDialogId = LookupBinding.DEFAULT_SELECTDIALOG_ID;
    private String selectDialogCommandId = LookupBinding.DEFAULT_SELECTDIALOG_COMMAND_ID;
    private String dataEditorId;
    private String dataEditorViewCommandId;
    private Object filter;
    private boolean enableViewCommand;
    private boolean loadDetailedObject = false;
    private Function<T, String> objectLabelFunction;
    private Function<String, ? extends Object> createFilterFromFieldFunction;
    private Class<T> requiredSourceClass;
    private Dimension dialogSize;

    public Dimension getDialogSize() {
        return dialogSize;
    }

    public void setDialogSize(Dimension dialogSize) {
        this.dialogSize = dialogSize;
    }

    public Class<T> getRequiredSourceClass() {
        return requiredSourceClass;
    }

    public void setRequiredSourceClass(Class<T> requiredSourceClass) {
        this.requiredSourceClass = requiredSourceClass;
    }

    public Function<String, ? extends Object> getCreateFilterFromFieldFunction() {
        return createFilterFromFieldFunction;
    }

    public void setCreateFilterFromFieldFunction(Function<String, ? extends Object> createFilterFromFieldFunction) {
        this.createFilterFromFieldFunction = createFilterFromFieldFunction;
    }

    public Function<T, String> getObjectLabelFunction() {
        return objectLabelFunction;
    }

    public void setObjectLabelFunction(Function<T, String> objectLabelFunction) {
        this.objectLabelFunction = objectLabelFunction;
    }

    @Autowired
    private ApplicationConfig applicationConfig;


    public boolean isLoadDetailedObject()
    {
        return loadDetailedObject;
    }


    public void setLoadDetailedObject(boolean loadDetailedObject)
    {
        this.loadDetailedObject = loadDetailedObject;
    }

    public LookupBinder()
    {
        enableViewCommand = false;
    }

    public void setAutoPopupDialog(int autoPopupDialog)
    {
        this.autoPopupDialog = autoPopupDialog;
    }

    public void setRevertValueOnFocusLost(boolean revertValueOnFocusLost)
    {
        this.revertValueOnFocusLost = revertValueOnFocusLost;
    }

    public void setSelectDialogId(String selectDialogId)
    {
        this.selectDialogId = selectDialogId;
    }

    public void setSelectDialogCommandId(String selectDialogCommandId)
    {
        this.selectDialogCommandId = selectDialogCommandId;
    }

    public Binding bind(FormModel formModel, String formPropertyPath, Map context)
    {
        LookupBinding<T> referableBinding = getLookupBinding(formModel, formPropertyPath, context);
        referableBinding.setAutoPopupdialog(getAutoPopupDialog());
        referableBinding.setRevertValueOnFocusLost(isRevertValueOnFocusLost());
        referableBinding.setSelectDialogCommandId(getSelectDialogCommandId());
        referableBinding.setSelectDialogId(getSelectDialogId());
        referableBinding.setDataEditorViewCommandId(dataEditorViewCommandId);
        referableBinding.setEnableViewCommand(enableViewCommand);
        referableBinding.setFilter(filter);
        referableBinding.setLoadDetailedObject(loadDetailedObject);
        referableBinding.setCreateFilterFromFieldFunction(createFilterFromFieldFunction);
        referableBinding.setObjectLabelFunction(objectLabelFunction);
        referableBinding.setDialogSize(dialogSize);
        return referableBinding;
    }

    protected LookupBinding<T> getLookupBinding(FormModel formModel, String formPropertyPath, Map context) {
        return new LookupBinding<T>(getDataEditor(), formModel, formPropertyPath, requiredSourceClass);
    }

    public Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context)
    {
        throw new UnsupportedOperationException("This binder needs a special component that cannot be given");
    }

    protected int getAutoPopupDialog()
    {
        return autoPopupDialog;
    }

    protected DefaultDataEditorWidget getDataEditor()
    {
        Object dataEditor = applicationConfig.applicationContext().getBean(dataEditorId);
        Assert.isInstanceOf(DefaultDataEditorWidget.class, dataEditor);
        return (DefaultDataEditorWidget) dataEditor;
    }

    protected boolean isRevertValueOnFocusLost()
    {
        return revertValueOnFocusLost;
    }

    protected String getSelectDialogCommandId()
    {
        return selectDialogCommandId;
    }

    protected String getSelectDialogId()
    {
        return selectDialogId;
    }

    public void setDataEditorViewCommandId(String dataEditorViewCommandId)
    {
        this.dataEditorViewCommandId = dataEditorViewCommandId;
    }

    public void setEnableViewCommand(boolean enableViewCommand)
    {
        this.enableViewCommand = enableViewCommand;
    }

    public void setFilter(Object filter)
    {
        this.filter = filter;
    }

    public String getDataEditorViewCommandId()
    {
        return dataEditorViewCommandId;
    }

    public Object getFilter()
    {
        return filter;
    }

    public boolean isEnableViewCommand()
    {
        return enableViewCommand;
    }

    public String getDataEditorId() {
        if(dataEditorId == null) {
            return "defaultDataEditor";
        }
        return dataEditorId;
    }

    public void setDataEditorId(String dataEditorId) {
        this.dataEditorId = dataEditorId;
    }
}

