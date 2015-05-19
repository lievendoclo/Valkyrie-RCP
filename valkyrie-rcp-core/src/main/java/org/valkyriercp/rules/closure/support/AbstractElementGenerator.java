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
import org.valkyriercp.rules.closure.ElementGenerator;
import org.valkyriercp.rules.constraint.Constraint;

/**
 * Base superclass for process templates.
 *
 * @author Keith Donald
 */
public abstract class AbstractElementGenerator implements ElementGenerator {

	/** Wrapping instance for internal usage. */
	private ElementGenerator wrappedGenerator;

	/** <code>true</code> if this ElementGenerator may run once. */
	private boolean runOnce = false;

	/** Current status of this ElementGenerator. */
	private volatile ProcessStatus status = ProcessStatus.CREATED;

	/**
	 * Default constructor.
	 */
	protected AbstractElementGenerator() {
	}

	/**
	 * Constructor.
	 *
	 * @param runOnce <code>true</code> if this ElementGenerator may only be
	 * run once (will throw an Exception if called more than once).
	 */
	protected AbstractElementGenerator(boolean runOnce) {
		this.runOnce = runOnce;
	}

	/**
	 * Create an wrapper instance for internal usage.
	 *
	 * @param wrappedTemplate
	 */
	private AbstractElementGenerator(ElementGenerator wrappedTemplate) {
		this.wrappedGenerator = wrappedTemplate;
	}

