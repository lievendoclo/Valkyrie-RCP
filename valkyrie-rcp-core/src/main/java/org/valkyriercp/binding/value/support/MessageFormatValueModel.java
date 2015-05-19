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
package org.valkyriercp.binding.value.support;

import org.valkyriercp.binding.value.ValueModel;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;

/**
 * A value model that takes a set of "argument" ValueModels, formats their
 * values into strings, and then inserts these formatted strings into the
 * provided pattern at the appropriate places. Any changes to the "argument"
 * ValueModels will cause this value model to also update.
 *
 * @author Oliver Hutchison
 * @see java.text.MessageFormat
 */
public class MessageFormatValueModel extends AbstractDerivedValueModel {

    private final String pattern;

    private final ValueModel[] argumentValueModels;

    private String value;

    /**
     * Constructs a new MessageFormatValueModel with a single argument.
     * @param pattern the pattern
     * @param argumentValueModel the single value model holding the
     * value to be formatted and substituted
     */
    public MessageFormatValueModel(String pattern, ValueModel argumentValueModel) {
        this(pattern, new ValueModel[] {argumentValueModel});
    }

    /**
     * Constructs a new MessageFormatValueModel with a group of arguments.
     * @param pattern the pattern
     * @param argumentValueModels an array of value models holding the
     * value to be formatted and substituted
     */
    public MessageFormatValueModel(String pattern, ValueModel[] argumentValueModels) {
        super((ValueModel[])new HashSet(Arrays.asList(argumentValueModels)).toArray(new ValueModel[0]));
        this.pattern = pattern;
        this.argumentValueModels = argumentValueModels;
        // prime the initial value
        sourceValuesChanged();
    }

    protected void sourceValuesChanged() {
        String oldValue = value;
        value = MessageFormat.format(pattern, getArgumentValues());
        fireValueChange(oldValue, value);
    }

    public Object getValue() {
        return value;
    }

    private Object[] getArgumentValues() {
        Object[] values = new Object[argumentValueModels.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = argumentValueModels[i].getValue();
        }
        return values;
    }
}

