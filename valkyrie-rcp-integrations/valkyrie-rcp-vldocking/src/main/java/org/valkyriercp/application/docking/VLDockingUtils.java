package org.valkyriercp.application.docking;

import com.vldocking.swing.docking.*;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

/**
 * Utility class for dealing with VLDocking.
 *
 * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
 */
public final class VLDockingUtils {

    /**
     * The activation infix for <em>active</em> UI property names.
     */
    private static final String ACTIVE_INFIX = ".active";

    /**
     * The '{@value #DOT}' character.
     */
    private static final char DOT = '.';

    /**
     * The activation infix for <em>inactive</em> UI property names.
     */
    private static final String INACTIVE_INFIX = ".inactive";

    /**
     * Utility classes should not have a public or default constructor.
     */
    private VLDockingUtils() {

    }

    /**
     * Fixes an VLDocking bug consisting on dockables that belong to a docking desktop have no state on it.
     *
     * @param dockable
     *            the dockable candidate.
     * @return its dockable state. If none then registers the dockable again and ensures dockable state is not null.
     */
    public static DockableState fixVLDockingBug(DockingDesktop dockingDesktop, Dockable dockable) {

        Assert.notNull(dockingDesktop, "dockingDesktop");
        Assert.notNull(dockable, "dockable");

        final DockingContext dockingContext = dockingDesktop.getContext();
        final DockKey dockKey = dockable.getDockKey();

        DockableState dockableState = dockingDesktop.getDockableState(dockable);

        final Boolean thisFixApplies = (dockingContext.getDockableByKey(dockKey.getKey()) != null);
        if ((thisFixApplies) && (dockableState == null)) {
            dockingDesktop.registerDockable(dockable);
            dockableState = dockingDesktop.getDockableState(dockable);
//            dockKey.setLocation(dockableState.getLocation());

            Assert.notNull(dockableState, "dockableState");
        }

        return dockableState;
    }

    /**
     * Transforms a UI object key into an activation aware key name.
     *
     * @param key
     *            the key.
     * @param active
     *            <code>true</code> for <em>active</em> UI keys and <code>false</code> for inactive.
     * @return the transformed key.
     */
    public static String activationKey(String key, Boolean active) {

        Assert.notNull(key, "key");
        Assert.notNull(active, "active");

        final int index = key.lastIndexOf(VLDockingUtils.DOT);
        final String overlay = active ? VLDockingUtils.ACTIVE_INFIX : VLDockingUtils.INACTIVE_INFIX;

//        return StringUtils.overlay(key, overlay, index, index);
        return key; //TODO
    }

    /**
     * Null safe version of {@link #findDockViewTitleBar(java.awt.Component)}.
     *
     * @param component
     *            the component.
     * @return the dock view title bar.
     *
     * @see #findDockViewTitleBar(java.awt.Component)
     */
    public static DockViewTitleBar nullSafeFindDockViewTitleBar(Component component) {

        final DockViewTitleBar dockViewTitleBar;
        if (component != null) {
            dockViewTitleBar = VLDockingUtils.findDockViewTitleBar(component);
        } else {
            dockViewTitleBar = null;
        }

        return dockViewTitleBar;
    }

    /**
     * Find the dock view title bar associated to the dockable container of a given component (if any).
     *
     * @param component
     *            the component.
     * @return the dock view title bar.
     */
    public static DockViewTitleBar findDockViewTitleBar(Component component) {

        Assert.notNull(component, "component");

        final SingleDockableContainer sdc = DockingUtilities.findSingleDockableContainerAncestor(component);
        final DockViewTitleBar dockViewTitleBar;

        if (sdc == null) {
            dockViewTitleBar = null;
        } else if (sdc instanceof DockView) {
            dockViewTitleBar = ((DockView) sdc).getTitleBar();
        } else if (sdc instanceof AutoHideExpandPanel) {
            dockViewTitleBar = ((AutoHideExpandPanel) sdc).getTitleBar();
        } else {
            dockViewTitleBar = null;
        }

        return dockViewTitleBar;
    }

    /**
     * A bean useful for dealing with focus changed events within VLDocking.
     *
     * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
     */
    public static class FocusGainedBean {

        /**
         * The number of instances, useful for debugging.
         */
        private static int instanceCount = 0;

        /**
         * The target event.
         */
        private PropertyChangeEvent event;

        /**
         * The last active title bar.
         */
        private DockViewTitleBar lastTitleBar;

        /**
         * The description of what's happening.
         */
        private String whatsHappening;

        /**
         * Default constructor: increase instance count.
         */
        public FocusGainedBean() {

            ++FocusGainedBean.instanceCount;
        }

        /**
         * Gets the target event.
         *
         * @return the event.
         */
        public final PropertyChangeEvent getEvent() {

            return this.event;
        }

        /**
         * Sets the target event.
         *
         * @param event
         *            the event to set.
         * @return <code>this</code>.
         */
        public final FocusGainedBean setEvent(PropertyChangeEvent event) {

            Assert.notNull(event, "event");

            this.event = event;

            return this;
        }

        /**
         * Gets the old focused component propagated by an event.
         *
         * @return the component.
         */
        public final Component getOldComponent() {

            final Component oldComponent;
            if ((this.getEvent() != null) && this.getEvent().getOldValue() instanceof Component) {
                oldComponent = (Component) this.getEvent().getOldValue();
            } else {
                oldComponent = null;
            }
            return oldComponent;
        }

        /**
         * Gets the new focused component propagated by an event.
         *
         * @return the component.
         */
        public final Component getNewComponent() {

            final Component newComponent;
            if ((this.getEvent() != null) && this.getEvent().getNewValue() instanceof Component) {
                newComponent = (Component) this.getEvent().getNewValue();
            } else {
                newComponent = null;
            }
            return newComponent;
        }

