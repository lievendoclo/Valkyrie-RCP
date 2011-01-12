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
