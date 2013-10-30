package org.valkyriercp.component;

/**
 * Interface that enables a component to set the focus on a default
 * component
 *
 * @author Jan Hoskens
 */
public interface Focussable
{
    /**
     * Set the focus on a default component
     *
     * On implementation, remember to use EventQueue.invokeLater(Runnable).
     *
     * @see java.awt.EventQueue#invokeLater(java.lang.Runnable)
     */
    public void grabFocus();
}

