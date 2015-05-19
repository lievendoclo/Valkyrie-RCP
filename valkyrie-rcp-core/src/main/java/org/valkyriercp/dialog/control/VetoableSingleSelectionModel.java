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
package org.valkyriercp.dialog.control;

import javax.swing.*;

/**
 * Custom <code>SingleSelectionModel</code> implementation that allows for
 * vetoing the selection of tabs.
 * <p>
 * The {@link #selectionAllowed(int)} method must return <code>true</code> if
 * the tab at the index can be selected, or <code>false</code> if not.
 *
 * @author Peter De Bruycker
 */
public abstract class VetoableSingleSelectionModel extends DefaultSingleSelectionModel {

	public final void setSelectedIndex(int index) {
		if (selectionAllowed(index)) {
			super.setSelectedIndex(index);
		}
	}

	protected abstract boolean selectionAllowed(int index);
}