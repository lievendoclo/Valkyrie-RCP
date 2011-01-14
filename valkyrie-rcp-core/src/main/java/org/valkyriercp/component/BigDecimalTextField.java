package org.valkyriercp.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * This class can have different "read" and "write" formats. When showing the
 * number the "read" format will be used. If the user enters the inputfield
 * (gains focus), the "write" format will be used.
 * </p>
 *
 * <p>
 * A maximum of decimals/non-decimals can be specified so no more numbers can be
 * input than strictly defined.
 * </p>
 *
 * <p>
 * A boolean can be specified to allow only positive numbers or positive and
 * negative numbers. Switching between positive and negative can be done by
 * using the +/- buttons anywhere in the inputfield.
 * </p>
 *
 * TODO There's a third option: only negative numbers, this should be
 * configurable as well.
 *
 * @author Jan Hoskens
 *
 */
public class BigDecimalTextField extends JTextField {

	private static final long serialVersionUID = -601376040393562990L;

	Log log = LogFactory.getLog(BigDecimalTextField.class);

	public static final NumberFormat DEFAULT_FORMAT = new DecimalFormat("###,###,###,##0.######");

	public static final NumberFormat DEFAULT_UNFORMAT = new DecimalFormat("#0.#######");

	public static final DecimalFormatSymbols symbols = new DecimalFormatSymbols();

	private Class numberClass = null;

	private final NumberFormat format;

	private final NumberFormat unformat;

	private Integer scale;

	private List listeners;

	private boolean internallySettingText = false;

	/**
	 * Default constructor.
	 */
	public BigDecimalTextField() {
		this(2, 4, true);
	}

	/**
	 * @see #BigDecimalTextField(int, int, boolean, Class, NumberFormat,
	 * NumberFormat)
	 */
	public BigDecimalTextField(int nrOfNonDecimals, int nrOfDecimals, boolean negativeSign) {
		this(nrOfNonDecimals, nrOfDecimals, negativeSign, BigDecimal.class);
	}

	/**
	 * @see #BigDecimalTextField(int, int, boolean, Class, NumberFormat,
	 * NumberFormat)
	 */
	public BigDecimalTextField(int nrOfNonDecimals, int nrOfDecimals, boolean negativeSign, Class numberClass) {
		this(nrOfNonDecimals, nrOfDecimals, negativeSign, numberClass, DEFAULT_FORMAT);
	}

	/**
	 * @see #BigDecimalTextField(int, int, boolean, Class, NumberFormat,
	 * NumberFormat)
	 */
	public BigDecimalTextField(int nrOfNonDecimals, int nrOfDecimals, boolean negativeSign, Class numberClass,
			NumberFormat format) {
		this(nrOfNonDecimals, nrOfDecimals, negativeSign, numberClass, format, DEFAULT_UNFORMAT);
	}

	/**
	 * @param nrOfNonDecimals Number of non-decimals.
	 * @param nrOfDecimals Number of decimals.
	 * @param negativeSign Negative numbers allowed.
	 * @param numberClass Class type (default BigDecimal).
	 * @param format The "read"-format.
	 * @param unformat The "edit"-format.
	 */
	public BigDecimalTextField(int nrOfNonDecimals, int nrOfDecimals, boolean negativeSign, Class numberClass,
			NumberFormat format, NumberFormat unformat) {
		super();
		Assert.notNull(format);
		Assert.notNull(unformat);
		this.format = format;
		setBigDecimalFormat(format, numberClass);
		this.unformat = unformat;
		setBigDecimalFormat(unformat, numberClass);
		this.numberClass = numberClass;
		setDocument(new BigDecimalDocument(nrOfNonDecimals, nrOfDecimals, negativeSign));
		addFocusListener(new FormatFocusListener());
	}

