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
package org.valkyriercp.core;

import java.beans.PropertyChangeListener;

/**
 * Interface implemented by domain objects that can publish property change
 * events. Clients can use this interface to subscribe to the object for change
 * notifications.
 *
 * @author Keith Donald
 */
public interface PropertyChangePublisher {

	/**
	 * Register a listener to all properties of this publisher.
	 *
	 * @param listener the <code>PropertyChangeListener</code> to register.
	 */
	void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Register a listener to a specific property.
	 *
	 * @param propertyName the property to monitor.
	 * @param listener the <code>PropertyChangeListener</code> to register.
	 */
	void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

	/**
	 * Remove the listener from all properties of this publisher.
	 *
	 * @param listener the <code>PropertyChangeListener</code> to remove.
	 */
	void removePropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Remove the listener from a specific property.
	 *
	 * @param propertyName the property that was being monitored.
	 * @param listener the <code>PropertyChangeListener</code> to remove.
	 */
	void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}