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
package org.valkyriercp.binding.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessor;
import org.springframework.binding.collection.AbstractCachingMapDecorator;
import org.springframework.util.Assert;
import org.valkyriercp.binding.MutablePropertyAccessStrategy;
import org.valkyriercp.binding.PropertyMetadataAccessStrategy;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.AbstractValueModel;
import org.valkyriercp.binding.value.support.ValueHolder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * An abstract implementation of <code>MutablePropertyAccessStrategy</code>
 * that provides support for concrete implementations.
 *
 * <p>
 * As this class delegates to a <code>PropertyAccessor</code> for property
 * access, the support for type resolution and <b>nested properties</b> depends
 * on the implementation of the <code>PropertyAccessor</code>
 *
 * @author Oliver Hutchison
 * @author Arne Limburg
 */
public abstract class AbstractPropertyAccessStrategy implements MutablePropertyAccessStrategy {

	private final ValueModel domainObjectHolder;

	private final String basePropertyPath;

	private final ValueModelCache valueModelCache;

	private final PropertyMetadataAccessStrategy metaAspectAccessor;

	/**
	 * Creates a new instance of AbstractPropertyAccessStrategy that will
	 * provide access to the properties of the provided object.
	 *
	 * @param object the object to be accessed through this class.
	 */
	public AbstractPropertyAccessStrategy(Object object) {
		this(new ValueHolder(object));
	}

	/**
	 * Creates a new instance of AbstractPropertyAccessStrategy that will
	 * provide access to the object contained by the provided value model.
	 *
	 * @param domainObjectHolder value model that holds the object to be
	 * accessed through this class
	 */
	public AbstractPropertyAccessStrategy(final ValueModel domainObjectHolder) {
		Assert.notNull(domainObjectHolder, "domainObjectHolder must not be null.");
		this.domainObjectHolder = domainObjectHolder;
		this.domainObjectHolder.addValueChangeListener(new DomainObjectChangeListener());
		this.basePropertyPath = "";
		this.valueModelCache = new ValueModelCache();
		this.metaAspectAccessor = new PropertyMetaAspectAccessor();
	}

	/**
	 * Creates a child instance of AbstractPropertyAccessStrategy that will
	 * delegate to its parent for property access.
	 *
	 * @param parent AbstractPropertyAccessStrategy which will be used to
	 * provide property access
	 * @param basePropertyPath property path that will as a base when accessing
	 * the parent AbstractPropertyAccessStrategy
	 */
	protected AbstractPropertyAccessStrategy(AbstractPropertyAccessStrategy parent, String basePropertyPath) {
		this.domainObjectHolder = parent.getPropertyValueModel(basePropertyPath);
		this.basePropertyPath = basePropertyPath;
		this.valueModelCache = parent.valueModelCache;
		this.metaAspectAccessor = new PropertyMetaAspectAccessor();
	}

	/**
	 * Subclasses may override this method to supply user metadata for the
	 * specified <code>propertyPath</code> and <code>key</code>. The
	 * default implementation invokes {@link #getAllUserMetadataFor(String)} and
	 * uses the returned Map with the <code>key</code> parameter to find the
	 * correlated value.
	 *
	 * @param propertyPath path of property relative to this bean
	 * @param key
	 * @return metadata associated with the specified key for the property or
	 * <code>null</code> if there is no custom metadata associated with the
	 * property and key.
	 */
	protected Object getUserMetadataFor(String propertyPath, String key) {
		final Map allMetadata = getAllUserMetadataFor(propertyPath);
		return allMetadata != null ? allMetadata.get(key) : null;
	}

	/**
	 * Subclasses may override this method to supply user metadata for the
	 * specified <code>propertyPath</code>. The default implementation always
	 * returns <code>null</code>.
	 *
	 * @param propertyPath path of property relative to this bean
	 * @return all metadata for the specified property in the form of a Map
	 * containing String keys and Object values. This method may return
	 * <code>null</code> if there is no metadata for the property.
	 */
	protected Map getAllUserMetadataFor(String propertyPath) {
		return null;
	}

