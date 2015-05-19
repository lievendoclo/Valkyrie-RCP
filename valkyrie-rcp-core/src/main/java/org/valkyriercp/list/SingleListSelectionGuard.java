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
package org.valkyriercp.list;

import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.core.Guarded;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SingleListSelectionGuard implements PropertyChangeListener {
    private ValueModel selectionIndexHolder;

    private Guarded guarded;

    public SingleListSelectionGuard(ValueModel selectionIndexHolder, Guarded guarded) {
        this.selectionIndexHolder = selectionIndexHolder;
        this.selectionIndexHolder.addValueChangeListener(this);
        this.guarded = guarded;
        propertyChange(null);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        Integer value = (Integer)selectionIndexHolder.getValue();
        if (value == null || value.intValue() == -1) {
            guarded.setEnabled(false);
        }
        else {
            guarded.setEnabled(true);
        }
    }
}
