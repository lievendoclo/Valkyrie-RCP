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

import javax.swing.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Factory that creates a hashmap for use with a JSlider and its labelMap.
 * @author ldo
 */
public class SliderLabelFactory
{
    private Map<Integer, String> labels;

    /**
     * Creates a new SliderLabelFactory
     */
    public SliderLabelFactory()
    {
        labels = new HashMap<Integer, String>();
    }

    /**
     * Sets the string labels for specific values.
     */
    public void setLabels(Map<Integer, String> labels)
    {
        this.labels = labels;
    }

    /**
     * Gets a map with integer values with the corresponding JLabel
     * for that value
     */
    public Hashtable<Integer, JLabel> getSliderLabels()
    {
        Hashtable<Integer, JLabel> dict = new Hashtable<Integer, JLabel>();
        for(Map.Entry<Integer, String> entry : labels.entrySet())
        {
            dict.put(entry.getKey(), new JLabel(entry.getValue()));
        }
        return dict;
    }

}
