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

import org.valkyriercp.component.JideOssComponentFactory;
import org.valkyriercp.component.JideOverlayService;
import org.valkyriercp.component.OverlayService;
import org.valkyriercp.factory.ComponentFactory;
import org.valkyriercp.form.binding.BindingFactoryProvider;
import org.valkyriercp.form.binding.jide.JideBindingFactoryProvider;

public abstract class AbstractJideOssApplicationConfig extends AbstractApplicationConfig {
    @Override
    public ComponentFactory componentFactory() {
        return new JideOssComponentFactory();
    }

    @Override
    public BindingFactoryProvider bindingFactoryProvider() {
        return new JideBindingFactoryProvider();
    }

    @Override
    public OverlayService overlayService() {
        return new JideOverlayService();
    }
}
