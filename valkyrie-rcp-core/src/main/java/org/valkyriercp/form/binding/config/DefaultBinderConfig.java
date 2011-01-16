package org.valkyriercp.form.binding.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.BinderSelectionStrategy;
import org.valkyriercp.form.binding.swing.*;
import org.valkyriercp.form.binding.swing.date.JXDatePickerDateFieldBinder;

import javax.swing.*;

@Configuration
public class DefaultBinderConfig {

    @Autowired
    private BinderSelectionStrategy binderSelectionStrategy;


}
