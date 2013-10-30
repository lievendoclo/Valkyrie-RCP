package org.valkyriercp.widget.editor.provider;

public class DataProviderEvent
{

    /**
     * Type of event: New, Update or Delete.
     */
    private final int eventType;

    /**
     * The new entity: result of update event or created object of new event.
     */
    private final Object newEntity;

    /**
     * The old entity: object that was deleted or previous value of update
     * event.
     */
    private final Object oldEntity;

    /**
     * New object created event. Should have a newEntity.
     */
    public static final int EVENT_TYPE_NEW = 1;

    /**
     * Object updated event. Should have both: a newEntity and an oldEntity.
     */
    public static final int EVENT_TYPE_UPDATE = 2;

    /**
     * Object deleted event. Should have an oldEntity.
     */
    public static final int EVENT_TYPE_DELETE = 3;

    public DataProviderEvent(final int eventType, Object oldEntity, Object newEntity)
    {
        this.eventType = eventType;
        this.oldEntity = oldEntity;
        this.newEntity = newEntity;
    }

    public Object getNewEntity()
    {
        return this.newEntity;
    }

    public Object getOldEntity()
    {
        return oldEntity;
    }

    public int getEventType()
    {
        return this.eventType;
    }

    public static final DataProviderEvent newEntityEvent(Object newEntity)
    {
        return new DataProviderEvent(EVENT_TYPE_NEW, null, newEntity);
    }

    public static final DataProviderEvent updateEntityEvent(Object oldEntity, Object newEntity)
    {
        return new DataProviderEvent(EVENT_TYPE_UPDATE, oldEntity, newEntity);
    }

    public static final DataProviderEvent deleteEntityEvent(Object oldEntity)
    {
        return new DataProviderEvent(EVENT_TYPE_DELETE, oldEntity, null);
    }
}
