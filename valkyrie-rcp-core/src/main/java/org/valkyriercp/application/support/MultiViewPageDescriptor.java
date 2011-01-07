package org.valkyriercp.application.support;

import org.valkyriercp.application.PageLayoutBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiViewPageDescriptor  extends AbstractPageDescriptor {

    private List viewDescriptors = new ArrayList();

    public void buildInitialLayout(PageLayoutBuilder pageLayout) {
        for (Iterator iter = viewDescriptors.iterator(); iter.hasNext();) {
            String viewDescriptorId = (String) iter.next();
            pageLayout.addView(viewDescriptorId);
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

