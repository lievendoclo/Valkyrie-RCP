package org.valkyriercp.form.binding.swing;

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;
import org.valkyriercp.form.binding.swing.text.DocumentFactory;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.Map;

/**
 * @author Oliver Hutchison
 */
public class TextComponentBinder extends AbstractBinder {
    public static final String CONVERT_EMPTY_STRING_TO_NULL_KEY = "convertEmptyStringToNull";
    public static final String PROMPT_KEY_KEY = "promptKey";
    public static final String READ_ONLY_KEY = "readOnly";
    public static final String SELECT_ALL_ON_FOCUS_KEY = "selectAllOnFocus";
    public static final String DOCUMENT_FACTORY_KEY = "documentFactory";
    private String promptKey;
    private boolean convertEmptyStringToNull;
    private DocumentFactory documentFactory;
    private boolean readOnly;
    private boolean selectAllOnFocus;


    public TextComponentBinder() {
        super(String.class, new String[] {CONVERT_EMPTY_STRING_TO_NULL_KEY, PROMPT_KEY_KEY, READ_ONLY_KEY, SELECT_ALL_ON_FOCUS_KEY, DOCUMENT_FACTORY_KEY});
    }

    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof JTextComponent, "Control must be an instance of JTextComponent.");
        TextComponentBinding textComponentBinding = new TextComponentBinding((JTextComponent) control, formModel, formPropertyPath);
        if (context.containsKey(CONVERT_EMPTY_STRING_TO_NULL_KEY))
            textComponentBinding.setConvertEmptyStringToNull((Boolean) context.get(CONVERT_EMPTY_STRING_TO_NULL_KEY));
        else
            textComponentBinding.setConvertEmptyStringToNull(convertEmptyStringToNull);
        if (context.containsKey(PROMPT_KEY_KEY))
            textComponentBinding.setPromptKey((String) context.get(PROMPT_KEY_KEY));
        else
            textComponentBinding.setPromptKey(promptKey);
        if (context.containsKey(READ_ONLY_KEY))
            textComponentBinding.setReadOnly((Boolean) context.get(READ_ONLY_KEY));
        else
            textComponentBinding.setReadOnly(readOnly);
        if (context.containsKey(SELECT_ALL_ON_FOCUS_KEY))
            textComponentBinding.setSelectAllOnFocus((Boolean) context.get(SELECT_ALL_ON_FOCUS_KEY));
        else
            textComponentBinding.setSelectAllOnFocus(selectAllOnFocus);
        return textComponentBinding;
    }

    protected JTextComponent createTextComponent() {
        return getComponentFactory().createTextField();
    }

    protected JComponent createControl(Map context) {
        JTextComponent textComponent = createTextComponent();
        DocumentFactory contextDocumentFactory = (DocumentFactory) context.get(DOCUMENT_FACTORY_KEY);
        if (contextDocumentFactory != null) {
            textComponent.setDocument(contextDocumentFactory.createDocument());
        } else if (documentFactory != null) {
            textComponent.setDocument(documentFactory.createDocument());
        }
        return textComponent;
    }

    public boolean isConvertEmptyStringToNull() {
        return convertEmptyStringToNull;
    }

    public void setConvertEmptyStringToNull(boolean convertEmptyStringToNull) {
        this.convertEmptyStringToNull = convertEmptyStringToNull;
    }

    public String getPromptKey() {
        return promptKey;
    }

    public void setPromptKey(String promptKey) {
        this.promptKey = promptKey;
    }

    public DocumentFactory getDocumentFactory() {
        return documentFactory;
    }

    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isSelectAllOnFocus() {
        return selectAllOnFocus;
    }

    public void setSelectAllOnFocus(boolean selectAllOnFocus) {
        this.selectAllOnFocus = selectAllOnFocus;
    }
}


