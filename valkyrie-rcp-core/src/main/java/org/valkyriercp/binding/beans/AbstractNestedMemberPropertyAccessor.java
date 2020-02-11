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
package org.valkyriercp.binding.beans;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.*;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * This implementation extends {@link AbstractMemberPropertyAccessor} with the
 * functionality of nested property handling.
 * 
 * @author Arne Limburg
 * 
 */
public abstract class AbstractNestedMemberPropertyAccessor extends
		AbstractMemberPropertyAccessor {

	private AbstractNestedMemberPropertyAccessor parentPropertyAccessor;

	private String basePropertyName;

	private final LoadingCache childPropertyAccessors = CacheBuilder.newBuilder().build(new CacheLoader<Object, Object>() {
		@Override
		public Object load(Object key) throws Exception {
			return createChildPropertyAccessor((String) key);
		}
	});

	private final boolean strictNullHandlingEnabled;

	protected AbstractNestedMemberPropertyAccessor(Class targetClass,
			boolean fieldAccessEnabled, boolean strictNullHandlingEnabled) {
		super(targetClass, fieldAccessEnabled);
		this.strictNullHandlingEnabled = strictNullHandlingEnabled;
	}

	public AbstractNestedMemberPropertyAccessor(
			AbstractNestedMemberPropertyAccessor parent, String baseProperty) {
		super(parent.getPropertyType(baseProperty), parent
				.isFieldAccessEnabled());
		parentPropertyAccessor = parent;
		basePropertyName = baseProperty;
		strictNullHandlingEnabled = parent.strictNullHandlingEnabled;
	}

	public boolean isStrictNullHandlingEnabled() {
		return strictNullHandlingEnabled;
	}

	protected AbstractNestedMemberPropertyAccessor getParentPropertyAccessor() {
		return parentPropertyAccessor;
	}

	protected String getBasePropertyName() {
		return basePropertyName;
	}

	public Object getTarget() {
		if (parentPropertyAccessor != null && basePropertyName != null) {
			return parentPropertyAccessor.getPropertyValue(basePropertyName);
		} else {
			return null;
		}
	}

	@Override
	public Class getTargetClass() {
		if (parentPropertyAccessor != null) {
			return parentPropertyAccessor.getPropertyType(basePropertyName);
		} else {
			return super.getTargetClass();
		}
	}

	@Override
	public boolean isReadableProperty(String propertyPath) {
		if (PropertyAccessorUtils.isNestedProperty(propertyPath)) {
			String baseProperty = getBasePropertyName(propertyPath);
			String childPropertyPath = getChildPropertyPath(propertyPath);
			if (!super.isReadableProperty(baseProperty)) {
				return false;
			} else {
				return ((PropertyAccessor) childPropertyAccessors
						.getUnchecked(baseProperty))
						.isReadableProperty(childPropertyPath);
			}
		} else {
			return super.isReadableProperty(propertyPath);
		}
	}

	@Override
	public boolean isWritableProperty(String propertyPath) {
		if (PropertyAccessorUtils.isNestedProperty(propertyPath)) {
			String baseProperty = getBasePropertyName(propertyPath);
			String childPropertyPath = getChildPropertyPath(propertyPath);
			return super.isReadableProperty(baseProperty)
					&& ((PropertyAccessor) childPropertyAccessors
							.getUnchecked(baseProperty))
							.isWritableProperty(childPropertyPath);
		} else {
			return super.isWritableProperty(propertyPath);
		}
	}

	@Override
	public Class getPropertyType(String propertyPath) {
		if (PropertyAccessorUtils.isNestedProperty(propertyPath)) {
			String baseProperty = getBasePropertyName(propertyPath);
			String childPropertyPath = getChildPropertyPath(propertyPath);
			return ((PropertyAccessor) childPropertyAccessors.getUnchecked(baseProperty))
					.getPropertyType(childPropertyPath);
		} else {
			return super.getPropertyType(propertyPath);
		}
	}

	@Override
	public TypeDescriptor getPropertyTypeDescriptor(String propertyName)
			throws BeansException {
		try {
			String actualPropertyName = org.springframework.beans.PropertyAccessorUtils
					.getPropertyName(propertyName);
			PropertyDescriptor pd = new PropertyDescriptor(actualPropertyName,
					getTargetClass());
			if (pd != null) {
				Class type = getPropertyType(propertyName);
				if (pd.getReadMethod() != null) {
					return new TypeDescriptor(new MethodParameter(
							pd.getReadMethod(), -1));
				} else if (pd.getWriteMethod() != null) {
					return new TypeDescriptor(
							BeanUtils.getWriteMethodParameter(pd));
				}
			}
		} catch (InvalidPropertyException ex) {
			// Consider as not determinable.
		} catch (IntrospectionException e) {
			throw new RuntimeException("Error creating property descriptor", e);
		}
		return null;
	}

	@Override
	public Object getPropertyValue(String propertyPath) {
		if (PropertyAccessorUtils.isNestedProperty(propertyPath)) {
			String baseProperty = getBasePropertyName(propertyPath);
			String childPropertyPath = getChildPropertyPath(propertyPath);
			return ((PropertyAccessor) childPropertyAccessors.getUnchecked(baseProperty))
					.getPropertyValue(childPropertyPath);
		} else if (isStrictNullHandlingEnabled() && getTarget() == null) {
			throw new NullValueInNestedPathException(getTargetClass(),
					propertyPath);
		} else {
			return super.getPropertyValue(propertyPath);
		}
	}

	@Override
	public void setPropertyValue(String propertyPath, Object value) {
		if (PropertyAccessorUtils.isNestedProperty(propertyPath)) {
			String baseProperty = getBasePropertyName(propertyPath);
			String childPropertyPath = getChildPropertyPath(propertyPath);
			((PropertyAccessor) childPropertyAccessors.getUnchecked(baseProperty))
					.setPropertyValue(childPropertyPath, value);
		} else if (isStrictNullHandlingEnabled() && getTarget() == null) {
			throw new NullValueInNestedPathException(getTargetClass(),
					propertyPath);
		} else {
			super.setPropertyValue(propertyPath, value);
		}
	}

	protected String getBasePropertyName(String propertyPath) {
		int index = PropertyAccessorUtils
				.getFirstNestedPropertySeparatorIndex(propertyPath);
		return index == -1 ? propertyPath : propertyPath.substring(0, index);
	}

	protected String getChildPropertyPath(String propertyPath) {
		int index = PropertyAccessorUtils
				.getFirstNestedPropertySeparatorIndex(propertyPath);
		if (index == -1) {
			return "";
		}
		return propertyPath.substring(index + 1);
	}

	protected PropertyAccessor getChildPropertyAccessor(String propertyName) {
		return (PropertyAccessor) childPropertyAccessors.getUnchecked(propertyName);
	}

	protected abstract AbstractNestedMemberPropertyAccessor createChildPropertyAccessor(
			String propertyName);

	protected void clearChildPropertyAccessorCache() {
		childPropertyAccessors.invalidateAll();
	}
}