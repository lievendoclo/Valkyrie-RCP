package org.valkyriercp.form.binding.swing;

import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.component.BigDecimalTextField;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.support.AbstractBinder;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * <p>Binder for numeric fields. Constructs a {@see org.springframework.richclient.form.binding.swing.NumberBinding} which holds
 * a special inputfield {@see org.springframework.richclient.swing.BigDecimalTextField}.</p>
 *
 * <p>This binder comes with a set of configuration properties which makes this easy reusable.</p>
 *
 * <p>
 * Examples:
 * <pre>
 * &lt;bean id="euroBinder" class="org.springframework.richclient.form.binding.swing.NumberBinder" lazy-init="true"&gt;
 *    &lt;property name="format"&gt;
 *     &lt;value&gt;###,###,###,##0.00&lt;/value&gt;
 *   &lt;/property&gt;
 *   &lt;property name="nrOfDecimals"&gt;
 *     &lt;value type="int"&gt;2&lt;/value&gt;
 *   &lt;/property&gt;
 *   &lt;property name="leftDecoration"&gt;
 *     &lt;value&gt;&#x20ac;&lt;/value&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 *
 * <pre>
 * &lt;bean id="percentageBinder" class="org.springframework.richclient.form.binding.swing.NumberBinder" lazy-init="true"&gt;
 *   &lt;property name="nrOfNonDecimals"&gt;
 *     &lt;value type="int"&gt;3&lt;/value&gt;
 *   &lt;/property&gt;
 *   &lt;property name="nrOfDecimals"&gt;
 *     &lt;value type="int"&gt;4&lt;/value&gt;
 *   &lt;/property&gt;
 *   &lt;property name="rightDecoration"&gt;
 *     &lt;value&gt;%&lt;/value&gt;
 *   &lt;/property&gt;
 *   &lt;property name="shiftFactor"&gt;
 *     &lt;value type="java.math.BigDecimal"&gt;100&lt;/value&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * </p>
 *
 * TODO it might be better to get the number of decimals/nonDecimals from the format
 *
 * @author jh
 *
 */
public class NumberBinder extends AbstractBinder
{

    public static final String FORMAT_KEY = "format";
    public static final String UNFORMAT_KEY = "unformat";
    public static final String SCALE_KEY = "scale";
    public static final String NR_OF_NON_DECIMALS_KEY = "nrOfNonDecimals";
    public static final String NR_OF_DECIMALS_KEY = "nrOfDecimals";
    public static final String NEGATIVE_SIGN_KEY = "negativeSign";
    public static final String ALIGNMENT_KEY = "alignment";
    public static final String READ_ONLY_KEY = "readOnly";
    public static final String LEFT_DECORATION_KEY = "leftDecoration";
    public static final String RIGHT_DECORATION_KEY = "rightDecoration";
    public static final String SHIFT_FACTOR_KEY = "shiftFactor";
    protected boolean readOnly = false;

    protected String format = null;

    protected String unformat = null;

    protected int nrOfDecimals = 2;

    protected int nrOfNonDecimals = 10;

    protected boolean negativeSign = true;

    protected String leftDecoration = null;

    protected String rightDecoration = null;

    // actual displayed value is multiplied/divided inner value
    protected BigDecimal shiftFactor = null;

    protected Integer scale = null;

    protected int alignment = SwingConstants.RIGHT;

    /**
     * <p>Default constructor.</p>
     *
     * <p>Sets BigDecimal as requiredSourceClass.</p>
     */
    public NumberBinder() {
        this(BigDecimal.class);
    }

    /**
     * Constructor taking the requiredSourceClass for this binder.
     *
     * @param requiredSourceClass
     *            Required source class.
     */
    public NumberBinder(Class requiredSourceClass)
    {
        super(requiredSourceClass, new String[] {FORMAT_KEY, UNFORMAT_KEY, SCALE_KEY, NR_OF_DECIMALS_KEY, NR_OF_NON_DECIMALS_KEY,
        NEGATIVE_SIGN_KEY, ALIGNMENT_KEY, READ_ONLY_KEY, LEFT_DECORATION_KEY, RIGHT_DECORATION_KEY,
        SHIFT_FACTOR_KEY});
    }

