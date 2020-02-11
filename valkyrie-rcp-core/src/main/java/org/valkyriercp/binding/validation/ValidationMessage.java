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
package org.valkyriercp.binding.validation;

import org.valkyriercp.core.Message;

/**
 * A specific type of message that relates to a property.
 * <code>ValidationMessage</code>s often find their origin in validation
 * triggered by a constraint on a property. This information is additionally
 * kept available in this <code>ValidationMessage</code>.
 */
public interface ValidationMessage extends Message {

	/**
	 * The property name for messages that have a global scope i.e. do not apply
	 * to a specific property.
	 */
	public static final String GLOBAL_PROPERTY = "<<<global>>>";

	/**
	 * The property that this validation message applies to; or
	 * <code>GLOBAL_PROPERTY</code> if this message does not apply to a
	 * specific property.
	 */
	String getProperty();

}

