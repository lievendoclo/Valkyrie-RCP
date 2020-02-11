package org.valkyriercp.convert.converters;

import org.springframework.util.NumberUtils;

/**
 * A one-way converter that can convert from any JDK-standard Number implementation to any other JDK-standard Number
 * implementation.
 * 
 * Support Number classes include byte, short, integer, float, double, long, big integer, big decimal. This class
 * delegates to {@link NumberUtils#convertNumberToTargetClass(Number, Class)} to perform the conversion.
 * 
 * @see Byte
 * @see Short
 * @see Integer
 * @see Long
 * @see java.math.BigInteger
 * @see Float
 * @see Double
 * @see java.math.BigDecimal
 * 
 * @author Keith Donald
 */
public class NumberToNumber implements Converter {

	public Class<?> getSourceClass() {
		return Number.class;
	}

	public Class<?> getTargetClass() {
		return Number.class;
	}

	@SuppressWarnings("unchecked")
	public Object convertSourceToTargetClass(Object source, Class<?> targetClass) {
		return NumberUtils.convertNumberToTargetClass((Number) source, (Class<? extends Number>) targetClass);
	}
}
