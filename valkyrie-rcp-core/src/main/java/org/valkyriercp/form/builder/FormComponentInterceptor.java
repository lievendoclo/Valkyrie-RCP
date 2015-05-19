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
package org.valkyriercp.form.builder;

import javax.swing.*;

/**
 * <p>
 * This provides a way to wrap functionality around components and labels.
 * </p>
 *
 * <p>
 * Example uses include changing the color when validation fails, or attaching a
 * right-click popup menu.
 * </p>
 *
 * @author oliverh
 */
public interface FormComponentInterceptor {

	/**
	 * Perform some kind of processing on the label.
	 *
	 * @param propertyName the name of the property that the label is for
	 * @param label the label to process
	 */
	public void processLabel(String propertyName, JComponent label);

	/**
	 * Perform some kind of processing on the component.
	 *
	 * @param propertyName the name of the property that the component is for
	 * @param component the component to process
	 */
	public void processComponent(String propertyName, JComponent component);

}