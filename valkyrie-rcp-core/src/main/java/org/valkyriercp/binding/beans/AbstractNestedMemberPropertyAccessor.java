package org.valkyriercp.binding.beans;

import org.springframework.beans.*;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.PropertyTypeDescriptor;
import org.springframework.util.CachingMapDecorator;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

/**
 * This implementation extends {@link AbstractMemberPropertyAccessor} with the
 * functionality of nested property handling.
 *
 * @author Arne Limburg
 *
 */
public abstract class AbstractNestedMemberPropertyAccessor extends AbstractMemberPropertyAccessor {

	private AbstractNestedMemberPropertyAccessor parentPropertyAccessor;

	private String basePropertyName;

	private final ChildPropertyAccessorCache childPropertyAccessors = new ChildPropertyAccessorCache();

	private final boolean strictNullHandlingEnabled;

	protected AbstractNestedMemberPropertyAccessor(Class targetClass, boolean fieldAccessEnabled,
			boolean strictNullHandlingEnabled) {
		super(targetClass, fieldAccessEnabled);
		this.strictNullHandlingEnabled = strictNullHandlingEnabled;
	}

	public AbstractNestedMemberPropertyAccessor(AbstractNestedMemberPropertyAccessor parent, String baseProperty) {
		super(parent.getPropertyType(baseProperty), parent.isFieldAccessEnabled());
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
		}
		else {
			return null;
		}
	}

	public Class getTargetClass() {
		if (parentPropertyAccessor != null) {
			return parentPropertyAccessor.getPropertyType(basePropertyName);
		}
		else {
			return super.getTargetClass();
		}
	}

	public boolean isReadableProperty(String propertyPath) {
		if (PropertyAccessorUtils.isNestedProperty(propertyPath)) {
			String baseProperty = getBasePropertyName(propertyPath);
			String childPropertyPath = getChildPropertyPath(propertyPath);
			if (!super.isReadableProperty(baseProperty)) {
				return false;
			}
			else {
				return ((PropertyAccessor) childPropertyAccessors.get(baseProperty))
						.isReadableProperty(childPropertyPath);
			}
		}
		else {
			return super.isReadableProperty(propertyPath);
		}
	}

	public boolean isWritableProperty(String propertyPath) {
		if (PropertyAccessorUtils.isNestedProperty(propertyPath)) {
			String baseProperty = getBasePropertyName(propertyPath);
			String childPropertyPath = getChildPropertyPath(propertyPath);
			return super.isReadableProperty(baseProperty)
					&& ((PropertyAccessor) childPropertyAccessors.get(baseProperty))
							.isWritableProperty(childPropertyPath);
		}
		else {
			return super.isWritableProperty(propertyPath);
		}
	}

	public Class getPropertyType(String propertyPath) {
		if (PropertyAccessorUtils.isNestedProperty(propertyPath)) {
			String baseProperty = getBasePropertyName(propertyPath);
			String childPropertyPath = getChildPropertyPath(propertyPath);
			return ((PropertyAccessor) childPropertyAccessors.get(baseProperty)).getPropertyType(childPropertyPath);
		}
		else {
			return super.getPropertyType(propertyPath);
		}
	}

    public TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException {
		try {
			String actualPropertyName = org.springframework.beans.PropertyAccessorUtils.getPropertyName(propertyName);
			PropertyDescriptor pd = new PropertyDescriptor(actualPropertyName, getTargetClass());
			if (pd != null) {
				Class type = getPropertyType(propertyName);
				if (pd.getReadMethod() != null) {
					return new PropertyTypeDescriptor(pd, new MethodParameter(pd.getReadMethod(), -1), type);
				}
				else if (pd.getWriteMethod() != null) {
					return new PropertyTypeDescriptor(pd, BeanUtils.getWriteMethodParameter(pd), type);
				}
			}
		}
		catch (InvalidPropertyException ex) {
			// Consider as not determinable.
		} catch (IntrospectionException e) {
            throw new RuntimeException("Error creating property descriptor", e);
        }
        return null;
	}

	public Object getPropertyValue(String propertyPath) {
		if (PropertyAccessorUtils.isNestedProperty(propertyPath)) {
			String baseProperty = getBasePropertyName(propertyPath);
			String childPropertyPath = getChildPropertyPath(propertyPath);
			return ((PropertyAccessor) childPropertyAccessors.get(baseProperty)).getPropertyValue(childPropertyPath);
		}
		else if (isStrictNullHandlingEnabled() && getTarget() == null) {
			throw new NullValueInNestedPathException(getTargetClass(), propertyPath);
		}
		else {
			return super.getPropertyValue(propertyPath);
		}
	}

	public void setPropertyValue(String propertyPath, Object value) {
		if (PropertyAccessorUtils.isNestedProperty(propertyPath)) {
			String baseProperty = getBasePropertyName(propertyPath);
			String childPropertyPath = getChildPropertyPath(propertyPath);
			((PropertyAccessor) childPropertyAccessors.get(baseProperty)).setPropertyValue(childPropertyPath, value);
		}
		else if (isStrictNullHandlingEnabled() && getTarget() == null) {
			throw new NullValueInNestedPathException(getTargetClass(), propertyPath);
		}
		else {
			super.setPropertyValue(propertyPath, value);
		}
	}

	protected String getBasePropertyName(String propertyPath) {
		int index = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(propertyPath);
		return index == -1 ? propertyPath : propertyPath.substring(0, index);
	}

	protected String getChildPropertyPath(String propertyPath) {
		int index = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(propertyPath);
		if (index == -1) {
			return "";
		}
		return propertyPath.substring(index + 1);
	}

	protected PropertyAccessor getChildPropertyAccessor(String propertyName) {
		return (PropertyAccessor) childPropertyAccessors.get(propertyName);
	}

	protected abstract AbstractNestedMemberPropertyAccessor createChildPropertyAccessor(String propertyName);

	protected void clearChildPropertyAccessorCache() {
		childPropertyAccessors.clear();
	}

	private class ChildPropertyAccessorCache extends CachingMapDecorator {

		protected Object create(Object propertyName) {
			return createChildPropertyAccessor((String) propertyName);
		}
	}
}