package org.valkyriercp.application.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.core.style.ToStringCreator;

/**
 * Application event that communicates lifecycle changes in application objects.
 *
 * @author Keith Donald
 */
public class LifecycleApplicationEvent extends ApplicationEvent {
    private String eventType;

    public static final String CREATED = "lifecycleEvent.created";

    public static final String MODIFIED = "lifecycleEvent.modified";

    public static final String DELETED = "lifecycleEvent.deleted";

    public LifecycleApplicationEvent(String eventType, Object source) {
        super(source);
        this.eventType = eventType;
    }

    public Object getObject() {
        return getSource();
    }

    public boolean objectIs(Class clazz) {
        if (clazz.isAssignableFrom(getSource().getClass()))
            return true;

        return false;
    }

    public String getEventType() {
        return eventType;
    }

    public String toString() {
        return new ToStringCreator(this).toString();
    }
}
