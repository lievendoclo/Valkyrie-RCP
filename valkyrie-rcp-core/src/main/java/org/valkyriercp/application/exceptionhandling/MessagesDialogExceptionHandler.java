package org.valkyriercp.application.exceptionhandling;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.ErrorCoded;

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
public class MessagesDialogExceptionHandler<T extends MessagesDialogExceptionHandler<T>> extends AbstractDialogExceptionHandler<T> {

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

    public T withWrapLength(int wrapLength) {
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

    public T withIdentLength(int identLength) {
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

    public T withMessagesKey(String messagesKey) {
        setMessagesKey(messagesKey);
        return self();
    }


    public String resolveExceptionCaption(Throwable throwable) {
        String[] messagesKeys = getMessagesKeys(throwable, ".caption");
        return messageSourceAccessor.getMessage(new DefaultMessageSourceResolvable(
                messagesKeys, messagesKeys[0]));
    }

    public Object createExceptionContent(Throwable throwable) {
        String[] messagesKeys = getMessagesKeys(throwable, ".description");
        String[] parameters = new String[]{
            formatMessage(throwable.getMessage())
        };
        return messageSourceAccessor.getMessage(new DefaultMessageSourceResolvable(
                messagesKeys, parameters, messagesKeys[0]));
    }

    protected String[] getMessagesKeys(Throwable throwable, String keySuffix) {
        if (messagesKey != null) {
            return new String[] {messagesKey};
        }
        List<String> messageKeyList = new ArrayList<String>();
        Class clazz = throwable.getClass();
        if (throwable instanceof ErrorCoded)
        {
            messageKeyList.add(((ErrorCoded) throwable).getErrorCode() + keySuffix);
        }
        if (throwable instanceof SQLException)
        {
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
        String identString = StringUtils.leftPad("", identLength);
        String newLineWithIdentString = "\n" + identString;
        StringBuilder formattedMessageBuilder = new StringBuilder(identString);
        StringTokenizer messageTokenizer = new StringTokenizer(message, "\n");
        while (messageTokenizer.hasMoreTokens()) {
            String messageToken = messageTokenizer.nextToken();
            formattedMessageBuilder.append(WordUtils.wrap(messageToken, wrapLength, newLineWithIdentString, true));
            if (messageTokenizer.hasMoreTokens()) {
                formattedMessageBuilder.append(newLineWithIdentString);
            }
        }
        return formattedMessageBuilder.toString();
    }

}

