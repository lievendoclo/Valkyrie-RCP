package org.valkyriercp.application.docking;

import com.vldocking.swing.docking.Dockable;
import com.vldocking.swing.docking.DockingDesktop;

/**
 * @author Rogan Dawes
 */
public interface VLDockingLayoutManager {

    void addDockable(DockingDesktop desktop, Dockable dockable);

    void removeDockable(DockingDesktop desktop, Dockable dockable);

}
