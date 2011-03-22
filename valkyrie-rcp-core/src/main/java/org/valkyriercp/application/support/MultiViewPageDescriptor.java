package org.valkyriercp.application.support;

import com.google.common.collect.Lists;
import org.valkyriercp.application.PageLayoutBuilder;
import org.valkyriercp.application.ViewDescriptor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiViewPageDescriptor  extends AbstractPageDescriptor {

    private List<ViewDescriptor> viewDescriptors = Lists.newArrayList();

    public void buildInitialLayout(PageLayoutBuilder pageLayout) {
        for (ViewDescriptor viewDescriptor : getViewDescriptors()) {
            pageLayout.addView(viewDescriptor.getId());
        }
    }

    public List<ViewDescriptor> getViewDescriptors() {
        return viewDescriptors;
    }

    public void setViewDescriptors(List<ViewDescriptor> viewDescriptors) {
        this.viewDescriptors = viewDescriptors;
    }

    public void setBeanName(String name) {
        setId(name);
    }

}

