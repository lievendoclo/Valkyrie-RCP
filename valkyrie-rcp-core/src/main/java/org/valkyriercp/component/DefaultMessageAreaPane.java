package org.valkyriercp.component;

import com.jgoodies.forms.layout.Sizes;
import org.jdesktop.swingx.icon.EmptyIcon;
import org.springframework.util.StringUtils;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.core.Message;
import org.valkyriercp.factory.AbstractControlFactory;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Keith Donald
 * @author Oliver Hutchison
 */
public class DefaultMessageAreaPane extends AbstractControlFactory implements MessagePane, PropertyChangeListener {
	private static final int ONE_LINE_IN_DLU = 10;

	public static final int DEFAULT_LINES_TO_DISPLAY = 2;

	private int linesToDisplay;

	private JLabel messageLabel;

	private Icon defaultIcon = new EmptyIcon(16, 16);

	private DefaultMessageAreaModel messageAreaModel;

	public DefaultMessageAreaPane() {
		this(DEFAULT_LINES_TO_DISPLAY);
	}

	public DefaultMessageAreaPane(int linesToDisplay) {
		init(linesToDisplay, this);
	}

	public DefaultMessageAreaPane(Messagable delegateFor) {
		this(DEFAULT_LINES_TO_DISPLAY, delegateFor);
	}

	public DefaultMessageAreaPane(int linesToDisplay, Messagable delegateFor) {
		init(linesToDisplay, delegateFor);
	}

	private void init(int linesToDisplay, Messagable delegateFor) {
		this.linesToDisplay = linesToDisplay;
		this.messageAreaModel = new DefaultMessageAreaModel(delegateFor);
		this.messageAreaModel.addPropertyChangeListener(this);
	}

	public void setDefaultIcon(Icon defaultIcon) {
		this.defaultIcon = defaultIcon;
	}

	protected JComponent createControl() {
		if (messageLabel == null) {
			this.messageLabel = new JLabel();
			this.messageAreaModel.renderMessage(messageLabel);
		}
		int prefWidth = messageLabel.getPreferredSize().width;
		int prefHeight = Sizes.dialogUnitYAsPixel(linesToDisplay * ONE_LINE_IN_DLU, messageLabel);

		int iconHeight = getDefaultIcon().getIconHeight() + messageLabel.getIconTextGap() * 2;
		prefHeight = Math.max(iconHeight, prefHeight);

		messageLabel.setPreferredSize(new Dimension(prefWidth, prefHeight));
		messageLabel.setOpaque(false);
		messageLabel.setVerticalAlignment(SwingConstants.TOP);
		messageLabel.setVerticalTextPosition(SwingConstants.TOP);
		messageLabel.setIcon(getDefaultIcon());
		return messageLabel;
	}

	private Icon getDefaultIcon() {
		return defaultIcon;
	}

	public Message getMessage() {
		return messageAreaModel.getMessage();
	}

	public void setMessage(Message message) {
		messageAreaModel.setMessage(message);
		if (messageLabel != null) {
			messageAreaModel.renderMessage(messageLabel);
		}
	}

	public void clearMessage() {
		messageAreaModel.setMessage(null);
	}

	public boolean isMessageShowing() {
		if (messageLabel == null) {
			return false;
		}
		return StringUtils.hasText(messageLabel.getText()) && messageLabel.isVisible();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		messageAreaModel.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		messageAreaModel.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		messageAreaModel.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		messageAreaModel.removePropertyChangeListener(propertyName, listener);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (messageLabel == null) {
			this.messageLabel = new JLabel();
		}
		messageAreaModel.getMessage().renderMessage(messageLabel);
	}
}
