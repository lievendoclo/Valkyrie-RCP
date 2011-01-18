package org.valkyriercp.application.support;

import org.junit.Assert;
import org.junit.Test;
import org.valkyriercp.application.PageLayoutBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MultiViewPageDescriptorTests {
    @Test
    public void testBuildInitialLayout() {
        MultiViewPageDescriptor pageDescriptor = new MultiViewPageDescriptor();

        List descriptors = new ArrayList();
        descriptors.add("view0");
        descriptors.add("view1");
        descriptors.add("view2");
        descriptors.add("view3");

        pageDescriptor.setViewDescriptors(descriptors);
        Assert.assertSame(descriptors, pageDescriptor.getViewDescriptors());

        PageLayoutBuilder mockBuilder = mock(PageLayoutBuilder.class);
        pageDescriptor.buildInitialLayout(mockBuilder);

        verify(mockBuilder).addView("view0");
        verify(mockBuilder).addView("view1");
        verify(mockBuilder).addView("view2");
        verify(mockBuilder).addView("view3");
    }

    @Test
    public void testBeanAware() {
        MultiViewPageDescriptor pageDescriptor = new MultiViewPageDescriptor();

        pageDescriptor.setBeanName("bean name");

        Assert.assertEquals("the bean name must be set as id", "bean name", pageDescriptor.getId());
    }
}
