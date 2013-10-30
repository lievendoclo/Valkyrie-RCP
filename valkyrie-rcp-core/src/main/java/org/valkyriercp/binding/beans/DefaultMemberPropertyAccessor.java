package org.valkyriercp.binding.beans;

import org.springframework.beans.*;
import org.springframework.core.JdkVersion;
import org.springframework.core.MethodParameter;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class implements actual access to properties for
 * <tt>AbstractNestedMemberPropertyAccessor</tt>.
 *
 * @author Arne Limburg
 */
public class DefaultMemberPropertyAccessor extends AbstractNestedMemberPropertyAccessor {

    private Object target;

    private boolean fixedTargetClass;

    public DefaultMemberPropertyAccessor(Class targetClass) {
        this(targetClass, null, false, false);
    }

    public DefaultMemberPropertyAccessor(Object target) {
        this(target, false, true);
    }

    public DefaultMemberPropertyAccessor(Object target, boolean fieldAccessEnabled, boolean strictNullHandlingEnabled) {
        super(target.getClass(), fieldAccessEnabled, strictNullHandlingEnabled);
        setTarget(target);
    }

    public DefaultMemberPropertyAccessor(Class targetClass, Object target, boolean fieldAccessEnabled, boolean strictNullHandlingEnabled) {
        super(targetClass, fieldAccessEnabled, strictNullHandlingEnabled);
        fixedTargetClass = true;
        setTarget(target);
    }

    protected DefaultMemberPropertyAccessor(AbstractNestedMemberPropertyAccessor parent, String baseProperty) {
        super(parent, baseProperty);
    }

    public Object getTarget() {
        if (target != null) {
            return target;
        } else {
            return super.getTarget();
        }
    }

    public void setTarget(Object target) {
        if (getParentPropertyAccessor() != null) {
            throw new IllegalStateException("explicite setting of target is not allowed for child property accessors");
        }
        this.target = target;
        if (!fixedTargetClass && target != null && target.getClass() != getTargetClass()) {
            setTargetClass(target.getClass());
            clearChildPropertyAccessorCache();
        }
    }

    public Object getIndexedPropertyValue(String propertyName) throws BeansException {
        if (getPropertyType(propertyName) == null) {
            throw new NotReadablePropertyException(getTargetClass(), propertyName,
                    "property type could not be determined");
        }
        String rootPropertyName = getRootPropertyName(propertyName);
        Member readAccessor = getReadPropertyAccessor(rootPropertyName);
        if (readAccessor == null) {
            throw new NotReadablePropertyException(getTargetClass(), propertyName,
                    "Neither non-static field nor get-method exists for indexed property");
        }
        Object rootProperty = getPropertyValue(rootPropertyName);
        if (rootProperty == null) {
            if (isStrictNullHandlingEnabled()) {
                throw new NullValueInNestedPathException(getTargetClass(), propertyName);
            } else if (isWritableProperty(rootPropertyName)) {
                return null;
            } else {
                throw new NotReadablePropertyException(getTargetClass(), propertyName);
            }
        }
        Object[] indices;
        try {
            indices = getIndices(propertyName);
        } catch (Exception e) {
            // could not convert indices
            throw createNotReadablePropertyException(propertyName, e);
        }
        return getPropertyValue(rootProperty, indices);
    }

    public Object getSimplePropertyValue(String propertyName) throws BeansException {
        Member readAccessor = getReadPropertyAccessor(propertyName);
        if (readAccessor == null) {
            throw new NotReadablePropertyException(getTargetClass(), propertyName,
                    "Neither non-static field nor get-method does exist");
        }
        Object target = getTarget();
        if (target == null) {
            return null;
        }
        try {
            ReflectionUtils.makeAccessible(readAccessor);
            if (readAccessor instanceof Field) {
                return ((Field) readAccessor).get(target);
            } else {// readAccessor instanceof Method
                return ((Method) readAccessor).invoke(target, null);
            }
        } catch (IllegalAccessException e) {
            throw new InvalidPropertyException(getTargetClass(), propertyName, "Property is not accessible", e);
        } catch (InvocationTargetException e) {
            ReflectionUtils.handleInvocationTargetException(e);
            throw new IllegalStateException(
                    "An unexpected state occured during getSimplePropertyValue(String). This may be a bug.");
        }
    }

    private Object getPropertyValue(Object assemblage, Object[] indices) {
        return getPropertyValue(assemblage, indices, 0);
    }

    private Object getPropertyValue(Object assemblage, Object[] indices, int parameterIndex) {
        if (assemblage == null) {
            if (isStrictNullHandlingEnabled()) {
                throw new NullValueInNestedPathException(getTargetClass(), "");
            } else {
                return null;
            }
        }
        Object value = null;
        if (assemblage.getClass().isArray()) {
            value = getArrayValue(assemblage, (Integer) indices[parameterIndex]);
        } else if (assemblage instanceof List) {
            value = getListValue((List) assemblage, (Integer) indices[parameterIndex]);
        } else if (assemblage instanceof Map) {
            value = getMapValue((Map) assemblage, indices[parameterIndex]);
        } else if (assemblage instanceof Collection) {
            value = getCollectionValue((Collection) assemblage, (Integer) indices[parameterIndex]);
        } else {
            throw new IllegalStateException(
                    "getPropertyValue(Object, Object[], int) called with neither array nor collection nor map");
        }
        if (parameterIndex == indices.length - 1) {
            return value;
        }
        if (value == null) {
            if (isStrictNullHandlingEnabled()) {
                throw new InvalidPropertyException(getTargetClass(), "", "");
            } else {
                return null;
            }
        }
        return getPropertyValue(value, indices, parameterIndex + 1);
    }

