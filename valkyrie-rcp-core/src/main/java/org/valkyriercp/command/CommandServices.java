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
