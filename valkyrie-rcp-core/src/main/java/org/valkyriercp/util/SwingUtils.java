package org.valkyriercp.util;

import org.springframework.util.Assert;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Low level swing related utils.
 *
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
 */
public final class SwingUtils {

    /**
     * Utility classes should not have a public or default constructor.
     */
    private SwingUtils() {

        super();
    }

    /**
     * Ensures the given runnable is executed in the event dispatcher thread and waits until executin is completed.
     *
     * @param runnable
     *            the runnable.
     *
     * @see #runInEventDispatcherThread(Runnable, Boolean)
     */
    public static void runInEventDispatcherThread(Runnable runnable) {

        SwingUtils.runInEventDispatcherThread(runnable, Boolean.TRUE);
    }

    /**
     * Ensures the given runnable is executed into the event dispatcher thread.
     *
     * @param runnable
     *            the runnable.
     * @param wait
     *            whether should wait until execution is completed.
     */
    public static void runInEventDispatcherThread(Runnable runnable, Boolean wait) {

        Assert.notNull(runnable, "runnable");
        Assert.notNull(wait, "wait");

        if (EventQueue.isDispatchThread()) {
            runnable.run();
        } else if (wait) {
            try {
                EventQueue.invokeAndWait(runnable);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            EventQueue.invokeLater(runnable);
        }
    }

    /**
     * Does a pre-order search of a component with a given name.
     *
     * @param name
     *            the name.
     * @param parent
     *            the root component in hierarchy.
     * @return the found component (may be null).
     */
    public static Component getDescendantNamed(String name, Component parent) {

        Assert.notNull(name, "name");
        Assert.notNull(parent, "parent");

        if (name.equals(parent.getName())) { // Base case
            return parent;
        } else if (parent instanceof Container) { // Recursive case
            for (final Component component : ((Container) parent).getComponents()) {

                final Component foundComponent = SwingUtils.getDescendantNamed(name, component);

                if (foundComponent != null) {
                    return foundComponent;
                }
            }
        }

        return null;
    }

    /**
     * Generates a component to view an image.
     *
     * @param image
     *            the image.
     * @return the component.
     */
    public static JComponent generateComponent(Image image) {

        if (image == null) {
            return new LabelUIResource("Image is null");
        }

        return SwingUtils.generateComponent(new ImageIcon(image));
    }

    /**
     * Generates a component to view an icon.
     *
     * @param icon
     *            the icon
     * @return the component.
     */
    public static JComponent generateComponent(Icon icon) {

        if (icon == null) {
            return new LabelUIResource("Icon is null");
        }

        return new LabelUIResource(icon);
    }

    /**
     * A <code>JLabel</code> that implements {@link UIResource}.
     * <p>
     * This is an alternative to <code>org.springframework.richclient.widget.ImageViewWidget</code> that implements
     * <code>UIResource</code>.
     *
     * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
     */
    public static class LabelUIResource extends JLabel implements UIResource {

        /**
         * This is <code>Serializable</code>.
         */
        private static final long serialVersionUID = 6220541408412819253L;

        /**
         * Creates a <code>LabelUIResource</code> given a text.
         *
         * @param text
         *            the text.
         */
        public LabelUIResource(String text) {

            super(text);
        }

        /**
         * Creates a <code>LabelUIResource</code> given an image.
         *
         * @param image
         *            the image.
         */
        public LabelUIResource(Icon image) {

            super(image);
        }
    }
}