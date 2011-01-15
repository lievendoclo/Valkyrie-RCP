package org.valkyriercp.factory;

import org.jdesktop.swingx.JXTable;

import javax.swing.table.TableModel;

public class DefaultTableFactory implements TableFactory {
    @Override
    public JXTable createTable() {
        return new JXTable();
    }

    @Override
    public JXTable createTable(TableModel model) {
        return new JXTable(model);
    }
}