    private Object getArrayValue(Object array, Integer index) {
        if (Array.getLength(array) > index.intValue()) {
            return Array.get(array, index.intValue());
        } else if (isStrictNullHandlingEnabled()) {
            throw new InvalidPropertyException(getTargetClass(), "", "");
        } else {
            return null;
        }
    }

    private Object getListValue(List list, Integer index) {
        if (list.size() > index.intValue()) {
            return list.get(index.intValue());
        } else if (isStrictNullHandlingEnabled()) {
            throw new InvalidPropertyException(getTargetClass(), "", "");
        } else {
            return null;
        }
    }

    private Object getMapValue(Map map, Object key) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            if (!JdkVersion.isAtLeastJava15()) {
                // we don't know the type of the keys, so we fall back to
                // comparing toString()
                for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
                    Map.Entry entry = (Map.Entry) i.next();
                    if (entry.getKey() == key
                            || (entry.getKey() != null && key != null && entry.getKey().toString().equals(
                            key.toString()))) {
                        return entry.getValue();
                    }
                }
            }
            return null;
        }
    }

    private Object getCollectionValue(Collection collection, Integer index) {
        if (collection.size() > index.intValue()) {
            Iterator iterator = collection.iterator();
            for (int i = 0; i < index.intValue(); i++) {
                iterator.next();
            }
            return iterator.next();
        } else if (isStrictNullHandlingEnabled()) {
            throw new InvalidPropertyException(getTargetClass(), "", "");
        } else {
            return null;
        }
    }

    public void setIndexedPropertyValue(String propertyName, Object value) throws BeansException {
        String parentPropertyName = getParentPropertyName(propertyName);
        Object parentValue;
        try {
            parentValue = getPropertyValue(parentPropertyName);
        } catch (NotReadablePropertyException e) {
            throw new NotWritablePropertyException(getTargetClass(), propertyName, "parent property is not readable", e);
        }
        if (parentValue == null) {
            if (isWritableProperty(parentPropertyName)) {
                throw new NullValueInNestedPathException(getTargetClass(), propertyName);
            } else {
                throw new NotWritablePropertyException(getTargetClass(), propertyName);
            }
        }
        Object[] indices;
        try {
            indices = getIndices(propertyName);
        } catch (Exception e) {
            throw new NotWritablePropertyException(getTargetClass(), propertyName, "wrong index type", e);
        }
        Object index = indices[indices.length - 1];
        Object newParentValue = setAssemblageValue(getPropertyType(parentPropertyName), parentValue, index, value);
        if (newParentValue != parentValue) {
            setPropertyValue(parentPropertyName, newParentValue);
        }
    }

    public void setSimplePropertyValue(String propertyName, Object value) throws BeansException {
        Member writeAccessor = getWritePropertyAccessor(propertyName);
        if (writeAccessor == null) {
            throw new NotWritablePropertyException(getTargetClass(), propertyName,
                    "Neither non-static, non-final field nor set-method does exist");
        }
        Object target = getTarget();
        if (target == null) {
            throw new NullValueInNestedPathException(getTargetClass(), propertyName);
        }
        try {
            ReflectionUtils.makeAccessible(writeAccessor);
            if (writeAccessor instanceof Field) {
                ((Field) writeAccessor).set(target, value);
            } else {// writeAccessor instanceof Method
                ((Method) writeAccessor).invoke(target, new Object[]{value});
            }
        } catch (IllegalAccessException e) {
            throw new InvalidPropertyException(getTargetClass(), propertyName, "Property is not accessible", e);
        } catch (InvocationTargetException e) {
            ReflectionUtils.handleInvocationTargetException(e);
            throw new IllegalStateException(
                    "An unexpected state occured during setPropertyValue(String, Object). This may be a bug.");
        }
    }

    protected AbstractNestedMemberPropertyAccessor createChildPropertyAccessor(String propertyName) {
        return new DefaultMemberPropertyAccessor(this, propertyName);
    }

    public Object convertIfNecessary(Object value, Class requiredType, MethodParameter methodParam)
            throws TypeMismatchException {
        // TODO Auto-generated method stub
        return null;
    }

    public static class ReflectionUtils extends org.springframework.util.ReflectionUtils {

        public static void makeAccessible(Member member) {
            if (!Modifier.isPublic(member.getModifiers()) ||
                    !Modifier.isPublic(member.getDeclaringClass().getModifiers())) {
                if (member instanceof Field) {
                    ((Field) member).setAccessible(true);
                } else if (member instanceof Constructor) {
                    ((Constructor) member).setAccessible(true);
                } else if (member instanceof Method) {
                    ((Method) member).setAccessible(true);
                }
            }
        }
    }
}