        /**
         * Gets the last active title bar.
         *
         * @return the last active title bar.
         */
        public final DockViewTitleBar getLastTitleBar() {

            return this.lastTitleBar;
        }

        /**
         * Sets the last active title bar.
         *
         * @param lastActiveTitleBar
         *            the last active title bar to set.
         *
         * @return <code>this</code>.
         */
        public final FocusGainedBean setLastTitleBar(DockViewTitleBar lastActiveTitleBar) {

            this.lastTitleBar = lastActiveTitleBar;

            return this;
        }

        /**
         * Gets the old focused title bar propagated by an event.
         *
         * @return the title bar.
         */
        public final DockViewTitleBar getOldTitleBar() {

            return VLDockingUtils.nullSafeFindDockViewTitleBar(this.getOldComponent());
        }

        /**
         * Gets the new focused title bar propagated by an event.
         *
         * @return the title bar.
         */
        public final DockViewTitleBar getNewTitleBar() {

            return VLDockingUtils.nullSafeFindDockViewTitleBar(this.getNewComponent());
        }

        /**
         * Gets the description of what's happening.
         *
         * @return the description.
         */
        public final String getWhatsHappening() {

            return this.whatsHappening;
        }

        /**
         * Sets the description of what's happening.
         *
         * @param whatsHappening
         *            the description to set.
         *
         * @return <code>this</code>.
         */
        public final FocusGainedBean setWhatsHappening(String whatsHappening) {

            Assert.notNull(whatsHappening, "whatsHappening");

            this.whatsHappening = whatsHappening;

            return this;
        }

//        /** //TODO
//         * {@inheritDoc}
//         */
//        @Override
//        public String toString() {
//
////            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE) //
////                    .append(FocusGainedBean.instanceCount) //
////                    .append(this.getWhatsHappening()) //
////                    .append("lastTitleBar", this.titleBarToString(this.getLastTitleBar()))//
////                    .append("oldTitleBar", this.titleBarToString(this.getOldTitleBar())) //
////                    .append("newTitleBar", this.titleBarToString(this.getNewTitleBar())) //
////                    .append("oldComponent", this.componentToString(this.getOldComponent()))//
////                    .append("newComponent", this.componentToString(this.getNewComponent()))//
////                    .toString();
//        }

        /**
         * Returns a legible representation of a given component.
         *
         * @param component
         *            the target component.
         * @return a string.
         */
        private String componentToString(Component component) {

            final StringBuffer sb = new StringBuffer();
            sb.append(ObjectUtils.identityToString(component));

            if (component != null) {
                sb.append(": ").append((component.getName() != null) ? component.getName() : component.toString());
            }

            return sb.toString();
        }

        /**
         * Returns a legible representation of a given dockable.
         *
         * @param titleBar
         *            the target title bar.
         * @return a string.
         */
        private String titleBarToString(DockViewTitleBar titleBar) {

            final StringBuffer sb = new StringBuffer();
            sb.append(ObjectUtils.identityToString(titleBar));

            final Character prefix;
            final String text;
            if (titleBar != null) {
                prefix = titleBar.isActive() ? '+' : '-';
                text = (titleBar.getDockable() != null) ? titleBar.getDockable().getDockKey().getName() : null;
            } else {
                prefix = '-';
                text = null;
            }

            sb.append(prefix).append(text);

            return sb.toString();
        }
    }

    /**
     * A set of colors with its own semantic.
     *
     * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
     */
    public static enum DockingColor {

        /**
         * The active widget color.
         */
        ACTIVE_WIDGET("Bluebell.activeWidgetColor"), // instead of "activeCaption"
        /**
         * The inactive widget color.
         */
        INACTIVE_WIDGET("Bluebell.inactiveWidgetColor"), // instead of ""controlLtHighlight""
        /**
         * The background color.
         */
        BACKGROUND("Bluebell.backgroundColor"), // instead of control "DockingDesktop.backgroundColor"
        /**
         * The highlight color.
         */
        HIGHLIGHT("Bluebell.highlightColor"), // instead of "controlHighlight"
        /**
         * The shadow color.
         */
        SHADOW("Bluebell.shadowColor"); // controlDkShadow

        /**
         * The key to query the UIManager for getting the associated color.
         */
        private String key;

        /**
         * Creates the enumerated type.
         *
         * @param key
         *            the key to query the UIManager.
         */
        private DockingColor(String key) {

            this.setKey(key);
        }

        /**
         * Gets the related color.
         *
         * @return the related color.
         */
        public Color getColor() {

            return UIManager.getColor(this.getKey());
        }

        /**
         * Gets the key.
         *
         * @return the key
         */
        public String getKey() {

            return this.key;
        }

        /**
         * Sets the key.
         *
         * @param key
         *            the key to set.
         */
        private void setKey(String key) {

            Assert.notNull(key, "key");

            this.key = key;
        }
    }

    /**
     * An enum to distinguish between dock view types.
     *
     * @author <a href = "mailto:julio.arguello@gmail.com" >Julio Argüello (JAF)</a>
     */
    public static enum DockViewType {
        /**
         * Closed dockable state.
         */
        CLOSED,
        /**
         * Docked dockable state.
         */
        DOCKED,
        /**
         * Floating dockable state.
         */
        FLOATING,
        /**
         * Hidden dockable state.
         */
        HIDDEN,
        /**
         * Maximized dockable state.
         */
        MAXIMIZED,
        /**
         * Tabbed dockable state.
         */
        TABBED
    }
}