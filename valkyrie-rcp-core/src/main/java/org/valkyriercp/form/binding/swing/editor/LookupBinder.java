package org.valkyriercp.form.binding.swing.editor;

import com.google.common.base.Function;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.util.ValkyrieRepository;
import org.valkyriercp.widget.editor.DefaultDataEditorWidget;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class LookupBinder<T> implements Binder
{
    public static final String REQUIRED_SOURCE_CLASS_KEY = "requiredSourceClass";
    public static final String AUTO_POPUP_DIALOG_KEY = "autoPopupDialog";
    public static final String REVERT_VALUE_ON_FOCUS_LOST_KEY = "revertValueOnFocusLost";
    public static final String SELECT_DIALOG_COMMAND_ID_KEY = "selectDialogCommandId";
    public static final String SELECT_DIALOG_ID_KEY = "selectDialogId";
    public static final String DATA_EDITOR_VIEW_COMMAND_ID_KEY = "dataEditorViewCommandId";
    public static final String ENABLE_VIEW_COMMAND_KEY = "enableViewCommand";
    public static final String FILTER_KEY = "filter";
    public static final String LOAD_DETAILED_OBJECT_KEY = "loadDetailedObject";
    public static final String CREATE_FILTER_FROM_FIELD_FUNCTION_KEY = "createFilterFromFieldFunction";
    public static final String OBJECT_LABEL_FUNCTION_KEY = "objectLabelFunction";
    public static final String DIALOG_SIZE_KEY = "dialogSize";
    public static final String DATA_EDITOR_ID_KEY = "dataEditorId";

    private int autoPopupDialog = LookupBinding.AUTOPOPUPDIALOG_NO_UNIQUE_MATCH;
    private boolean revertValueOnFocusLost = true;
    private String selectDialogId = LookupBinding.DEFAULT_SELECTDIALOG_ID;
    private String selectDialogCommandId = LookupBinding.DEFAULT_SELECTDIALOG_COMMAND_ID;
    private String dataEditorId;
    private String dataEditorViewCommandId;
    private Object filter;
    private boolean enableViewCommand;
    private boolean loadDetailedObject = false;
    private Function<T, String> objectLabelFunction = new Function<T, String>() {
        @Override
        public String apply(T t) {
            if(t == null)
                return null;
            return t.toString();
        }
    };
    private Function<String, ? extends Object> createFilterFromFieldFunction = new Function<String, Object>() {
        @Override
        public Object apply(String s) {
            return null;
        }
    };
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
        Integer autoPopupDialog;
        if(context.containsKey(AUTO_POPUP_DIALOG_KEY))              
            autoPopupDialog = (Integer) context.get(AUTO_POPUP_DIALOG_KEY);
        else
            autoPopupDialog = this.autoPopupDialog;
        Boolean revertValueOnFocusLost;
        if(context.containsKey(REVERT_VALUE_ON_FOCUS_LOST_KEY)) 
             revertValueOnFocusLost = (Boolean) context.get(REVERT_VALUE_ON_FOCUS_LOST_KEY);
        else
            revertValueOnFocusLost = this.revertValueOnFocusLost;
        String selectDialogCommandId;
        if(context.containsKey(SELECT_DIALOG_COMMAND_ID_KEY))
            selectDialogCommandId = (String) context.get(SELECT_DIALOG_COMMAND_ID_KEY);
        else
            selectDialogCommandId = this.selectDialogCommandId;
        String selectDialogId;
        if(context.containsKey(SELECT_DIALOG_ID_KEY))
            selectDialogId = (String) context.get(SELECT_DIALOG_ID_KEY);
        else
            selectDialogId = this.selectDialogId;
        String dataEditorViewCommandId;
        if(context.containsKey(DATA_EDITOR_VIEW_COMMAND_ID_KEY))
            dataEditorViewCommandId = (String) context.get(DATA_EDITOR_VIEW_COMMAND_ID_KEY);
        else
            dataEditorViewCommandId = this.dataEditorViewCommandId;
        Boolean enableViewCommand;
        if(context.containsKey(ENABLE_VIEW_COMMAND_KEY))
            enableViewCommand = (Boolean) context.get(ENABLE_VIEW_COMMAND_KEY);
        else
            enableViewCommand = this.enableViewCommand;
        Object filter;
        if(context.containsKey(FILTER_KEY))
            filter = context.get(FILTER_KEY);
        else
            filter = this.filter;
        Boolean loadDetailedObject;
        if(context.containsKey(LOAD_DETAILED_OBJECT_KEY))
            loadDetailedObject = (Boolean) context.get(LOAD_DETAILED_OBJECT_KEY);
        else
            loadDetailedObject = this.loadDetailedObject;
        Function<String, ? extends Object> createFilterFromFieldFunction;
        if(context.containsKey(CREATE_FILTER_FROM_FIELD_FUNCTION_KEY))
            createFilterFromFieldFunction = (Function<String, ? extends Object>) context.get(CREATE_FILTER_FROM_FIELD_FUNCTION_KEY);
        else
            createFilterFromFieldFunction = this.createFilterFromFieldFunction;
        Function<T, String> objectLabelFunction;
        if(context.containsKey(OBJECT_LABEL_FUNCTION_KEY))
            objectLabelFunction = (Function<T, String>) context.get(OBJECT_LABEL_FUNCTION_KEY);
        else
            objectLabelFunction = this.objectLabelFunction;
        Dimension dialogSize;
        if(context.containsKey(DIALOG_SIZE_KEY))
            dialogSize = (Dimension) context.get(DIALOG_SIZE_KEY);
        else
            dialogSize = this.dialogSize;
        LookupBinding<T> referableBinding = getLookupBinding(formModel, formPropertyPath, context);
        referableBinding.setAutoPopupdialog(autoPopupDialog);
        referableBinding.setRevertValueOnFocusLost(revertValueOnFocusLost);
        referableBinding.setSelectDialogCommandId(selectDialogCommandId);
        referableBinding.setSelectDialogId(selectDialogId);
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
        Class<T> requiredSourceClass;
        if(context.containsKey(REQUIRED_SOURCE_CLASS_KEY)) {
            requiredSourceClass = (Class<T>) context.get(REQUIRED_SOURCE_CLASS_KEY);
        } else {
            requiredSourceClass = this.requiredSourceClass;
        }
        if(requiredSourceClass == null)  {
            throw new IllegalStateException("requiredSourceClass should not be null");
        }
        return new LookupBinding<T>(getDataEditor(context), formModel, formPropertyPath, requiredSourceClass);
    }

    public Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context)
    {
        throw new UnsupportedOperationException("This binder needs a special component that cannot be given");
    }

    protected int getAutoPopupDialog()
    {
        return autoPopupDialog;
    }

    protected DefaultDataEditorWidget getDataEditor(Map context)
    {
        String dataEditorId;
        if(context.containsKey(DATA_EDITOR_ID_KEY)) {
            dataEditorId = (String) context.get(DATA_EDITOR_ID_KEY);
        } else {
            dataEditorId = this.dataEditorId;
        }
        if(dataEditorId == null)  {
            throw new IllegalStateException("dataEditorId should not be null");
        }
        Object dataEditor = ValkyrieRepository.getInstance().getApplicationConfig().applicationContext().getBean(dataEditorId);
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
        return dataEditorId;
    }

    public void setDataEditorId(String dataEditorId) {
        this.dataEditorId = dataEditorId;
    }
}

