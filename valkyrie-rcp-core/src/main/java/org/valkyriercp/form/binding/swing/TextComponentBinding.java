package org.valkyriercp.form.binding.swing;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.swing.AsYouTypeTextComponentAdapter;
import org.valkyriercp.form.binding.support.AbstractBinding;
import org.valkyriercp.text.SelectAllFocusListener;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * @author Oliver Hutchison
 */
public class TextComponentBinding extends AbstractBinding
{

    private final JTextComponent textComponent;
    private boolean convertEmptyStringToNull;
    private String promptKey;
    private boolean readOnly;
    private boolean selectAllOnFocus;

    public TextComponentBinding(JTextComponent textComponent, FormModel formModel, String formPropertyPath)
    {
        super(formModel, formPropertyPath, String.class);
        this.textComponent = textComponent;
    }

    protected JComponent doBindControl()
    {
        final ValueModel valueModel = getValueModel();
        try
        {
            textComponent.setText((String) valueModel.getValue());
        }
        catch (ClassCastException e)
        {
            IllegalArgumentException ex = new IllegalArgumentException("Class cast exception converting '"
                    + getProperty() + "' property value to string - did you install a type converter?");
            ex.initCause(e);
            throw ex;
        }
//        if (getPromptKey() != null)
//            PromptSupport.setPrompt(RcpSupport.getMessage(getPromptKey()), textComponent);
        new AsYouTypeTextComponentAdapter(textComponent, valueModel, convertEmptyStringToNull);
        if(selectAllOnFocus)
        {
            textComponent.addFocusListener(new SelectAllFocusListener(textComponent));
        }
        return textComponent;
    }

    public boolean isReadOnly()
    {
        return super.isReadOnly() || readOnly;
    }

    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    protected void readOnlyChanged()
    {
        textComponent.setEditable(!isReadOnly());
    }

    protected void enabledChanged()
    {
        textComponent.setEnabled(isEnabled());
    }

    public String getPromptKey()
    {
        return promptKey;
    }

    public void setPromptKey(String promptKey)
    {
        this.promptKey = promptKey;
    }

    public boolean isConvertEmptyStringToNull()
    {
        return convertEmptyStringToNull;
    }

    public void setConvertEmptyStringToNull(boolean convertEmptyStringToNull)
    {
        this.convertEmptyStringToNull = convertEmptyStringToNull;
    }

    public boolean isSelectAllOnFocus()
    {
        return selectAllOnFocus;
    }

    public void setSelectAllOnFocus(boolean selectAllOnFocus)
    {
        this.selectAllOnFocus = selectAllOnFocus;
    }
}