	/**
	 * When parsing a number, BigDecimalFormat can return numbers different than
	 * BigDecimal. This method will ensure that when using a {@link BigDecimal}
	 * or a {@link java.math.BigInteger}, the formatter will return a {@link BigDecimal}
	 * in order to prevent loss of precision. Note that you should use the
	 * {@link DecimalFormat} to make this work.
	 *
	 * @param format
	 * @param numberClass
	 *
	 * @see #getValue()
	 * @see DecimalFormat#setParseBigDecimal(boolean)
	 */
	private static final void setBigDecimalFormat(NumberFormat format, Class numberClass) {
		if (format instanceof DecimalFormat && ((numberClass == BigDecimal.class) || (numberClass == BigInteger.class))) {
			((DecimalFormat) format).setParseBigDecimal(true);
		}
	}

	/**
	 * Add a UserInputListener.
	 *
	 * @param listener UserInputListener.
	 *
	 * @see UserInputListener
	 */
	public void addUserInputListener(UserInputListener listener) {
		if (this.listeners == null)
			this.listeners = new ArrayList();
		this.listeners.add(listener);
	}

	/**
	 * Remove a UserInputListener.
	 *
	 * @param listener UserInputListener.
	 *
	 * @see UserInputListener
	 */
	public void removeUserInputListener(UserInputListener listener) {
		if (listeners != null) {
			this.listeners.remove(listener);
		}
	}

	/**
	 * Fire an event to all UserInputListeners.
	 */
	private void fireUserInputChange() {
		if (!internallySettingText && (this.listeners != null)) {
			for (Iterator it = this.listeners.iterator(); it.hasNext();) {
				UserInputListener userInputListener = (UserInputListener) it.next();
				userInputListener.update(this);
			}
		}
	}

	/**
	 * Parses a number from the inputField and will adjust it's class if needed.
	 *
	 * @return Number the Parsed number.
	 */
	public Number getValue() {
		if ((getText() == null) || "".equals(getText().trim()))
			return null;
		try {
			Number n = format.parse(getText());
			if (n.getClass() == this.numberClass)
				return n;
			else if (this.numberClass == BigDecimal.class) {
				BigDecimal bd = new BigDecimal(n.doubleValue());
				if (scale != null) {
					bd = bd.setScale(scale.intValue(), BigDecimal.ROUND_HALF_UP);
				}
				return bd;
			}
			else if (this.numberClass == Double.class)
				return new Double(n.doubleValue());
			else if (this.numberClass == Float.class)
				return new Float(n.floatValue());
			else if (this.numberClass == BigInteger.class)
				// we have called setBigDecimalFormat to make sure a BigDecimal
				// is returned so use toBigInteger on that class
				return ((BigDecimal) n).toBigInteger();
			else if (this.numberClass == Long.class)
				return new Long(n.longValue());
			else if (this.numberClass == Integer.class)
				return new Integer(n.intValue());
			else if (this.numberClass == Short.class)
				return new Short(n.shortValue());
			else if (this.numberClass == Byte.class)
				return new Byte(n.byteValue());
			return null;
		}
		catch (Exception pe) {
			log.error("Error:  " + getText() + " is not a number.", pe);
			return null;
		}
	}

	/**
	 * Format the number and show it.
	 *
	 * @param number Number to set.
	 */
	public void setValue(Number number) {
		String txt = null;
		if (number != null) {
			txt = this.format.format(number);
		}
		setText(txt);
	}

	/**
	 * Set text internally: will change text but not fire any event.
	 *
	 * @param s Text to set.
	 */
	private void setTextInternally(String s) {
		internallySettingText = true;
		setText(s);
		internallySettingText = false;
	}

	/**
	 * <p>
	 * When inputField gets focus, the contents will switch to "edit"-format
	 * (=unformat). In most cases a format without all decorations, just the
	 * number. In addition a selectAll() will be done.
	 * </p>
	 *
	 * TODO check if selectAll() is appropriate in all cases.
	 *
	 * <p>
	 * When inputField loses focus, the contents will switch to "read"-format
	 * (=format). This will probably contain some decorations.
	 * </p>
	 */
	class FormatFocusListener implements FocusListener {

		/**
		 * Focus gained: "edit"-format and selectAll.
		 */
		public void focusGained(FocusEvent e) {
			String s = getText();
			setTextInternally(format(unformat, format, s));
			selectAll();
		}

