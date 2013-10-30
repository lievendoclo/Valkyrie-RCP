package org.valkyriercp.application.support;

import java.awt.Image;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import org.valkyriercp.application.Application;
import org.valkyriercp.application.ApplicationDescriptor;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ApplicationWindowFactory;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.application.WindowManager;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.image.ImageSource;
import org.valkyriercp.image.NoSuchImageResourceException;

//@Component
public class DefaultApplication implements Application {
	private static final String DEFAULT_APPLICATION_IMAGE_KEY = "applicationInfo.image";

	@Autowired
	protected ApplicationConfig applicationConfig;

	@Autowired
	protected ApplicationLifecycleAdvisor lifecycleAdvisor;

	@Autowired
	protected ApplicationWindowFactory applicationWindowFactory;

	@Autowired
	protected ApplicationPageFactory applicationPageFactory;

	@Autowired
	protected ApplicationDescriptor applicationDescriptor;

	@Autowired
	private WindowManager windowManager;

	boolean forceShutdown;

	@PostConstruct
	private void postConstruct() {
		windowManager.addObserver(new CloseApplicationObserver());
	}

	@Override
	public void start() {
		lifecycleAdvisor.onPreStartup();
		openWindow(lifecycleAdvisor.getStartingPageDescriptor());
		lifecycleAdvisor.onPostStartup();
	}

	public void openWindow(String pageDescriptorId) {
		ApplicationWindow window = initWindow(createNewWindow());
		applicationConfig.windowManager().setActiveWindow(window);
		if (pageDescriptorId == null) {
			window.showPage(applicationPageFactory.createApplicationPage(
					window, new MultiViewPageDescriptor()));
		} else {
			window.showPage(pageDescriptorId);
		}
	}

	public void openWindow(PageDescriptor pageDescriptor) {
		ApplicationWindow window = initWindow(createNewWindow());
		applicationConfig.windowManager().setActiveWindow(window);
		window.showPage(pageDescriptor);
	}

	protected ApplicationWindow initWindow(ApplicationWindow newWindow) {
		windowManager.add(newWindow);
		return newWindow;
	}

	protected ApplicationWindow createNewWindow() {
		return applicationWindowFactory.createApplicationWindow();
	}

	@Override
	public String getName() {
		if (applicationDescriptor != null
				&& StringUtils.hasText(applicationDescriptor.getDisplayName()))
			return applicationDescriptor.getDisplayName();

		return "Valkyrie RCP Application";
	}

	@Override
	public Image getImage() {
		if (applicationDescriptor != null
				&& applicationDescriptor.getImage() != null)
			return applicationDescriptor.getImage();

		try {
			ImageSource isrc = applicationConfig.imageSource();
			return isrc.getImage(DEFAULT_APPLICATION_IMAGE_KEY);
		} catch (NoSuchImageResourceException e) {
			return null;
		}
	}

	public void close() {
		close(false, 0);
	}

	public boolean isForceShutdown() {
		return forceShutdown;
	}

	public void close(boolean force, int exitCode) {
		forceShutdown = force;
		try {
			if (applicationConfig.windowManager().close()) {
				forceShutdown = true;
				if (applicationConfig.applicationContext() instanceof ConfigurableApplicationContext) {
					((ConfigurableApplicationContext) applicationConfig
							.applicationContext()).close();
				}
				applicationConfig.applicationLifecycleAdvisor().onShutdown();
			}
		} finally {
			if (isForceShutdown()) {
				System.exit(exitCode);
			}
		}
	}

	/*
	 * Closes the application once all windows have been closed.
	 */
	private class CloseApplicationObserver implements Observer {

		private boolean firstWindowCreated = false;

		@Override
		public void update(Observable o, Object arg) {
			int numOpenWidows = windowManager.getWindows().length;
			// make sure we only close the application after at least 1 window
			// has been added
			if (!firstWindowCreated && numOpenWidows > 0) {
				firstWindowCreated = true;
			} else if (firstWindowCreated && numOpenWidows == 0) {
				close();
			}
		}
	}

}
