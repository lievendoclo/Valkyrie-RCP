package org.valkyriercp.binding.format.support;

import org.springframework.util.NumberUtils;
import org.valkyriercp.binding.format.InvalidFormatException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

/**
 * Converts from various <code>Number</code> specializations to <code>String</code> and back.
 *
 * @author Keith Donald
 */
public class NumberFormatter extends AbstractFormatter {

	private NumberFormat numberFormat;

	/**
	 * Default constructor. The formatter will use "toString" when formatting a value and "valueOf" when parsing a
	 * value.
	 */
	public NumberFormatter() {
	}

	/**
	 * Create a new number formatter.
	 * @param numberFormat the number format to use
	 */
	public NumberFormatter(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

	/**
	 * Create a new number formatter.
	 * @param numberFormat the number format to use
	 * @param allowEmpty should this formatter allow empty input arguments?
	 */
	public NumberFormatter(NumberFormat numberFormat, boolean allowEmpty) {
		super(allowEmpty);
		this.numberFormat = numberFormat;
	}

	protected String doFormatValue(Object number) {
		if (this.numberFormat != null) {
			// use NumberFormat for rendering value
			return this.numberFormat.format(number);
		} else {
			// use toString method for rendering value
			return number.toString();
		}
	}

	protected Object doParseValue(String text, Class targetClass) throws IllegalArgumentException {
		// use given NumberFormat for parsing text
		if (this.numberFormat != null) {
			return NumberUtils.parseNumber(text, targetClass, this.numberFormat);
		}
		// use default valueOf methods for parsing text
		else {
			return NumberUtils.parseNumber(text, targetClass);
		}
	}

	// convenience methods

	public Byte parseByte(String formattedString) throws InvalidFormatException {
		return (Byte) parseValue(formattedString, Byte.class);
	}

	public Short parseShort(String formattedString) throws InvalidFormatException {
		return (Short) parseValue(formattedString, Short.class);
	}

	public Integer parseInteger(String formattedString) throws InvalidFormatException {
		return (Integer) parseValue(formattedString, Integer.class);
	}

	public Long parseLong(String formattedString) throws InvalidFormatException {
		return (Long) parseValue(formattedString, Long.class);
	}

	public Float parseFloat(String formattedString) throws InvalidFormatException {
		return (Float) parseValue(formattedString, Float.class);
	}

	public Double parseDouble(String formattedString) throws InvalidFormatException {
		return (Double) parseValue(formattedString, Double.class);
	}

	public BigInteger parseBigInteger(String formattedString) throws InvalidFormatException {
		return (BigInteger) parseValue(formattedString, BigInteger.class);
	}

	public BigDecimal parseBigDecimal(String formattedString) throws InvalidFormatException {
		return (BigDecimal) parseValue(formattedString, BigDecimal.class);
	}
}