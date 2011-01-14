package org.valkyriercp.binding.format.support;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.valkyriercp.binding.format.Formatter;
import org.valkyriercp.binding.format.FormatterFactory;
import org.valkyriercp.binding.format.Style;

import java.util.Locale;

/**
 * Base class for formatter factories. Manages the locale used by the produced formatters using Spring's
 * {@link org.springframework.context.i18n.LocaleContext} system.
 *
 * @author Keith Donald
 */
public abstract class AbstractFormatterFactory implements FormatterFactory {

	private LocaleContext localeContext = new SimpleLocaleContext(Locale.getDefault());

	private Style defaultDateStyle = Style.MEDIUM;

	private Style defaultTimeStyle = Style.MEDIUM;

	/**
	 * Sets the locale context used. Defaults to a {@link SimpleLocaleContext} holding the system default locale.
	 */
	public void setLocaleContext(LocaleContext localeContext) {
		this.localeContext = localeContext;
	}

	/**
	 * Returns the locale in use.
	 */
	protected Locale getLocale() {
		return localeContext.getLocale();
	}

	/**
	 * Returns the default date style. Defaults to {@link Style#MEDIUM}.
	 */
	protected Style getDefaultDateStyle() {
		return defaultDateStyle;
	}

	/**
	 * Set the default date style.
	 */
	public void setDefaultDateStyle(Style defaultDateStyle) {
		this.defaultDateStyle = defaultDateStyle;
	}

	/**
	 * Returns the default time style. Defaults to {@link Style#MEDIUM}.
	 */
	public Style getDefaultTimeStyle() {
		return defaultTimeStyle;
	}

	/**
	 * Set the default time style.
	 */
	public void setDefaultTimeStyle(Style defaultTimeStyle) {
		this.defaultTimeStyle = defaultTimeStyle;
	}

	public Formatter getDateFormatter() {
		return getDateFormatter(getDefaultDateStyle());
	}

	public Formatter getDateTimeFormatter() {
		return getDateTimeFormatter(getDefaultDateStyle(), getDefaultTimeStyle());
	}

	public Formatter getTimeFormatter() {
		return getTimeFormatter(getDefaultTimeStyle());
	}
}