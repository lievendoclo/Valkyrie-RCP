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
package org.valkyriercp.application.config.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.swing.*;
import org.valkyriercp.form.binding.swing.date.JXDatePickerDateFieldBinder;
import org.valkyriercp.form.binding.swing.editor.LookupBinder;

@Configuration
public class DefaultBinderConfig {
    @Bean
    public Binder jxDatePickerDateFieldBinder() {
        return new JXDatePickerDateFieldBinder();
    }

    @Bean
    public Binder checkBoxBinder() {
        return new CheckBoxBinder();
    }

    @Bean
    public Binder formattedTextFieldBinder() {
        return new FormattedTextFieldBinder(null);
    }

    @Bean
    public Binder formattedTextFieldStringBinder() {
        return new FormattedTextFieldBinder(String.class);
    }

    @Bean
    public Binder comboBoxBinder() {
        return new ComboBoxBinder();
    }

    @Bean
    public Binder enumComboBoxBinder() {
        return new EnumComboBoxBinder();
    }

    @Bean
    public Binder labelBinder() {
        return new LabelBinder();
    }

    @Bean
    public Binder numberBinder() {
        return new NumberBinder();
    }

    @Bean
    public Binder textComponentBinder() {
        return new TextComponentBinder();
    }

    @Bean
    public Binder listBinder() {
        return new ListBinder();
    }

    @Bean
    public Binder textAreaBinder() {
        return new TextAreaBinder();
    }

    @Bean
    public Binder toggleButtonBinder() {
        return new ToggleButtonBinder();
    }

    @Bean
    public Binder trueFalseNullBinder() {
        StringSelectionListBinder binder = new StringSelectionListBinder();
        binder.setKeys(StringSelectionListBinder.TRUE_FALSE_NULL);
        binder.setLabelId("trueFalseNullBinder");
        return binder;
    }

    @Bean
    public Binder genericLookupBinder() {
        return new LookupBinder();
    }
}
