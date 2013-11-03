package org.valkyriercp.rules.reporting;

import java.util.Locale;

public interface MessageTranslatorFactory {
    /**
	 * Creates a message translator by using the given object name resolver and
	 * the default locale
	 *
	 * @param resolver
	 *            the object name resolver which is used to resolve a name to
	 *            use in the translated message for an object name
	 * @return the created message translator instance, must not be null
	 */
	MessageTranslator createTranslator(ObjectNameResolver resolver);

	/**
	 * Creates a message translator by using the given object name resolver and
	 * the locale
	 *
	 * @param resolver
	 *            the object name resolver which is used to resolve a name to
	 *            use in the translated message for an object name
	 * @param locale
	 *            the locale for the translated messages, if null the default
	 *            locale is used
	 * @return the created message translator instance, must not be null
	 */
	MessageTranslator createTranslator(ObjectNameResolver resolver,
			Locale locale);
}
