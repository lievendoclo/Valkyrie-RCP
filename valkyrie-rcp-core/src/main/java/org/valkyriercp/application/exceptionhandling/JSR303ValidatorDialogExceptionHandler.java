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

import javax.swing.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.awt.*;
import java.util.ArrayList;

/**
 * Displays the validation errors to the user.
 * @author Geoffrey De Smet
 * @since 0.3
 */
public class JSR303ValidatorDialogExceptionHandler<SELF extends JSR303ValidatorDialogExceptionHandler<SELF>> extends AbstractDialogExceptionHandler<SELF> {

    private static final String CAPTION_KEY = "jsr303ValidatorDialogExceptionHandler.caption";
    private static final String EXPLANATION_KEY = "jsr303ValidatorDialogExceptionHandler.explanation";

    public String resolveExceptionCaption(Throwable throwable) {
        return getMessageSourceAccessor().getMessage(CAPTION_KEY, CAPTION_KEY);
    }

    public Object createExceptionContent(Throwable throwable) {
        if (!(throwable instanceof ConstraintViolationException)) {
            String ILLEGAL_THROWABLE_ARGUMENT
                    = "Could not handle exception: throwable is not an ConstraintViolationException:\n"
                    + throwable.getClass().getName();
            logger.error(ILLEGAL_THROWABLE_ARGUMENT);
            return ILLEGAL_THROWABLE_ARGUMENT;
        }
        ConstraintViolationException invalidStateException = (ConstraintViolationException) throwable;
        String explanation = getMessageSourceAccessor().getMessage(EXPLANATION_KEY, EXPLANATION_KEY);
        JPanel panel = new JPanel(new BorderLayout());
        JLabel explanationLabel = new JLabel(explanation);
        panel.add(explanationLabel, BorderLayout.NORTH);
        java.util.List<String> invalidValueMessageList = new ArrayList<String>();
        for (ConstraintViolation invalidValue : invalidStateException.getConstraintViolations()) {
            StringBuffer messageBuffer = new StringBuffer();
            String propertyName = invalidValue.getPropertyPath().toString();
            messageBuffer.append(getMessageSourceAccessor().getMessage(propertyName + ".label", propertyName));
            messageBuffer.append(" ");
            messageBuffer.append(invalidValue.getMessage());
            invalidValueMessageList.add(messageBuffer.toString());
        }
        JList invalidValuesJList = new JList(invalidValueMessageList.toArray());
        JScrollPane invalidValuesScrollPane = new JScrollPane(invalidValuesJList,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(invalidValuesScrollPane, BorderLayout.CENTER);
        return panel;
    }

}

