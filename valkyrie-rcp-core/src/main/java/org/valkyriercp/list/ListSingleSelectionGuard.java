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

/**
 * This class applies a guard to a {@link Guarded} object that only enables the
 * guarded object if the provided list selection model value holder has exactly
 * one item selected.
 *
 * @author Larry Streepy
 *
 */
public class ListSingleSelectionGuard extends AbstractListSelectionGuard {

    /**
     * Constructor.
     *
     * @param selectionHolder ValueModel holding the list selection (value must
     *        be an array of int (<code>int[]</code).
     * @param guarded Object to guard
     */
    public ListSingleSelectionGuard( ValueModel selectionHolder, Guarded guarded ) {
        super(selectionHolder, guarded);
    }

    /**
     * Determine if the guarded object should be enabled based on the contents
     * of the current selection model value.
     *
     * @param selected The array of selected rows
     * @return boolean true if the guarded object should be enabled
     */
    protected boolean shouldEnable( int[] selected ) {
        return selected != null && selected.length == 1;
    }
}