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
