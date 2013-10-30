package org.valkyriercp.form.builder;

import javax.swing.*;

/**
 * <p>
 * This provides a way to wrap functionality around components and labels.
 * </p>
 *
 * <p>
 * Example uses include changing the color when validation fails, or attaching a
 * right-click popup menu.
 * </p>
 *
 * @author oliverh
 */
public interface FormComponentInterceptor {

	/**
	 * Perform some kind of processing on the label.
	 *
	 * @param propertyName the name of the property that the label is for
	 * @param label the label to process
	 */
	public void processLabel(String propertyName, JComponent label);

	/**
	 * Perform some kind of processing on the component.
	 *
	 * @param propertyName the name of the property that the component is for
	 * @param component the component to process
	 */
	public void processComponent(String propertyName, JComponent component);

}