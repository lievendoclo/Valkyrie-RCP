package org.valkyriercp.binding.value.support;

import org.springframework.util.ObjectUtils;

import javax.swing.event.EventListenerList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Convenience class that provides propertyChange support. Listeners can be
 * (un)registered on specific properties while firing of
 * {@link java.beans.PropertyChangeEvent}s is eased.
 */
public final class PropertyChangeSupport implements Serializable {

	/** Lists all the generic listeners. */
	transient private EventListenerList listeners;

	/** Contains SwingPropertyChangeSupports for individual properties. */
	private Map children;

	/** Source of the events. */
	private Object source;

	// Serialization version ID
	static final long serialVersionUID = 7162625831330845068L;

	/**
	 * Constructs a SwingPropertyChangeSupport object.
	 *
	 * @param sourceBean The bean to be given as the source for any events.
	 */
	public PropertyChangeSupport(Object sourceBean) {
		if (sourceBean == null) {
			throw new NullPointerException();
		}
		this.source = sourceBean;
	}

	/**
	 * Add a PropertyChangeListener to the listener list. The listener is
	 * registered for all properties.
	 *
	 * @param listener The PropertyChangeListener to be added
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (listener instanceof PropertyChangeListenerProxy) {
			PropertyChangeListenerProxy proxy = (PropertyChangeListenerProxy) listener;
			addPropertyChangeListener(proxy.getPropertyName(), (PropertyChangeListener) proxy.getListener());
		}
		else {
			if (listeners == null) {
				listeners = new EventListenerList();
			}
			listeners.add(PropertyChangeListener.class, listener);
		}
	}

	/**
	 * Remove a PropertyChangeListener from the listener list. This removes a
	 * PropertyChangeListener that was registered for all properties.
	 *
	 * @param listener The PropertyChangeListener to be removed
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		if (listener instanceof PropertyChangeListenerProxy) {
			PropertyChangeListenerProxy proxy = (PropertyChangeListenerProxy) listener;
			removePropertyChangeListener(proxy.getPropertyName(), (PropertyChangeListener) proxy.getListener());
		}
		else {
			if (listeners == null) {
				return;
			}
			listeners.remove(PropertyChangeListener.class, listener);
		}
	}

	/**
	 * Returns an array of all the listeners that were added to the
	 * SwingPropertyChangeSupport object with addPropertyChangeListener().
	 * <p>
	 * If some listeners have been added with a named property, then the
	 * returned array will be a mixture of PropertyChangeListeners and
	 * <code>PropertyChangeListenerProxy</code>s. If the calling method is
	 * interested in distinguishing the listeners then it must test each element
	 * to see if it's a <code>PropertyChangeListenerProxy</code> perform the
	 * cast and examine the parameter.
	 *
	 * <pre>
	 * PropertyChangeListener[] listeners = support.getPropertyChangeListeners();
	 * for (int i = 0; i &lt; listeners.length; i++) {
	 * 	if (listeners[i] instanceof PropertyChangeListenerProxy) {
	 * 		PropertyChangeListenerProxy proxy = (PropertyChangeListenerProxy) listeners[i];
	 * 		if (proxy.getPropertyName().equals(&quot;foo&quot;)) {
	 * 			// proxy is a PropertyChangeListener which was associated
	 * 			// with the property named &quot;foo&quot;
	 * 		}
	 * 	}
	 * }
	 * </pre>
	 *
	 * @see java.beans.PropertyChangeListenerProxy
	 * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners
	 * @return all of the <code>PropertyChangeListener</code> s added or an
	 * empty array if no listeners have been added
	 * @since 1.4
	 */
	public PropertyChangeListener[] getPropertyChangeListeners() {
		List returnList = new ArrayList();

		// Add all the PropertyChangeListeners
		if (listeners != null) {
			returnList.addAll(Arrays.asList(listeners.getListeners(PropertyChangeListener.class)));
		}

		// Add all the PropertyChangeListenerProxys
		if (children != null) {
			Iterator iterator = children.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				PropertyChangeSupport child = (PropertyChangeSupport) children.get(key);
				PropertyChangeListener[] childListeners = child.getPropertyChangeListeners();
				for (int index = childListeners.length - 1; index >= 0; index--) {
					returnList.add(new PropertyChangeListenerProxy(key, childListeners[index]));
				}
			}
		}
		return (PropertyChangeListener[]) returnList.toArray(new PropertyChangeListener[returnList.size()]);
	}

	/**
	 * Add a PropertyChangeListener for a specific property. The listener will
	 * be invoked only when a call on firePropertyChange names that specific
	 * property.
	 *
	 * @param propertyName The name of the property to listen on.
	 * @param listener The PropertyChangeListener to be added
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (children == null) {
			children = new HashMap();
		}
		PropertyChangeSupport child = (PropertyChangeSupport) children.get(propertyName);
		if (child == null) {
			child = new PropertyChangeSupport(source);
			children.put(propertyName, child);
		}
		child.addPropertyChangeListener(listener);
	}

	/**
	 * Remove a PropertyChangeListener for a specific property.
	 *
	 * @param propertyName The name of the property that was listened on.
	 * @param listener The PropertyChangeListener to be removed
	 */
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (children == null) {
			return;
		}
		PropertyChangeSupport child = (PropertyChangeSupport) children.get(propertyName);
		if (child == null) {
			return;
		}
		child.removePropertyChangeListener(listener);
	}

	/**
	 * Returns an array of all the listeners which have been associated with the
	 * named property.
	 *
	 * @return all of the <code>PropertyChangeListeners</code> associated with
	 * the named property or an empty array if no listeners have been added
	 */
	public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
		List returnList = new ArrayList();

		if (children != null) {
			PropertyChangeSupport support = (PropertyChangeSupport) children.get(propertyName);
			if (support != null) {
				returnList.addAll(Arrays.asList(support.getPropertyChangeListeners()));
			}
		}
		return (PropertyChangeListener[]) (returnList.toArray(new PropertyChangeListener[0]));
	}

	/**
	 * Report a bound property update to any registered listeners. No event is
	 * fired if old and new are equal and non-null.
	 *
	 * @param propertyName The programmatic name of the property that was
	 * changed.
	 * @param oldValue The old value of the property.
	 * @param newValue The new value of the property.
	 */
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		firePropertyChange(new PropertyChangeEvent(source, propertyName, oldValue, newValue));
	}

	/**
	 * Fire an existing PropertyChangeEvent to any registered listeners. No
	 * event is fired if the given event's old and new values are equal and
	 * non-null.
	 *
	 * @param evt The PropertyChangeEvent object.
	 */
	public void firePropertyChange(PropertyChangeEvent evt) {
		Object oldValue = evt.getOldValue();
		Object newValue = evt.getNewValue();
		if (ObjectUtils.nullSafeEquals(oldValue, newValue)) {
			return;
		}

		String propertyName = evt.getPropertyName();
		PropertyChangeSupport child = null;
		if (children != null) {
			if (children != null && propertyName != null) {
				child = (PropertyChangeSupport) children.get(propertyName);
			}
		}

		if (listeners != null) {
			Object[] listenerList = listeners.getListenerList();
			for (int i = 0; i <= listenerList.length - 2; i += 2) {
				if (listenerList[i] == PropertyChangeListener.class) {
					((PropertyChangeListener) listenerList[i + 1]).propertyChange(evt);
				}
			}
		}

		if (child != null) {
			child.firePropertyChange(evt);
		}
	}

	/**
	 * Check if there are any listeners for a specific property.
	 *
	 * @param propertyName the property name.
	 * @return true if there are ore or more listeners for the given property
	 */
	public boolean hasListeners(String propertyName) {
		if (listeners != null && listeners.getListenerCount(PropertyChangeListener.class) > 0) {
			// there is a generic listener
			return true;
		}
		if (children != null) {
			PropertyChangeSupport child = (PropertyChangeSupport) children.get(propertyName);
			if (child != null) {
				// The child will always have a listeners ArrayList.
				return child.hasListeners(propertyName);
			}
		}
		return false;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();

		if (listeners != null) {
			Object[] listenerList = listeners.getListenerList();
			for (int i = 0; i <= listenerList.length - 2; i += 2) {
				if (listenerList[i] == PropertyChangeListener.class
						&& (PropertyChangeListener) listenerList[i + 1] instanceof Serializable) {
					s.writeObject(listenerList[i + 1]);
				}
			}
		}
		s.writeObject(null);
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
		s.defaultReadObject();

		Object listenerOrNull;
		while (null != (listenerOrNull = s.readObject())) {
			addPropertyChangeListener((PropertyChangeListener) listenerOrNull);
		}
	}

}