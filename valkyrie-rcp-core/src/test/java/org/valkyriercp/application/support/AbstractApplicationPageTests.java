package org.valkyriercp.application.support;

import org.junit.Before;
import org.junit.Test;
import org.valkyriercp.AbstractValkyrieTest;
import org.valkyriercp.application.ApplicationPage;

import javax.swing.*;

import static org.junit.Assert.*;

/**
 * Abstract base testcase for {@link org.valkyriercp.application.ApplicationPage} implementations.
 *
 * @author Peter De Bruycker
 */
public abstract class AbstractApplicationPageTests extends AbstractValkyrieTest {

    private AbstractApplicationPage applicationPage;
    private TestView testView1;
    private TestView testView2;

    @Before
    public void setUp() throws Exception {
        setUpViews();

        applicationPage = (AbstractApplicationPage) createApplicationPage();
        assertNotNull("createApplicationPage returns null", applicationPage);

        SimpleViewDescriptorRegistry viewDescriptorRegistry = new SimpleViewDescriptorRegistry();
        viewDescriptorRegistry.addViewDescriptor(new SimpleViewDescriptor("testView1", testView1));
        viewDescriptorRegistry.addViewDescriptor(new SimpleViewDescriptor("testView2", testView2));

        applicationPage.setViewDescriptorRegistry(viewDescriptorRegistry);

        applicationPage.setPageComponentPaneFactory(new SimplePageComponentPaneFactory());

        applicationPage.setDescriptor(new EmptyPageDescriptor());

        // trigger control creation
        JComponent control = applicationPage.getControl();
        assertNotNull("getControl cannot return null", control);
    }

    private void setUpViews() {
        testView1 = new TestView("this is test view 1");
        testView2 = new TestView("this is test view 2");
    }

    protected abstract ApplicationPage createApplicationPage();

    @Test
    public void testShowViewAndClose() {
        assertNull(applicationPage.getView("testView1"));

        applicationPage.showView("testView1");

        TestView view = (TestView) applicationPage.getView("testView1");

        assertNotNull(view);
        assertEquals("testView1", view.getId());

        applicationPage.close(view);
        assertNull(applicationPage.getView("testView1"));
    }

    @Test
    public void testShowViewWithInput() {
        Object input = "the input";

        applicationPage.showView("testView1", input);

        TestView view = applicationPage.getView("testView1");
        assertNotNull(view);

        assertTrue(view.isSetInputCalled());
        assertEquals(input, view.getInput());
    }

    @Test
    public void testShowView() {
        assertSame(testView1, applicationPage.showView("testView1"));
        assertSame(testView1, applicationPage.getActiveComponent());

        assertSame(testView2, applicationPage.showView("testView2"));
        assertSame(testView2, applicationPage.getActiveComponent());
    }

    @Test
    public void testShowViewWithoutInput() {
        applicationPage.showView("testView1");

        TestView view = applicationPage.getView("testView1");
        assertNotNull(view);

        assertFalse(view.isSetInputCalled());
    }

    @Test
    public void testGetView() {
        assertNull(applicationPage.getView("testView1"));

        applicationPage.showView("testView1");

        TestView view = applicationPage.getView("testView1");

        assertNotNull(view);
        assertEquals("testView1", view.getId());

        applicationPage.close(view);
        assertNull(applicationPage.getView("testView1"));
    }

    private static class TestView extends AbstractView {

        private String label;
        private Object input;
        private boolean setInputCalled;

        public TestView(String label) {
            super("testView");
            this.label = label;
        }

        @Override
        protected JComponent createControl() {
            return new JLabel(label);
        }

        @Override
        public void setInput(Object input) {
            this.input = input;
            setInputCalled = true;
        }

        public Object getInput() {
            return input;
        }

        public boolean isSetInputCalled() {
            return setInputCalled;
        }

    }
}
