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