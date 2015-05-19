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

import org.valkyriercp.component.TitlePane;
import org.valkyriercp.core.DefaultMessage;
import org.valkyriercp.core.DescriptionConfigurable;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.core.Message;
import org.valkyriercp.image.config.ImageConfigurable;
import org.valkyriercp.util.GuiStandardUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;

public abstract class TitledApplicationDialog extends ApplicationDialog implements Messagable, ImageConfigurable,
        DescriptionConfigurable {
	private TitlePane titlePane = new TitlePane();

	private Message description = new DefaultMessage("Title pane description");

	private JComponent pageControl;

	private JComponent contentPane;

	public TitledApplicationDialog() {
		super();
	}

	public TitledApplicationDialog(String title, Window parent) {
		super(title, parent);
	}

	public TitledApplicationDialog(String title, Window parent, CloseAction closeAction) {
		super(title, parent, closeAction);
	}

	public void setCaption(String shortDescription) {
		//throw new UnsupportedOperationException("What can I do with a caption?");
	}

	public void setDescription(String description) {
		this.description = new DefaultMessage(description);
		setMessage(this.description);
	}

	public void setTitlePaneTitle(String titleAreaText) {
		titlePane.setTitle(titleAreaText);
	}

	protected String getTitlePaneTitle() {
		return titlePane.getTitle();
	}

	public void setTitlePaneImage(Image image) {
		titlePane.setImage(image);
	}

	protected Image getTitlePaneImage() {
		return titlePane.getImage();
	}

	public void setImage(Image image) {
		setTitlePaneImage(image);
	}

	public Message getMessage() {
		return titlePane.getMessage();
	}

	public void setMessage(Message message) {
		if (message == null || DefaultMessage.EMPTY_MESSAGE.equals(message)) {
			titlePane.setMessage(getDescription());
		}
		else {
			titlePane.setMessage(message);
		}
	}

	public boolean isMessageShowing() {
		return titlePane.isMessageShowing();
	}

	protected Message getDescription() {
		return description;
	}

	protected void setContentPane(JComponent c) {
		if (isControlCreated()) {
			pageControl.remove(contentPane);
			contentPane = c;
			pageControl.add(contentPane);
			pageControl.revalidate();
			pageControl.repaint();
		}
		else {
			throw new IllegalStateException("Cannot set content pane until control is created");
		}
	}

	protected void addDialogComponents() {
		JComponent dialogContentPane = createDialogContentPane();
		getDialog().getContentPane().add(dialogContentPane, BorderLayout.CENTER);
		getDialog().getContentPane().add(createButtonBar(), BorderLayout.SOUTH);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Creates an additional panel at the top containing a title/message area.
	 * This can be used in conjunction with validation reporters to show the
	 * most recent error or to simply show a title and a description of the
	 * current Dialog.
	 *
	 * Use {@link #createTitledDialogContentPane()} to add your custom components.
	 */
	protected JComponent createDialogContentPane() {
		pageControl = new JPanel(new BorderLayout());
		JPanel titlePaneContainer = new JPanel(new BorderLayout());
		setMessage(getDescription());
		titlePaneContainer.add(titlePane.getControl());
		titlePaneContainer.add(new JSeparator(), BorderLayout.SOUTH);
		pageControl.add(titlePaneContainer, BorderLayout.NORTH);
		contentPane = createTitledDialogContentPane();
		if (getPreferredSize() != null) {
			contentPane.setPreferredSize(getPreferredSize());
		}
		GuiStandardUtils.attachDialogBorder(contentPane);
		pageControl.add(contentPane);
		return pageControl;
	}

	/**
	 *
	 * @return a component that will be added as the content of the Titled Dialog.
	 */
	protected abstract JComponent createTitledDialogContentPane();

	/**
	 * Dispose of the dialog content.
	 */
	protected void disposeDialogContentPane() {
		contentPane = null;
		pageControl = null;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		titlePane.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		titlePane.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		titlePane.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		titlePane.removePropertyChangeListener(propertyName, listener);
	}
}
