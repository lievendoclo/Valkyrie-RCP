package org.valkyriercp.factory;

import javax.swing.*;
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
    public JTable createTable();

    /**
     * Construct a JTable with the specified table model
     * @param model TableModel to install into the new table
     * @return new table instance
     */
    public JTable createTable(TableModel model);
}
