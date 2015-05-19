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
        numberBinder.setLeftDecoration("???");
        numberBinder.setFormat("#,##0.00");
        return numberBinder;
    }
}
