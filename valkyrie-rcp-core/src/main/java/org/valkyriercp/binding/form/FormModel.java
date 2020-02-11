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
package org.valkyriercp.binding.form;

import org.valkyriercp.convert.converters.Converter;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.core.Authorizable;
import org.valkyriercp.core.PropertyChangePublisher;

import java.util.Set;

/**
 * <p>
 * A formModel groups valueModels and actions that are related to the same
 * backing object. It will manage the creation/handling of the valueModels,
 * their associated metadata and an overal state.
 * </p>
 *
 * <p>
 * The formModel can be in one of three 'user' states:
 * <ul>
 * <li><em>Disabled:</em> all user interaction should be ignored.</li>
 * <li><em>Enabled and readOnly:</em> user may interact, but not edit.</li>
 * <li><em>Enabled and editable:</em> user may interact and edit where
 * needed.</li>
 * </ul>
 * These states are counterparts of the enabled/editable state of a
 * component/textcomponent.
 * </p>
 *
 * <p>
 * The formModel also tracks its inner states:
 * <ul>
 * <li><em>Dirty:</em> one of its valueModels has a changed value.</li>
 * <li><em>Committable:</em> all valueModels are in an 'error-free' state.</li>
 * </ul>
 * </>
 *
 * @see ConfigurableFormModel
 *
 * @author Keith Donald
 * @author Oliver Hutchison
 */
public interface FormModel extends PropertyChangePublisher, Authorizable {

	/** The name of the bound property <em>dirty</em>. */
	static final String DIRTY_PROPERTY = "dirty";

	/** The name of the bound property <em>enabled</em>. */
	static final String ENABLED_PROPERTY = "enabled";

	/** The name of the bound property <em>readonly</em>. */
	static final String READONLY_PROPERTY = "readOnly";

	/** The name of the bound property <em>committable</em>. */
	static final String COMMITTABLE_PROPERTY = "committable";

	/**
	 * Returns the id that is used to identify this form model.
	 */
	String getId();

	/**
	 * Returns the object currently backing this form. This object is held by
	 * the FormObjectHolder.
	 */
	Object getFormObject();

	/**
	 * Sets the object currently backing this form.
	 */
	void setFormObject(Object formObject);

	/**
	 * Returns the value model which holds the object currently backing this
	 * form.
	 */
	ValueModel getFormObjectHolder();

	/**
	 * Returns a value model that holds the value of the specified form
	 * property.
	 *
	 * @throws org.springframework.beans.InvalidPropertyException if the form has no such property
	 */
	ValueModel getValueModel(String formProperty);

	/**
	 * Returns a type converting value model for the given form property. The
	 * type of the value returned from the returned value model is guaranteed to
	 * be of class targetClass.
	 *
	 * @throws org.springframework.beans.InvalidPropertyException if the form has no such property
	 * @throws IllegalArgumentException if no suitable converter from the
	 * original property class to the targetClass can be found
	 */
	<T> ValueModel<T> getValueModel(String formProperty, Class<T> targetClass);

	/**
	 * Returns the metadata for the given form field.
	 */
	FieldMetadata getFieldMetadata(String field);

	/**
	 * Returns the fields that are used by this formModel. Each field has an
	 * associated ValueModel.
	 */
	Set getFieldNames();

	/**
	 * Register converters for a given property name.
	 *
	 * @param propertyName name of property on which to register converters
	 * @param toConverter Convert from source to target type
	 * @param fromConverter Convert from target to source type
	 */
	void registerPropertyConverter(String propertyName, Converter toConverter, Converter fromConverter);

	/**
	 * Returns true if the form has a value model for the provided property
	 * name.
	 */
	boolean hasValueModel(String formProperty);

	/**
	 * Commits any changes buffered by the form property value models into the
	 * current form backing object.
	 *
	 * @throws IllegalStateException if the form model is not committable
	 * @see #isCommittable()
	 */
	void commit();

	/**
	 * Reverts any dirty value models back to the original values that were
	 * loaded from the current form backing object since last call to either
	 * commit or revert or since the last change of the form backing object.
	 */
	void revert();

	/**
	 * Reset the form by replacing the form object with a newly instantiated
	 * object of the type of the current form object. Note that this may lead to
	 * NPE's if the newly created object has null sub-objects and this form
	 * references any of these objects.
	 */
	void reset();

	/**
	 * Does this form model buffer changes?
	 */
	boolean isBuffered();

	/**
	 * Returns true if any of the value models holding properties of this form
	 * have been modified since the last call to either commit or revert or
	 * since the last change of the form backing object.
	 */
	boolean isDirty();

	/**
	 * A form can be enabled/disabled which reflects a global state on the
	 * associated valueModels and their metaData. It may be viewed as enabling
	 * the visual representatives of the valuemodels. All user related
	 * interaction should be disabled. This is usually viewed as a grey-out of
	 * the visual form.
	 *
	 * Returns <code>true</code> if this form is enabled.
	 */
	boolean isEnabled();

	/**
	 * A form can be set as readOnly which reflects a global state on the
	 * valueModels and their metaData. A form may be enabled and readonly when
	 * all values are accessible, but not changeable. A form can be seen as not
	 * readOnly if some visual representatives of the valuemodels are set to
	 * editable/changeable.
	 *
	 * @return <code>true</code> if this form is readOnly.
	 */
	boolean isReadOnly();

	/**
	 * Returns true if the changes held by this form are able to be committed. A
	 * form is committable when it and it's child form models have no validation
	 * errors.
	 */
	boolean isCommittable();

	/**
	 * Adds the specified listener to the list if listeners notified when a
	 * commit happens.
	 */
	void addCommitListener(CommitListener listener);

	/**
	 * Removes the specified listener to the list if listeners notified when a
	 * commit happens.
	 */
	void removeCommitListener(CommitListener listener);

	/**
	 * FIXME: this should be on the FieldMetadata class
	 */
	FieldFace getFieldFace(String field);
}