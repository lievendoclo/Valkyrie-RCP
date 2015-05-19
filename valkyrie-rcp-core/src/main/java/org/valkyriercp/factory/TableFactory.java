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
package org.valkyriercp.factory;

import org.jdesktop.swingx.JXTable;

import javax.swing.table.TableModel;

/**
 * A simple interface for creating JTable object, this allows the developer to create an
 * application specific table factory where, say, each tables have a set of renderers
 * installed, are sortable etc.
 *
 * @author Benoit Xhenseval
 *
 */
public interface TableFactory {

    /**
     * Construct a JTable with a default model
     * @return new table instance
     */
    public JXTable createTable();

    /**
     * Construct a JTable with the specified table model
     * @param model TableModel to install into the new table
     * @return new table instance
     */
    public JXTable createTable(TableModel model);
}
