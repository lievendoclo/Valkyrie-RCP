package org.valkyriercp.application.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.MessageSourceAccessor;

import java.text.MessageFormat;

public class MessageResolver {
    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    public String[] getMessageKeys(String id, String name, String type)
    {
        boolean idNotEmpty = (id == null) || id.trim().equals("") ? false : true;
        String[] keys = new String[idNotEmpty ? 3 : 2];
        int i = 0;
        if (idNotEmpty)
        {
            keys[i++] = id + "." + name + "." + type;
        }
        keys[i++] = name + "." + type;
        keys[i] = name;
        return keys;
    }

    public String getMessage(String id, String name, String type)
    {
        String[] messageKeys = getMessageKeys(id, name, type);

        return messageSourceAccessor.getMessage(new DefaultMessageSourceResolvable(messageKeys, null,
                messageKeys[messageKeys.length - 1]));
    }

    public String getMessage(MessageSourceResolvable msr)
    {
        return messageSourceAccessor.getMessage(msr);
    }

    public String getMessage(String id)
    {
        return messageSourceAccessor.getMessage(id, "");
    }

    public String getMessage(String id, String name, String type, Object[] params)
    {
        String message = getMessage(id, name, type);
        if (params != null)
            return MessageFormat.format(message, params);
        return message;
    }
}
