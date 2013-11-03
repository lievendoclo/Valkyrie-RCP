package org.valkyriercp.form.binding.swing;

import net.miginfocom.swing.MigLayout;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.component.BigDecimalTextField;
import org.valkyriercp.component.UserInputListener;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;
import java.math.BigDecimal;

/**
 * Binding to handle Numbers. Can be configured in different with shiftFactor/shiftScale and
 * decorations.
 *
 * @author jh
 *
 */
public class NumberBinding extends CustomBinding implements UserInputListener
{

    protected final BigDecimalTextField numberField;

    protected final boolean readOnly;

    private final String leftDecoration;

    private final String rightDecoration;

    private final BigDecimal shiftFactor;

    private int shiftScale;

    private boolean isSettingValue = false;

    /**
     * Creates a NumberBinding.
     *
     * @param requiredClass
     *            Required class for this binding.
     * @param component
     *            The BigDecimalTextField to use.
     * @param readOnly
     *            Force readonly at all times.
     * @param leftDecoration
     *            Decorating label with string at left side.
     * @param rightDecoration
     *            Decorating label with string at right side.
     * @param shiftFactor
     *            Shifting factor to use when setting/getting number in
     *            inputfield. Can eg be used to display percentages as ###.##
     *            instead of #.####.
     * @param shiftScale
     *            Scale to set on BigDecimal.
     * @param formModel
     *            FormModel.
     * @param formPropertyPath
     *            PropertyPath.
     */
    public NumberBinding(Class requiredClass, BigDecimalTextField component, boolean readOnly,
            String leftDecoration, String rightDecoration, BigDecimal shiftFactor, int shiftScale,
            FormModel formModel, String formPropertyPath)
    {
        super(formModel, formPropertyPath, requiredClass);
        this.numberField = component;
        this.readOnly = readOnly;
        this.leftDecoration = leftDecoration;
        this.rightDecoration = rightDecoration;
        this.shiftFactor = shiftFactor;
        this.shiftScale = shiftScale;
    }

    /**
     * @inheritDoc
     */
    protected void valueModelChanged(Object newValue)
    {
        this.isSettingValue = true;
        if ((this.shiftFactor != null) && (newValue != null)) // if shifting, class is BigDecimal
            this.numberField.setValue(((BigDecimal) newValue).multiply(this.shiftFactor));
        else
            this.numberField.setValue((Number) newValue);
        readOnlyChanged();
        this.isSettingValue = false;
    }

    /**
     * @inheritDoc
     */
    protected JComponent doBindControl()
    {
        valueModelChanged(getValue());
        this.numberField.addUserInputListener(this);
        if ((this.leftDecoration == null) && (this.rightDecoration == null))
            return this.numberField;

        return createPanelWithDecoration();
    }

    /**
     * @inheritDoc
     */
    public void update(JComponent component)
    {
        if (!this.isSettingValue && NumberBinding.this.numberField.isEditable())
        {
            Number value = NumberBinding.this.numberField.getValue();
            if ((value != null) && (NumberBinding.this.shiftFactor != null))
                NumberBinding.this.controlValueChanged(((BigDecimal) value).divide(NumberBinding.this.shiftFactor,
                        NumberBinding.this.shiftScale, BigDecimal.ROUND_UP));
            else
                NumberBinding.this.controlValueChanged(value);
        }
    }

    /**
     * Create a panel with (possibly) decorations on both sides.
     *
     * TODO This leaves one problem: when validating and eg coloring/adding overlay the
     * panel is used instead of the inputfield. There could be an interface that
     * returns the correct component to be handled while validating.
     *
     * @return a decorated component which contains the inputfield.
     */
    private JComponent createPanelWithDecoration()
    {
        StringBuffer columnLayout = new StringBuffer();
        if (this.leftDecoration != null)
            columnLayout.insert(0, "[]");
        columnLayout.append("[grow]");
        if (this.rightDecoration != null)
            columnLayout.append("[]");

        JPanel panel = new JPanel(new MigLayout("insets 0", columnLayout.toString(), "")) {
            public void requestFocus() {
                NumberBinding.this.numberField.requestFocus();
            }

        };
        if (this.leftDecoration != null)
        {
            panel.add(new JLabel(this.leftDecoration));
        }
        panel.add(this.numberField, "grow");
        if (this.rightDecoration != null)
        {
            panel.add(new JLabel(this.rightDecoration));
        }
        return panel;
    }

    /**
     * @inheritDoc
     */
    protected void readOnlyChanged()
    {
        numberField.setEditable(isEnabled() && !this.readOnly && !isReadOnly());
    }

    /**
     * @inheritDoc
     */
    protected void enabledChanged()
    {
        this.numberField.setEnabled(isEnabled());
        readOnlyChanged();
    }
}

