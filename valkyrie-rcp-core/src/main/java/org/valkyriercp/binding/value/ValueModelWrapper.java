package org.valkyriercp.binding.value;

/**
 * A value model that wraps another value model, possibly adding additional
 * functionality such as type conversion or buffering.
 *
 * @author Keith Donald
 * @author Oliver Hutchison
 */
public interface ValueModelWrapper {

	/**
	 * Returns the <code>ValueModel</code> wrapped by this
	 * <code>ValueModelWrapper</code>.
	 *
	 * @return the wrapped value model.
	 */
	ValueModel getWrappedValueModel();

	/**
	 * Returns the inner most <code>ValueModel</code> wrapped by this
	 * <code>ValueModelWrapper</code>.
	 *
	 * @return the inner most wrapped value model.
	 */
	ValueModel getInnerMostWrappedValueModel();
}
