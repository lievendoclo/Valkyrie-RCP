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

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Displays a message to the user which is fetched from the I18N files
 * based on the class and superclasses of the throwable.
 * <p/>
 * For example if an IllegalArgumentException is thrown, it will search for
 * java.lang.IllegalArgumentException.caption and java.lang.IllegalArgumentException.description first,
 * and if it cant find that it will try in order:
 * java.lang.RuntimeException.caption/description, java.lang.Exception.caption/description and
 * java.lang.Throwable.caption/description.
 * <p/>
 * The exception message is passed as a parameter, but is idented and wrapped first.
 * Note for the repacing of {0} to work in a property file double quotes(") need to be escaped (\")
 * and that single quotes (') should be avoided (escaping doesn't seem to work).
 * @author Geoffrey De Smet
 * @since 0.3
 */
public class MessagesDialogExceptionHandler<SELF extends MessagesDialogExceptionHandler<SELF>> extends AbstractDialogExceptionHandler<SELF> {

    private int wrapLength = 120;
    private int identLength = 2;

    private String messagesKey = null;

    /**
     * Sets the wrap length applied on the exception message passed as a parameter.
     * Defaults to 120.
     * @param wrapLength
     */
    public void setWrapLength(int wrapLength) {
        this.wrapLength = wrapLength;
    }

    public SELF wrappingAt(int wrapLength) {
        setWrapLength(wrapLength);
        return self();
    }

    /**
     * Sets the identation applied on the exception message passed as a parameter.
     * Defaults to 2.
     * @param identLength
     */
    public void setIdentLength(int identLength) {
        this.identLength = identLength;
    }

    public SELF withIdentLength(int identLength) {
        setIdentLength(identLength);
        return self();
    }

    /**
     * If messagesKey is set, the caption and description shown in the dialog
     * are not based dynamically on the throwable,
     * but instead statically on the keys messageKey.caption and messageKey.description.
     *
     * @param messagesKey the key used for the caption and title
     */
    public void setMessagesKey(String messagesKey) {
        this.messagesKey = messagesKey;
    }

    public SELF withMessagesKey(String messagesKey) {
        setMessagesKey(messagesKey);
        return self();
    }


    public String resolveExceptionCaption(Throwable throwable) {
        String[] messagesKeys = getMessagesKeys(throwable, ".caption");
        return getApplicationConfig().messageSourceAccessor().getMessage(new DefaultMessageSourceResolvable(
                messagesKeys, messagesKeys[0]));
    }

    public Object createExceptionContent(Throwable throwable) {
        String[] messagesKeys = getMessagesKeys(throwable, ".description");
        String[] parameters = new String[]{
            formatMessage(throwable.getMessage())
        };
        return getApplicationConfig().messageSourceAccessor().getMessage(new DefaultMessageSourceResolvable(
                messagesKeys, parameters, messagesKeys[0]));
    }

    protected String[] getMessagesKeys(Throwable throwable, String keySuffix) {
        if (messagesKey != null) {
            return new String[] {messagesKey};
        }
        List<String> messageKeyList = new ArrayList<String>();
        Class clazz = throwable.getClass();
        if (throwable instanceof SQLException) {
            messageKeyList.add(SQLException.class.getName() + "." + ((SQLException) throwable).getErrorCode() + keySuffix);
        }
        while (clazz != Object.class) {
            messageKeyList.add(clazz.getName() + keySuffix);
            clazz = clazz.getSuperclass();
        }
        return messageKeyList.toArray(new String[messageKeyList.size()]);
    }

    protected String formatMessage(String message) {
        if (message == null) {
            return "";
        }
        String identString = StringUtils.leftPad("", identLength, ' ');
        String newLineWithIdentString = "\n" + identString;
        StringBuilder formattedMessageBuilder = new StringBuilder(identString);
        StringTokenizer messageTokenizer = new StringTokenizer(message, "\n");
        while (messageTokenizer.hasMoreTokens()) {
            String messageToken = messageTokenizer.nextToken();
            formattedMessageBuilder.append(wrap(messageToken, wrapLength));
            if (messageTokenizer.hasMoreTokens()) {
                formattedMessageBuilder.append(newLineWithIdentString);
            }
        }
        return formattedMessageBuilder.toString();
    }

    private static String wrap(String str, int wrapLength) {
        int offset = 0;
        StringBuilder resultBuilder = new StringBuilder();

        while ((str.length() - offset) > wrapLength) {
            if (str.charAt(offset) == ' ') {
                offset++;
                continue;
            }

            int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);
            // if the next string with length maxLength doesn't contain ' '
            if (spaceToWrapAt < offset) {
                spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
                // if no more ' '
                if (spaceToWrapAt < 0) {
                    break;
                }
            }

            resultBuilder.append(str.substring(offset, spaceToWrapAt));
            resultBuilder.append("\n");
            offset = spaceToWrapAt + 1;
        }

        resultBuilder.append(str.substring(offset));
        return resultBuilder.toString();
    }

}

