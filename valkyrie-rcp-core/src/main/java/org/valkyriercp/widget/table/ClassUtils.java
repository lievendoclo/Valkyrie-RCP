package org.valkyriercp.widget.table;

import org.apache.commons.beanutils.MethodUtils;

import java.lang.reflect.Method;

/**
 * A number of helper classes to retrieve getter and setter methods.
 * 
 * @author Jan Hoskens
 * @since 0.5.0
 */
public class ClassUtils
{

    /**
     * No instantiation possible.
     */
    private ClassUtils()
    {
    }

    /**
     * Lookup the getter method for the given property. This can be a getXXX() or a isXXX() method.
     * 
     * @param clazz
     *            type which contains the property.
     * @param propertyName
     *            name of the property.
     * @return a Method with read-access for the property.
     */
    public static Method getReadMethod(Class<?> clazz, String propertyName) throws NoSuchMethodError
    {
        String propertyNameCapitalized = capitalize(propertyName);
        try
        {
            return clazz.getMethod("get" + propertyNameCapitalized);
        }
        catch (Exception e)
        {
            try
            {
                return clazz.getMethod("is" + propertyNameCapitalized);
            }
            catch (Exception e1)
            {
                throw new NoSuchMethodError("Could not find getter (getXX or isXXX) for property: "
                        + propertyName);
            }
        }
    }

    /**
     * Returns the type of the property checking if it actually does return something and wrapping primitives
     * if needed.
     * 
     * @param getter
     *            the method to access the property.
     * @return the type of the property.
     * @throws IllegalArgumentException
     *             if the method has a {@link Void} return type.
     */
    public static Class<?> getTypeForProperty(Method getter)
    {
        Class<?> returnType = getter.getReturnType();
        if (returnType.equals(Void.TYPE))
            throw new IllegalArgumentException("Getter " + getter.toString() + " does not have a returntype.");
        else if (returnType.isPrimitive())
            return MethodUtils.getPrimitiveWrapper(returnType);
        return returnType;
    }

    /**
     * Lookup the setter method for the given property.
     * 
     * @param clazz
     *            type which contains the property.
     * @param propertyName
     *            name of the property.
     * @param propertyType
     *            type of the property.
     * @return a Method with write-access for the property.
     */
    public static final Method getWriteMethod(Class<?> clazz, String propertyName, Class<?> propertyType)
    {
        String propertyNameCapitalized = capitalize(propertyName);
        try
        {
            return clazz.getMethod("set" + propertyNameCapitalized, new Class[]{propertyType});
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Small helper method to capitalize the first character of the given string.
     * 
     * @param s
     *            string to capitalize
     * @return a string starting with a capital character.
     */
    public static String capitalize(String s)
    {
        if (s == null || s.length() == 0)
        {
            return s;
        }
        char chars[] = s.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * Create an {@link Accessor} for the given property. A property may be nested using the dot character.
     * 
     * @param clazz
     *            the type containing the property.
     * @param propertyName
     *            the name of the property.
     * @return an Accessor for the property.
     */
    public static Accessor getAccessorForProperty(final Class<?> clazz, final String propertyName)
    {
        int splitPoint = propertyName.indexOf('.');
        if (splitPoint > 0)
        {
            String firstPart = propertyName.substring(0, splitPoint);
            String secondPart = propertyName.substring(splitPoint + 1);
            return new NestedAccessor(clazz, firstPart, secondPart);
        }
        return new SimpleAccessor(clazz, propertyName);
    }

    /**
     * Create a {@link Writer} for the given property. A property may be nested using the dot character.
     * 
     * @param clazz
     *            the type containing the property.
     * @param propertyName
     *            the name of the property.
     * @return a Writer for the property.
     */
    public static Writer getWriterForProperty(final Class<?> beanClass, final String propertyName)
    {
        int splitPoint = propertyName.indexOf('.');
        if (splitPoint > 0)
        {
            String firstPart = propertyName.substring(0, splitPoint);
            String secondPart = propertyName.substring(splitPoint + 1);
            return new NestedWriter(beanClass, firstPart, secondPart);
        }
        return new SimpleWriter(beanClass, propertyName);
    }
}