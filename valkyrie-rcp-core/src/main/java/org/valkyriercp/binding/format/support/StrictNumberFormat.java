package org.valkyriercp.binding.format.support;

import java.text.*;
import java.util.Currency;

/**
 * This is a decorator class for NumberFormat to ensure an exact number parsing.
 * The {@link java.text.NumberFormat} class allows parsing of numbers in strings like
 * '2abc' but at the richclient end we don't want this to be a valid parsing.
 * Therefor a specific NumberFormat that doesn't allow any other input than a
 * number.
 *
 * @author Yudhi Widyatama
 * @author Jan Hoskens
 *
 */
public class StrictNumberFormat extends NumberFormat {
	NumberFormat inner;

	public StrictNumberFormat(NumberFormat instance) {
		inner = instance;
	}

	public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
		return inner.format(number, toAppendTo, pos);
	}

	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		return inner.format(number, toAppendTo, pos);
	}

	public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos) {
		return inner.format(number, toAppendTo, pos);
	}

	public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
		return inner.formatToCharacterIterator(obj);
	}

	public Currency getCurrency() {
		return inner.getCurrency();
	}

	public int getMaximumFractionDigits() {
		return inner.getMaximumFractionDigits();
	}

	public int getMaximumIntegerDigits() {
		return inner.getMaximumIntegerDigits();
	}

	public int getMinimumFractionDigits() {
		return inner.getMinimumFractionDigits();
	}

	public int getMinimumIntegerDigits() {
		return inner.getMinimumIntegerDigits();
	}

	public int hashCode() {
		return inner.hashCode();
	}

	public boolean isGroupingUsed() {
		return inner.isGroupingUsed();
	}

	public boolean isParseIntegerOnly() {
		return inner.isParseIntegerOnly();
	}

	public Number parse(String source, ParsePosition parsePosition) {
		return inner.parse(source, parsePosition);
	}

	public Number parse(String source) throws ParseException {
		// idea taken from
		// org.apache.commons.validator.routines.AbstractFormatValidator
		ParsePosition parsePosition = new ParsePosition(0);
		Number result = inner.parse(source, parsePosition);
		if (parsePosition.getErrorIndex() > -1)
			throw new ParseException("Invalid format", parsePosition.getIndex());
		if (parsePosition.getIndex() < source.length())
			throw new ParseException("Invalid format[ii]", parsePosition.getIndex());
		return result;
	}

	public Object parseObject(String source) throws ParseException {
		return inner.parseObject(source);
	}

	public void setCurrency(Currency currency) {
		inner.setCurrency(currency);
	}

	public void setGroupingUsed(boolean newValue) {
		inner.setGroupingUsed(newValue);
	}

	public void setMaximumFractionDigits(int newValue) {
		inner.setMaximumFractionDigits(newValue);
	}

	public void setMaximumIntegerDigits(int newValue) {
		inner.setMaximumIntegerDigits(newValue);
	}

	public void setMinimumFractionDigits(int newValue) {
		inner.setMinimumFractionDigits(newValue);
	}

	public void setMinimumIntegerDigits(int newValue) {
		inner.setMinimumIntegerDigits(newValue);
	}

	public void setParseIntegerOnly(boolean value) {
		inner.setParseIntegerOnly(value);
	}
}