		/**
		 * Focus lost: "read"-format.
		 */
		public void focusLost(FocusEvent e) {
			String s = getText();
			setTextInternally(format(format, unformat, s));
		}

		/**
		 * Format a string.
		 *
		 * @param toFormat Change to this format.
		 * @param fromFormat Current format to be changed.
		 * @param s String to be reformatted.
		 * @return String which holds the number in the new format.
		 */
		private String format(NumberFormat toFormat, NumberFormat fromFormat, String s) {
			if (!"".equals(s)) {
				try {
					return toFormat.format(fromFormat.parse(s));
				}
				catch (ParseException pe) {
					log.error("Fout: De ingevulde waarde " + getText() + " is geen nummer.", pe);
				}
			}
			return null;
		}
	}

	/**
	 * Specific document that allows only input of numbers, decimal separator
	 * (or alternative) and sign. Maximum number of decimals/non-decimals will
	 * be respected at all times. Signing can be changed anywhere in the
	 * inputField by simply clicking +/-. Decimal separator input can be done
	 * with alternative character to allow both comma and point.
	 *
	 * @author jh
	 */
	class BigDecimalDocument extends PlainDocument {

		private final int nrOfNonDecimals;

		private final int nrOfDecimals;

		private final boolean negativeSign;

		private final char decimalSeparator = symbols.getDecimalSeparator();

		private final char alternativeSeparator;

		/**
		 * @see #BigDecimalDocument(int, int, boolean, char)
		 */
		public BigDecimalDocument() {
			this(10, 2, true);
		}

		/**
		 * @see #BigDecimalDocument(int, int, boolean, char)
		 */
		public BigDecimalDocument(int nrOfNonDecimals, int nrOfDecimals, boolean negativeSign) {
			this(nrOfNonDecimals, nrOfDecimals, negativeSign, symbols.getGroupingSeparator());
		}

		/**
		 * Constructor with several configurations. Alternative separator can be
		 * given in order to make input easier. Eg. Comma and point can be used
		 * for decimal separation.
		 *
		 * @param nrOfNonDecimals Maximum number of non-decimals.
		 * @param nrOfDecimals Maximum number of decimals.
		 * @param negativeSign Negative sign allowed.
		 * @param alternativeSeparator Alternative separator.
		 */
		public BigDecimalDocument(int nrOfNonDecimals, int nrOfDecimals, boolean negativeSign, char alternativeSeparator) {
			this.nrOfNonDecimals = nrOfNonDecimals;
			this.nrOfDecimals = nrOfDecimals;
			this.negativeSign = negativeSign;
			this.alternativeSeparator = alternativeSeparator;
		}

		/**
		 * Handles string insertion, checks several things like number of
		 * non-decimals/decimals/sign...
		 *
		 * @inheritDoc
		 */
		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
			// first doing the single keys, then review what can be used for
			// cut/paste actions
			if ("-".equals(str)) {
				if (this.negativeSign) // set - or flip to + if it's already
				// there
				{
					if ((this.getLength() == 0) || !this.getText(0, 1).equals("-"))
						super.insertString(0, str, a);
					else if (!(this.getLength() == 0) && this.getText(0, 1).equals("-"))
						super.remove(0, 1);
					fireUserInputChange();
				}
				return;
			}
			else if ("+".equals(str)) {
				if (this.negativeSign && (!(this.getLength() == 0) && this.getText(0, 1).equals("-"))) {
					super.remove(0, 1);
					fireUserInputChange();
				}
				return;
			}
            else if (isShortCut(str))
            {
                handleShortCut(str, offset, a);
                return;
            }
			// check decimal signs
			else if ((str.length() == 1)
					&& ((this.alternativeSeparator == str.charAt(0)) || (this.decimalSeparator == str.charAt(0)))) {
				if ((nrOfDecimals > 0) && (nrOfDecimals >= (getLength() - offset))
						&& (getText(0, getLength()).indexOf(this.decimalSeparator) == -1)) {
					super.insertString(offset, Character.toString(this.decimalSeparator), a);
					fireUserInputChange();
				}
				return;
			}
			String s = getText(0, offset) + str;
			if (offset < getLength()) {
				s += getText(offset, getLength() - offset);
			}

