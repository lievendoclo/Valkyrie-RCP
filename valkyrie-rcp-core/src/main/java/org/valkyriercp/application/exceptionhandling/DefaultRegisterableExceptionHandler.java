package org.valkyriercp.application.exceptionhandling;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.util.StringUtils;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.core.DefaultMessage;
import org.valkyriercp.core.Message;
import org.valkyriercp.core.Severity;

import javax.swing.*;

public class DefaultRegisterableExceptionHandler<T extends DefaultRegisterableExceptionHandler<T>> extends AbstractRegisterableExceptionHandler<T> {
    @Autowired
    private ApplicationConfig applicationConfig;

    public void uncaughtException(Thread thread, Throwable throwable) {
        LogFactory.getLog(ApplicationLifecycleAdvisor.class).error(throwable.getMessage(), throwable);
        String exceptionMessage;
        if (throwable instanceof MessageSourceResolvable) {
            exceptionMessage = applicationConfig.messageSourceAccessor().getMessage((MessageSourceResolvable) throwable);
        } else {
            exceptionMessage = throwable.getLocalizedMessage();
        }
        if (!StringUtils.hasText(exceptionMessage)) {
            String defaultMessage = "An application exception occurred.\nPlease contact your administrator.";
            exceptionMessage = applicationConfig.messageSourceAccessor()
                    .getMessage("applicationDialog.defaultException", defaultMessage);
        }

        Message message = new DefaultMessage(exceptionMessage, Severity.ERROR);
        ApplicationWindow activeWindow = applicationConfig.windowManager().getActiveWindow();
        JFrame parentFrame = (activeWindow == null) ? null : activeWindow.getControl();
        JOptionPane.showMessageDialog(parentFrame, message.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

}

