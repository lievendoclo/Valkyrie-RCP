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