	protected abstract PropertyAccessor getPropertyAccessor();

	public ValueModel getDomainObjectHolder() {
		return domainObjectHolder;
	}

	public ValueModel getPropertyValueModel(String propertyPath) throws BeansException {
		return (ValueModel) valueModelCache.get(getFullPropertyPath(propertyPath));
	}

	/**
	 * Returns a property path that includes the base property path of the
	 * class.
	 */
	protected String getFullPropertyPath(String propertyPath) {
		if (basePropertyPath.equals("")) {
			return propertyPath;
		}
		else if (propertyPath.equals("")) {
			return basePropertyPath;
		}
		else {
			return basePropertyPath + '.' + propertyPath;
		}
	}

	/**
	 * Extracts the property name from a propertyPath.
	 */
	protected String getPropertyName(String propertyPath) {
		int lastSeparator = getLastPropertySeparatorIndex(propertyPath);
		if (lastSeparator == -1) {
			return propertyPath;
		}
		if (propertyPath.charAt(lastSeparator) == PropertyAccessor.NESTED_PROPERTY_SEPARATOR_CHAR)
			return propertyPath.substring(lastSeparator + 1);

		return propertyPath.substring(lastSeparator);
	}

	/**
	 * Returns the property name component of the provided property path.
	 */
	protected String getParentPropertyPath(String propertyPath) {
		int lastSeparator = getLastPropertySeparatorIndex(propertyPath);
		return lastSeparator == -1 ? "" : propertyPath.substring(0, lastSeparator);
	}

	/**
	 * Returns the index of the last nested property separator in the given
	 * property path, ignoring dots in keys (like "map[my.key]").
	 */
	protected int getLastPropertySeparatorIndex(String propertyPath) {
		boolean inKey = false;
		for (int i = propertyPath.length() - 1; i >= 0; i--) {
			switch (propertyPath.charAt(i)) {
			case PropertyAccessor.PROPERTY_KEY_SUFFIX_CHAR:
				inKey = true;
				break;
			case PropertyAccessor.PROPERTY_KEY_PREFIX_CHAR:
				return i;
			case PropertyAccessor.NESTED_PROPERTY_SEPARATOR_CHAR:
				if (!inKey) {
					return i;
				}
				break;
			}
		}
		return -1;
	}

	public abstract MutablePropertyAccessStrategy getPropertyAccessStrategyForPath(String propertyPath)
			throws BeansException;

	public abstract MutablePropertyAccessStrategy newPropertyAccessStrategy(ValueModel domainObjectHolder);

	public Object getDomainObject() {
		return domainObjectHolder.getValue();
	}

	public PropertyMetadataAccessStrategy getMetadataAccessStrategy() {
		return metaAspectAccessor;
	}

	public Object getPropertyValue(String propertyPath) throws BeansException {
		return getPropertyValueModel(propertyPath).getValue();
	}

	/**
	 * Called when the domain object is changed.
	 */
	protected abstract void domainObjectChanged();

