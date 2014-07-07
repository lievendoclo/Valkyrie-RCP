package org.valkyriercp.binding.beans;

import java.beans.PropertyEditor;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.AbstractPropertyAccessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.core.JdkVersion;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;

/**
 * PropertyAccessor implementation that determines property types by field, if
 * available. Otherwise methods are used.
 * 
 * Actual access to properties is left for implementation for subclasses.
 * 
 * This implementation does not support nested properties. Use
 * {@link AbstractNestedMemberPropertyAccessor}, if you need nested
 * property-support.
 * 
 * @author Arne Limburg
 * 
 */
public abstract class AbstractMemberPropertyAccessor extends
		AbstractPropertyAccessor {

	private Class targetClass;

	private final boolean fieldAccessEnabled;

	private final Map readAccessors = new HashMap();

	private final Map writeAccessors = new HashMap();

	/**
	 * Creates a new <tt>AbstractMemberPropertyAccessor</tt>.
	 * 
	 * @param targetClass
	 *            the target class.
	 * @param fieldAccessEnabled
	 *            whether field access should be used for property type
	 *            determination.
	 */
	protected AbstractMemberPropertyAccessor(Class targetClass,
			boolean fieldAccessEnabled) {
		this.fieldAccessEnabled = fieldAccessEnabled;
		registerDefaultEditors();
		setTargetClass(targetClass);
	}

	/**
	 * Clears all cached members and introspect methods again. If fieldAccess is
	 * enabled introspect fields as well.
	 * 
	 * @param targetClass
	 *            the target class.
	 */
	protected void setTargetClass(Class targetClass) {
		this.targetClass = targetClass;
		this.readAccessors.clear();
		this.writeAccessors.clear();
		introspectMethods(targetClass, new HashSet());
		if (isFieldAccessEnabled()) {
			introspectFields(targetClass, new HashSet());
		}
	}

	/**
	 * Introspect fields of a class. This excludes static fields and handles
	 * final fields as readOnly.
	 * 
	 * @param type
	 *            the class to inspect.
	 * @param introspectedClasses
	 *            a set of already inspected classes.
	 */
	private void introspectFields(Class type, Set introspectedClasses) {
		if (type == null || Object.class.equals(type) || type.isInterface()
				|| introspectedClasses.contains(type)) {
			return;
		}
		introspectedClasses.add(type);
		introspectFields(type.getSuperclass(), introspectedClasses);
		Field[] fields = type.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (!Modifier.isStatic(fields[i].getModifiers())) {
				readAccessors.put(fields[i].getName(), fields[i]);
				if (!Modifier.isFinal(fields[i].getModifiers())) {
					writeAccessors.put(fields[i].getName(), fields[i]);
				}
			}
		}
	}

	/**
	 * Introspect class for accessor methods. This includes methods starting
	 * with 'get', 'set' and 'is'.
	 * 
	 * @param type
	 *            class to introspect.
	 * @param introspectedClasses
	 *            set of already inspected classes.
	 */
	private void introspectMethods(Class type, Set introspectedClasses) {
		if (type == null || Object.class.equals(type)
				|| introspectedClasses.contains(type)) {
			return;
		}
		introspectedClasses.add(type);
		Class[] interfaces = type.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			introspectMethods(interfaces[i], introspectedClasses);
		}
		introspectMethods(type.getSuperclass(), introspectedClasses);
		Method[] methods = type.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			String methodName = methods[i].getName();
			if (methodName.startsWith("get")
					&& methods[i].getParameterTypes().length == 0) {
				readAccessors.put(getPropertyName(methodName, 3), methods[i]);
			} else if (methodName.startsWith("is")
					&& methods[i].getParameterTypes().length == 0) {
				readAccessors.put(getPropertyName(methodName, 2), methods[i]);
			} else if (methodName.startsWith("set")
					&& methods[i].getParameterTypes().length == 1) {
				writeAccessors.put(getPropertyName(methodName, 3), methods[i]);
			}
		}
	}

	/**
	 * Returns whether this PropertyAccessor should inspect fields.
	 */
	public boolean isFieldAccessEnabled() {
		return fieldAccessEnabled;
	}

	/**
	 * Returns the class used to introspect members.
	 */
	public Class getTargetClass() {
		return targetClass;
	}

	/**
	 * Return the read accessor for the given property.
	 * 
	 * @param propertyName
	 *            name of the property.
	 * @return a Member to read the property or <code>null</code>.
	 */
	protected Member getReadPropertyAccessor(String propertyName) {
		return (Member) readAccessors.get(propertyName);
	}

	/**
	 * Return the write accessor for the given property.
	 * 
	 * @param propertyName
	 *            name of the property.
	 * @return a Member to write the property or <code>null</code>.
	 */
	protected Member getWritePropertyAccessor(String propertyName) {
		return (Member) writeAccessors.get(propertyName);
	}

	/**
	 * Return any accessor, be it read or write, for the given property.
	 * 
	 * @param propertyName
	 *            name of the property.
	 * @return an accessor for the property or <code>null</code>
	 */
	protected Member getPropertyAccessor(String propertyName) {
		if (readAccessors.containsKey(propertyName)) {
			return (Member) readAccessors.get(propertyName);
		} else {
			return (Member) writeAccessors.get(propertyName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isReadableProperty(String propertyName) {
		if (PropertyAccessorUtils.isIndexedProperty(propertyName)) {
			String rootProperty = getRootPropertyName(propertyName);
			String parentProperty = getParentPropertyName(propertyName);
			return isReadableProperty(rootProperty)
					&& checkKeyTypes(propertyName)
					&& (!getPropertyType(parentProperty).isArray()
							|| checkSize(propertyName) || isWritableProperty(parentProperty))
					&& ((isReadableProperty(parentProperty) && getPropertyValue(parentProperty) != null) || isWritableProperty(parentProperty));
		} else {
			return readAccessors.containsKey(propertyName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isWritableProperty(String propertyName) {
		if (PropertyAccessorUtils.isIndexedProperty(propertyName)) {
			// if an indexed property is readable it is writable, too
			return isReadableProperty(propertyName);
		} else {
			return writeAccessors.containsKey(propertyName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Class getPropertyType(String propertyName) {
		if (PropertyAccessorUtils.isIndexedProperty(propertyName)) {
			int nestingLevel = PropertyAccessorUtils
					.getNestingLevel(propertyName);
			if (JdkVersion.getMajorJavaVersion() >= JdkVersion.JAVA_15) {
				Member accessor = getPropertyAccessor(getRootPropertyName(propertyName));
				if (accessor instanceof Field) {
					return GenericCollectionTypeResolver
							.getIndexedValueFieldType((Field) accessor,
									nestingLevel);
				} else {
					Method accessorMethod = (Method) accessor;
					MethodParameter parameter = new MethodParameter(
							accessorMethod,
							accessorMethod.getParameterTypes().length - 1);
					return GenericCollectionTypeResolver
							.getIndexedValueMethodType(parameter, nestingLevel);
				}
			} else {
				// we can only resolve array types in Java 1.4
				Class type = getPropertyType(getRootPropertyName(propertyName));
				for (int i = 0; i < nestingLevel; i++) {
					if (type.isArray()) {
						type = type.getComponentType();
					} else {
						return Object.class; // cannot resolve type
					}
				}
				return type;
			}
		} else {
			Member readAccessor = (Member) readAccessors.get(propertyName);
			if (readAccessor instanceof Field) {
				return ((Field) readAccessor).getType();
			} else if (readAccessor instanceof Method) {
				return ((Method) readAccessor).getReturnType();
			}
			Member writeAccessor = (Member) writeAccessors.get(propertyName);
			if (writeAccessor instanceof Field) {
				return ((Field) writeAccessor).getType();
			} else if (writeAccessor instanceof Method) {
				return ((Method) writeAccessor).getParameterTypes()[0];
			}
		}
		return null;
	}

	/**
	 * Determine the type of the key used to index the collection/map. When jdk
	 * is at least 1.5, maps can be specified with generics and their key type
	 * can be resolved.
	 * 
	 * @param propertyName
	 *            name of the property.
	 * @return the type of the key. An integer if it's not a map, {@link String}
	 *         if the jdk is less than 1.5, a specific type if the map was
	 *         generified.
	 */
	public Class getIndexedPropertyKeyType(String propertyName) {
		if (!PropertyAccessorUtils.isIndexedProperty(propertyName)) {
			throw new IllegalArgumentException("'" + propertyName
					+ "' is no indexed property");
		}
		Class type = getPropertyType(getParentPropertyName(propertyName));
		if (!Map.class.isAssignableFrom(type)) {
			return Integer.class;
		}
		if (JdkVersion.getMajorJavaVersion() >= JdkVersion.JAVA_15) {
			int nestingLevel = PropertyAccessorUtils
					.getNestingLevel(propertyName) - 1;
			Member accessor = getPropertyAccessor(getRootPropertyName(propertyName));
			if (accessor instanceof Field) {
				return GenericCollectionTypeResolver.getMapKeyFieldType(
						(Field) accessor, nestingLevel);
			} else if (accessor instanceof Method) {
				MethodParameter parameter = new MethodParameter(
						(Method) accessor,
						((Method) accessor).getParameterTypes().length - 1,
						nestingLevel);
				return GenericCollectionTypeResolver
						.getMapKeyParameterType(parameter);
			} else {
				throw new InvalidPropertyException(getTargetClass(),
						propertyName, "property not accessable");
			}
		} else {
			return String.class; // the default for Java 1.4
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getPropertyValue(String propertyName) throws BeansException {
		if (PropertyAccessorUtils.isIndexedProperty(propertyName)) {
			return getIndexedPropertyValue(propertyName);
		} else {
			return getSimplePropertyValue(propertyName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPropertyValue(String propertyName, Object value)
			throws BeansException {
		if (PropertyAccessorUtils.isIndexedProperty(propertyName)) {
			setIndexedPropertyValue(propertyName, value);
		} else {
			setSimplePropertyValue(propertyName, value);
		}
	}

	/**
	 * Retrieve the value of an indexed property.
	 * 
	 * @param propertyName
	 *            name of the property.
	 * @return value of the property.
	 */
	protected abstract Object getIndexedPropertyValue(String propertyName);

	/**
	 * Retrieve the value of a simple property (non-indexed).
	 * 
	 * @param propertyName
	 *            name of the property.
	 * @return value of the property.
	 */
	protected abstract Object getSimplePropertyValue(String propertyName);

	/**
	 * Set the value of an indexed property.
	 * 
	 * @param propertyName
	 *            name of the property.
	 * @param value
	 *            new value for the property.
	 */
	protected abstract void setIndexedPropertyValue(String propertyName,
			Object value);

	/**
	 * Set the value of a simple property (non-indexed).
	 * 
	 * @param propertyName
	 *            name of the property.
	 * @param value
	 *            new value for the property.
	 */
	protected abstract void setSimplePropertyValue(String propertyName,
			Object value);

	/**
	 * Returns the propertyName based on the methodName. Cuts of the prefix and
	 * removes first capital.
	 * 
	 * @param methodName
	 *            name of method to convert.
	 * @param prefixLength
	 *            length of prefix to cut of.
	 * @return property name.
	 */
	protected String getPropertyName(String methodName, int prefixLength) {
		return Character.toLowerCase(methodName.charAt(prefixLength))
				+ methodName.substring(prefixLength + 1);
	}

	/**
	 * Returns the root property of an indexed property. The root property is
	 * the property that contains no indices.
	 * 
	 * @param propertyName
	 *            the name of the property.
	 * @return the root property.
	 */
	protected String getRootPropertyName(String propertyName) {
		int location = propertyName.indexOf(PROPERTY_KEY_PREFIX);
		return location == -1 ? propertyName : propertyName.substring(0,
				location);
	}

	/**
	 * Return the parent property name of an indexed property or the empty
	 * string.
	 * 
	 * @param propertyName
	 *            the name of the property.
	 * @return the empty string or the parent property name if it was indexed.
	 */
	protected String getParentPropertyName(String propertyName) {
		if (!PropertyAccessorUtils.isIndexedProperty(propertyName)) {
			return "";
		} else {
			return propertyName.substring(0,
					propertyName.lastIndexOf(PROPERTY_KEY_PREFIX_CHAR));
		}
	}

	protected boolean checkKeyTypes(String propertyName) {
		try {
			getIndices(propertyName);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected Object[] getIndices(String propertyName) {
		int location = propertyName.indexOf(PROPERTY_KEY_PREFIX);
		if (location == -1) {
			return new Object[0];
		}
		String[] indexStrings = split(propertyName.substring(location));
		Object[] indices = new Object[indexStrings.length];
		String rootPropertyName = getRootPropertyName(propertyName);
		String indexedPropertyName = rootPropertyName;
		for (int i = 0; i < indices.length; i++) {
			indexedPropertyName += '[' + indexStrings[i] + ']';
			Class keyType = getIndexedPropertyKeyType(indexedPropertyName);
			indices[i] = convert(keyType, indexStrings[i]);
		}
		return indices;
	}

	private Object convert(Class targetClass, String value) {
		if (Object.class.equals(targetClass)
				|| String.class.equals(targetClass)) {
			return value;
		}
		PropertyEditor editor = getDefaultEditor(targetClass);
		editor.setAsText(value);
		return editor.getValue();
	}

	private String[] split(String indices) {
		Assert.isTrue(indices.startsWith(PROPERTY_KEY_PREFIX));
		Assert.isTrue(indices.endsWith(PROPERTY_KEY_SUFFIX));
		List result = new ArrayList();
		int fromIndex = 1;
		int toIndex = -1;
		while ((toIndex = indices.indexOf("][", fromIndex)) != -1) {
			result.add(indices.substring(fromIndex, toIndex));
			fromIndex = toIndex + 2;
		}
		result.add(indices.substring(fromIndex, indices.length() - 1));
		return (String[]) result.toArray(new String[result.size()]);
	}

	private boolean checkSize(String propertyName) {
		String parentPropertyName = getParentPropertyName(propertyName);
		if (!getPropertyType(parentPropertyName).isArray()) {
			try {
				// collections are considered to be expandable
				// so if it is not null, any index matches
				return getPropertyValue(parentPropertyName) != null;
			} catch (NotReadablePropertyException e) {
				return false;
			}
		}
		int from = propertyName.lastIndexOf(PROPERTY_KEY_PREFIX_CHAR) + 1;
		int to = propertyName.length() - 1;
		int index = Integer.parseInt(propertyName.substring(from, to));
		try {
			Object parentProperty = getPropertyValue(parentPropertyName);
			return parentProperty != null
					&& Array.getLength(parentProperty) > index;
		} catch (NotReadablePropertyException e) {
			return false;
		}
	}

	protected NotReadablePropertyException createNotReadablePropertyException(
			String propertyName, Exception e) {
		if (JdkVersion.getMajorJavaVersion() >= JdkVersion.JAVA_14) {
			NotReadablePropertyException beanException = new NotReadablePropertyException(
					getTargetClass(), propertyName);
			beanException.initCause(e);
			return beanException;
		} else {
			ByteArrayOutputStream stackTrace = new ByteArrayOutputStream();
			PrintWriter stackTraceWriter = new PrintWriter(stackTrace);
			e.printStackTrace(stackTraceWriter);
			stackTraceWriter.close();
			return new NotReadablePropertyException(getTargetClass(),
					propertyName, new String(stackTrace.toByteArray()));
		}
	}

	/**
	 * Helper method for subclasses to set values of indexed properties, like
	 * map-values, collection-values or array-values.
	 * 
	 * @param assemblageType
	 *            either map or collection or array
	 * @param assemblage
	 *            the assemblage to set the value on
	 * @param index
	 *            the index to set the value at
	 * @param value
	 *            the value to set
	 * @return the assemblage
	 */
	protected Object setAssemblageValue(Class assemblageType,
			Object assemblage, Object index, Object value) {
		if (assemblageType.isArray()) {
			int i = ((Integer) index).intValue();
			if (Array.getLength(assemblage) <= i) {
				Object newAssemblage = Array.newInstance(
						assemblageType.getComponentType(), i + 1);
				System.arraycopy(assemblage, 0, newAssemblage, 0,
						Array.getLength(assemblage));
				assemblage = newAssemblage;
			}
			Array.set(assemblage, i, value);
		} else if (List.class.isAssignableFrom(assemblageType)) {
			int i = ((Integer) index).intValue();
			List list = (List) assemblage;
			if (list.size() > i) {
				list.set(i, value);
			} else {
				while (list.size() < i) {
					list.add(null);
				}
				list.add(value);
			}
		} else if (Map.class.isAssignableFrom(assemblageType)) {
			((Map) assemblage).put(index, value);
		} else if (assemblage instanceof Collection) {
			((Collection) assemblage).add(value);
		} else {
			throw new IllegalArgumentException(
					"assemblage must be of type array, collection or map.");
		}
		return assemblage;
	}
}