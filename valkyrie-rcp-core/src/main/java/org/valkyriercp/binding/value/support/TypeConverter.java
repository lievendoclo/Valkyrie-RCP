package org.valkyriercp.binding.value.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.binding.convert.ConversionExecutor;
import org.valkyriercp.binding.value.DerivedValueModel;
import org.valkyriercp.binding.value.ValueChangeDetector;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.rules.closure.Closure;

import java.beans.PropertyChangeListener;

/**
 * A value model wrapper that supports converting the wrapped value to and from
 * another type using the supplied conversion Closures.
 *
 * @author Keith Donald
 * @author Oliver Hutchison
 */
@Configurable
public class TypeConverter extends AbstractValueModelWrapper implements DerivedValueModel {

	private final Closure convertTo;

	private final Closure convertFrom;

    @Autowired
	private ValueChangeDetector valueChangeDetector;

	/**
	 * Convenience constructor using conversionExecutors.
	 *
	 * @param wrappedModel the inner valueModel
	 * @param convertTo conversion to use when setting a value.
	 * @param convertFrom conversion to use when getting a value.
	 *
	 * @see #TypeConverter(ValueModel, Closure, Closure)
	 */
	public TypeConverter(ValueModel wrappedModel, ConversionExecutor convertTo, ConversionExecutor convertFrom) {
		this(wrappedModel, new ConversionExecutorClosure(convertTo), new ConversionExecutorClosure(convertFrom));
	}

	/**
	 * Constructor which uses Closure blocks to convert between values.
	 *
	 * @param wrappedModel the inner valueModel
	 * @param convertTo Closure to execute when setting a value.
	 * @param convertFrom Closure to execute when getting a value.
	 */
	public TypeConverter(ValueModel wrappedModel, Closure convertTo, Closure convertFrom) {
		super(wrappedModel);
		this.convertTo = convertFrom;
		this.convertFrom = convertTo;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Value from inner model will be converted using the supplied convertFrom closure.
	 */
	public Object getValue() throws IllegalArgumentException {
		return convertFrom.call(super.getValue());
	}

	public void setValueSilently(Object value, PropertyChangeListener listenerToSkip) throws IllegalArgumentException {
		// only set the convertTo value if the convertFrom value has changed
		if (getValueChangeDetector().hasValueChanged(getValue(), value)) {
			super.setValueSilently(convertTo.call(value), listenerToSkip);
		}
	}

	public ValueModel[] getSourceValueModels() {
		return new ValueModel[] { getWrappedValueModel() };
	}

	public boolean isReadOnly() {
		return false;
	}

	public void setValueChangeDetector(ValueChangeDetector valueChangeDetector) {
		this.valueChangeDetector = valueChangeDetector;
	}

	protected ValueChangeDetector getValueChangeDetector() {
		return valueChangeDetector;
	}

	/**
	 * Helper class wrapping ConversionExecutors in a Closure.
	 */
	private static class ConversionExecutorClosure implements Closure {

		private final ConversionExecutor conversionExecutor;

		public ConversionExecutorClosure(ConversionExecutor conversionExecutor) {
			this.conversionExecutor = conversionExecutor;
		}

		public Object call(Object argument) {
			return conversionExecutor.execute(argument);
		}

	}
}
