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
package org.valkyriercp.binding.value;

/**
 * Interface to be implemented by classes that listen for the commit and revert
 * events fired by a <code>CommitTriger</code>.
 *
 * @author Oliver Hutchison
 * @see CommitTrigger
 */
public interface CommitTriggerListener {

	/**
	 * Called when a commit event is fired.
	 */
	void commit();

	/**
	 * Called when a revert event is fired.
	 */
	void revert();
}