    /**
     * Force this inputField to be readOnly.
     *
     * @param readOnly
     */
    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    /**
     * Set a decoration to the left of the inputField.
     *
     * @param leftDecoration
     *            Decoration to be placed.
     */
    public void setLeftDecoration(String leftDecoration)
    {
        this.leftDecoration = leftDecoration;
    }

    /**
     * Set a decoration to the right of the inputField.
     *
     * @param rightDecoration
     *            Decoration to be placed.
     */
    public void setRightDecoration(String rightDecoration)
    {
        this.rightDecoration = rightDecoration;
    }

    /**
     * Format that will be used to show this number.
     *
     * @param format
     *            NumberFormat.
     */
    public void setFormat(String format)
    {
        this.format = format;
    }

    /**
     * <p>
     * Format that will be used when user is editing the field.
     * </p>
     *
     * <p>
     * eg. when inputField gets focus, all formatting can be disabled. If focus
     * is shifted, number will be formatted.
     * </p>
     *
     * @param unformat
     *            NumberFormat
     */
    public void setUnformat(String unformat)
    {
        this.unformat = unformat;
    }

    /**
     * Maximum number of decimals.
     *
     * @param nrOfDecimals
     */
    public void setNrOfDecimals(int nrOfDecimals)
    {
        this.nrOfDecimals = nrOfDecimals;
    }

    /**
     * Maximum number of non-decimals.
     *
     * @param nrOfNonDecimals
     */
    public void setNrOfNonDecimals(int nrOfNonDecimals)
    {
        this.nrOfNonDecimals = nrOfNonDecimals;
    }

    /**
     * Allow negative numbers. Default is <code>true</code>.
     *
     * @param negativeSign
     *            True if negative numbers are used.
     */
    public void setNegativeSign(boolean negativeSign)
    {
        this.negativeSign = negativeSign;
    }

    /**
     * <p>
     * BigDecimals can be shifted right/left when storing.
     * </p>
     *
     * <p>
     * Eg. percentages may be shown as <code>###.##</code> and saved as
     * <code>#.####</code>.
     * </p>
     *
     * @param shiftFactor
     *            Factor to shift number when saved.
     */
    public void setShiftFactor(BigDecimal shiftFactor)
    {
        Assert.isTrue(getRequiredSourceClass() == BigDecimal.class);
        // Only BigDecimal's can divide safely
        this.shiftFactor = shiftFactor;
    }

    /**
     * Enforce a given scale for the result.
     *
     * @param scale
     *            The scale to set.
     *
     * @see BigDecimal#setScale(int)
     */
    public void setScale(Integer scale) {
        this.scale = scale;
    }

    /**
     * Sets the horizontal aligment of the BigDecimalTextField. Default is SwingConstants.RIGHT.
     *
     * @param alignment
     *            Horizontal alignment to set.
     */
    public void setAlignment(int alignment)
    {
        this.alignment = alignment;
    }

