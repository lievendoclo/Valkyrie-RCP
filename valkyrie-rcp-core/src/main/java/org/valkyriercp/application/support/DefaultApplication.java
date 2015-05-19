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
package org.valkyriercp.application.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import org.valkyriercp.application.*;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.image.ImageSource;
import org.valkyriercp.image.NoSuchImageResourceException;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

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
