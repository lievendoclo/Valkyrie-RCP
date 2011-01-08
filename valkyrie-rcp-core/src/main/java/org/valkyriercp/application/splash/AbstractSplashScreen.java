package org.valkyriercp.application.splash;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.valkyriercp.component.ShadowBorderFrame;
import org.valkyriercp.util.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * An abstract helper implementation of the {@link SplashScreen} interface.
 *
 * <p>
 * The splash screen produced by this class will be an undecorated, centered
 * frame containing the component provided by {@link #createContentPane()},
 * which is the only method that subclasses need to implement.
 * </p>
 *
 * @author Peter De Bruycker
 */
public abstract class AbstractSplashScreen implements SplashScreen {

	/**
	 * The message resource key used to look up the splash screen's frame title.
	 */
	public static final String SPLASH_TITLE_KEY = "splash.title";

	private JFrame frame;

	private MessageSource messageSource;

	private String iconResourcePath;

	private boolean shadowBorder;

	protected final Log logger = LogFactory.getLog(getClass());

	private boolean rootFrame = true;

	/**
	 * Returns the location of the image to be used as the icon for the splash
	 * screen's frame. The splash screen produced by this class is undecorated,
	 * so the icon will only be visible in the frame's taskbar icon. If the
	 * given path starts with a '/', it is interpreted to be relative to the
	 * root of the runtime classpath. Otherwise it is interpreted to be relative
	 * to the subdirectory of the classpath root that corresponds to the package
	 * of this class.
	 *
	 * @return The location of the icon resource.
	 */
	public String getIconResourcePath() {
		return iconResourcePath;
	}

	/**
	 * Sets the location of the image to be used as the icon for the splash
	 * screen's frame. The splash screen produced by this class is undecorated,
	 * so the icon will only be visible in the frame's taskbar icon. If the
	 * given path starts with a '/', it is interpreted to be relative to the
	 * root of the runtime classpath. Otherwise it is interpreted to be relative
	 * to the subdirectory of the classpath root that corresponds to the package
	 * of this class.
	 *
	 * @param iconResourcePath The location of the icon resource.
	 */
	public void setIconResourcePath(String iconResourcePath) {
		this.iconResourcePath = iconResourcePath;
	}

	/**
	 * Returns the message source used to resolve localized messages.
	 *
	 * @return The message source, or null.
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * Sets the message source used to resolve localized messages.
	 *
	 * @param messageSource The message source.
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public final void dispose() {

		if (frame != null) {
			frame.dispose();
			frame = null;
		}

	}

	/**
	 * Creates and displays an undecorated, centered splash screen containing
	 * the component provided by {@link #createContentPane()}. If this instance
	 * has been provided with a {@link MessageSource}, it will be used to
	 * retrieve the splash screen's frame title under the key
	 * {@value #SPLASH_TITLE_KEY}.
	 */
	public final void splash() {
		frame = shadowBorder ? new ShadowBorderFrame() : new JFrame();
		if (isRootFrame())
			JOptionPane.setRootFrame(frame);

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setUndecorated(true);

		frame.setTitle(loadFrameTitle());
		frame.setIconImage(loadFrameIcon());

		Component content = createContentPane();
		if (content != null) {
			frame.getContentPane().add(content);
		}
		frame.pack();
		WindowUtils.centerOnScreen(frame);
		frame.setVisible(true);
	}

	/**
	 * Loads the message under the key {@value #SPLASH_TITLE_KEY} if a
	 * {@link MessageSource} has been set on this instance.
	 *
	 * @return The message resource stored under the key
	 * {@value #SPLASH_TITLE_KEY}, or null if no message source has been set.
	 */
	private String loadFrameTitle() {
		try {
			return messageSource == null ? null : messageSource.getMessage(SPLASH_TITLE_KEY, null, null);
		}
		catch (NoSuchMessageException e) {
			return null;
		}
	}

	private Image loadFrameIcon() {
		if (iconResourcePath == null) {
			return null;
		}

		URL url = this.getClass().getResource(iconResourcePath);
		if (url == null) {
			logger.warn("Unable to locate splash screen in classpath at: " + iconResourcePath);
			return null;
		}
		return Toolkit.getDefaultToolkit().createImage(url);
	}

	/**
	 * Returns the component to be displayed in the splash screen's main frame.
	 * If the returned value is null the frame for the splash screen will still
	 * be created but will not have any content
	 *
	 * @return The content pane component. Can be null.
	 */
	protected abstract Component createContentPane();

	/**
	 * Returns whether the splash screen will be rendered with a shadow border.
	 * The shadow border will let the background show through.
	 *
	 * @return whether to show a shadow border or not
	 */
	public boolean isShadowBorder() {
		return shadowBorder;
	}

	/**
	 * Sets whether the splash screen will be rendered with a shadow border. If
	 * <code>true</code> a border will be shown.
	 *
	 * @param shadowBorder Show the shadow border or not
	 */
	public void setShadowBorder(boolean shadowBorder) {
		this.shadowBorder = shadowBorder;
	}

	/**
	 * The frame that is used to display this splashscreen can be used as
	 * rootFrame for other dialogs/windows that need a Frame before the
	 * applicationFrame is created. (eg login).
	 *
	 * @param rootFrame <code>true</code> if the created frame must be set as
	 * rootFrame. Defaults to <code>true</code>.
	 * @see JOptionPane#setRootFrame(java.awt.Frame)
	 */
	public void setRootFrame(boolean rootFrame) {
		this.rootFrame = rootFrame;
	}

	/**
	 * @return <code>true</code> if the created frame must be set as rootFrame.
	 */
	public boolean isRootFrame() {
		return this.rootFrame;
	}
}
