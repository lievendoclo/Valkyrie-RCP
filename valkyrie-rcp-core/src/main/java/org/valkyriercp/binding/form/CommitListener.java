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
 * Listener interface for objects interested in intercepting before and after a
 * form model is commited.
 *
 * @author Keith Donald
 * @author Oliver Hutchison
 */
public interface CommitListener {

	/**
	 * Called just before a form model is about to commit.
	 */
	void preCommit(FormModel formModel);

	/**
	 * Called just after a form model is commited.
	 */
	void postCommit(FormModel formModel);
}
