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
package org.valkyriercp.rules.closure.support;

import org.valkyriercp.rules.closure.Closure;

/**
 * Convenient class to implement workflow.
 *
 * @author Keith Donald
 */
public abstract class AbstractElementGeneratorWorkflow extends AbstractElementGenerator {

	/**
	 * @see AbstractElementGenerator#AbstractElementGenerator()
	 */
	protected AbstractElementGeneratorWorkflow() {
		super();
	}

	/**
	 * @see AbstractElementGenerator#AbstractElementGenerator(boolean)
	 */
	protected AbstractElementGeneratorWorkflow(boolean runOnce) {
		super(runOnce);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Defines the workflow sequence.
	 */
	public final void run(Closure templateCallback) {
		reset();
		setRunning();
		doSetup();
		while (processing()) {
			templateCallback.call(doWork());
		}
		setCompleted();
		doCleanup();
	}

	/**
	 * Setup the workflow.
	 */
	protected void doSetup() {

	}

	/**
	 * @return <code>true</code> if ElementGenerator is still processing.
	 */
	protected boolean processing() {
		return hasMoreWork() && !isStopped();
	}

	/**
	 * @return <code>true</code> if more work has to be done.
	 */
	protected abstract boolean hasMoreWork();

	/**
	 * @return the object to process (with the given closure).
	 */
	protected abstract Object doWork();

	/**
	 * Clean up after workflow.
	 */
	protected void doCleanup() {

	}
}
