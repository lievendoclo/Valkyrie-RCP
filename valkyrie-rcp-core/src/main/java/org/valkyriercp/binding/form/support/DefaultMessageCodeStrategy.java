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
package org.valkyriercp.binding.form.support;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Default implementation for {@link MessageCodeStrategy}. It creates message codes as follows:
 * <p>
 * <code>{contextId}.{field}.{suffix}</code><br>
 * <code>{field}.{suffix}</code> - without a contextId<br>
 * <code>{field}</code> - without a contextId and no suffix<br>
 * <p>
 * If field contains a name which is separated by <code>'.'</code> like <code>'fieldcontext.field'</code>:
 * <p>
 * <code>{contextId}.fieldcontext.field.{suffix}</code><br>
 * <code>{contextId}.field.{suffix}</code><br>
 * <code>fieldcontext.field.{suffix}</code> - without a contextId<br>
 * <code>field.{suffix}</code> - without a contextId<br>
 * <code>fieldcontext.field</code> - without a contextId and no suffix<br>
 * <code>field</code> - without a contextId and no suffix<br>
 * <p>
 *
 * @author Mathias Broekelmann
 *
 */
public class DefaultMessageCodeStrategy implements MessageCodeStrategy {

    public String[] getMessageCodes(String contextId, String field, String[] suffixes) {
        boolean hasContextId = StringUtils.hasText(contextId);
        String[] fieldPathElements = StringUtils.delimitedListToStringArray(field, ".");
        Collection keys = new ArrayList((hasContextId ? 2 * fieldPathElements.length : fieldPathElements.length)
                * (suffixes == null ? 1 : suffixes.length));
        if (hasContextId) {
            String prefix = contextId + '.';
            addKeys(keys, prefix, fieldPathElements, suffixes);
        }
        addKeys(keys, "", fieldPathElements, suffixes);
        return (String[]) keys.toArray(new String[keys.size()]);
    }

    private void addKeys(Collection keys, String prefix, String[] fieldPathElements, String[] suffix) {
        final int size = fieldPathElements.length;
        final int suffixSize = suffix == null ? 0 : suffix.length;
        for (int i = 0; i < size; i++) {
            StringBuffer path = new StringBuffer(prefix);
            for (int j = i; j < size; j++) {
                path.append(fieldPathElements[j]);
                if (j + 1 < size) {
                    path.append('.');
                }
            }
            if (suffixSize == 0) {
                keys.add(path.toString());
            } else {
                for (int j = 0; j < suffixSize; j++) {
                    String currentSuffix = suffix[j];
                    if (StringUtils.hasText(currentSuffix)) {
                        keys.add(path.toString() + "." + currentSuffix);
                    } else {
                        keys.add(path.toString());
                    }

                }
            }
        }
    }

}