	/**
	 * Keeps beanWrapper up to date with the value held by domainObjectHolder.
	 */
	private class DomainObjectChangeListener implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			domainObjectChanged();
		}
	}

	/**
	 * A cache of value models generated for specific property paths.
	 */
	private class ValueModelCache extends AbstractCachingMapDecorator {

		protected Object create(Object propertyPath) {
			String fullPropertyPath = getFullPropertyPath((String) propertyPath);
			String parentPropertyPath = getParentPropertyPath(fullPropertyPath);
			ValueModel parentValueModel = parentPropertyPath == "" ? domainObjectHolder : (ValueModel) valueModelCache
					.get(parentPropertyPath);
			return new PropertyValueModel(parentValueModel, fullPropertyPath);
		}
	}

	/**
	 * A value model that wraps a single JavaBean property. Delegates to the
	 * beanWrapperr for getting and setting the value. If the wrapped JavaBean
	 * supports publishing property change events this class will also register
	 * a property change listener so that changes to the property made outside
	 * of this value model may also be detected and notified to any value change
	 * listeners registered with this class.
	 */
	private class PropertyValueModel extends AbstractValueModel {

		private final ValueModel parentValueModel;

		private final String propertyPath;

		private final String propertyName;

		private PropertyChangeListener beanPropertyChangeListener;

		private Object savedParentObject;

		private Object savedPropertyValue;

		private boolean settingBeanProperty;

		public PropertyValueModel(ValueModel parentValueModel, String propertyPath) {
			this.parentValueModel = parentValueModel;
			this.parentValueModel.addValueChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					parentValueChanged();
				}
			});
			this.propertyPath = propertyPath;
			this.propertyName = getPropertyName(propertyPath);
			if (getPropertyAccessor().isReadableProperty(propertyPath)) {
				this.savedPropertyValue = getPropertyAccessor().getPropertyValue(propertyPath);
			}
			updateBeanPropertyChangeListener();
		}

		public Object getValue() {
			savedPropertyValue = getPropertyAccessor().getPropertyValue(propertyPath);
			return savedPropertyValue;
		}

		public void setValue(Object value) {
			// TODO: make this thread safe
			try {
				settingBeanProperty = true;
				getPropertyAccessor().setPropertyValue(propertyPath, value);
			}
			finally {
				settingBeanProperty = false;
			}
			fireValueChange(savedPropertyValue, value);
			savedPropertyValue = value;
		}

		/**
		 * Called when the parent object changes.
		 */
		private void parentValueChanged() {
			updateBeanPropertyChangeListener();
			fireValueChange(savedPropertyValue, getValue());
		}

		/**
		 * Called by the parent JavaBean if it supports PropertyChangeEvent
		 * notifications and the property wrapped by this value model has
		 * changed.
		 */
		private void propertyValueChanged() {
			if (!settingBeanProperty) {
				fireValueChange(savedPropertyValue, getValue());
			}
		}

		/**
		 * If the parent JavaBean supports property change notification register
		 * this class as a property change listener.
		 */
		private synchronized void updateBeanPropertyChangeListener() {
			final Object currentParentObject = parentValueModel.getValue();
			if (currentParentObject != savedParentObject) {
				// remove PropertyChangeListener from old parent
				if (beanPropertyChangeListener != null) {
					PropertyChangeSupportUtils.removePropertyChangeListener(savedParentObject, propertyName,
							beanPropertyChangeListener);
					beanPropertyChangeListener = null;
				}
				// install PropertyChangeListener on new parent
				if (currentParentObject != null
						&& PropertyChangeSupportUtils.supportsBoundProperties(currentParentObject.getClass())) {
					beanPropertyChangeListener = new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent evt) {
							propertyValueChanged();
						}
					};
					PropertyChangeSupportUtils.addPropertyChangeListener(currentParentObject, propertyName,
							beanPropertyChangeListener);
				}
				savedParentObject = currentParentObject;
			}
		}
	}

	/**
	 * Implementation of PropertyMetadataAccessStrategy that simply delegates to
	 * the beanWrapper.
	 */
	private class PropertyMetaAspectAccessor implements PropertyMetadataAccessStrategy {

		public Class getPropertyType(String propertyPath) {
			return getPropertyAccessor().getPropertyType(getFullPropertyPath(propertyPath));
		}

		public boolean isReadable(String propertyPath) {
			return getPropertyAccessor().isReadableProperty(getFullPropertyPath(propertyPath));
		}

		public boolean isWriteable(String propertyPath) {
			return getPropertyAccessor().isWritableProperty(getFullPropertyPath(propertyPath));
		}

		public Object getUserMetadata(String propertyPath, String key) {
			return getUserMetadataFor(propertyPath, key);
		}

		public Map getAllUserMetadata(String propertyPath) {
			return getAllUserMetadataFor(propertyPath);
		}
	}

}
