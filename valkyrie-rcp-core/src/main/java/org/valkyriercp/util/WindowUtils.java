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
package org.valkyriercp.util;

import org.springframework.util.Assert;

import java.awt.*;

public class WindowUtils {

	private WindowUtils() {
	}

	/**
	 * Return the system screen size.
	 *
	 * @return The dimension of the system screen size.
	 */
	public static Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	/**
	 * Return the centering point on the screen for the object with the
	 * specified dimension.
	 *
	 * @param dimension the dimension of an object
	 * @return The centering point on the screen for that object.
	 */
	public static Point getCenteringPointOnScreen(Dimension dimension) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		if (dimension.width > screen.width) {
			dimension.width = screen.width;
		}
		if (dimension.height > screen.height) {
			dimension.height = screen.height;
		}
		return new Point((screen.width - dimension.width) / 2, (screen.height - dimension.height) / 2);
	}

	/**
	 * Pack the window, center it on the screen, and set the window visible.
	 *
	 * @param window the window to center and show.
	 */
	public static void centerOnScreenAndSetVisible(Window window) {
		window.pack();
		centerOnScreen(window);
		window.setVisible(true);
	}

	/**
	 * Take the window and center it on the screen.
	 * <p>
	 * This works around a bug in setLocationRelativeTo(...): it currently does
	 * not take multiple monitors into accounts on all operating systems.
	 *
	 * @param window the window to center
	 */
	public static void centerOnScreen(Window window) {
		Assert.notNull(window, "window cannot be null");

		// This works around a bug in setLocationRelativeTo(...): it currently
		// does not take multiple monitors into accounts on all operating
		// systems.
		try {
			// Note that if this is running on a JVM prior to 1.4, then an
			// exception will be thrown and we will fall back to
			// setLocationRelativeTo(...).
			final Rectangle screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

			final Dimension windowSize = window.getSize();
			final int x = screenBounds.x + ((screenBounds.width - windowSize.width) / 2);
			final int y = screenBounds.y + ((screenBounds.height - windowSize.height) / 2);
			window.setLocation(x, y);
		}
		catch (Throwable t) {
			window.setLocationRelativeTo(window);
		}
	}

	/**
	 * Pack the window, center it relative to it's parent, and set the window
	 * visible.
	 *
	 * @param window the window to center and show.
	 */
	public static void centerOnParentAndSetVisible(Window window) {
		window.pack();
		centerOnParent(window, window.getParent());
		window.setVisible(true);
	}

	/**
	 * Center the window relative to it's parent. If the parent is null, or not showing,
	 * the window will be centered on the screen
	 *
	 * @param window the window to center
	 * @param parent the parent
	 */
	public static void centerOnParent(Window window, Component parent) {
		if (parent == null || !parent.isShowing()) {
			// call our own centerOnScreen so we work around bug in
			// setLocationRelativeTo(null)
			centerOnScreen(window);
		}
		else {
			window.setLocationRelativeTo(parent);
		}
	}

	/**
	 * Return a <code>Dimension</code> whose size is defined not in terms of
	 * pixels, but in terms of a given percent of the screen's width and height.
	 *
	 * <P>
	 * Use to set the preferred size of a component to a certain percentage of
	 * the screen.
	 *
	 * @param percentWidth percentage width of the screen, in range
	 * <code>1..100</code>.
	 * @param percentHeight percentage height of the screen, in range
	 * <code>1..100</code>.
	 */
	public static final Dimension getDimensionFromPercent(int percentWidth, int percentHeight) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return calcDimensionFromPercent(screenSize, percentWidth, percentHeight);
	}

	private static Dimension calcDimensionFromPercent(Dimension dimension, int percentWidth, int percentHeight) {
		int width = dimension.width * percentWidth / 100;
		int height = dimension.height * percentHeight / 100;
		return new Dimension(width, height);
	}

	public static int getScreenWidth() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return screenSize.width;
	}

	public static int getScreenHeight() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return screenSize.height;
	}

}