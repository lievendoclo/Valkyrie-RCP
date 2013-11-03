package org.valkyriercp.application.support;

import com.google.common.collect.Lists;
import org.valkyriercp.application.PageLayoutBuilder;
import org.valkyriercp.application.ViewDescriptor;

import java.util.List;

public class MultiViewPageDescriptor  extends AbstractPageDescriptor {

    private List viewDescriptors = Lists.newArrayList();

    public void buildInitialLayout(PageLayoutBuilder pageLayout) {
        for (Object viewDescriptor : getViewDescriptors()) {
            String descriptor = null;
            if (viewDescriptor instanceof String) {
               descriptor = (String) viewDescriptor;
            } else if(viewDescriptor instanceof ViewDescriptor) {
                descriptor = ((ViewDescriptor) viewDescriptor).getId();
            } else {
                throw new IllegalStateException("ViewDescriptors should either be String or ViewDescriptor elements");
            }
            pageLayout.addView(descriptor);
        }
    }

    public List getViewDescriptors() {
        return viewDescriptors;
    }

    public void setViewDescriptors(List viewDescriptors) {
        this.viewDescriptors = viewDescriptors;
    }

    public void setBeanName(String name) {
        setId(name);
    }

}

