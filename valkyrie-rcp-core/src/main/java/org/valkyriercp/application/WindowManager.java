package org.valkyriercp.application;

import org.springframework.util.Assert;
import org.valkyriercp.core.PropertyChangePublisher;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class WindowManager extends Observable implements PropertyChangePublisher {

    /**
     * List of windows managed by this window manager (element type:
     * <code>Window</code>).
     */
    private List windows = new ArrayList();

    /**
     * Parent window manager, or <code>null</code> if none.
     */
    private WindowManager parentManager;

    /**
     * List of window managers who have this window manager as their parent
     * (element type: <code>WindowManager</code>).
     */
    private List subManagers;

    /**
     * Holds the currently active window.
     */
    private ApplicationWindow activeWindow;

    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    /**
     * Creates an empty window manager without a parent window manager (that is,
     * a root window manager).
     */
    public WindowManager() {
    }

    /**
     * Creates an empty window manager with the given window manager as parent.
     *
     * @param parent
     *            the parent window manager
     */
    public WindowManager(WindowManager parent) {
        Assert.notNull(parent);
        parent.addWindowManager(this);
    }

    /**
     * Adds the given window to the set of windows managed by this window
     * manager. Does nothing is this window is already managed by this window
     * manager.
     *
     * @param window
     *            the window
     */
    public void add(ApplicationWindow window) {
        if (activeWindow == null) // first window will be set as activeWindow
            setActiveWindow(window);

        if (!windows.contains(window)) {
            windows.add(window);
            window.setWindowManager(this);
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Adds the given window manager to the list of window managers that have
     * this one as a parent.
     *
     * @param wm
     *            the child window manager
     */
    private void addWindowManager(WindowManager wm) {
        if (subManagers == null) {
            subManagers = new ArrayList();
        }
        if (!subManagers.contains(wm)) {
            subManagers.add(wm);
            wm.parentManager = this;
        }
    }

    /**
     * Attempts to close all windows managed by this window manager, as well as
     * windows managed by any descendent window managers.
     *
     * @return <code>true</code> if all windows were sucessfully closed, and
     *         <code>false</code> if any window refused to close
     */
    public boolean close() {
        List t = (List)((ArrayList)windows).clone();
        Iterator e = t.iterator();
        while (e.hasNext()) {
            ApplicationWindow window = (ApplicationWindow)e.next();
            if (!window.close()) { return false; }
        }
        if (subManagers != null) {
            e = subManagers.iterator();
            while (e.hasNext()) {
                WindowManager wm = (WindowManager)e.next();
                if (!wm.close()) { return false; }
            }
        }
        return true;
    }

    /**
     * Returns this window manager's set of windows.
     *
     * @return a possibly empty list of window
     */
    public ApplicationWindow[] getWindows() {
        ApplicationWindow managed[] = new ApplicationWindow[windows.size()];
        windows.toArray(managed);
        return managed;
    }

    /**
     * @return the parent of this WindowManager
     */
    public WindowManager getParent() {
        return parentManager;
    }

    /**
     * Removes the given window from the set of windows managed by this window
     * manager. Does nothing is this window is not managed by this window
     * manager.
     *
     * @param window
     *            the window
     */
    public final void remove(ApplicationWindow window) {
        if (windows.contains(window)) {
            windows.remove(window);
            window.setWindowManager(null);
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Set the currently active window. When a window gets focus, it will set itself
     * as the current window of it's manager.
     *
     * @param window
     */
    public final void setActiveWindow(ApplicationWindow window)
    {
        final ApplicationWindow old = this.activeWindow;
        this.activeWindow = window;
        if (getParent() != null) // let things ripple up
            getParent().setActiveWindow(window);
        getChangeSupport().firePropertyChange("activeWindow", old, window);
    }

    /**
     * @return the active window.
     */
    public final ApplicationWindow getActiveWindow()
    {
        return this.activeWindow;
    }

    /**
     * @return Number of windows managed by this instance.
     */
    public int size() {
        return windows.size();
    }


    protected PropertyChangeSupport getChangeSupport() {
        return changeSupport;
    }




    //
    // METHODS FROM INTERFACE PropertyChangePublisher
    //


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getChangeSupport().addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        getChangeSupport().addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getChangeSupport().removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        getChangeSupport().removePropertyChangeListener(propertyName, listener);
    }
}
