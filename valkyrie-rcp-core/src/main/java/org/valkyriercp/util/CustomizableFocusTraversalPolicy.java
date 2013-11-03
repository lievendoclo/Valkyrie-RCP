package org.valkyriercp.util;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * A LayoutFocusTraversalPolicy that allows for individual containers to have a
 * custom focus order.
 *
 * @author oliverh
 */
public class CustomizableFocusTraversalPolicy extends LayoutFocusTraversalPolicy {

    private static final String FOCUS_ORDER_PROPERTY_NAME = "customFocusOrder";

    /**
     * Installs an instance of CustomizableFocusTraversalPolicy as the default
     * focus traversal policy.
     */
    public static void installCustomizableFocusTraversalPolicy() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalPolicy(
                new CustomizableFocusTraversalPolicy());
    }

    /**
     * Sets a custom focus traversal order for the given container. Child
     * components for which there is no order specified will receive focus after
     * components that do have an order specified in the standard "layout"
     * order.
     *
     * @param container
     *            the container
     * @param componentsInOrder
     *            a list of child components in the order that thay should
     *            receive focus
     */
    public static void customizeFocusTraversalOrder(JComponent container, java.util.List componentsInOrder) {
        for (Iterator i = componentsInOrder.iterator(); i.hasNext();) {
            Component comp = (Component)i.next();
            if (comp.getParent() != container) {
                throw new IllegalArgumentException("Component [" + comp + "] is not a child of [" + container + "].");
            }
        }
        container.putClientProperty(FOCUS_ORDER_PROPERTY_NAME, createOrderMapFromList(componentsInOrder));
    }

    private static Map createOrderMapFromList(java.util.List componentsInOrder) {
        HashMap orderMap = new HashMap(componentsInOrder.size());
        for (int i = 0; i < componentsInOrder.size(); i++) {
            orderMap.put(componentsInOrder.get(i), new Integer(i));
        }
        return orderMap;
    }

    /**
     * Creates a new CustomizableFocusTraversalPolicy
     */
    public CustomizableFocusTraversalPolicy() {
        setComparator(new CustomizableFocusTraversalComparator(getComparator()));
    }

    private static class CustomizableFocusTraversalComparator implements Comparator {

        private Comparator layoutComparator;

        private CustomizableFocusTraversalComparator(Comparator layoutComparator) {
            this.layoutComparator = layoutComparator;
        }

        public int compare(Object o1, Object o2) {
            Component comp1 = (Component)o1;
            Component comp2 = (Component)o2;
            if (comp1 == comp2) {
                return 0;
            }
            Map order = getFocusOrder(comp1);
            if (order != null && comp1.getParent() == comp2.getParent()) {
                return compareSameParent(order, comp1, comp2);
            }
            else if (comp1.getParent() != comp2.getParent()) {
                return compareClosestAncestor(comp1, comp2);
            }
            else {
                return layoutComparator.compare(comp1, comp2);
            }
        }

        private Map getFocusOrder(Component comp) {
            Component parent = comp.getParent();
            return (Map)((parent instanceof JComponent) ? ((JComponent)parent)
                    .getClientProperty(FOCUS_ORDER_PROPERTY_NAME) : null);
        }

        private int compareSameParent(Map order, Component comp1, Component comp2) {
            Integer index1 = (Integer)order.get(comp1);
            Integer index2 = (Integer)order.get(comp2);
            if (index1 != null && index2 != null) {
                return index1.intValue() - index2.intValue();
            }
            else if (index1 != null) {
                return -1;
            }
            else if (index2 != null) {
                return 1;
            }
            else {
                return layoutComparator.compare(comp1, comp2);
            }
        }

        public int compareClosestAncestor(Component comp1, Component comp2) {
            java.util.List comp1Ancestors = getAncestors(comp1);
            java.util.List comp2Ancestors = getAncestors(comp2);
            int index1 = comp1Ancestors.size();
            int index2 = comp2Ancestors.size();
            while (true) {
                if (index1 > 0) {
                    comp1 = (Component)comp1Ancestors.get(--index1);
                }
                else {
                    return -1;
                }
                if (index2 > 0) {
                    comp2 = (Component)comp2Ancestors.get(--index2);
                }
                else {
                    return 1;
                }
                if (comp1 != comp2) {
                    break;
                }
            }
            return compare(comp1, comp2);
        }

        private java.util.List getAncestors(Component comp) {
            java.util.List ancestors = new ArrayList();
            while (comp != null) {
                if (comp instanceof Window) {
                    break;
                }
                ancestors.add(comp);
                comp = comp.getParent();
            }
            return ancestors;
        }
    }
}