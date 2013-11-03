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
