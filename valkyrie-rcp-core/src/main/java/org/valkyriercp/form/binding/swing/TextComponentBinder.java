package org.valkyriercp.form.binding.swing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.factory.ComponentFactory;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;
import org.valkyriercp.form.binding.swing.text.DocumentFactory;
import org.valkyriercp.form.binding.swing.text.RegExDocumentFactory;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.Map;

/**
 * @author Oliver Hutchison
 */
public class TextComponentBinder extends AbstractBinder {
    private String promptKey;
    private boolean convertEmptyStringToNull;
    private DocumentFactory documentFactory;
    private boolean readOnly;
    private boolean selectAllOnFocus;


    public TextComponentBinder() {
        super(String.class);
    }

    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof JTextComponent, "Control must be an instance of JTextComponent.");
        TextComponentBinding textComponentBinding = new TextComponentBinding((JTextComponent) control, formModel, formPropertyPath);
        if (context.containsKey("convertEmptyStringToNull"))
            textComponentBinding.setConvertEmptyStringToNull((Boolean) context.get("convertEmptyStringToNull"));
        else
            textComponentBinding.setConvertEmptyStringToNull(convertEmptyStringToNull);
        if (context.containsKey("promptKey"))
            textComponentBinding.setPromptKey((String) context.get("promptKey"));
        else
            textComponentBinding.setPromptKey(promptKey);
        if (context.containsKey("readOnly"))
            textComponentBinding.setReadOnly((Boolean) context.get("readOnly"));
        else
            textComponentBinding.setReadOnly(readOnly);
        if (context.containsKey("selectAllOnFocus"))
            textComponentBinding.setSelectAllOnFocus((Boolean) context.get("selectAllOnFocus"));
        else
            textComponentBinding.setSelectAllOnFocus(selectAllOnFocus);
        return textComponentBinding;
    }

    protected JTextComponent createTextComponent() {
        return getComponentFactory().createTextField();
    }

    protected JComponent createControl(Map context) {
        JTextComponent textComponent = createTextComponent();
        DocumentFactory contextDocumentFactory = (DocumentFactory) context.get("documentFactory");
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


