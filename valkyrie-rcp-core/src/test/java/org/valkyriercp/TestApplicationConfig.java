package org.valkyriercp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;

@Configuration
public class TestApplicationConfig extends AbstractApplicationConfig {
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        ObjectPostProcessor<Object> objectPostProcessor = new ObjectPostProcessor<Object>() {
            public <T> T postProcess(T object) {
                return object;
            }
        };
        return new AuthenticationManagerBuilder(objectPostProcessor)
                .inMemoryAuthentication().withUser("user").password("user")
                .roles("USER").and().withUser("admin").password("admin")
                .roles("ADMIN").and().and().build();
    }
}