	/**
	 * @return the wrapped ElementGenerator or <code>null</code>.
	 */
	protected ElementGenerator getWrappedTemplate() {
		return wrappedGenerator;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean allTrue(Constraint constraint) {
		WhileTrueController controller = new WhileTrueController(this, constraint);
		run(controller);
		return controller.allTrue();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean anyTrue(Constraint constraint) {
		return findFirst(constraint, null) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public ElementGenerator findAll(final Constraint constraint) {
		return new AbstractElementGenerator(this) {
			public void run(final Closure closure) {
				getWrappedTemplate().run(new IfBlock(constraint, closure));
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	public Object findFirst(Constraint constraint) {
		return findFirst(constraint, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object findFirst(Constraint constraint, Object defaultIfNoneFound) {
		ObjectFinder finder = new ObjectFinder(this, constraint);
		run(finder);
		return (finder.foundObject() ? finder.getFoundObject() : defaultIfNoneFound);
	}

	/**
	 * @return <code>true</code> if the generator is stopped.
	 */
	public boolean isStopped() {
		return this.status == ProcessStatus.STOPPED;
	}

	/**
	 * @return <code>true</code> if the generator has completed it's task.
	 */
	public boolean isFinished() {
		return this.status == ProcessStatus.COMPLETED;
	}

	/**
	 * @return <code>true</code> if the generator is still running.
	 */
	public boolean isRunning() {
		return this.status == ProcessStatus.RUNNING;
	}

	/**
	 * Stop the generator.
	 */
	public void stop() throws IllegalStateException {
		if (this.wrappedGenerator != null) {
			wrappedGenerator.stop();
		}
		this.status = ProcessStatus.STOPPED;
	}

	/**
	 * Run a block of code (Closure) until a specific test (Constraint) is
	 * passed.
	 */
	public void runUntil(Closure templateCallback, final Constraint constraint) {
		run(new UntilTrueController(this, templateCallback, constraint));
	}

	/**
	 * Reset the ElementGenerator if possible.
	 *
	 * @throws UnsupportedOperationException if this ElementGenerator was a
	 * runOnce instance.
	 */
	protected void reset() {
		if (this.status == ProcessStatus.STOPPED || this.status == ProcessStatus.COMPLETED) {
			if (this.runOnce) {
				throw new UnsupportedOperationException("This process template can only safely execute once; "
						+ "instantiate a new instance per request");
			}

			this.status = ProcessStatus.RESET;
		}
	}

	/**
	 * Set status running.
	 */
	protected void setRunning() {
		this.status = ProcessStatus.RUNNING;
	}

	/**
	 * Set status completed.
	 */
	protected void setCompleted() {
		this.status = ProcessStatus.COMPLETED;
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract void run(Closure templateCallback);

	/**
	 * When the passed object returns false for the given constraint, the
	 * ElementGenerator will be stopped. Afterwards the {@link #allTrue()}
	 * method can be used to check if all objects passed the test.
	 */
	private static class WhileTrueController extends Block {
		private static final long serialVersionUID = 1L;

		/**
		 * The ElementGenerator that spawns the elements and that will be
		 * stopped when the constraint returns false.
		 */
		private ElementGenerator template;

		/** The constraint to test against. */
		private Constraint constraint;

		/**
		 * Stores the outcome: true if all handled objects passed the
		 * constraint.
		 */
		private boolean allTrue = true;

		/**
		 * Constructor.
		 *
		 * @param template the ElementGenerator that spawns the elements and
		 * that will be stopped if an object fails the constraint.
		 * @param constraint the constraint to test against.
		 */
		public WhileTrueController(ElementGenerator template, Constraint constraint) {
			this.template = template;
			this.constraint = constraint;
		}

		/**
		 * {@inheritDoc}
		 */
		protected void handle(Object o) {
			if (!this.constraint.test(o)) {
				this.allTrue = false;
				this.template.stop();
			}
		}

		/**
		 * @return <code>true</code> if all objects passed the constraint.
		 */
		public boolean allTrue() {
			return allTrue;
		}
	}

	/**
	 * Each passed object will be tested against a Constraint. If returning
	 * true, the ElementGenerator will be stopped. If false, the given Closure
	 * will be executed with the object and the ElementGenerator will continue.
	 */
	private static class UntilTrueController extends Block {
		private static final long serialVersionUID = 1L;

		/**
		 * The ElementGenerator that spawns the element and that will be stopped
		 * when the constraint returns true.
		 */
		private ElementGenerator template;

		/**
		 * The closure that has to be executed on all objects until the
		 * constraint returns true.
		 */
		private Closure templateCallback;

		/**
		 * The constraint used to test the objects.
		 */
		private Constraint constraint;

		/**
		 * Constructor.
		 *
		 * @param template ElementGenerator that spawns the elements and that
		 * will be stopped if the constraint returns true.
		 * @param templateCallback Closure-code executed on each passed object.
		 * @param constraint constraint that will stop the ElementGenerator if
		 * returning true.
		 */
		public UntilTrueController(ElementGenerator template, Closure templateCallback, Constraint constraint) {
			this.template = template;
			this.templateCallback = templateCallback;
			this.constraint = constraint;
		}

		/**
		 * {@inheritDoc}
		 */
		protected void handle(Object o) {
			if (this.constraint.test(o)) {
				this.template.stop();
			}
			else {
				this.templateCallback.call(o);
			}
		}
	}

	/**
	 * Will check each passed object against a constraint and call stop if the
	 * constraint returns true. The Object which passed the test will be the
	 * saved for retrieval.
	 */
	private static class ObjectFinder extends Block {
		private static final long serialVersionUID = 1L;

		/**
		 * The generator that spawns the elements and that will be stopped when
		 * the specified element is found.
		 */
		private ElementGenerator generator;

		/** Constraint to test against. */
		private Constraint constraint;

		/**
		 * The object that passed the test in the constraint or null if not
		 * found.
		 */
		private Object foundObject;

		/**
		 * Constructor.
		 *
		 * @param generator the generator which spawns the elements and that
		 * must be stopped when the element is found.
		 * @param constraint the constraint to test against.
		 */
		public ObjectFinder(ElementGenerator generator, Constraint constraint) {
			this.generator = generator;
			this.constraint = constraint;
		}

		/**
		 * {@inheritDoc}
		 */
		protected void handle(Object o) {
			if (this.constraint.test(o)) {
				foundObject = o;
				generator.stop();
			}
		}

		/**
		 * @return <code>true</code> if the object was found.
		 */
		public boolean foundObject() {
			return foundObject != null;
		}

		/**
		 * @return the object that complies with the constraint or
		 * <code>null</code> if not found.
		 */
		public Object getFoundObject() {
			return foundObject;
		}
	}

}