			boolean isNegative = s.startsWith("-");
			char[] sarr = isNegative ? s.substring(1).toCharArray() : s.toCharArray();
			int sep = -1;
			int numberLength = 0; // count numbers, no special characters
			for (int i = 0; i < sarr.length; i++) {
				if (sarr[i] == this.decimalSeparator) {
					if (sep != -1) {// double decimalseparator??
						log
								.warn("Error while inserting string: " + s + "[pos=" + i + "]"
										+ " Double decimalseparator?");
						return;
					}
					sep = i;
					if (numberLength > this.nrOfNonDecimals) {// too many
						// digits left
						// of decimal
						// separator
						log.warn("Error while inserting string: " + s + "[pos=" + i + "]" + " Too many non decimals? ["
								+ this.nrOfNonDecimals + "]");
						return;
					}
					else if ((sarr.length - sep - 1) > this.nrOfDecimals) {// too
						// many
						// digits
						// right
						// of
						// decimal
						// separator
						log.warn("Error while inserting string: " + s + "[pos=" + i + "]" + " Too many decimals? ["
								+ this.nrOfDecimals + "]");
						return;
					}
				}
				else if (sarr[i] == symbols.getGroupingSeparator()) {
					// ignore character
				}
				else if (!Character.isDigit(sarr[i])) {// non digit, no
					// grouping/decimal
					// separator not allowed
					log.warn("Error while inserting string: " + s + "[pos=" + i + "]"
							+ " String contains character that is no digit or separator?");
					return;
				}
				else
					++numberLength;
			}
			if ((sep == -1) && (numberLength > this.nrOfNonDecimals)) {// no
				// separator,
				// number
				// too
				// big
				log.warn("Error while inserting string: " + s + " Too many non decimals? [" + this.nrOfNonDecimals
						+ "]");
				return;
			}
			super.insertString(offset, str, a);
			fireUserInputChange();
		}

        private void handleShortCut(String str, int offset, AttributeSet a) throws BadLocationException
        {
            log.debug("handing shortcut " + str);
            if (getLength() == 0)
            {
                if (str.equals("k"))
                {
                    super.insertString(0, "1000", a);
                }
                else if (str.equals("m"))
                {
                    super.insertString(0, "1000000", a);
                }
                else if (str.equals("b"))
                {
                    super.insertString(0, "1000000000", a);
                }
            }
            else if (getLength() == 1 && (getText(0, 1).equals("-") || getText(0, 1).equals("+")))
            {
            }
            else
            {
                String text = getText(0, offset);
                text = text.replace(',', '.');
                BigDecimal dec = new BigDecimal(text);
                if (str.equals("k"))
                {
                    dec = dec.scaleByPowerOfTen(3);
                }
                else if (str.equals("m"))
                {
                    dec = dec.scaleByPowerOfTen(6);
                }
                else if (str.equals("b"))
                {
                    dec = dec.scaleByPowerOfTen(9);
                }
                super.remove(0, offset);
                String outcome = dec.toBigIntegerExact().toString();
                outcome = outcome.replace('.', decimalSeparator);
                super.insertString(0, outcome, a);
                fireUserInputChange();
            }

        }

        private boolean isShortCut(String str)
        {
            return str.equals("k") || str.equals("m") || str.equals("b");
        }

		/**
		 * Will trigger the UserInputListeners once after removing.
		 *
		 * @inheritDoc
		 */
		public void remove(int offs, int len) throws BadLocationException {
			super.remove(offs, len);
			fireUserInputChange();
		}

		/**
		 * Will trigger the UserInputListeners once after replacing.
		 *
		 * @inheritDoc
		 */
		public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			boolean oldInternallySettingText = internallySettingText;
			internallySettingText = true;
			super.replace(offset, length, text, attrs);
			internallySettingText = oldInternallySettingText;
			fireUserInputChange();
		}
	}

	/**
	 * @return Returns the scale.
	 */
	public Integer getScale() {
		return scale;
	}

	/**
	 * @param scale The scale to set.
	 */
	public void setScale(Integer scale) {
		this.scale = scale;
	}
}

