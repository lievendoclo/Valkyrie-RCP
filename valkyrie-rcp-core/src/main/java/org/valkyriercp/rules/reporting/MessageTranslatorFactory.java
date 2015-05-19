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
