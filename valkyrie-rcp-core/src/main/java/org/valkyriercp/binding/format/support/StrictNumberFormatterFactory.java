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
