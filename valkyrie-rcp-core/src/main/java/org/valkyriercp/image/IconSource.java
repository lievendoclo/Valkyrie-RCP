package org.valkyriercp.image;

import javax.swing.*;

/**
 * A loader and cache for Swing <code>ImageIcons</code>.
 *
 * @author Keith Donald
 * @see javax.swing.ImageIcon
 */
public interface IconSource {

    /**
     * Return an <code>ImageIcon</code> using its <code>String</code> key.
     *
     * @param key
     *            a key for the icon.
     * @return The image icon.
     * @throws NoSuchImageResourceException,
     *             if no resource is found and no broken image indicator is set.
     */
    public Icon getIcon(String key);

}