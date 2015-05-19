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

import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.valkyriercp.core.PropertyChangePublisher;

import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Consists exclusively of static convenience methods for adding
 * and removing <code>PropertyChangeListener</code>s for bound
 * JavaBean properties.<p>
 *
 * TODO: Move this code into {@link org.springframework.beans.BeanUtils}
 *
 * @author Karsten Lentzsch
 * @author Oliver Hutchison
 */
public abstract class PropertyChangeSupportUtils {

    /**
     * Holds the class parameter list that is used to lookup
     * the adder and remover methods for PropertyChangeListeners.
     */
    private static final Class[] NAMED_PCL_PARAMS = new Class[] {String.class, PropertyChangeListener.class};

    /**
     * Checks and answers whether the given class supports bound properties,
     * i.e. it provides a pair of bound property event listener registration methods:
     * <pre>
     * public void addPropertyChangeListener(String, PropertyChangeListener);
     * public void removePropertyChangeListener(String, PropertyChangeListener);
     * </pre>
     *
     * @param beanClass the class to test
     * @return true if the class supports bound properties, false otherwise
     */
    public static boolean supportsBoundProperties(Class beanClass) {
        return PropertyChangePublisher.class.isAssignableFrom(beanClass)
                || ((getNamedPCLAdder(beanClass) != null) && (getNamedPCLRemover(beanClass) != null));
    }

    /**
     * Adds a named property change listener to the given JavaBean. The bean
     * must provide the optional support for listening on named properties
     * as described in section 7.4.5 of the
     * <a href="http://java.sun.com/products/javabeans/docs/spec.html">Java Bean
     * Specification</a>. The bean class must provide the method:
     * <pre>
     * public void addPropertyChangeListener(String, PropertyChangeListener);
     * </pre>
     *
     * @param bean          the JavaBean to add a property change handler
     * @param propertyName  the name of the property to be observed
     * @param listener      the listener to add

     * @throws PropertyNotBindableException
     *     if the property change handler cannot be added successfully
     */
    public static void addPropertyChangeListener(Object bean, String propertyName, PropertyChangeListener listener) {
        Assert.notNull(propertyName, "The property name must not be null.");
        Assert.notNull(listener, "The listener must not be null.");
        if (bean instanceof PropertyChangePublisher) {
            ((PropertyChangePublisher)bean).addPropertyChangeListener(propertyName, listener);
        }
        else {
            Class beanClass = bean.getClass();
            Method namedPCLAdder = getNamedPCLAdder(beanClass);
            if (namedPCLAdder == null)
                throw new FatalBeanException("Could not find the bean method"
                        + "/npublic void addPropertyChangeListener(String, PropertyChangeListener);/nin bean '" + bean
                        + "'");
            try {
                namedPCLAdder.invoke(bean, new Object[] {propertyName, listener});
            }
            catch (InvocationTargetException e) {
                throw new FatalBeanException("Due to an InvocationTargetException we failed to add "
                        + "a named PropertyChangeListener to bean '" + bean + "'", e);
            }
            catch (IllegalAccessException e) {
                throw new FatalBeanException("Due to an IllegalAccessException we failed to add "
                        + "a named PropertyChangeListener to bean '" + bean + "'", e);
            }
        }
    }

    /**
     * Removes a named property change listener to the given JavaBean. The bean
     * must provide the optional support for listening on named properties
     * as described in section 7.4.5 of the
     * <a href="http://java.sun.com/products/javabeans/docs/spec.html">Java Bean
     * Specification</a>. The bean class must provide the method:
     * <pre>
     * public void removePropertyChangeHandler(String, PropertyChangeListener);
     * </pre>
     *
     * @param bean          the bean to remove the property change listener from
     * @param propertyName  the name of the observed property
     * @param listener      the listener to remove
     * @throws FatalBeanException
     *     if the property change handler cannot be removed successfully
     */
    public static void removePropertyChangeListener(Object bean, String propertyName, PropertyChangeListener listener) {
        Assert.notNull(propertyName, "The property name must not be null.");
        Assert.notNull(listener, "The listener must not be null.");
        if (bean instanceof PropertyChangePublisher) {
            ((PropertyChangePublisher)bean).removePropertyChangeListener(propertyName, listener);
        }
        else {
            Class beanClass = bean.getClass();
            Method namedPCLRemover = getNamedPCLRemover(beanClass);
            if (namedPCLRemover == null)
                throw new FatalBeanException("Could not find the bean method"
                        + "/npublic void removePropertyChangeListener(String, PropertyChangeListener);/nin bean '"
                        + bean + "'");
            try {
                namedPCLRemover.invoke(bean, new Object[] {propertyName, listener});
            }
            catch (InvocationTargetException e) {
                throw new FatalBeanException("Due to an InvocationTargetException we failed to remove "
                        + "a named PropertyChangeListener from bean '" + bean + "'", e);
            }
            catch (IllegalAccessException e) {
                throw new FatalBeanException("Due to an IllegalAccessException we failed to remove "
                        + "a named PropertyChangeListener from bean '" + bean + "'", e);
            }
        }
    }

    /**
     * Looks up and returns the method that adds a PropertyChangeListener
     * for a specified property name to instances of the given class.
     *
     * @param beanClass   the class that provides the adder method
     * @return the method that adds the PropertyChangeListeners
     */
    private static Method getNamedPCLAdder(Class beanClass) {
        try {
            return beanClass.getMethod("addPropertyChangeListener", NAMED_PCL_PARAMS);
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Looks up and returns the method that removes a PropertyChangeListener
     * for a specified property name from instances of the given class.
     *
     * @param beanClass   the class that provides the remover method
     * @return the method that removes the PropertyChangeListeners
     */
    private static Method getNamedPCLRemover(Class beanClass) {
        try {
            return beanClass.getMethod("removePropertyChangeListener", NAMED_PCL_PARAMS);
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }
}

