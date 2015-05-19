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
package org.valkyriercp.dialog;

import org.springframework.util.Assert;
import org.valkyriercp.component.DefaultMessageAreaModel;
import org.valkyriercp.core.Guarded;
import org.valkyriercp.core.Message;
import org.valkyriercp.core.support.LabeledObjectSupport;
import org.valkyriercp.factory.AbstractControlFactory;
import org.valkyriercp.factory.ControlFactory;
import org.valkyriercp.image.config.IconConfigurable;
import org.valkyriercp.util.ValkyrieRepository;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A convenience implementation of the DialogPage interface. Recommended to be
 * used as a base class for all GUI dialog pages (or panes.)
 *
 * @author Keith Donald
 * @see DialogPage
 */
public abstract class AbstractDialogPage extends LabeledObjectSupport implements DialogPage, ControlFactory, Guarded,
        IconConfigurable {

	private final MessageChangeHandler messageChangeHandler = new MessageChangeHandler();

	private String pageId;

	private Icon icon;

	private boolean pageComplete = true;

	private DefaultMessageAreaModel messageBuffer;

	private boolean visible = true;

	private AbstractControlFactory factory = new AbstractControlFactory() {
		public JComponent createControl() {
			return AbstractDialogPage.this.createControl();
		}
	};
    private boolean autoConfigure;

    /**
	 * Creates a new dialog page. This titles of this dialog page will be
	 * configured using the default ObjectConfigurer.
	 *
	 * @param pageId the id of this dialog page. This will be used to configure
	 * the page.
	 */
	protected AbstractDialogPage(String pageId) {
		this(pageId, true);
	}

	/**
	 * Creates a new dialog page.
	 *
	 * @param pageId the id of this dialog page
	 * @param autoConfigure whether or not to use an ObjectConfigurer to
	 * configure the titles of this dialog page using the given pageId
	 */
	protected AbstractDialogPage(String pageId, boolean autoConfigure) {
		this.messageBuffer = new DefaultMessageAreaModel(this);
		this.messageBuffer.addPropertyChangeListener(messageChangeHandler);
		setId(pageId, autoConfigure);
        configure();
	}

	/**
	 * Creates a new dialog page with the given title.
	 *
	 * @param pageId the id of this dialog page
	 * @param autoConfigure whether or not to use an ObjectConfigurer to
	 * configure the titles of this dialog page using the given pageId
	 * @param title the title of this dialog page, or <code>null</code> if
	 * none
	 */
	protected AbstractDialogPage(String pageId, boolean autoConfigure, String title) {
		this(pageId, autoConfigure);
		if (title != null) {
			setTitle(title);
		}
	}

	/**
	 * Creates a new dialog page with the given title and image.
	 *
	 * @param pageId the id of this dialog page
	 * @param autoConfigure whether or not to use an ObjectConfigurer to
	 * configure the titles of this dialog page using the given pageId
	 * @param title the title of this dialog page, or <code>null</code> if
	 * none
	 * @param icon the image for this dialog page, or <code>null</code> if
	 * none
	 */
	protected AbstractDialogPage(String pageId, boolean autoConfigure, String title, Image icon) {
		this(pageId, autoConfigure, title);
		if (icon != null) {
			setImage(icon);
		}
	}

	public String getId() {
		return pageId;
	}

	protected void setId(String pageId, boolean autoConfigure) {
        this.autoConfigure = autoConfigure;
        Assert.hasText(pageId, "pageId is required");
		String oldValue = this.pageId;
		this.pageId = pageId;
		firePropertyChange("id", oldValue, pageId);
	}

    private void configure() {
        if (autoConfigure) {
			if (logger.isDebugEnabled()) {
				logger.debug("Auto configuring dialog page with id " + pageId);
			}
			ValkyrieRepository.getInstance().getApplicationConfig().applicationObjectConfigurer().configure(this, pageId);
		}
    }

	public String getTitle() {
		return getDisplayName();
	}

	public Message getMessage() {
		return messageBuffer.getMessage();
	}

	/**
	 * Sets or clears the message for this page.
	 *
	 * @param newMessage the message, or <code>null</code> to clear the
	 * message
	 */
	public void setMessage(Message newMessage) {
		messageBuffer.setMessage(newMessage);
	}

	public boolean hasErrorMessage() {
		return messageBuffer.hasErrorMessage();
	}

	public boolean hasWarningMessage() {
		return messageBuffer.hasWarningMessage();
	}

	public boolean hasInfoMessage() {
		return messageBuffer.hasInfoMessage();
	}

	public void setVisible(boolean visible) {
		boolean oldValue = this.visible;
		getControl().setVisible(visible);
		this.visible = visible;
		firePropertyChange("visible", oldValue, visible);
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isPageComplete() {
		return pageComplete;
	}

	public void setPageComplete(boolean pageComplete) {
		boolean oldValue = this.pageComplete;
		this.pageComplete = pageComplete;
		firePropertyChange("pageComplete", oldValue, pageComplete);
	}

	public boolean isEnabled() {
		return isPageComplete();
	}

	public void setEnabled(boolean enabled) {
		setPageComplete(enabled);
	}

	public JComponent getControl() {
		return factory.getControl();
	}

	public boolean isControlCreated() {
		return factory.isControlCreated();
	}

	public Window getParentWindowControl() {
		return SwingUtilities.getWindowAncestor(getControl());
	}

	/**
	 * This default implementation of an <code>AbstractDialogPage</code>
	 * method does nothing. Subclasses should override to take some action in
	 * response to a help request.
	 */
	public void performHelp() {
		// do nothing by default
	}

	protected abstract JComponent createControl();

	private class MessageChangeHandler implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
		}
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public Icon getIcon() {
		return icon;
	}
}

