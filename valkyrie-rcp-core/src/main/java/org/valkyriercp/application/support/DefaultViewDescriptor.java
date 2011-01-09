package org.valkyriercp.application.support;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.View;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.command.config.CommandButtonLabelInfo;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.ShowViewCommand;
import org.valkyriercp.core.support.LabeledObjectSupport;

import java.util.Collections;
import java.util.Map;

/**
 * Provides a standard implementation of {@link ViewDescriptor}.
 *
 * @author Keith Donald
 */
public class DefaultViewDescriptor extends LabeledObjectSupport implements ViewDescriptor, BeanNameAware,
        InitializingBean {
    private String id;

    private Class<? extends View> viewClass;

    private Map<String, Object> viewProperties;

    public DefaultViewDescriptor() {
        // default constructor for spring creation
    }

    public DefaultViewDescriptor(String id, Class<? extends View> viewClass) {
        this(id, viewClass, Collections.<String, Object> emptyMap());
    }

    public DefaultViewDescriptor(String id, Class<? extends View> viewClass, Map<String, Object> viewProperties) {
        setId(id);
        setViewClass(viewClass);
        setViewProperties(viewProperties);
    }

    public void setBeanName(String beanName) {
        setId(beanName);
    }

    public void setId(String id) {
        Assert.notNull("id is required");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public void setViewClass(Class<? extends View> viewClass) {
        Assert.notNull(viewClass, "viewClass cannot be null");
        Assert.isTrue(View.class.isAssignableFrom(viewClass), "viewClass doesn't derive from View");

        this.viewClass = viewClass;
    }

    public void setViewProperties(Map<String, Object> viewProperties) {
        this.viewProperties = viewProperties;
    }

    public PageComponent createPageComponent() {
        return createView();
    }

    protected View createView() {
        Assert.state(viewClass != null, "View class to instantiate is not set");
        Object o = BeanUtils.instantiateClass(viewClass);
        Assert.isTrue((o instanceof View), "View class '" + viewClass
                + "' was instantiated, but instance is not a View!");
        View view = (View) o;
        view.setDescriptor(this);
        if (viewProperties != null) {
            BeanWrapper wrapper = new BeanWrapperImpl(view);
            wrapper.setPropertyValues(viewProperties);
        }

        if (view instanceof InitializingBean) {
            try {
                ((InitializingBean) view).afterPropertiesSet();
            } catch (Exception e) {
                throw new BeanInitializationException("Problem running on " + view, e);
            }
        }
        return view;
    }

    public CommandButtonLabelInfo getShowViewCommandLabel() {
        return getLabel();
    }

    public ActionCommand createShowViewCommand(ApplicationWindow window) {
        return new ShowViewCommand(this, window);
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(id, "id is mandatory");
        Assert.notNull(viewClass, "viewClass is mandatory");
    }

}
