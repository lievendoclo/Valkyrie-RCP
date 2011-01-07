package org.valkyriercp.application.config.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.valkyriercp.application.Application;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ApplicationWindowConfigurer;
import org.valkyriercp.application.StatusBar;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.support.DefaultStatusBar;
import org.valkyriercp.command.support.CommandGroup;

@Component
public abstract class AbstractApplicationLifecycleAdvisor implements ApplicationLifecycleAdvisor {
    @Autowired
    protected Application application;

    @Autowired
    protected ApplicationContext applicationContext;

	private ApplicationWindow openingWindow;

    protected String startingPageId;

    @Override
    public String getStartingPageId() {
        return startingPageId;
    }

    public void setStartingPageId(String startingPageId) {
        this.startingPageId = startingPageId;
    }

    @Override
    public void onPreStartup() {}

    @Override
    public void onPostStartup() {}

    @Override
    public void onWindowCreated(ApplicationWindow window) {}

    @Override
    public void onWindowOpened(ApplicationWindow window) {}

    @Override
    public boolean onPreWindowClose(ApplicationWindow window) { return true; }

    /**
	 * Hook called right before the application opens a window.
	 *
	 * @param configurer
	 */
	@Override
    public void onPreWindowOpen(ApplicationWindowConfigurer configurer) {
		configurer.setTitle(application.getName());
		configurer.setImage(application.getImage());
	}

    public ApplicationWindow getOpeningWindow() {
        return openingWindow;
    }

    public void setOpeningWindow(ApplicationWindow openingWindow) {
        this.openingWindow = openingWindow;
    }

    @Override
    public void onCommandsCreated(ApplicationWindow window) {}

    /**
	 * Create the menuBar for the application.
	 *
	 * @return a CommandGroup.
	 */
	public CommandGroup getMenuBarCommandGroup() {
		return new CommandGroup();
	}

	/**
	 * Create the toolBar for the application.
	 *
	 * @return a CommandGroup.
	 */
	public CommandGroup getToolBarCommandGroup() {
		return new CommandGroup();
	}

    /**
	 * Create the statusBar for the application.
	 *
	 * @return a statusBar.
	 */
	public StatusBar getStatusBar() {
		return new DefaultStatusBar();
	}

    @Override
    public void onShutdown() { }
}
