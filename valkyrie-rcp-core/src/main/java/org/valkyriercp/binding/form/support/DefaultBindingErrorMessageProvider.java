package org.valkyriercp.binding.form.support;

import org.springframework.beans.PropertyAccessException;
import org.springframework.context.support.MessageSourceAccessor;
import org.valkyriercp.binding.form.BindingErrorMessageProvider;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.format.InvalidFormatException;
import org.valkyriercp.binding.validation.ValidationMessage;
import org.valkyriercp.binding.validation.support.DefaultValidationMessage;
import org.valkyriercp.core.Severity;
import org.valkyriercp.util.ValkyrieRepository;

/**
 * Default implementation of <code>BindingErrorMessageProvider</code>.
 *
 * @author Oliver Hutchison
 */
public class DefaultBindingErrorMessageProvider implements BindingErrorMessageProvider {

    private MessageSourceAccessor messageSourceAccessor;

    protected MessageSourceAccessor getMessageSourceAccessor() {
        if(messageSourceAccessor == null)
            return ValkyrieRepository.getInstance().getApplicationConfig().messageSourceAccessor();
        return messageSourceAccessor;
    }

    public ValidationMessage getErrorMessage(FormModel formModel, String propertyName, Object valueBeingSet, Exception e) {
        String messageCode = getMessageCodeForException(e);
        Object[] args = new Object[] {formModel.getFieldFace(propertyName).getDisplayName(),
                UserMetadata.isFieldProtected(formModel, propertyName) ? "***" : valueBeingSet};
        String message = getMessageSourceAccessor().getMessage(messageCode, args, messageCode);
        return new DefaultValidationMessage(propertyName, Severity.ERROR, message);
    }

    protected String getMessageCodeForException(Exception e) {
        if (e instanceof PropertyAccessException) {
            return ((PropertyAccessException)e).getErrorCode();
        }
        else if (e instanceof NullPointerException) {
            return "required";
        }
        else if (e instanceof InvalidFormatException) {
            return "typeMismatch";
        }
        else if (e instanceof IllegalArgumentException) {
            return "typeMismatch";
        }
        else if (e.getCause() instanceof Exception) {
            return getMessageCodeForException((Exception)e.getCause());
        }
        else {
            return "unknown";
        }
    }

    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }
}