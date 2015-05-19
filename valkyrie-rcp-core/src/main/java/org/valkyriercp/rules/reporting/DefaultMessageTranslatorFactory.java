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
package org.valkyriercp.rules.reporting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class DefaultMessageTranslatorFactory implements
        MessageTranslatorFactory {

    @Autowired
    private MessageSource messageSource;

    public MessageTranslator createTranslator(ObjectNameResolver resolver) {
        return createTranslator(resolver, Locale.getDefault());
    }

    public MessageTranslator createTranslator(ObjectNameResolver resolver,
                                              Locale locale) {
        return new DefaultMessageTranslator(messageSource, resolver, locale);
    }
}
