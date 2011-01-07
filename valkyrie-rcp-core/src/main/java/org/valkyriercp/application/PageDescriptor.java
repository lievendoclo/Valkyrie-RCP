package org.valkyriercp.application;

import org.valkyriercp.core.DescribedElement;
import org.valkyriercp.core.VisualizedElement;

public interface PageDescriptor extends VisualizedElement, DescribedElement{
    public String getId();

    public void buildInitialLayout(PageLayoutBuilder pageLayout);
}
