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

/**
 * Sub-interface implemented by form models that can be part of a form model
 * hierarchy.
 * <p>
 * In a HierarchicalFormModel:
 * <ul>
 * <li>the enabled state of the parent is inherited by the children. So if the
 * parent is disabled then the child is also disabled; however if the child is
 * disabled the parent might not be disabled.
 * <li>the dirty state of the chidren is inherited by the parent. So if one or
 * more of the children are dirty then the parent is also dirty. However if the
 * parent is dirty the children my not be.
 * <li>the readOnly state of the parent is inherited by the children. If the
 * parent is readOnly, all children are readOnly as well. However a child may be
 * readOnly while the parent is not.
 * </ul>
 *
 * @author Oliver Hutchison
 */
public interface HierarchicalFormModel extends FormModel {

	/**
	 * Returns the parent form model or null of there is none.
	 */
	HierarchicalFormModel getParent();

	/**
	 * Returns an array of child form models.
	 */
	FormModel[] getChildren();

	/**
	 * Sets the parent form model.
	 */
	void setParent(HierarchicalFormModel parent);

	/**
	 * Remove the parent form model
	 */
	void removeParent();

	/**
	 * Adds a new child to the form model. The child form model will have it's
	 * parent set to this.
	 */
	void addChild(HierarchicalFormModel child);

	/**
	 * Removes a child from this form model.
	 */
	void removeChild(HierarchicalFormModel child);
}