package org.valkyriercp.binding.value;

import javax.swing.*;
import java.util.List;

/**
 * Simple sub interface that combines <code>List</code> and
 * <code>ListModel</code>.
 *
 * @author Keith Donald
 */
public interface ObservableList extends List, ListModel {
    public IndexAdapter getIndexAdapter(int index);
}
