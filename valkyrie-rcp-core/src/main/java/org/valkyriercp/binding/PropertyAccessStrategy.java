package org.valkyriercp.binding;

import org.springframework.beans.BeansException;

/**
 * A strategy for accessing a domain object's properties. Different
 * implementations could access different backing objects such as a javabean
 * <code>(BeanPropertyAccessStrategy)</code>, hashmap, rowset, or other data
 * structure.
 *
 * @author Keith Donald
 */
public interface PropertyAccessStrategy {

	/**
	 * Get the value of a property.
	 *
	 * @param propertyPath name of the property to get the value of
	 * @return the value of the property
	 * @throws org.springframework.beans.FatalBeanException if there is no such property, if the property
	 * isn't readable, or if the property getter throws an exception.
	 */
	Object getPropertyValue(String propertyPath) throws BeansException;

	/**
	 * Get a metadata accessor, which can return meta information about
	 * particular properties of the backed domain object.
	 *
	 * @return The meta accessor.
	 */
	PropertyMetadataAccessStrategy getMetadataAccessStrategy();

	/**
	 * Return the target, backing domain object for which property access
	 * requests are targeted against.
	 *
	 * @return The backing target object.
	 */
	Object getDomainObject();

}