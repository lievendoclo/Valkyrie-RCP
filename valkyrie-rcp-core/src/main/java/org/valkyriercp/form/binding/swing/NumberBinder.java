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
        super(BigDecimal.class);
    }

    /**
     * Constructor taking the requiredSourceClass for this binder.
     *
     * @param requiredSourceClass
     *            Required source class.
     */
    public NumberBinder(Class requiredSourceClass)
    {
        super(requiredSourceClass);
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
        if(context.containsKey("format")) {
            format = (String) context.get("format");
        } else {
            format = this.format;
        }
        if(context.containsKey("unformat")) {
            unformat = (String) context.get("unformat");
        } else {
            unformat = this.unformat;
        }
        if(context.containsKey("scale")) {
            scale = (Integer) context.get("scale");
        } else {
            scale = this.scale;
        }
        if(context.containsKey("nrOfNonDecimals")) {
            nrOfNonDecimals = (Integer) context.get("nrOfNonDecimals");
        } else {
            nrOfNonDecimals = this.nrOfNonDecimals;
        }
        if(context.containsKey("nrOfDecimals")) {
            nrOfDecimals = (Integer) context.get("nrOfDecimals");
        } else {
            nrOfDecimals = this.nrOfDecimals;
        }
        if(context.containsKey("negativeSign")) {
            negativeSign = (Boolean) context.get("negativeSign");
        } else {
            negativeSign = this.negativeSign;
        }
        if(context.containsKey("alignment")) {
            alignment = (Integer) context.get("alignment");
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

        if(context.containsKey("nrOfNonDecimals")) {
            nrOfNonDecimals = (Integer) context.get("nrOfNonDecimals");
        } else {
            nrOfNonDecimals = this.nrOfNonDecimals;
        }
        if(context.containsKey("nrOfDecimals")) {
            nrOfDecimals = (Integer) context.get("nrOfDecimals");
        } else {
            nrOfDecimals = this.nrOfDecimals;
        }
        if(context.containsKey("readOnly")) {
            readOnly = (Boolean) context.get("readOnly");
        } else {
            readOnly = this.readOnly;
        }
        if(context.containsKey("leftDecoration")) {
            leftDecoration = (String) context.get("leftDecoration");
        } else {
            leftDecoration = this.leftDecoration;
        }
        if(context.containsKey("rightDecoration")) {
            rightDecoration = (String) context.get("rightDecoration");
        } else {
            rightDecoration = this.rightDecoration;
        }
        if(context.containsKey("shiftFactor")) {
            shiftFactor = (BigDecimal) context.get("shiftFactor");
        } else {
            shiftFactor = this.shiftFactor;
        }

        return new NumberBinding(getRequiredSourceClass(), (BigDecimalTextField) control, readOnly,
                leftDecoration, rightDecoration, shiftFactor, nrOfDecimals
                + nrOfNonDecimals, formModel, formPropertyPath);
    }

}
