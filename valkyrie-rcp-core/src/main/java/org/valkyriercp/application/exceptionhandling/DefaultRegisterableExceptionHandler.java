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

public class DefaultRegisterableExceptionHandler<SELF extends DefaultRegisterableExceptionHandler<SELF>> extends AbstractRegisterableExceptionHandler<SELF> {
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