    /**
     * @inheritDoc
     */
    protected JComponent createControl(Map context) {
        BigDecimalTextField component = null;
        String format;
        String unformat;
        Integer scale;
        Integer nrOfNonDecimals;
        Integer nrOfDecimals;
        Boolean negativeSign;
        Integer alignment;
        if(context.containsKey(FORMAT_KEY)) {
            format = (String) context.get(FORMAT_KEY);
        } else {
            format = this.format;
        }
        if(context.containsKey(UNFORMAT_KEY)) {
            unformat = (String) context.get(UNFORMAT_KEY);
        } else {
            unformat = this.unformat;
        }
        if(context.containsKey(SCALE_KEY)) {
            scale = (Integer) context.get(SCALE_KEY);
        } else {
            scale = this.scale;
        }
        if(context.containsKey(NR_OF_NON_DECIMALS_KEY)) {
            nrOfNonDecimals = (Integer) context.get(NR_OF_NON_DECIMALS_KEY);
        } else {
            nrOfNonDecimals = this.nrOfNonDecimals;
        }
        if(context.containsKey(NR_OF_DECIMALS_KEY)) {
            nrOfDecimals = (Integer) context.get(NR_OF_DECIMALS_KEY);
        } else {
            nrOfDecimals = this.nrOfDecimals;
        }
        if(context.containsKey(NEGATIVE_SIGN_KEY)) {
            negativeSign = (Boolean) context.get(NEGATIVE_SIGN_KEY);
        } else {
            negativeSign = this.negativeSign;
        }
        if(context.containsKey(ALIGNMENT_KEY)) {
            alignment = (Integer) context.get(ALIGNMENT_KEY);
        } else {
            alignment = this.alignment;
        }

        if (format == null) {
            component = new BigDecimalTextField(nrOfNonDecimals,
                    nrOfDecimals, negativeSign, getRequiredSourceClass());
        }
        if (component == null && unformat == null) {
            component = new BigDecimalTextField(nrOfNonDecimals,
                    nrOfDecimals, negativeSign, getRequiredSourceClass(),
                    new DecimalFormat(format));
        }

        if (component == null) {
            component = new BigDecimalTextField(nrOfNonDecimals,
                    nrOfDecimals, negativeSign, getRequiredSourceClass(),
                    new DecimalFormat(format), new DecimalFormat(unformat));
        }

        if (scale != null) {
            component.setScale(scale);
        }

        component.setHorizontalAlignment(alignment);

        return component;
    }

    /**
     * @inheritDoc
     */
    protected Binding doBind(JComponent control, FormModel formModel,
                             String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof BigDecimalTextField,
                "Control must be an instance of BigDecimalTextField.");
        Integer nrOfNonDecimals;
        Integer nrOfDecimals;
        Boolean readOnly;
        String leftDecoration;
        String rightDecoration;
        BigDecimal shiftFactor;

        if(context.containsKey(NR_OF_NON_DECIMALS_KEY)) {
            nrOfNonDecimals = (Integer) context.get(NR_OF_NON_DECIMALS_KEY);
        } else {
            nrOfNonDecimals = this.nrOfNonDecimals;
        }
        if(context.containsKey(NR_OF_DECIMALS_KEY)) {
            nrOfDecimals = (Integer) context.get(NR_OF_DECIMALS_KEY);
        } else {
            nrOfDecimals = this.nrOfDecimals;
        }
        if(context.containsKey(READ_ONLY_KEY)) {
            readOnly = (Boolean) context.get(READ_ONLY_KEY);
        } else {
            readOnly = this.readOnly;
        }
        if(context.containsKey(LEFT_DECORATION_KEY)) {
            leftDecoration = (String) context.get(LEFT_DECORATION_KEY);
        } else {
            leftDecoration = this.leftDecoration;
        }
        if(context.containsKey(RIGHT_DECORATION_KEY)) {
            rightDecoration = (String) context.get(RIGHT_DECORATION_KEY);
        } else {
            rightDecoration = this.rightDecoration;
        }
        if(context.containsKey(SHIFT_FACTOR_KEY)) {
            shiftFactor = (BigDecimal) context.get(SHIFT_FACTOR_KEY);
        } else {
            shiftFactor = this.shiftFactor;
        }

        return new NumberBinding(getRequiredSourceClass(), (BigDecimalTextField) control, readOnly,
                leftDecoration, rightDecoration, shiftFactor, nrOfDecimals
                + nrOfNonDecimals, formModel, formPropertyPath);
    }

}
