package org.valkyriercp.binding.form.support;

import org.valkyriercp.binding.value.DirtyTrackingValueModel;
import org.valkyriercp.binding.value.ValueChangeDetector;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.AbstractValueModelWrapper;
import org.valkyriercp.binding.value.support.ValueHolder;
import org.valkyriercp.util.EventListenerListHelper;
import org.valkyriercp.util.ValkyrieRepository;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

/**
 * <p>
 * A value model wrapper that mediates between the (wrapped) data value model
 * and the derived view value model. Allows for value change event delivery to
 * be disabled.
 * </p>
 *
 * <p>
 * Use the provided method {@link #setDeliverValueChangeEvents(boolean)} to
 * enable/disable the event mechanism. This makes it possible to change all
 * valueModels and only then fire all events (as it is used in
 * {@link AbstractFormModel}). Events are handled internally by the
 * {@link #dirtyChangeListeners} and the {@link #mediatedValueHolder}.
 * </p>
 *
 * <p>
 * As this is also a {@link DirtyTrackingValueModel}, the implementation allows
 * reverting to the original value by using {@link #revertToOriginal()}, which
 * uses the value stored in {@link #originalValue} to reset the
 * wrappedValueModel. This originalValue is updated to hold the wrappedValue
 * when using {@link #clearDirty()}.
 * </p>
 *
 * <p>Small sketch to illustrate the positioning and usage:</p>
 * <pre>
 * &lt;setup&gt;
 * External actor -> FormModelMediatingValueModel -> wrappedValueModel
 *                   holds originalValue = ori       wrappedValue = ori
 *                   events = enabled
 *
 * &lt;use case&gt;
 * events disabled -> events = disabled            -> wrappedValue = ori
 * write value A   -> delagates to wrappedModel    -> wrappedValue = A
 *                    originalValue = ori
 *                    update dirty state
 * events enabled  -> events = enabled             -> wrappedValue = A
 *                    sends events (dirty...)
 * clearDirty      -> originalValue = A            -> wrappedValue = A
 * OR
 * revertToOriginal-> set originalValue on wrapped -> wrappedValue = ori
 *                    update dirty state
 * </pre>
 *
 * @author Oliver Hutchison
 */
public class FormModelMediatingValueModel extends AbstractValueModelWrapper implements DirtyTrackingValueModel,
        PropertyChangeListener {

	/** Holds all propertyChangeListeners that are interested in Dirty changes. */
	private final EventListenerListHelper dirtyChangeListeners = new EventListenerListHelper(
			PropertyChangeListener.class);

	/** Allows to turn off the tracking mechanism. */
	private boolean deliverValueChangeEvents = true;

	/** Holds the originalValue. Used to register listeners. */
	private final ValueHolder mediatedValueHolder;

	/** The original value of the wrapped ValueModel. */
	private Object originalValue;

	/** Previous dirty state. */
	private boolean oldDirty;

	/** Dirty tracking can be disabled. */
	private final boolean trackDirty;

	/**
	 * Constructor which defaults <code>trackDirty=true</code>.
	 *
	 * @param propertyValueModel the ValueModel to mediate.
	 */
	public FormModelMediatingValueModel(ValueModel propertyValueModel) {
		this(propertyValueModel, true);
	}

	/**
	 * Constructor.
	 *
	 * @param propertyValueModel the valueModel to mediate.
	 * @param trackDirty disable/enable dirty tracking.
	 */
	public FormModelMediatingValueModel(ValueModel propertyValueModel, boolean trackDirty) {
		super(propertyValueModel);
		super.addValueChangeListener(this);
		this.originalValue = getValue();
		this.mediatedValueHolder = new ValueHolder(originalValue);
		this.trackDirty = trackDirty;
	}

	public void setValueSilently(Object value, PropertyChangeListener listenerToSkip) {
		super.setValueSilently(value, this);
		if (deliverValueChangeEvents) {
			mediatedValueHolder.setValueSilently(value, listenerToSkip);
			updateDirtyState();
		}
	}

	// called by the wrapped value model
	public void propertyChange(PropertyChangeEvent evt) {
		originalValue = getValue();
		if (deliverValueChangeEvents) {
			mediatedValueHolder.setValue(originalValue);
			updateDirtyState();
		}
	}

	/**
	 * <p>
	 * Enable/disable the event mechanism. Makes it possible to control the
	 * timing of event firing (delay the events).
	 * </p>
	 *
	 * <p>
	 * When disabling, no dirty events will be fired and the mediating
	 * valueHolder will not set it's value. The latter results in not firing
	 * other events like valueChangedEvents.
	 * </p>
	 * <p>
	 * When enabling, original (stored) value is compared to the newer value in
	 * the wrapped model and the necessary events are fired
	 * (dirty/valueChanged).
	 * <p>
	 *
	 * @param deliverValueChangeEvents boolean to enable/disable event
	 * mechanism.
	 */
	public void setDeliverValueChangeEvents(boolean deliverValueChangeEvents) {
		boolean oldDeliverValueChangeEvents = this.deliverValueChangeEvents;
		this.deliverValueChangeEvents = deliverValueChangeEvents;
		if (!oldDeliverValueChangeEvents && deliverValueChangeEvents) {
			mediatedValueHolder.setValue(getValue());
			updateDirtyState();
		}
	}

	public boolean isDirty() {
		return trackDirty && getValueChangeDetector().hasValueChanged(originalValue, getValue());
	}

	public void clearDirty() {
		if (isDirty()) {
			originalValue = getValue();
			updateDirtyState();
		}
	}

	public void revertToOriginal() {
		if (isDirty()) {
			setValue(originalValue);
		}
	}

	/**
	 * Check the dirty state and fire events if needed.
	 */
	protected void updateDirtyState() {
		boolean dirty = isDirty();
		if (oldDirty != dirty) {
			oldDirty = dirty;
			firePropertyChange(DIRTY_PROPERTY, !dirty, dirty);
		}
	}

	/**
	 * @return a {@link ValueChangeDetector} to use when checking the dirty
	 * state.
	 */
	protected ValueChangeDetector getValueChangeDetector() {
		return ValkyrieRepository.getInstance().getApplicationConfig().valueChangeDetector();
	}

	public void addValueChangeListener(PropertyChangeListener listener) {
		mediatedValueHolder.addValueChangeListener(listener);
	}

	public void removeValueChangeListener(PropertyChangeListener listener) {
		mediatedValueHolder.removeValueChangeListener(listener);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (DIRTY_PROPERTY.equals(propertyName)) {
			dirtyChangeListeners.add(listener);
		}
		else if (VALUE_PROPERTY.equals(propertyName)) {
			addValueChangeListener(listener);
		}
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		throw new UnsupportedOperationException("not implemented");
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (DIRTY_PROPERTY.equals(propertyName)) {
			dirtyChangeListeners.remove(listener);
		}
		else if (VALUE_PROPERTY.equals(propertyName)) {
			removeValueChangeListener(listener);
		}
	}

	/**
	 * Handles the dirty event firing.
	 *
	 * @param propertyName implementation only handles DIRTY_PROPERTY.
	 * @param oldValue
	 * @param newValue
	 */
	protected final void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		if (DIRTY_PROPERTY.equals(propertyName)) {
			PropertyChangeEvent evt = new PropertyChangeEvent(this, propertyName, Boolean.valueOf(oldValue), Boolean
					.valueOf(newValue));
			for (Iterator i = dirtyChangeListeners.iterator(); i.hasNext();) {
				((PropertyChangeListener) i.next()).propertyChange(evt);
			}
		}
	}
}