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
