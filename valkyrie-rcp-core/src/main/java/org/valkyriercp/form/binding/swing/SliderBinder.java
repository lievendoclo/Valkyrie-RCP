/**
 * Copyright (C) 2015 Valkyrie RCP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.valkyriercp.form.binding.swing;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;
import java.util.Map;

/**
 * Binder class for integer values that displays a slider. Can use a {@link SliderLabelFactory} for custom
 * labels. If a {@link SliderLabelFactory} is not present and the majorTickValue is set, the binding will
 * create it's own labels based on that value.
 *
 * @author ldo
 */
public class SliderBinder implements Binder
{

    private int maxValue;
    private int minValue;
    private boolean readOnly;
    private int maxTickSpacing;

    private SliderLabelFactory sliderLabelFactory;

    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context)
    {
        SliderBinding binding = new SliderBinding(formModel, formPropertyPath);
        binding.setMaxValue(maxValue);
        binding.setMinValue(minValue);
        binding.setReadOnly(readOnly);
        binding.setMajorTickSpacing(maxTickSpacing);
        binding.setSliderLabelFactory(sliderLabelFactory);
        binding.setReadOnly(readOnly);
        return binding;
    }

    @Override
    public Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context)
    {
        throw new UnsupportedOperationException("This binder creates its own component");
    }

    /**
     * Sets the maximum value of the slider
     */
    public void setMaxValue(int maxValue)
    {
        this.maxValue = maxValue;
    }

    /**
     * Sets the minimum value of the slider
     */
    public void setMinValue(int minValue)
    {
        this.minValue = minValue;
    }

    /**
     * Sets whether the control is readonly
     */
    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    /**
     * Sets the major tick value of the slider
     */
    public void setMajorTickSpacing(int maxTickSpacing)
    {
        this.maxTickSpacing = maxTickSpacing;
    }

    /**
     * Set the factory for the custom labels
     */
    public void setSliderLabelFactory(SliderLabelFactory sliderLabelFactory)
    {
        this.sliderLabelFactory = sliderLabelFactory;
    }

}