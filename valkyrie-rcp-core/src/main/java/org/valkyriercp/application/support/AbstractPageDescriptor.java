package org.valkyriercp.application.support;

import org.springframework.beans.factory.BeanNameAware;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.core.support.LabeledObjectSupport;

public abstract class AbstractPageDescriptor extends LabeledObjectSupport implements PageDescriptor, BeanNameAware {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBeanName(String name) {
        setId(name);
    }
}
