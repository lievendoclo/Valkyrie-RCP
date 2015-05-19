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
package org.valkyriercp.util;

import org.springframework.binding.collection.AbstractCachingMapDecorator;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Helper implementation of an event listener list.
 * <p>
 * Provides methods for maintaining a list of listeners and firing events on
 * that list. This class is thread safe and serializable.
 * <p>
 * Usage Example:
 *
 * <pre>
 * private EventListenerListHelper fooListeners = new EventListenerListHelper(FooListener.class);
 *
 * public void addFooListener(FooListener listener) {
 * 	fooListeners.add(listener);
 * }
 *
 * public void removeFooListener(FooListener listener) {
 * 	fooListeners.remove(listener);
 * }
 *
 * protected void fireFooXXX() {
 * 	fooListeners.fire(&quot;fooXXX&quot;, new Event());
 * }
 *
 * protected void fireFooYYY() {
 * 	fooListeners.fire(&quot;fooYYY&quot;);
 * }
 * </pre>
 *
 * @author Oliver Hutchison
 * @author Keith Donald
 */
public class EventListenerListHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	private static final Iterator EMPTY_ITERATOR = new Iterator() {

        /**
         * {@inheritDoc}
         */
        public boolean hasNext() {
			return false;
		}

        /**
         * Unsupported operation.
         *
         * @throws UnsupportedOperationException always.
         */
		public void remove() {
			throw new UnsupportedOperationException();
		}

