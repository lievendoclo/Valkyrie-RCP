package org.valkyriercp.binding.value.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.valkyriercp.binding.value.ValueChangeDetector;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.core.support.AbstractPropertyChangePublisher;
import org.valkyriercp.util.ValkyrieRepository;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * An abstract class that minimizes the effort required to implement
 * the {@link ValueModel} interface. It provides convenience methods
 * to convert boolean, double, float, int, and long to their
 * corresponding Object values.
 *
 * <p>Subclasses must implement <code>getValue()</code> and
 * <code>setValue(Object)</code> to get and set the observable value.
 *
 * @author Karsten Lentzsch
 * @author Keith Donald
 * @author Oliver Hutchison
 */
public abstract class AbstractValueModel extends AbstractPropertyChangePublisher implements ValueModel {

    protected final Log logger = LogFactory.getLog(getClass());

    private final ThreadLocal listenerToSkipHolder = new ThreadLocal();

    private ValueChangeDetector valueChangeDetector;

    public final void setValueSilently(Object newValue, PropertyChangeListener listenerToSkip) {
        // We need to keep track of listenerToSkip on a per thread basis as it's
        // possible that this value model may be accessed from multiple threads.
        final Object oldListenerToSkip = listenerToSkipHolder.get();
        try {
            listenerToSkipHolder.set(listenerToSkip);
            setValue(newValue);
        }
        finally {
            listenerToSkipHolder.set(oldListenerToSkip);
        }
    }

    public final void addValueChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(VALUE_PROPERTY, listener);
    }

    public final void removeValueChangeListener(PropertyChangeListener listener) {
        removePropertyChangeListener(VALUE_PROPERTY, listener);
    }

    /**
     * This method can be called when it in necessary to send a
     * PropertyChangeEvent to any registered PropertyChangeListeners
     * even though the encapsulated value has not changed.
     *
     * FIXME: This needs a much better name!
     */
    protected void fireValueChangeWhenStillEqual() {
        Object value = getValue();
        fireValueChangeEvent(value, value);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param oldValue the boolean value before the change
     * @param newValue the boolean value after the change
     */
    protected final void fireValueChange(boolean oldValue, boolean newValue) {
        fireValueChange(Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param oldValue the int value before the change
     * @param newValue the int value after the change
     */
    protected final void fireValueChange(int oldValue, int newValue) {
        fireValueChange(new Integer(oldValue), new Integer(newValue));
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param oldValue the long value before the change
     * @param newValue the long value after the change
     */
    protected final void fireValueChange(long oldValue, long newValue) {
        fireValueChange(new Long(oldValue), new Long(newValue));
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param oldValue the double value before the change
     * @param newValue the double value after the change
     */
    protected final void fireValueChange(double oldValue, double newValue) {
        fireValueChange(new Double(oldValue), new Double(newValue));
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param oldValue the float value before the change
     * @param newValue the float value after the change
     */
    protected void fireValueChange(Object oldValue, Object newValue) {
        if (hasValueChanged(oldValue, newValue)) {
            fireValueChangeEvent(oldValue, newValue);
        }
    }

    /**
     * Delegates to configured <code>ValueChangeDetector</code>.
     */
    protected boolean hasValueChanged(Object oldValue, Object newValue) {
        return getValueChangeDetector().hasValueChanged(oldValue, newValue);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type. This method does not check if there is any change
     * between the old and new value unlike the various fireValueChanged() methods.
     */
    protected void fireValueChangeEvent(Object oldValue, Object newValue) {
        if (logger.isDebugEnabled()) {
            logger.debug("Firing value changed event. Old value='" + oldValue + "' new value='" + newValue + "'");
        }
        final PropertyChangeListener[] propertyChangeListeners = getPropertyChangeListeners(VALUE_PROPERTY);
        if (propertyChangeListeners.length > 0) {
            final Object listenerToSkip = listenerToSkipHolder.get();
            final PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this, VALUE_PROPERTY, oldValue,
                    newValue);
            for (int i = 0; i < propertyChangeListeners.length; i++) {
                PropertyChangeListener listener = propertyChangeListeners[i];
                if (listener != listenerToSkip) {
                    listener.propertyChange(propertyChangeEvent);
                }
            }
        }
    }

    /**
     * Set the object that will be used to detect changes between two values.
     * @param valueChangeDetector to use
     */
    public void setValueChangeDetector(ValueChangeDetector valueChangeDetector) {
        this.valueChangeDetector = valueChangeDetector;
    }

    /**
     * Get the installed value change detector.  If none has been directly installed then
     * get the one configured in the application context.
     * @return value change detector to use
     */
    protected ValueChangeDetector getValueChangeDetector() {
        if(valueChangeDetector == null)
            return ValkyrieRepository.getInstance().getApplicationConfig().valueChangeDetector();
        else
            return valueChangeDetector;
    }

}