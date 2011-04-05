package org.valkyriercp.application.support;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.context.support.StaticApplicationContext;
import org.valkyriercp.application.ViewDescriptor;

import javax.swing.*;

public class BeanFactoryViewDescriptorRegistryTests {

    /**
     * Test method for {@link BeanFactoryViewDescriptorRegistry#getViewDescriptor(java.lang.String)}.
     */
    @Test
    public final void testGetViewDescriptor() {

        BeanFactoryViewDescriptorRegistry registry = new BeanFactoryViewDescriptorRegistry();
        StaticApplicationContext appCtx = new StaticApplicationContext();
        registry.setApplicationContext(appCtx);

        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.addPropertyValue("viewClass", NullView.class);
        appCtx.registerSingleton("view1", DefaultViewDescriptor.class, mpv);
        appCtx.registerSingleton("view2", DefaultViewDescriptor.class, mpv);
        appCtx.registerSingleton("bogusView", String.class);

        Assert.assertNotNull(registry.getViewDescriptor("view1"));
        Assert.assertNotNull(registry.getViewDescriptor("view2"));

        Assert.assertNull("Should return null when viewName not found", registry.getViewDescriptor("bogus"));

        try {
            registry.getViewDescriptor("bogusView");
            Assert.fail("Should have thrown BeanNotOfRequiredTypeException");
        }
        catch (BeanNotOfRequiredTypeException e) {
            //do nothing, test succeeded
        }

    }

    /**
     * Performs the following assertions on the
     * {@link BeanFactoryViewDescriptorRegistry#getViewDescriptors()} method:
     *
     * <ul>
     * <li>The method does not return null if there are no view descriptors in the underlying
     * registry</li>
     * <li>The correct number of descriptors are returned.</li>
     * </ul>
     */
    @Test
    public void testGetViewDescriptors() {

        BeanFactoryViewDescriptorRegistry registry = new BeanFactoryViewDescriptorRegistry();
        StaticApplicationContext appCtx = new StaticApplicationContext();
        registry.setApplicationContext(appCtx);

        ViewDescriptor[] viewDescriptors = registry.getViewDescriptors();

        Assert.assertNotNull("View descriptor array should never be null", viewDescriptors);
        Assert.assertEquals("Should be no view descriptors in the array", 0, viewDescriptors.length);

        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.addPropertyValue("viewClass", NullView.class);
        appCtx.registerSingleton("view1", DefaultViewDescriptor.class, mpv);
        appCtx.registerSingleton("view2", DefaultViewDescriptor.class, mpv);

        viewDescriptors = registry.getViewDescriptors();
        Assert.assertEquals("Should be 2 view descriptors in the array", 2, viewDescriptors.length);

    }

    /**
     * Confirms that an IllegalArgumentException is thrown if a null viewName is passed to the
     * {@link BeanFactoryViewDescriptorRegistry#getViewDescriptor(String)} method.
     */
    @Test
    public void testForNullViewId() {

        try {
            new BeanFactoryViewDescriptorRegistry().getViewDescriptor(null);
            Assert.fail("Should have thrown an IllegalArgumentException for null view ID");
        }
        catch (IllegalArgumentException e) {
            //do nothing, test succeeded
        }

    }

    private class NullView extends AbstractView
    {

        protected NullView() {
            super("nullView");
        }

        protected JComponent createControl()
        {
            return new JPanel();
        }
    }

}