        /**
         * {@inheritDoc}
         */
		public Object next() {
			throw new UnsupportedOperationException();
		}
	};

	private static final Map methodCache = new AbstractCachingMapDecorator() {

        /**
         * Creates a value to cache under the given key {@code o}, which must be a
         * {@link MethodCacheKey}. The value to be created will be a {@link java.lang.reflect.Method} object that is
         * specified by the given key.
         *
         * @param o The key that the newly created object will be stored under. This is expected to
         * be an instance of {@link MethodCacheKey} that contains the class, method name and number
         * of parameters of the {@link java.lang.reflect.Method} to be created.
         *
         * @throws ClassCastException if {@code o} can not be assigned to {@link MethodCacheKey}.
         * @throws IllegalArgumentException if the listener class specified by {@code o}, does not
         * have an implementation of the method specified in the given key.
         */
		protected Object create(Object o) {
			MethodCacheKey key = (MethodCacheKey) o;
			Method fireMethod = null;

			Method[] methods = key.listenerClass.getMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (method.getName().equals(key.methodName) && method.getParameterTypes().length == key.numParams) {
					if (fireMethod != null) {
						throw new UnsupportedOperationException("Listener class [" + key.listenerClass
								+ "] has more than 1 implementation of method [" + key.methodName + "] with ["
								+ key.numParams + "] parameters.");
					}
					fireMethod = method;
				}
			}

			if (fireMethod == null) {
				throw new IllegalArgumentException("Listener class [" + key.listenerClass
						+ "] does not implement method [" + key.methodName + "] with [" + key.numParams
						+ "] parameters.");
			}
			return fireMethod;
		}
	};

	private final Class listenerClass;

	private volatile Object[] listeners = EMPTY_OBJECT_ARRAY;

	/**
	 * Create new <code>EventListenerListHelper</code> instance that will maintain
	 * a list of event listeners of the given class.
	 * @param listenerClass The class of the listeners that will be maintained by this list helper.
     *
     * @throws IllegalArgumentException if {@code listenerClass} is null.
     *
	 */
	public EventListenerListHelper(Class listenerClass) {
		Assert.notNull(listenerClass, "The listenerClass argument is required");
		this.listenerClass = listenerClass;
	}

	/**
	 * Returns whether or not any listeners are registered with this list.
     *
     * @return true if there are registered listeners.
	 */
	public boolean hasListeners() {
		return listeners.length > 0;
	}

	/**
	 * Returns true if there are no listeners registered with this list.
     *
     * @return true if there are no registered listeners.
	 */
	public boolean isEmpty() {
		return !hasListeners();
	}

	/**
	 * Returns the total number of listeners registered with this list.
     *
     * @return the total number of regisetered listeners.
	 */
	public int getListenerCount() {
		return listeners.length;
	}

	/**
	 * Returns an array of all the listeners registered with this list. This
	 * method is intended for use in subclasses that require the fastest
	 * possible access to the listener list. It is recommended that unless
	 * performance is absolutely critical access to the listener list should be
	 * through the <code>iterator</code>,<code>forEach</code> and
	 * <code>fire</code> methods only.
	 * <p>
	 * NOTE: The array returned by this method is used internally by this class
	 * and must NOT be modified.
	 */
	protected Object[] getListeners() {
		return listeners;
	}

	/**
	 * Returns an iterator over the list of listeners registered with this list. The returned
     * iterator does not allow removal of listeners. To remove a listener, use the
     * {@link #remove(Object)} method.
     *
     * @return An iterator for the registered listeners, never null.
	 */
	public Iterator iterator() {
		if (listeners == EMPTY_OBJECT_ARRAY)
			return EMPTY_ITERATOR;

		return new ObjectArrayIterator(listeners);
	}

	/**
	 * Invokes the method with the given name and no parameters on each of the listeners registered
     * with this list.
	 *
	 * @param methodName the name of the method to invoke.
     *
     * @throws IllegalArgumentException if no method with the given name and an empty parameter
     * list exists on the listener class maintained by this list helper.
	 */
	public void fire(String methodName) {
		if (listeners != EMPTY_OBJECT_ARRAY) {
			fireEventByReflection(methodName, EMPTY_OBJECT_ARRAY);
		}
	}

	/**
	 * Invokes the method with the given name and a single parameter on each of the listeners
     * registered with this list.
	 *
	 * @param methodName the name of the method to invoke.
	 * @param arg the single argument to pass to each invocation.
     *
     * @throws IllegalArgumentException if no method with the given name and a single formal
     * parameter exists on the listener class managed by this list helper.
	 */
	public void fire(String methodName, Object arg) {
		if (listeners != EMPTY_OBJECT_ARRAY) {
			fireEventByReflection(methodName, new Object[] { arg });
		}
	}

	/**
	 * Invokes the method with the given name and two parameters on each of the listeners
     * registered with this list.
	 *
	 * @param methodName the name of the method to invoke.
	 * @param arg1 the first argument to pass to each invocation.
	 * @param arg2 the second argument to pass to each invocation.
     *
     * @throws IllegalArgumentException if no method with the given name and 2 formal parameters
     * exists on the listener class managed by this list helper.
	 */
	public void fire(String methodName, Object arg1, Object arg2) {
		if (listeners != EMPTY_OBJECT_ARRAY) {
			fireEventByReflection(methodName, new Object[] { arg1, arg2 });
		}
	}

	/**
	 * Invokes the method with the given name and number of formal parameters on each of the
     * listeners registered with this list.
	 *
	 * @param methodName the name of the method to invoke.
	 * @param args an array of arguments to pass to each invocation.
     *
     * @throws IllegalArgumentException if no method with the given name and number of formal
     * parameters exists on the listener class managed by this list helper.
	 */
	public void fire(String methodName, Object[] args) {
		if (listeners != EMPTY_OBJECT_ARRAY) {
			fireEventByReflection(methodName, args);
		}
	}

	/**
	 * Adds <code>listener</code> to the list of registered listeners. If
	 * listener is already registered this method will do nothing.
     *
     * @param listener The event listener to be registered.
     *
     * @return true if the listener was registered, false if {@code listener} was null or it is
     * already registered with this list helper.
     *
     * @throws IllegalArgumentException if {@code listener} is not assignable to the class of
     * listener that this instance manages.
	 */
	public boolean add(Object listener) {
		if (listener == null) {
			return false;
		}
		checkListenerType(listener);
		synchronized (this) {
			if (listeners == EMPTY_OBJECT_ARRAY) {
				listeners = new Object[] { listener };
			}
			else {
				int listenersLength = listeners.length;
				for (int i = 0; i < listenersLength; i++) {
					if (listeners[i] == listener) {
						return false;
					}
				}
				Object[] tmp = new Object[listenersLength + 1];
				tmp[listenersLength] = listener;
				System.arraycopy(listeners, 0, tmp, 0, listenersLength);
				listeners = tmp;
			}
		}
		return true;
	}

	/**
     * Adds all the given listeners to the list of registered listeners. If any of the elements in
     * the array are null or are listeners that are already registered, they will not be registered
     * again.
     *
	 * @param listenersToAdd The collection of listeners to be added. May be null.
     *
     * @return true if the list of registered listeners changed as a result of attempting to
     * register the given collection of listeners.
     *
     * @throws IllegalArgumentException if any of the listeners in the given collection are of a
     * type that is not assignable to the class of listener that this instance manages.
	 */
	public boolean addAll(Object[] listenersToAdd) {
		if (listenersToAdd == null) {
			return false;
		}
		boolean changed = false;
		for (int i = 0; i < listenersToAdd.length; i++) {
			if (add(listenersToAdd[i])) {
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * Removes <code>listener</code> from the list of registered listeners.
     *
     * @param listener The listener to be removed.
     *
     * @throws IllegalArgumentException if {@code listener} is null or not assignable to the class
     * of listener that is maintained by this instance.
	 */
	public void remove(Object listener) {
		checkListenerType(listener);
		synchronized (this) {
			if (listeners == EMPTY_OBJECT_ARRAY)
				return;

			int listenersLength = listeners.length;
			int index = 0;
			for (; index < listenersLength; index++) {
				if (listeners[index] == listener) {
					break;
				}
			}
			if (index < listenersLength) {
				if (listenersLength == 1) {
					listeners = EMPTY_OBJECT_ARRAY;
				}
				else {
					Object[] tmp = new Object[listenersLength - 1];
					System.arraycopy(listeners, 0, tmp, 0, index);
					if (index < tmp.length) {
						System.arraycopy(listeners, index + 1, tmp, index, tmp.length - index);
					}
					listeners = tmp;
				}
			}
		}
	}

	/**
	 * Removes all registered listeners.
	 */
	public void clear() {
		synchronized (this) {
			if (this.listeners == EMPTY_OBJECT_ARRAY)
				return;

			this.listeners = EMPTY_OBJECT_ARRAY;
		}
	}

    /**
     * Invokes the method with the given name on each of the listeners registered with this list
     * helper. The given arguments are passed to each method invocation.
     *
     * @param methodName The name of the method to be invoked on the listeners.
     * @param eventArgs The arguments that will be passed to each method invocation. The number
     * of arguments is also used to determine the method to be invoked.
     *
     * @throws EventBroadcastException if an error occurs invoking the event method on any of the
     * listeners.
     */
	private void fireEventByReflection(String methodName, Object[] eventArgs) {
		Method eventMethod = (Method)methodCache.get(new MethodCacheKey(listenerClass, methodName, eventArgs.length));
		Object[] listenersCopy = listeners;
		for (int i = 0; i < listenersCopy.length; i++) {
			try {
				eventMethod.invoke(listenersCopy[i], eventArgs);
			}
			catch (InvocationTargetException e) {
				throw new EventBroadcastException("Exception thrown by listener", e.getCause());
			}
			catch (IllegalAccessException e) {
				throw new EventBroadcastException("Unable to invoke listener", e);
			}
		}
	}

    /**
     * Indicates that an error has occurred attempting to broadcast an event to listeners.
     */
	public static class EventBroadcastException extends NestedRuntimeException {

        /**
         * Creates a new {@code EventBroadcastException} with the given detail message and nested
         * exception.
         *
         * @param msg The detail message.
         * @param ex The nested exception.
         */
		public EventBroadcastException(String msg, Throwable ex) {
			super(msg, ex);
		}

	}

	private void checkListenerType(Object listener) {
		if (!listenerClass.isInstance(listener)) {
			throw new IllegalArgumentException("Listener [" + listener + "] is not an instance of [" + listenerClass
					+ "].");
		}
	}

	private static class ObjectArrayIterator implements Iterator {
		private final Object[] array;

		private int index;

		public ObjectArrayIterator(Object[] array) {
			this.array = array;
		}

		public boolean hasNext() {
			return index < array.length;
		}

		public Object next() {
			if (index > array.length - 1) {
				throw new NoSuchElementException();
			}

			return array[index++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static class MethodCacheKey {
		public final Class listenerClass;

		public final String methodName;

		public final int numParams;

		public MethodCacheKey(Class listenerClass, String methodName, int numParams) {
			Assert.notNull(listenerClass);
			Assert.notNull(methodName);

			this.listenerClass = listenerClass;
			this.methodName = methodName;
			this.numParams = numParams;
		}

		public boolean equals(Object o2) {
			// includes check for null
			if (!(o2 instanceof MethodCacheKey)) {
				return false;
			}

			MethodCacheKey k2 = (MethodCacheKey) o2;
			return listenerClass.equals(k2.listenerClass) && methodName.equals(k2.methodName)
					&& numParams == k2.numParams;
		}

        /**
         * {@inheritDoc}
         */
		public int hashCode() {
			return listenerClass.hashCode() ^ methodName.hashCode() ^ numParams;
		}
	}

    /**
     * Returns an object which is a copy of the collection of listeners registered with this instance.
     *
     * @return A copy of the registered listeners array, never null.
     */
	public Object toArray() {
		if (listeners == EMPTY_OBJECT_ARRAY)
			return Array.newInstance(listenerClass, 0);

		Object[] listenersCopy = listeners;
		Object copy = Array.newInstance(listenerClass, listenersCopy.length);
		System.arraycopy(listenersCopy, 0, copy, 0, listenersCopy.length);
		return copy;
	}

    /**
     * {@inheritDoc}
     */
	public String toString() {
		return new ToStringCreator(this).append("listenerClass", listenerClass).append("listeners", listeners)
				.toString();
	}
}