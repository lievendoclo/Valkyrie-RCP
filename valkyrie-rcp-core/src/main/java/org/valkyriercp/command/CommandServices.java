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
package org.valkyriercp.command;

import org.valkyriercp.command.config.CommandButtonConfigurer;
import org.valkyriercp.factory.ButtonFactory;
import org.valkyriercp.factory.ComponentFactory;
import org.valkyriercp.factory.MenuFactory;

public interface CommandServices {
    public ComponentFactory getComponentFactory();

	public ButtonFactory getToolBarButtonFactory();

    public ButtonFactory getButtonFactory();

    public MenuFactory getMenuFactory();

    public CommandButtonConfigurer getDefaultButtonConfigurer();

    public CommandButtonConfigurer getToolBarButtonConfigurer();

    public CommandButtonConfigurer getMenuItemButtonConfigurer();

    public CommandButtonConfigurer getPullDownMenuButtonConfigurer();
}
