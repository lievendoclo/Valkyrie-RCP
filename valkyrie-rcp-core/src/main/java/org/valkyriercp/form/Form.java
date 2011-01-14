package org.valkyriercp.form;

import org.valkyriercp.binding.form.ValidatingFormModel;
import org.valkyriercp.binding.validation.ValidationListener;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.core.Guarded;
import org.valkyriercp.core.Messagable;
import org.valkyriercp.factory.ControlFactory;

import java.util.List;

/**
 * The graphical representation of the {@link FormModel} by extending
 * {@link ControlFactory} and providing {@link FormModel} related methods.
 *
 * Additional methods that link with the model are:
 *
 * <ul>
 * <li>{@link #newSingleLineResultsReporter(Messagable)}: combine the
 * validation results of the model with a messagable component to show
 * validation messages.</li>
 * <li>{@link #addGuarded(Guarded)}, {@link #addGuarded(Guarded, int)} and
 * {@link #removeGuarded(Guarded)} to bind objects to the formModel state and
 * implement a suitable reaction. This can translate in eg a save-button that
 * will synchronize its enabled state with a dirty and no-error state in the
 * formModel.</li>
 * </ul>
 *
 * @author Keith Donald
 */
public interface Form extends ControlFactory {

	/**
	 * Returns the id of this form.
	 */
	String getId();

	/**
	 * Returns the formModel used by the form.
	 */
	ValidatingFormModel getFormModel();

	/**
	 * Convenience method to return the formObject currently used in the inner
	 * formModel.
	 */
	Object getFormObject();

	/**
	 * Convenience method to set the formObject on the inner formModel.
	 */
	void setFormObject(Object formObject);

	/**
	 * Convenience method to get the value of a specific property from the inner
	 * formModel.
	 */
	Object getValue(String formProperty);

	/**
	 * Convenience method to get the valueModel of a specific property from the
	 * inner formModel.
	 */
	ValueModel getValueModel(String formProperty);

	/**
	 * Add a ValidationListener.
	 */
	void addValidationListener(ValidationListener listener);

	/**
	 * Remove a ValidationListener.
	 */
	public void removeValidationListener(ValidationListener listener);

	/**
	 * Create a {@link ValidationResultsReporter} for this form, sending input
	 * to the given {@link Messagable}.
	 *
	 * TODO check why it's specifically mentioning "singleLine" in the method
	 * name (can be any validationResultsReporter)
	 *
	 * @param messageAreaPane the message receiver used by the created
	 * resultsReporter.
	 * @return a new ResultsReporter.
	 */
	public ValidationResultsReporter newSingleLineResultsReporter(Messagable messageAreaPane);

	/**
	 * Attach the given {@link Guarded} object with the default mask to the
	 * formModel.
	 *
	 * @see #addGuarded(Guarded, int)
	 * @see FormGuard
	 */
	public void addGuarded(Guarded guarded);

	/**
	 * Attach the given {@link Guarded} object with the specified mask to the
	 * formModel.
	 *
	 * @see FormGuard
	 */
	public void addGuarded(Guarded guarded, int mask);

	/**
	 * Detach the {@link Guarded} object.
	 */
	public void removeGuarded(Guarded guarded);

	/**
	 * Returns the list of ValidationResultsReporters of this Form.
	 */
	List getValidationResultsReporters();

	/**
	 * Add a ValidationResultsReporter to this Form.
	 */
	void addValidationResultsReporter(ValidationResultsReporter validationResultsReporter);

	/**
	 * Remove the given ValidationResultsReporter from this Form.
	 */
	void removeValidationResultsReporter(ValidationResultsReporter validationResultsReporter);

	/**
	 * Add the given Form as a child to this Form. FormModels and other aspects
	 * of this form must behave according to the parent-child relation.
	 */
	void addChildForm(Form form);

	/**
	 * Remove the given Form as child from this Form. Parent-child relation will
	 * be removed from their FormModels and other aspects as well.
	 */
	void removeChildForm(Form form);

	/**
	 * Returns <code>true</code> if the inner <code>FormModel</code> has
	 * errors.
	 */
	boolean hasErrors();

	/**
	 * Commit all values of the <code>FormModel</code>.
	 *
	 * @see FormModel#commit()
	 */
	void commit();

	/**
	 * Revert the <code>FormModel</code>.
	 *
	 * @see FormModel#revert()
	 */
	void revert();

	/**
	 * Reset the <code>FormModel</code>.
	 *
	 * @see FormModel#reset()
	 */
	void reset();
}