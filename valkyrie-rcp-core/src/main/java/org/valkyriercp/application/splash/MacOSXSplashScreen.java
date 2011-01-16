package org.valkyriercp.application.splash;

import org.valkyriercp.progress.*;

import javax.swing.*;
import java.awt.*;

/**
 * MacOSX style splash screen inspired by a blog post by <a
 * href="http://jroller.com/page/gfx?entry=wait_with_style_in_swing">Romain Guy</a>.
 *
 * @author Peter De Bruycker
 */
public class MacOSXSplashScreen extends AbstractSplashScreen implements MonitoringSplashScreen {
	private InfiniteProgressPanel progressPanel = new InfiniteProgressPanel();;

	protected Component createContentPane() {
		progressPanel.setPreferredSize(new Dimension(400, 250));
		progressPanel.shield = 0.5f;
		progressPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		progressPanel.setBackground(takeScreenshot());

		return progressPanel;
	}

	private Image takeScreenshot() {
		// take a screenshot
		try {
			Robot robot = new Robot();
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension dim = tk.getScreenSize();
			return robot.createScreenCapture(new Rectangle(0, 0, dim.width, dim.height));
		}
		catch (AWTException e) {
			e.printStackTrace();
			return null;
		}
	}

	public org.valkyriercp.progress.ProgressMonitor getProgressMonitor() {
		return new InfiniteProgressPanelProgressMonitor(progressPanel);
	}
}

