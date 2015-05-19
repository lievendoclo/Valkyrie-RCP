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
package org.valkyriercp.binding.format.support;

import org.valkyriercp.binding.format.Formatter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Extending the {@link SimpleFormatterFactory} to return a
 * {@link StrictNumberFormat} wrapping the default {@link java.text.NumberFormat} to have
 * strict number parsing.
 *
 * @author Yudhi Widyatama
 * @author Jan Hoskens
 */
public class StrictNumberFormatterFactory extends SimpleFormatterFactory {
	public Formatter getNumberFormatter(Class numberClass) {
		Locale locale = getLocale();
		NumberFormat instance = NumberFormat.getInstance(locale);
		NumberFormat wrappedInstance = new StrictNumberFormat(instance);
		return new NumberFormatter(wrappedInstance);
	}
}
