package org.valkyriercp.sample.showcase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.swing.NumberBinder;

import java.math.BigDecimal;

@Configuration
public class ShowcaseBinders {
    @Bean
    public Binder integerBinder() {
        return new NumberBinder(Integer.class);
    }

    @Bean
    public Binder euroBinder() {
        NumberBinder numberBinder = new NumberBinder(BigDecimal.class);
        numberBinder.setNrOfDecimals(2);
        numberBinder.setLeftDecoration("€");
        numberBinder.setFormat("#,##0.00");
        return numberBinder;
    }
}
