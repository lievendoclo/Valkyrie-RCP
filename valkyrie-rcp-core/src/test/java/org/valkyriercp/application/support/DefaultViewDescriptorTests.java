package org.valkyriercp.application.support;

import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * testcase for {@link DefaultViewDescriptor}
 *
 * @author Peter De Bruycker
 */
public class DefaultViewDescriptorTests {
    @Test
    public void testConstructor() {
        DefaultViewDescriptor descriptor = new DefaultViewDescriptor("theView", TestView.class);

        Assert.assertEquals("theView", descriptor.getId());
        Assert.assertEquals(TestView.class, descriptor.getViewClass());
    }

    @Test
    public void testViewCreation() {
        DefaultViewDescriptor descriptor = new DefaultViewDescriptor("theView", TestView.class);

        TestView view = (TestView) descriptor.createPageComponent();
        Assert.assertNotNull(view);
    }

    @Test
    public void testViewCreationWithProperties() {
        Map<String, Object> viewProperties = new HashMap<String, Object>();
        viewProperties.put("stringProperty", "test value");

        DefaultViewDescriptor descriptor = new DefaultViewDescriptor("theView", TestView.class, viewProperties);

        TestView view = (TestView) descriptor.createPageComponent();
        Assert.assertNotNull(view);

        Assert.assertEquals("test value", view.getStringProperty());
    }

    @Test
    public void testSetViewClass() throws Exception {
        DefaultViewDescriptor descriptor = new DefaultViewDescriptor();

        descriptor.setId("viewId");

        Class notAViewClass = String.class;

        try {
            descriptor.setViewClass(notAViewClass);
            Assert.fail("Must throw exception");
        } catch (IllegalArgumentException e) {
            // test passes
        }

    }

    public static class TestView extends AbstractView {

        private String stringProperty;

        @Override
        protected JComponent createControl() {
            return new JLabel("test");
        }

        public void setStringProperty(String stringProperty) {
            this.stringProperty = stringProperty;
        }

        public String getStringProperty() {
            return stringProperty;
        }

    }
}
