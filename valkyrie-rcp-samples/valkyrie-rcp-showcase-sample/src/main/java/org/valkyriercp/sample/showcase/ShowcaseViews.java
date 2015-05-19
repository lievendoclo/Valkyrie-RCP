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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.valkyriercp.sample.showcase.widget.BinderDemoWidget;
import org.valkyriercp.widget.HTMLViewWidget;
import org.valkyriercp.widget.Widget;
import org.valkyriercp.widget.WidgetProvider;
import org.valkyriercp.widget.WidgetViewDescriptor;

@Configuration
public class ShowcaseViews {

    @Bean
    public WidgetViewDescriptor startView() {
        return new WidgetViewDescriptor("startView", new WidgetProvider<Widget>() {
            @Override
            public Widget getWidget() {
                return new HTMLViewWidget(new ClassPathResource("/org/valkyriercp/sample/showcase/html/start.html"));
            }
        });
    }

    @Bean
    public WidgetViewDescriptor binderDemoView() {
        return new WidgetViewDescriptor("binderDemoView", new WidgetProvider<Widget>() {
            @Override
            public Widget getWidget() {
                return binderDemoWidget();
            }
        });
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    @Lazy
    public Widget binderDemoWidget() {
        return new BinderDemoWidget();
    }
}
