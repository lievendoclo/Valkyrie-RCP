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
package org.valkyriercp.widget;

import org.valkyriercp.command.support.ActionCommand;

/**
 * Widget that can pass a selection of objects, i.e. a table in which a selection can be made
 *
 * @author jh
 */
public interface SelectionWidget extends Widget
{
    /**
     * Command called to set the selection for the widget. I.e. a double-click listener on the table
     * that selects the current selected row and closes the view to the table.sluiten
     *
     * @param command
     */
    void setSelectionCommand(ActionCommand command);

    void removeSelectionCommand();

    /**
     * @return The selected object(s)
     */
    Object getSelection();
}

