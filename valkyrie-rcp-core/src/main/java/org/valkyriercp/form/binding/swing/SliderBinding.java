package org.valkyriercp.form.binding.swing;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.support.CustomBinding;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Binding for integers that show a slider. Can use a {@link SliderLabelFactory} for custom labels. If a
 * {@link SliderLabelFactory} is not present and the majorTickValue is set, the binding will create it's own
 * labels based on that value.
 *
 * @author ldo
 *
 */
public class SliderBinding extends CustomBinding
{

    private JSlider slider;
    private boolean readOnly;

    private SliderLabelFactory sliderLabelFactory;
    private int maxSpacing;

    /**
     * Creates a new binding
     *
     * @param formModel
     * @param formPropertyPath
     */
    public SliderBinding(FormModel formModel, String formPropertyPath)
    {
        super(formModel, formPropertyPath, Integer.class);
        slider = new JSlider();
        slider.addChangeListener(new ChangeListener()
        {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                controlValueChanged(slider.getValue());
            }
        });
    }

    @Override
    protected void valueModelChanged(Object newValue)
    {
        slider.setValue((Integer) newValue);
        readOnlyChanged();

    }

    @Override
    protected JComponent doBindControl()
    {
        if (sliderLabelFactory == null)
        {
            slider.createStandardLabels(maxSpacing);
        }
        else
        {
            slider.setLabelTable(sliderLabelFactory.getSliderLabels());
        }
        slider.setPaintLabels(true);
        slider.setValue((Integer) getValue());
        return slider;
    }

    @Override
    protected void enabledChanged()
    {
        this.slider.setEnabled(isEnabled());
        readOnlyChanged();

    }

    @Override
    protected void readOnlyChanged()
    {
        this.slider.setEnabled(isEnabled() && !isReadOnly() && !readOnly);
    }

    /**
     * Set the maximum value of the slider
     */
    public void setMaxValue(int maxValue)
    {
        slider.setMaximum(maxValue);
    }

    /**
     * Set the minimum value of the slider
     */
    public void setMinValue(int minValue)
    {
        slider.setMinimum(minValue);
    }

    /**
     * Set the major tick spacing of the slider
     */
    public void setMajorTickSpacing(int spacing)
    {
        this.maxSpacing = spacing;
        slider.setMajorTickSpacing(spacing);

    }

    /**
     * Set the minor tick spacing of the slider
     */
    public void setMinorTickSpacing(int spacing)
    {
        slider.setMinorTickSpacing(spacing);
    }

    /**
     * Set the factory for the custom labels
     */
    public void setSliderLabelFactory(SliderLabelFactory sliderLabelFactory)
    {
        this.sliderLabelFactory = sliderLabelFactory;
    }

}
