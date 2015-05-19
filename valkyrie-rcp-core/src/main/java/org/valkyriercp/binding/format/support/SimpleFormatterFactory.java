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
import org.valkyriercp.binding.format.Style;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Simple FormatterFactory implementation.
 *
 * @author Keith Donald
 */
public class SimpleFormatterFactory extends AbstractFormatterFactory {

	public Formatter getDateFormatter(Style style) {
		return new DateFormatter(SimpleDateFormat.getDateInstance(style.getCode(), getLocale()));
	}

	public Formatter getDateTimeFormatter(Style dateStyle, Style timeStyle) {
		return new DateFormatter(SimpleDateFormat.getDateTimeInstance(dateStyle.getCode(), timeStyle.getCode(),
				getLocale()));
	}

	public Formatter getTimeFormatter(Style style) {
		return new DateFormatter(SimpleDateFormat.getTimeInstance(style.getCode(), getLocale()));
	}

	public Formatter getNumberFormatter(Class numberClass) {
		return new NumberFormatter(NumberFormat.getNumberInstance(getLocale()));
	}

	public Formatter getCurrencyFormatter() {
		return new NumberFormatter(NumberFormat.getCurrencyInstance(getLocale()));
	}

	public Formatter getDateFormatter(String encodedFormat) {
		return new DateFormatter(new SimpleDateFormat(encodedFormat, getLocale()));
	}

	public Formatter getPercentFormatter() {
		return new NumberFormatter(NumberFormat.getPercentInstance(getLocale()));
	}
}
