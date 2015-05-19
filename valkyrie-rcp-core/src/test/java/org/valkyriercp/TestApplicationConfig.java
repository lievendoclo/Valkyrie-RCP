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
