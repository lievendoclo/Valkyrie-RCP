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

import org.valkyriercp.util.EventListenerListHelper;

import java.util.Iterator;

/**
 * A class that can be used to trigger an event on a group of objects. Mainly
 * intended to be used to trigger flush/revert in
 * <code>BufferedValueModel</code> but is useful in general.
 *
 * @author Keith Donald
 * @author Oliver Hutchison
 */
public class CommitTrigger {

	private final EventListenerListHelper listeners = new EventListenerListHelper(CommitTriggerListener.class);

	/**
	 * Constructs a <code>CommitTrigger</code>.
	 */
	public CommitTrigger() {
	}

	/**
	 * Triggers a commit event.
	 */
	public void commit() {
		for (Iterator i = listeners.iterator(); i.hasNext();) {
			((CommitTriggerListener) i.next()).commit();
		}
	}

	/**
	 * Triggers a revert event.
	 */
	public void revert() {
		for (Iterator i = listeners.iterator(); i.hasNext();) {
			((CommitTriggerListener) i.next()).revert();
		}
	}

	/**
	 * Adds the provided listener to the list of listeners that will be notified
	 * whenever a commit or revert event is fired.
	 *
	 * @param listener the <code>CommitTriggerListener</code> to add
	 */
	public void addCommitTriggerListener(CommitTriggerListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removed the provided listener to the list of listeners that will be
	 * notified whenever a commit or revert event is fired.
	 *
	 * @param listener the <code>CommitTriggerListener</code> to remove
	 */
	public void removeCommitTriggerListener(CommitTriggerListener listener) {
		listeners.remove(listener);
	}
}