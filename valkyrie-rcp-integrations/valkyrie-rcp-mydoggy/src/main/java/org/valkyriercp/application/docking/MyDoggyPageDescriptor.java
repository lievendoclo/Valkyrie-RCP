package org.valkyriercp.application.docking;

import org.springframework.core.io.Resource;
import org.valkyriercp.application.support.MultiViewPageDescriptor;

public class MyDoggyPageDescriptor extends MultiViewPageDescriptor {
    private Resource layoutFile;

    public MyDoggyPageDescriptor(Resource layoutFile) {
        this.layoutFile = layoutFile;
    }

    public Resource getLayoutFile() {
        return layoutFile;
    }
}
