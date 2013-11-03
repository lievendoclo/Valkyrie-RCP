package org.valkyriercp.form.binding.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.form.binding.BinderSelectionStrategy;

@Configuration
public class DefaultBinderConfig {

    @Autowired
    private BinderSelectionStrategy binderSelectionStrategy;


}
