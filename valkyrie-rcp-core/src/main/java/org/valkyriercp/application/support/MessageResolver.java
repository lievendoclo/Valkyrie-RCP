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
package org.valkyriercp.application.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.MessageSourceAccessor;

import java.text.MessageFormat;
import java.util.Locale;

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

    public String getMessage(final String[] messageCodes) {
        MessageSourceResolvable resolvable = new MessageSourceResolvable() {
            public String[] getCodes() {
                return messageCodes;
            }

            public Object[] getArguments() {
                return new Object[0];
            }

            public String getDefaultMessage() {
                return messageCodes[0];
            }
        };
        return messageSourceAccessor.getMessage(resolvable, Locale.getDefault());
    }

    public String getMessage(String messageCode, Object[] args) {
        return messageSourceAccessor.getMessage(messageCode, args, messageCode, Locale.getDefault());
    }

    public String getMessage(final String[] messageCodes, final Object[] args) {
        MessageSourceResolvable resolvable = new MessageSourceResolvable() {
            public String[] getCodes() {
                return messageCodes;
            }

            public Object[] getArguments() {
                return args;
            }

            public String getDefaultMessage() {
                return messageCodes[0];
            }
        };
        return messageSourceAccessor.getMessage(resolvable, Locale.getDefault());
    }
}
