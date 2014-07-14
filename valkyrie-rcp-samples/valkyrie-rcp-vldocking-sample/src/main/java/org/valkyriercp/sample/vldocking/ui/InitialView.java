/*
 * Copyright 2002-2006 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.valkyriercp.sample.vldocking.ui;

import org.springframework.core.io.Resource;
import org.valkyriercp.application.support.AbstractView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * This class defines the initial view to be presented in the sample application. It is constructed automatically by the
 * platform and configured according to the bean specification in the application context. Here's an example
 * configuration:
 * 
 * <pre>
 *       &lt;bean id=&quot;initialView&quot;
 *           class=&quot;org.springframework.richclient.application.support.DefaultViewDescriptor&quot;&gt;
 *           &lt;property name=&quot;viewClass&quot;&gt;
 *               &lt;value&gt;org.springframework.richclient.samples.simple.ui.InitialView&lt;/value&gt;
 *           &lt;/property&gt;
 *           &lt;property name=&quot;viewProperties&quot;&gt;
 *               &lt;map&gt;
 *                   &lt;entry key=&quot;firstMessage&quot;&gt;
 *                       &lt;value&gt;This is the first message!&lt;/value&gt;
 *                   &lt;/entry&gt;
 *                   &lt;entry key=&quot;descriptionTextPath&quot;&gt;
 *                       &lt;value&gt;org/springframework/richclient/samples/simple/ui/initialViewText.html&lt;/value&gt;
 *                   &lt;/entry&gt;
 *               &lt;/map&gt;
 *           &lt;/property&gt;
 *       &lt;/bean&gt;
 * </pre>
 * 
 * Note that the configuration specifies the properties to be set on this class indirectly. The property set on the
 * {@link org.valkyriercp.application.support.DefaultViewDescriptor} is called <code>viewProperties</code> and it takes a map of key/value pairs. Each key
 * is the name of a property to be set on the actual view class (this class) and the value is the value to set for that
 * property. So, two properties have been configured, <code>firstMessage</code> and <code>descriptionTextPath</code>.
 * The <code>firstMessage</code> value specifies the key of a message to be displayed and the
 * <code>descriptionTextPath</code> specifies the path to a file containing the text to place in the HTML panel that
 * makes up the main body of this view.
 * @author Larry Streepy
 */
public class InitialView extends AbstractView {

	private String firstMessage;

	private Resource descriptionTextPath;

    public InitialView() {
        super("initialView");
    }

    /**
	 * @return the firstMessage
	 */
	public String getFirstMessage() {
		return firstMessage;
	}

	/**
	 * Set the key to the message to be displayed first in the view
	 * @param firstMessage the firstMessage to set
	 */
	public void setFirstMessage(String firstMessage) {
		this.firstMessage = firstMessage;
	}

	/**
	 * @return the descriptionTextPath
	 */
	public Resource getDescriptionTextPath() {
		return descriptionTextPath;
	}

	/**
	 * Set the resource that references the file containing the description text to place in the description areas of
	 * this view. Note that even though this property is of type Resource, the Spring platform will automatically
	 * convert a string path into a resource.
	 * @param descriptionTextPath the descriptionTextPath to set
	 */
	public void setDescriptionTextPath(Resource descriptionTextPath) {
		this.descriptionTextPath = descriptionTextPath;
	}
	
	/**
	 * Create the actual UI control for this view. It will be placed into the window according to the layout of the page
	 * holding this view.
	 */
	protected JComponent createControl() {
		// In this view, we're just going to use standard Swing to place a
		// few controls.

		// The location of the text to display has been set as a Resource in the
		// property descriptionTextPath. So, use that resource to obtain a URL
		// and set that as the page for the text pane.

		JTextPane textPane = new JTextPane();
		JScrollPane spDescription = getApplicationConfig().componentFactory().createScrollPane(textPane);
		try {
			textPane.setPage(getDescriptionTextPath().getURL());
		}
		catch (IOException e) {
			throw new RuntimeException("Unable to load description URL", e);
		}

		JLabel lblMessage = getApplicationConfig().componentFactory().createLabel(getFirstMessage());
		lblMessage.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		JPanel panel = getApplicationConfig().componentFactory().createPanel(new BorderLayout());
		panel.add(spDescription);
		panel.add(lblMessage, BorderLayout.SOUTH);

		return panel;
	}
}