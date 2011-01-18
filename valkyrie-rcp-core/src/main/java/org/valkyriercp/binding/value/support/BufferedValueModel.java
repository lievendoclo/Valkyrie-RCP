package org.valkyriercp.binding.value.support;

import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;
import org.valkyriercp.binding.value.CommitTrigger;
import org.valkyriercp.binding.value.CommitTriggerListener;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.ValueModelWrapper;

import javax.annotation.PostConstruct;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A value model that wraps another value model; delaying or buffering changes
 * until a commit is triggered.
 *
 * TODO: more class docs...
 *
 * @author Karsten Lentzsch
 * @author Keith Donald
 * @author Oliver Hutchison
 */
public class BufferedValueModel extends AbstractValueModel implements ValueModelWrapper {

    /**
     * Name of the bound property <em>buffering</em>.
     */
    public static final String BUFFERING_PROPERTY = "buffering";

    private final ValueModel wrappedModel;

    private PropertyChangeListener wrappedModelChangeHandler;

    private Object bufferedValue;

    private CommitTrigger commitTrigger;

    private CommitTriggerListener commitTriggerHandler;

    private boolean buffering;

    /**
     * Constructs a <code>BufferedValueHolder</code> that wraps the given wrappedModel.
     *
     * @param wrappedModel the value model to be buffered
     */
    public BufferedValueModel(ValueModel wrappedModel) {
        this(wrappedModel, null);
    }

    /**
     * Constructs a <code>BufferedValueHolder</code> that wraps the given wrappedModel
     * and listens to the provided commitTrigger for commit and revert events.
     *
     * @param wrappedModel the value model to be buffered
     * @param commitTrigger the commit trigger that triggers the commit or flush event
     */
    public BufferedValueModel(ValueModel wrappedModel, CommitTrigger commitTrigger) {
        Assert.notNull(wrappedModel, "Wrapped value model can not be null.");
        this.wrappedModel = wrappedModel;
        this.bufferedValue = wrappedModel.getValue();
        setCommitTrigger(commitTrigger);
    }

    @PostConstruct
    public void postConstruct() {
        this.wrappedModelChangeHandler = new WrappedModelValueChangeHandler();
        this.wrappedModel.addValueChangeListener(wrappedModelChangeHandler);
    }

    /**
     * Returns the CommitTrigger that is used to trigger commit and flush events.
     *
     * @return the CommitTrigger that is used to trigger commit and flush events
     */
    public final CommitTrigger getCommitTrigger() {
        return commitTrigger;
    }

    /**
     * Sets the <code>CommitTrigger</code> that triggers the commit and flush events.
     *
     * @param commitTrigger  the commit trigger; or null to deregister the
     * existing trigger.
     */
    public final void setCommitTrigger(CommitTrigger commitTrigger) {
        if (this.commitTrigger == commitTrigger) {
            return;
        }
        if (this.commitTrigger != null) {
            this.commitTrigger.removeCommitTriggerListener(commitTriggerHandler);
            this.commitTrigger = null;
        }
        if (commitTrigger != null) {
            if (this.commitTriggerHandler == null) {
                this.commitTriggerHandler = new CommitTriggerHandler();
            }
            this.commitTrigger = commitTrigger;
            this.commitTrigger.addCommitTriggerListener(commitTriggerHandler);
        }
    }

    /**
     * Returns whether this model buffers a value or not, that is, whether
     * a value has been assigned since the last commit or flush.
     *
     * @return true if a value has been assigned since the last commit or revert
     */
    public boolean isBuffering() {
        return buffering;
    }

    /**
     * Returns the wrappedModel value if no value has been set since the last
     * commit or flush, and returns the buffered value otherwise.
     *
     * @return the buffered value
     */
    public Object getValue() {
        return bufferedValue;
    }

    /**
     * Sets a new buffered value and turns this BufferedValueModel into
     * the buffering state. The buffered value is not provided to the
     * underlying model until the trigger channel indicates a commit.
     *
     * @param value  the value to be buffered
     */
    public void setValue(Object value) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting buffered value to '" + value + "'");
        }
        final Object oldValue = getValue();
        this.bufferedValue = value;
        updateBuffering();
        fireValueChange(oldValue, bufferedValue);
    }

    /**
     * Returns the wrappedModel, i.e. the underlying ValueModel that provides
     * the unbuffered value.
     *
     * @return the ValueModel that provides the unbuffered value
     */
    public final ValueModel getWrappedValueModel() {
        return wrappedModel;
    }

    /**
     * Returns the inner most wrappedModel; i.e. the root ValueModel that provides
     * the unbuffered value. This is found by repeatedly unwrapping any ValueModelWrappers
     * until we find the inner most value model.
     *
     * @return the inner most ValueModel that provides the unbuffered value
     *
     * @see ValueModelWrapper#getInnerMostWrappedValueModel()
     */
    public final ValueModel getInnerMostWrappedValueModel() {
        if (wrappedModel instanceof ValueModelWrapper)
            return ((ValueModelWrapper)wrappedModel).getInnerMostWrappedValueModel();

        return wrappedModel;
    }

    /**
     * Called when the value held by the wrapped value model changes.
     */
    protected void onWrappedValueChanged() {
        if (logger.isDebugEnabled()) {
            logger.debug("Wrapped model value has changed; new value is '" + wrappedModel.getValue() + "'");
        }
        setValue(wrappedModel.getValue());
    }

    /**
     * Commits the value buffered by this value model back to the
     * wrapped value model.
     */
    public void commit() {
        if (isBuffering()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Committing buffered value '" + getValue() + "' to wrapped value model '" + wrappedModel
                        + "'");
            }
            wrappedModel.setValueSilently(getValueToCommit(), wrappedModelChangeHandler);
            setValue(wrappedModel.getValue()); // check if the wrapped model changed the committed value
        }
        else {
            if (logger.isDebugEnabled()) {
                logger.debug("No buffered edit to commit; nothing to do...");
            }
        }
    }

    /**
     * Provides a hook that allows for modification of the value that is committed
     * to the underlying value model.
     */
    protected Object getValueToCommit() {
        return bufferedValue;
    }

    /**
     * Updates the value of the buffering property. Fires a property change event
     * if there's been a change.
     */
    private void updateBuffering() {
        boolean wasBuffering = isBuffering();
        buffering = hasValueChanged(wrappedModel.getValue(), bufferedValue);
        firePropertyChange(BUFFERING_PROPERTY, wasBuffering, buffering);
    }

    /**
     * Reverts the value held by the value model back to the value held by the
     * wrapped value model.
     */
    public final void revert() {
        if (isBuffering()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Reverting buffered value '" + getValue() + "' to value '" + wrappedModel.getValue() + "'");
            }
            setValue(wrappedModel.getValue());
        }
        else {
            if (logger.isDebugEnabled()) {
                logger.debug("No buffered edit to commit; nothing to do...");
            }
        }
    }

    public String toString() {
        return new ToStringCreator(this).append("bufferedValue", bufferedValue).toString();
    }

    /**
     * Listens for changes to the wrapped value model.
     */
    private class WrappedModelValueChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            onWrappedValueChanged();
        }
    }

    /**
     * Listens for commit/revert on the commitTrigger.
     */
    private class CommitTriggerHandler implements CommitTriggerListener {
        public void commit() {
            if (logger.isDebugEnabled()) {
                logger.debug("Commit trigger fired commit event.");
            }
            BufferedValueModel.this.commit();
        }

        public void revert() {
            if (logger.isDebugEnabled()) {
                logger.debug("Commit trigger fired revert event.");
            }
            BufferedValueModel.this.revert();
        }
    }

}
