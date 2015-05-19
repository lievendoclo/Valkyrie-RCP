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
package org.valkyriercp.binding.value;

import org.valkyriercp.core.PropertyChangePublisher;

/**
 * Adds the <code>dirty</code> aspect to a valueModel, tracking changes when needed.
 */
public interface DirtyTrackingValueModel extends ValueModel, PropertyChangePublisher
{

    /** The name of the bound property <em>dirty</em>. */
    String DIRTY_PROPERTY = "dirty";

    /**
     * Returns <code>true</code> if value held by this model has changed since the last call to reset or the
     * last time a value came up from the inner model.
     */
    boolean isDirty();

    /**
     * Resets the dirty state of this model to <code>false</code>.
     */
    void clearDirty();

    /**
     * Reverts the value held by this model to the original value at the last call to reset or the last time a
     * value came up from the inner model.
     */
    void revertToOriginal();
}